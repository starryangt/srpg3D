package com.game.srpg.GlobalSystems

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Array
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/11/2016.
 */

class DeathProcess(val unit : GameUnit, val length : Float) : Ends{

    val interop = Interpolation.linear
    val end = EndCallbackManager()
    val fall = Interpolation.swingIn

    init{
    }

    var color = Color(0.3f, 0.3f, 0.3f, 1f)
    var ended = false

    var acc = 0f
    fun update(dt : Float){
        acc += dt
        val alpha = acc / length

        if(alpha > 2){
            unit.dead = true
            end.trigger()
            ended = true
        }
        if(alpha <= 1) {
            val angle = fall.apply(0f, -90f, alpha)
            unit.drawing.requestRotation(1f, 0f, 0f, angle)
            //unit.drawing.requestStop(true)
            unit.drawing.requestColor(color)
        }
        if(alpha > 1){
            color.a = interop.apply(1f, 0f, alpha - 1)
            unit.drawing.requestColor(color)
        }
    }

    override fun hasEnded(): Boolean {
        return ended
    }

}