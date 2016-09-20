package com.game.srpg.Shadows;

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector3
import com.game.srpg.GlobalSystems.Globals

/**
 * Created by FlyingJam on 8/5/2016.
 */

class PointLight(assetManager: AssetManager, position : Vector3) : ShLight(assetManager){

    init{
        this.position = position
        init()
    }

    val frameBuffer = FrameBufferCubeMap(Pixmap.Format.RGBA8888, Globals.Shadow.ShadowMapWidth, true)
    var depthMap : Cubemap? = null

    override fun applyToShader(sceneShader: ShaderProgram) {
        //sceneShader.begin()
        val depth = depthMap
        if(depth != null){
            depth.bind(shadowMapCube)
            sceneShader.setUniformi("u_depthCube", shadowMapCube)
            sceneShader.setUniformi("u_type", 2)
            sceneShader.setUniformf("u_cameraFar", camera.far)
            sceneShader.setUniformf("u_lightPosition", camera.position)
            sceneShader.setUniformf("u_shadowIntensity", shadowIntensity)
            sceneShader.setUniformf("u_lightScaling", lightScaling)
            sceneShader.setUniformf("u_roundError", roundError)
        }
        //sceneShader.end()
    }

    override fun init() {
        super.init()
        camera = PerspectiveCamera(
                Globals.Shadow.LightPointDefaultFOV,
                Globals.Shadow.LightCameraWidth,
                Globals.Shadow.LightCameraHeight)
        camera.near = 1f
        camera.far = Globals.Shadow.LightDefaultFar
        camera.position.set(position)
        camera.update()
    }

    override fun render(renCall : (ModelBatch) -> Unit){
        if(!needsUpdate){
            return
        }

        shaderProgram.begin()
        shaderProgram.setUniformf("u_cameraFar", camera.far)
        shaderProgram.setUniformf("u_lightPosition", camera.position)
        shaderProgram.end()


        shaderTextureProgram.begin()
        shaderTextureProgram.setUniformf("u_cameraFar", camera.far)
        shaderTextureProgram.setUniformf("u_lightPosition", camera.position)
        shaderTextureProgram.end()


        for(i in 0..5){
            val side = Cubemap.CubemapSide.values()[i]
            frameBuffer.begin(side, camera)

            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(camera)
            renCall(modelBatch)
            modelBatch.end()
        }

        frameBuffer.end()
        depthMap = frameBuffer.colorBufferTexture
        needsUpdate = false

    }

}