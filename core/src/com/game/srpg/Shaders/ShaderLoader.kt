package com.game.srpg.Shaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Array

/**
 * Created by FlyingJam on 8/21/2016.
 */

class ShaderParamater() : AssetLoaderParameters<ShaderProgram>(){

}

class ShaderLoader(resolver : FileHandleResolver)
: AsynchronousAssetLoader<ShaderProgram, ShaderParamater>(resolver){

    internal lateinit var fragProgram : String
    internal lateinit var vertProgram : String

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: ShaderParamater) {
        val vertPath = "shaders/$fileName/vert.glsl"
        val fragPath = "shaders/$fileName/frag.glsl"

        val vertFile = Gdx.files.internal(vertPath)
        val fragFile = Gdx.files.internal(fragPath)

        if(vertFile.exists() && fragFile.exists()){
            vertProgram = vertFile.readString()
            fragProgram = fragFile.readString()
        }
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: ShaderParamater): ShaderProgram {
        val shader = ShaderProgram(vertProgram, fragProgram)
        if(!shader.isCompiled){
            println(shader.log)
        }

        return shader
    }

    override fun getDependencies(fileName: String, file: FileHandle, parameter: ShaderParamater): Array<AssetDescriptor<Any>>? {
        return null
    }
}