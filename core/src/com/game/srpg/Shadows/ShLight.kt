package com.game.srpg.Shadows;

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector3

/**
 * Created by FlyingJam on 8/5/2016.
 */

abstract class ShLight(val assetManager: AssetManager,
                       var shadowIntensity : Float = 0.4f,
                       var lightScaling : Float = 0.6f,
                       var roundError : Float = 0.01f,
                       var lightPower : Float = 500f){

    internal lateinit var shaderProgram : ShaderProgram
    internal lateinit var shaderTextureProgram : ShaderProgram
    internal lateinit var modelBatch : ModelBatch

    var needsUpdate = true

    //light camera
    internal lateinit var camera : Camera

    //pos of light
    var position = Vector3()

    //Add uniforms to scene shader
    //so it sets the shadowmap and stuff
    abstract fun applyToShader(sceneShader : ShaderProgram)
    abstract fun render( renCall : (ModelBatch) -> Unit)


    open fun init(){
        shaderProgram = assetManager.get("depthmap_c", ShaderProgram::class.java)

        shaderTextureProgram = assetManager.get("depthmap_t", ShaderProgram::class.java)
        modelBatch = ModelBatch(object : DefaultShaderProvider(){
            override fun createShader(renderable : Renderable) : Shader {
                if(renderable.material.has(TextureAttribute.Diffuse)){
                    return DepthTextureMapShader(renderable, shaderTextureProgram)
                }
                else{
                    return DepthMapShader(renderable, shaderProgram)
                }
            }
        })
    }

    open fun setPower(power : Float){
        camera.far = power
        camera.update()
    }

    open fun dispose(){
        shaderProgram.dispose()
        shaderTextureProgram.dispose()
        modelBatch.dispose()
    }


}