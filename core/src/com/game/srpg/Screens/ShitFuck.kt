package com.game.srpg.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector3
import com.game.srpg.Cards.CardAnimation
import com.game.srpg.Cards.CardBatch
import com.game.srpg.GlobalSystems.getFrames
import com.game.srpg.GlobalSystems.glClear
import com.game.srpg.GlobalSystems.glEnableDepth
import com.game.srpg.Shadows.*

/**
 * Created by FlyingJam on 8/17/2016.
 */

class ShitFuck : ScreenAdapter(){

    val shader = createShader("wtf_v.glsl", "wtf_f.glsl")
    val shaderColor = createShader("wtf_v.glsl", "wtf_f_c.glsl")
    val shader2 = createShader("depthmap_v.glsl", "depthmap_f.glsl")

    val provider = object: DefaultShaderProvider(){
        override fun createShader(renderable: Renderable): Shader {
            if(renderable.material.has(TextureAttribute.Diffuse)){
                return SimpleTextureShader(renderable, shader)
            }
            else{
                return SimpleColorShader(renderable, shaderColor)
            }
        }
    }

    val modelBatch = ModelBatch(provider)
    val modelBatchd = ModelBatch()
    val builder = ModelBuilder()
    val camera = PerspectiveCamera(120f, 1280f, 720f)
    val controller = CameraInputController(camera)
    init{
        camera.lookAt(0f, 0f, 0f)
        camera.position.z = 20f
        camera.update()
        Gdx.input.inputProcessor = controller
    }
    val texture = Texture("fe.png")
    val badlogic = Texture("darkblue.png")

    val tem = Material(TextureAttribute.createDiffuse(badlogic))
    val mat = Material(ColorAttribute.createDiffuse(Color.BLUE))
    val tem2 = Material(TextureAttribute.createDiffuse(badlogic))
    val material = Material(ColorAttribute.createDiffuse(Color.WHITE))
    val flags = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()
    val model = builder.createBox(190f, 200f, 50f, mat, flags)
    val box = ModelInstance(model)
    val bigbox = builder.createBox(24f * 32f, 10f, 24 * 32f, tem,
            flags)

    val box2 = ModelInstance(bigbox)


    val cardBatch = CardBatch(texture)
    val frames = getFrames(texture, 24, 24, 4)
    val animation = CardAnimation(frames, 0.3f, Animation.PlayMode.LOOP_PINGPONG)
    val animation2 = CardAnimation(frames, 0.3f, Animation.PlayMode.LOOP_PINGPONG)
    init{
        cardBatch.add(animation.card)
        cardBatch.add(animation2.card)
    }


    init{
        box.transform.translate(0f, -10f, -50f)
        animation.card.transform.translate(0f, 0f, 10f)
        box2.transform.translate(2f, -50f, -50f)
        camera.far = 900f

        cardBatch.build()
    }

    val asdf = cardBatch

    override fun render(dt : Float){
        glEnableDepth()
        glClear()

        animation.card.transform.translate(0f, 1f, 1f)

        //modelBatch.begin(camera)
        //modelBatch.render(box)
        //modelBatch.render(cardBatch)
        //modelBatch.render(box2)
        //shadow.render { it.render(box2); it.render(box)}
        /*
        secondBuffer.begin()

        modelBatch2.begin(camera)
        modelBatch2.render(cardBatch)
        modelBatch2.end()//modelBatch.end()
        secondBuffer.end()

        //animation.update(dt)
        //cardBatch.build()
        //animation.card.update()
        //cardBatch.build()

*/
        //modelBatch.begin(camera)
        //modelBatch.render(cardBatch)
        //modelBatch.render(box2)
        //modelBatch.render(box)
        //modelBatch.end()


        camera.update()
        controller.update()
    }
}