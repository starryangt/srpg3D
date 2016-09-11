package com.game.srpg.Shadows

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/**
 * Created by FlyingJam on 8/16/2016.
 */

fun createShader(vertex : String, fragment : String) : ShaderProgram {
    val shader = ShaderProgram(Gdx.files.internal(vertex), Gdx.files.internal(fragment))
    if (!shader.isCompiled){
        val message = shader.log
        println("Fucked up: $message ")
        System.exit(0)
    }

    return shader
}
