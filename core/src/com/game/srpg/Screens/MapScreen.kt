package com.game.srpg.Screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.game.srpg.AI.AI
import com.game.srpg.Cards.CardAnimation
import com.game.srpg.Cards.CardBatch
import com.game.srpg.Combat.Combat
import com.game.srpg.GlobalSystems.*
import com.game.srpg.Map.GameMap
import com.game.srpg.Map.HighlightAttribute
import com.game.srpg.Map.MapHighlight
import com.game.srpg.Map.Phase.EnemyPhase
import com.game.srpg.Map.TempHighlightAttribute
import com.game.srpg.Shaders.MapHighlightShader
import com.game.srpg.Shadows.DirectionalLight
import com.game.srpg.Shadows.PointLight
import com.game.srpg.Shadows.Shadow
import com.game.srpg.Shadows.SimpleColorShader
import com.game.srpg.UI.*
import com.game.srpg.Units.Controller.UnitCameraFollower
import com.game.srpg.Units.GameUnit
import com.game.srpg.Units.UnitFactory

/**
 * Created by FlyingJam on 8/18/2016.
 */

class MapScreen(val game : Game, val assets : AssetWrapper) : ScreenAdapter() {


    var width = Gdx.graphics.width
    var height = Gdx.graphics.height
    val camera = PerspectiveCamera(60f, width.toFloat(), height.toFloat())
    val controller = CameraInputController(camera)
    val skin = assets.assetManager.get("uiskin.json", Skin::class.java)
    val ui = UI(skin)
    val map = GameMap(32, 32, 32, 32, assets, ui, this)
    val shadow = Shadow(assets, camera)

    val spotlight = DirectionalLight(assets.assetManager, Vector3(0f, 300f, 300f), Vector3(0f, 0f, 0f))

    val program = assets.assetManager.get("tile_c", ShaderProgram::class.java)

    val unit = UnitFactory.testUnit(assets, map)
    val enemy = UnitFactory.testEnemy(assets, map)

    val fps = FpsDrawer()

    val batch = ModelBatch(object : DefaultShaderProvider() {
        override fun createShader(renderable: Renderable): Shader {
            if (renderable.material.has(HighlightAttribute.Highlight)) {
                val highlight = renderable.userData as MapHighlight
                println("once!")
                return MapHighlightShader(renderable, highlight)
            }
            return SimpleColorShader(renderable, program)
        }
    })

    val normalBatch = ModelBatch()

    val model = assets.assetManager.get("tree.g3dj", Model::class.java)
    val instance = ModelInstance(model)

    val button = Button(skin)
    val action = MoveToAction()

    init {
        instance.transform.translate(64f, 0f, 0f)
        //instance.transform()
    }

    fun spawnFriendlyStat(unit: GameUnit) {
        val table = StatBox(unit, skin)
        val action = MoveToAction()
        action.setPosition(table.table.x, table.table.y)
        action.duration = .2f
        table.table.x -= table.table.width

        table.table.addAction(action)
    }

    init {
        camera.translate(0f, 200f, 30f)
        camera.rotate(Vector3(5f, 0f, 0f), -40f)
        camera.far = 1500f
        camera.update()

        spotlight.setPower(700f)

        map.addUnit(unit)
        map.addUnit(UnitFactory.testUnit(assets, map, 3, 2))
        map.addUnit(UnitFactory.testEnemy(assets, map, 2, 3))
        map.addUnit(UnitFactory.testEnemy(assets, map, 1, 2))
        map.addUnit(UnitFactory.testEnemy(assets, map, 4, 1))
        map.batch.build()

        //map.switch(EnemyPhase(map, map.getEnemies()))

        //AI.attackPhase(e, map)

        //Gdx.input.inputProcessor = controller
        shadow.addLight(spotlight)
    }

    fun fixedUpdate(){
        shadow.forceUpdate()
    }

    var acc = 0f
    val targetTime = 1/60f
    override fun render(delta: Float) {
        glClear()

        acc += delta
        if(acc >= targetTime){
            fixedUpdate()
            acc -= targetTime
        }

        map.update(delta)
        //shadow.forceUpdate()
        ui.update(delta)

        shadow.render {
            it.render(map.shadowedRenderables)
            it.render(instance)
            //it.render(instance) }
        }

        batch.begin(camera)
        batch.render(map.playerHighlight)
        //batch.render(map.totalEnemyHighlight)
        batch.render(map.particleSystem)
        //batch.render(instance)
        batch.end()

        ui.render(delta)
        fps.draw()

        camera.update()
        controller.update()
    }
}