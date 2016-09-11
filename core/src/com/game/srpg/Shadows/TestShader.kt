package com.game.srpg.Shadows

import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/**
 * Created by FlyingJam on 8/18/2016.
 */

class TestShader(var renderable : Renderable, p : ShaderProgram) : DefaultShader(renderable){
    init{
        this.program = p
    }

}