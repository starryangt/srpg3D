package com.game.srpg.Shadows;


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector3
import com.game.srpg.GlobalSystems.Globals

/**
 * Created by FlyingJam on 8/5/2016.
 */

class DirectionalLight(assetManager : AssetManager, position : Vector3, val direction : Vector3) : ShLight(assetManager){

    init{
        this.position = position
        init()
    }

    val frameBuffer = FrameBuffer(
            Pixmap.Format.RGB888,
            Globals.Shadow.ShadowMapWidth,
            Globals.Shadow.ShadowMapHeight,
            true)
    var depthMap : Texture? = null


    override fun applyToShader(sceneShader: ShaderProgram) {
        val depth = depthMap
        if(depth != null){
            depth.bind(shadowMapTex)
            sceneShader.setUniformi("u_depthMap", shadowMapTex)
            sceneShader.setUniformi("u_type", 1)
            sceneShader.setUniformMatrix("u_lightTrans", camera.combined)
            sceneShader.setUniformf("u_cameraFar", camera.far)
            sceneShader.setUniformf("u_lightPosition", camera.position)
            sceneShader.setUniformf("u_shadowIntensity", shadowIntensity)
            sceneShader.setUniformf("u_lightScaling", lightScaling)
            sceneShader.setUniformf("u_roundError", roundError)
        }
    }

    override fun init() {
        super.init()
        camera = PerspectiveCamera(
                Globals.Shadow.LightDirectionalDefaultFOV,
                Globals.Shadow.LightCameraWidth,
                Globals.Shadow.LightCameraHeight)
        camera.near = 1f
        camera.far = Globals.Shadow.LightDefaultFar
        camera.position.set(position)
        camera.lookAt(direction)
        camera.update()
    }

    override fun render(renCall : (ModelBatch) -> Unit){

        if(!needsUpdate){
            return
        }

        frameBuffer.begin()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT);

        shaderProgram.begin()
        shaderProgram.setUniformf("u_cameraFar", camera.far)
        shaderProgram.setUniformf("u_lightPosition", camera.position)
        shaderProgram.end()


        shaderTextureProgram.begin()
        shaderTextureProgram.setUniformf("u_cameraFar", camera.far)
        shaderTextureProgram.setUniformf("u_lightPosition", camera.position)
        shaderTextureProgram.end()


        modelBatch.begin(camera)
        renCall(modelBatch)
        modelBatch.end()

        frameBuffer.end()
        depthMap = frameBuffer.colorBufferTexture
        needsUpdate = false
    }

}