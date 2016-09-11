package com.game.srpg.Screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.g3d.utils.*
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector3
import com.game.srpg.Cards.CardAnimation
import com.game.srpg.Cards.CardBatch
import com.game.srpg.GlobalSystems.*
import com.game.srpg.Map.*
import com.game.srpg.Shadows.*
import com.game.srpg.Units.Components.CardDrawingComponent
import com.game.srpg.Units.Controller.MapController
import com.game.srpg.Units.GameUnit
import com.game.srpg.Units.UnitFactory

/**
 * Created by FlyingJam on 8/13/2016.
 */
fun hey(){

}
/*
class TestScreen(val parent : Game, val assetManager : AssetManager, val masterAtlas : TextureAtlas) : ScreenAdapter(){

    //val texture = assetManager.get("fe.png", Texture::class.java)
    val texture = masterAtlas.findRegion("fe").texture

    val cardBatch = CardBatch(texture)
    val frames = getFrames(texture, 64, 64, 4)

    val camera = PerspectiveCamera(60f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    val camController = CameraInputController(camera)

    val fps = FpsDrawer()
    val modelBuilder = ModelBuilder()
    val tempboxmat = Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY))
    val flags = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()

    val bigbox = modelBuilder.createBox(24 * 32f, 10f, 24 * 32f, tempboxmat,
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong())

    val gridModel = createMap(modelBuilder, 64, 64, 32, 32)
    val gridInstance = ModelInstance(gridModel)
    val mapInstance = ModelInstance(bigbox)

    val map = GameMap(24, 24, 32, 32, assetManager, masterAtlas)
    val unit = UnitFactory.testUnit(assetManager, masterAtlas, map)

    val shadow = Shadow(assetManager, camera)

    val spotLight = DirectionalLight(assetManager, Vector3(0f, 300f, 30f), Vector3(0f, 0f, 0f))

    val batch = SpriteBatch()

    val opaque = Material(
            ColorAttribute.createDiffuse(Color.CYAN),
            BlendingAttribute(true, 0.6f)
    )


    val square = createMapColor(24, 24, 32, 32)
    val instance = ModelInstance(square)

    val possibilities = map.path.movableRegion(0, 0, 5)
    val posMap = Texture(createSelectTexture(possibilities, map))
    val gridShader = assetManager.get("tile_c", ShaderProgram::class.java)

    init{
        createSelectTexture(possibilities, map)
        mapInstance.transform.translate(12 * 32f, -10f, -12 * 32f)
        instance.transform.translate(16f, -4f, -16f)
        //instance.transform.translate(12 * 32f, -10f, -12 * 32f)
        //animation.card.transform.translate(controller.worldX(), controller.worldY(), controller.worldZ())
        camera.far = 1000f
        camera.translate(0f, 170f, 200f)
        camera.lookAt(0f, 0f, -20f)
        //camController.setVelocity(90f)
        instance.transform.translate(0f, 5f, 0f)

        shadow.addLight(spotLight)
        shadowQuality = 1500



        val draw = unit.drawing
        if(draw is CardDrawingComponent){
            cardBatch.add(draw.card())
        }

        val cursor = map.cursor.drawing
        if(cursor is CardDrawingComponent){
            cardBatch.add(cursor.card())
        }

        cardBatch.build()
        //Gdx.input.inputProcessor = camController
        addListener(map.cursor)
    }

    val modelBatch = ModelBatch(object : DefaultShaderProvider(){
        override fun createShader(renderable: Renderable): Shader {
            if(renderable.material.has(ColorAttribute.Diffuse)){
                return SimpleColorShader(renderable, gridShader)
            }
            else{
                return super.createShader(renderable)
            }
        }
    })

    val rencall = {batch : ModelBatch -> batch.render(cardBatch); batch.render(mapInstance)}

    val highlight = MapHighlight(assetManager, map)

    init{
        map.addUnit(unit)
        //highlight.set(possibilities)
    }
    override fun render(dt : Float){

        glEnableDepth()
        glClear()
        unit.update(dt)
        cardBatch.build()
        shadow.forceUpdate()

        camController.update()
        camera.update()
        map.cursor.update(dt)
        camera.position.set(
                map.cursor.controllers.peekFirst().worldX(map.cursor),
                camera.position.y,
                map.cursor.controllers.peekFirst().worldZ(map.cursor) + 300
        )
        map.playerHighlight.update(dt)

        //cardBatch.build()

        //Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        //rencall(mb)
        //rencall(assfuckshit)
        shadow.render(rencall)
        modelBatch.begin(camera)
        modelBatch.render(gridInstance)
        map.playerHighlight.set(gridShader)
        modelBatch.render(map.playerHighlight.highlightInstance)
        //highlight.set(gridShader)
        //modelBatch.render(highlight.highlightInstance)
        //gridShader.begin()
        //posMap.bind(0)
        //gridShader.setUniformi("u_tileMap", 0)
        //gridShader.setUniformf("u_mapFactorX", map.width * map.tileWidth.toFloat())
        //gridShader.setUniformf("u_mapFactorY", map.height * map.tileHeight.toFloat())
        //gridShader.setUniformf("u_alpha", 0.3f)
        //gridShader.end()
        //val mat = camera.combined.inv()
        //gridShader.setUniformMatrix("u_projTrans", mat)
        //gridShader.end()
        //modelBatch.render(instance)
        modelBatch.end()
        fps.draw()


    }

}
*/