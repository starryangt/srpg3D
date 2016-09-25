package com.game.srpg.Map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import com.game.srpg.Units.Cursor.Cursor
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Pool
import com.game.srpg.Cards.CardBatch
import com.game.srpg.GlobalSystems.AssetWrapper
import com.game.srpg.GlobalSystems.addListener
import com.game.srpg.Map.Phase.EnemyPhase
import com.game.srpg.Map.Phase.Phase
import com.game.srpg.Map.Phase.PlayerPhase
import com.game.srpg.Map.Phase.PlayerStartPhase
import com.game.srpg.Screens.MapScreen
import com.game.srpg.UI.UI
import com.game.srpg.Units.Components.CardDrawingComponent
import com.game.srpg.Units.Components.UnitType
import com.game.srpg.Units.Controller.UnitCameraFollower
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/15/2016.
 */

class RenderableWrapper() : RenderableProvider{
    constructor(vararg providers : RenderableProvider) : this(){
        for(provider in providers){
            renderableProviders.add(provider)
        }
    }

    constructor (vararg ren : Renderable) : this(){
        for(renderable in ren){
            renderables.add(renderable)
        }
    }

    val renderables = Array<Renderable>()
    val renderableProviders = Array<RenderableProvider>()

    override fun getRenderables(renderables: Array<Renderable>?, pool: Pool<Renderable>?) {
        for(provider in renderableProviders){
            provider.getRenderables(renderables, pool)
        }
    }
}

class GameMap(width : Int,
              height : Int,
              val tileWidth : Int,
              val tileHeight : Int,
              val assets : AssetWrapper,
              val ui : UI,
              val screen : MapScreen) : Map(width, height), Disposable {

    companion object static{
        fun fromJson(filename : String, assets : AssetWrapper, ui : UI, screen : MapScreen) : GameMap{
            val fh = Gdx.files.internal(filename)
            val string = fh.readString()
            val json = Json()
            val info = json.fromJson(MapInformation::class.java, string)
            val map = GameMap(info.width, info.height, info.tileWidth, info.tileHeight, assets, ui, screen)
            for(wall in info.walls){
                map.path.setWall(wall)
            }

            map.models.loadModel(assets.assetManager.get("tree.g3dj"), "models/tree.g3dj")
            for(instance in info.instances){
                map.models.addInstance(instance.model, instance.position, instance.rotation)
            }
            map.models.buildCache()
            return map
        }
    }


    val masterAtlas = assets.masterAtlas
    val manager = assets.assetManager
    val cursor = Cursor(this, masterAtlas)
    val models = MapGeometry()
    val camController = UnitCameraFollower(cursor, screen.camera, screen.width, screen.height)
    val units = Array<GameUnit>()
    val unitIter = Array.ArrayIterator(units)
    val path = PathMap(width, height, units)
    val batch = CardBatch(masterAtlas.textures.first())
    val playerHighlight = MapHighlight(manager, this,
            colorSet = MapHighlight.ColorSet(
                    Color(.46f, .61f, .79f, 1f),
                    Color(1f, .41f, .38f, 1f),
                    Color(0f, 0f, 0f, 1f)))
    val totalEnemyHighlight = MapHighlight(manager, this, 0.1f, 0.15f, colorSet = MapHighlight.ColorSet(
             Color(), Color(.69f, .61f, .85f, 1f), Color()
    ))

    var phase : Phase = PlayerStartPhase(this)

    val particleSystem = ParticleSystem.get()
    val boardSprite = BillboardParticleBatch()

    internal lateinit var box : Model
    internal lateinit var table : ModelInstance
    init {
        boardSprite.setCamera(screen.camera)
        particleSystem.add(boardSprite)
        val param = ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.batches)
        assets.assetManager.load("snowparttest", ParticleEffect::class.java, param)
        assets.assetManager.finishLoading()
    }
    val origEffect = assets.assetManager.get("snowparttest", ParticleEffect::class.java)
    val effect = origEffect.copy()
    init{
        effect.scale(Vector3(5f, 5f, 5f))
        effect.translate(Vector3(0f, 5f, 0f))
        effect.init()
        effect.start()
        particleSystem.add(effect)
        val builder = ModelBuilder()
        val box = builder.createBox(tileWidth * width.toFloat(), 10f, tileHeight * height.toFloat(), Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.ColorUnpacked).toLong())
        table = ModelInstance(box)
        table.transform.translate(width * tileWidth.toFloat() * 0.5f, -10f, -height * tileHeight.toFloat() * 0.5f)

        phase.onEnter()
        //val drawing = cursor.drawing
        //if(drawing is CardDrawingComponent)
            //batch.add(drawing.card())

        //addListener(cursor)
    }


    val shadowedRenderables = RenderableWrapper(table, batch)
    val nonShadowed = RenderableWrapper()

    var acc = 0f
    val targetTime = 1/60f

    fun fixedUpdate(){
        //some things, like Libgdx's default particle effects, do not
        //scale by the change in time, and have to be update in a
        //fixed time step
        updateParticles()
    }

    fun update(dt : Float){
        acc += dt
        if(acc >= targetTime){
            fixedUpdate()
            acc -= targetTime
        }

        killUnits()
        unitIter.reset()
        for(unit in unitIter){
            unit.update(dt)
        }
        phase.update(dt)
        cursor.update(dt)
        camController.update(dt)
        playerHighlight.update(dt)
        totalEnemyHighlight.update(dt)
        batch.build()
    }

    fun switch(newPhase : Phase){
        phase.onExit()
        phase = newPhase
        phase.onEnter()
    }

    fun killUnits(){
        unitIter.reset()
        for(unit in unitIter){
            if(unit.dead){
                val drawing = unit.drawing
                if(drawing is CardDrawingComponent){
                    batch.remove(drawing.card())
                }
                println(units.size)
                units.removeValue(unit, false)
                println(units.size)
            }
        }
    }

    fun updateParticles(){
        particleSystem.update()
        particleSystem.begin()
        particleSystem.draw()
        particleSystem.end()
    }

    fun getEnemies() : Array<GameUnit>{
        return Array(units.filter { it.unitClass.type == UnitType.ENEMY }.toTypedArray())
    }

    fun getAllies() : Array<GameUnit>{
        return Array(units.filter { it.unitClass.type == UnitType.ALLY }.toTypedArray())
    }

    fun getUnit(x : Int, y : Int, check : (GameUnit) -> Boolean = { true }) : GameUnit?{
        val unit = units.find { it.mapX() == x && it.mapY() == y }
        if(unit != null){
            if(check(unit))
                return unit
        }
        return null
    }

    fun getUnit(index : Int, check : (GameUnit) -> Boolean = { true }) : GameUnit?{
        val x = path.indexToX(index)
        val y = path.indexToY(index)
        return getUnit(x, y, check)
    }

    fun isUnit(x : Int, y : Int, check: (GameUnit) -> Boolean = { true }) : Boolean{
        val unit = getUnit(x, y)
        if(unit != null){
            return check(unit)
        }
        return false
    }

    fun isUnit(index : Int, check : (GameUnit) -> Boolean = { true }) : Boolean{
        val unit = getUnit(index)
        if(unit != null){
            return check(unit)
        }
        return false
    }

    fun killUnit(x: Int, y : Int){

    }

    fun killUnit(unit : GameUnit){
        val draw = unit.drawing
        if(draw is CardDrawingComponent) {
            batch.remove(draw.card())
        }
        units.removeValue(unit, true)
    }

    fun addUnit(unit : GameUnit){
        val draw = unit.drawing
        if(draw is CardDrawingComponent){
            batch.add(draw.card())
        }
        units.add(unit)
    }

    fun mapToWorldX(x: Int, width: Int = tileWidth): Float {
        val offset = (tileWidth - width) / 2
        return (x * tileWidth).toFloat() + offset
    }

    fun mapToWorldY(y: Int, height: Int = tileHeight): Float {
        val offset = (tileHeight - height) / 2
        val realY = -y * tileHeight
        return (realY - offset).toFloat()
    }


    override fun dispose() {
        box.dispose()
        batch.dispose()
        effect.dispose()
        playerHighlight.dispose()
    }

}
