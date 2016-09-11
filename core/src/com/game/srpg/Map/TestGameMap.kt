package com.game.srpg.Map

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.game.srpg.Units.Cursor.Cursor
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
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

    val masterAtlas = assets.masterAtlas
    val manager = assets.assetManager
    val cursor = Cursor(this, masterAtlas)
    val units = Array<GameUnit>()
    val unitIter = Array.ArrayIterator(units)
    val path = PathMap(width, height, units)
    val batch = CardBatch(masterAtlas.textures.first())
    val playerHighlight = MapHighlight(manager, this)

    var phase : Phase = PlayerStartPhase(this)

    internal lateinit var box : Model
    internal lateinit var table : ModelInstance
    init{
        val builder = ModelBuilder()
        val box = builder.createBox(tileWidth * width.toFloat(), 10f, tileHeight * height.toFloat(), Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong())
        table = ModelInstance(box)
        table.transform.translate(width * tileWidth.toFloat() * 0.5f, -10f, -height * tileHeight.toFloat() * 0.5f)

        phase.onEnter()
        //val drawing = cursor.drawing
        //if(drawing is CardDrawingComponent)
            //batch.add(drawing.card())

        //addListener(cursor)
    }


    val shadowedRenderables = RenderableWrapper(batch, table)
    val nonShadowed = RenderableWrapper()

    fun update(dt : Float){
        unitIter.reset()
        for(unit in unitIter){
            unit.update(dt)
        }
        phase.update(dt)
        cursor.update(dt)
        playerHighlight.update(dt)
        batch.build()
    }

    fun switch(newPhase : Phase){
        phase.onExit()
        phase = newPhase
        phase.onEnter()
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
        playerHighlight.dispose()
    }

}