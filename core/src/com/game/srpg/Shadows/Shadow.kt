package com.game.srpg.Shadows

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable
import com.game.srpg.GlobalSystems.AssetWrapper
import java.util.*

/**
 * Created by FlyingJam on 8/16/2016.
 */

//TODO: change magic numbers with uniforms
//cache lights and shit

class Shadow(val assets : AssetWrapper, val camera : Camera) : Disposable{

    val assetManager = assets.assetManager
    var lights = ArrayList<ShLight>()
    var renderCall : ((ModelBatch) -> Unit)? = null

    val textureShaderProgram = assetManager.get("shadowed_scene_t", ShaderProgram::class.java)
    val colorShaderProgram = assetManager.get("shadowed_scene_c", ShaderProgram::class.java)
    val vertexColorShaderProgram = assetManager.get("shadowed_scene_vc", ShaderProgram::class.java)
    val shadowShaderProgram = assetManager.get("shadow_c", ShaderProgram::class.java)
    val textureShadowShaderProgram = assetManager.get("shadow_t", ShaderProgram::class.java)

    val shadowFrameBuffer = FrameBuffer(Pixmap.Format.RGBA8888,
            Gdx.graphics.width, Gdx.graphics.height, true)

    var width = Gdx.graphics.width.toFloat()
    var height = Gdx.graphics.height.toFloat()

    val modelBatchNormal = ModelBatch(object : DefaultShaderProvider(){
        override fun createShader(renderable : Renderable) : Shader {
            if(renderable.material.has(TextureAttribute.Diffuse)) {
                return SimpleTextureShader(renderable, textureShaderProgram)
            }
            else if (renderable.material.has(ColorAttribute.Diffuse)) {
                return SimpleColorShader(renderable, colorShaderProgram)
            }
            else {
                return super.createShader(renderable)
            }
        }
    })

    val modelBatchShadow = ModelBatch(object : DefaultShaderProvider(){
        override fun createShader(renderable: Renderable): Shader {
            if(renderable.material.has(TextureAttribute.Diffuse))
                return ShadowTextureMapShader(renderable, textureShadowShaderProgram, lights)
            else
                return ShadowMapShader(renderable, shadowShaderProgram, lights)
        }
    })

    val texIndex = 4

    private fun clear(){
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT);
    }

    fun forceUpdate(){
        for(light in lights){
            light.needsUpdate = true
        }
    }

    fun addLight(vararg l : ShLight){
        for(light in l){
            lights.add(light)
        }
    }

    fun render(call : (ModelBatch) -> Unit){
        renderCall = call
        shadowPass()
        mainPass()
    }

    var firstTime = true

    fun shadowPass(){

        val call = renderCall
        if(call != null) {
            for(light in lights){
                light.render(call)
            }

            shadowFrameBuffer.begin()
            clear()
            modelBatchShadow.begin(camera)
            call(modelBatchShadow)
            modelBatchShadow.end()
            shadowFrameBuffer.end()
            firstTime = false
        }
    }


    fun mainPass(){
        //bind shadowmap
        val call = renderCall
        if(call != null) {
            clear()
            shadowFrameBuffer.colorBufferTexture.bind(texIndex)

            textureShaderProgram.begin()
            textureShaderProgram.setUniformi("u_shadows", texIndex)
            textureShaderProgram.setUniformf("u_width", width)
            textureShaderProgram.setUniformf("u_height", height)
            textureShaderProgram.end()

            colorShaderProgram.begin()
            colorShaderProgram.setUniformi("u_shadows", texIndex)
            colorShaderProgram.setUniformf("u_width", width)
            colorShaderProgram.setUniformf("u_height", height)
            colorShaderProgram.end()


            modelBatchNormal.begin(camera)
            call(modelBatchNormal)
            modelBatchNormal.end()
        }
    }

    override fun dispose(){
        shadowFrameBuffer.dispose()
        shadowShaderProgram.dispose()
        textureShaderProgram.dispose()
        colorShaderProgram.dispose()
        textureShadowShaderProgram.dispose()
        modelBatchNormal.dispose()
        modelBatchShadow.dispose()
    }
}