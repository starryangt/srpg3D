package com.game.srpg.GlobalSystems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Array

/**
 * Created by FlyingJam on 8/13/2016.
 */

fun glEnableDepth(){
    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
    Gdx.gl20.glDepthMask(true)
}

fun glClear(){
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_STENCIL_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
}

fun getFrames(texture : Texture, width : Int, height : Int, size : Int) : Array<TextureRegion>{
    //TODO: make not shitty
    val temp = TextureRegion.split(texture, width, height)
    val array = Array<TextureRegion>(size)
    for(x in 0..size-1){
        array.add(temp[0][x])
    }
    return array
}

fun clamp(value : Int, min : Int, max : Int) : Int{
    return Math.max(min, Math.min(max, value))
}

class ActualLerp : Interpolation(){
    override fun apply(a: Float): Float {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun apply(start: Float, end: Float, a: Float): Float {
        return MathUtils.lerp(start, end, a)
    }
}

class BackAndForth : Interpolation(){
    override fun apply(a: Float): Float {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun apply(start: Float, end: Float, a: Float): Float {
        if(a <= 0.5){
            return MathUtils.lerp(start, end, a * 2)
        }
        else{
            return MathUtils.lerp(end, start, a * 2 - 1)
        }
    }
}

class Interop{
    companion object static{
        val ActualLerp = com.game.srpg.GlobalSystems.ActualLerp()
        val BackAndForth = com.game.srpg.GlobalSystems.BackAndForth()
    }
}