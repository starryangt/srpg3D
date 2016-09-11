package com.game.srpg.Units.Controller

import com.badlogic.gdx.math.Interpolation
import com.game.srpg.GlobalSystems.ActualLerp
import com.game.srpg.Map.GameMap
import com.game.srpg.Map.MapCoordinates

/**
 * Created by FlyingJam on 8/20/2016.
 */

class MoveAnimationController(
        val map : GameMap,
        var startX : Int,
        var startY : Int,
        var endX : Int,
        var endY : Int,
        var length : Float,
        var yOffset : Float = 0f
        ) : UnitWorldController(){


    val interpolation = ActualLerp()
    var currentWorldX = map.mapToWorldX(startX, 32)
    var currentWorldY = map.mapToWorldY(startY, 0)

    var startWorldX = currentWorldX
    var startWorldY = currentWorldY

    var endWorldX = map.mapToWorldX(endX, 32)
    var endWorldY = map.mapToWorldY(endY, 0)


    var accumulator = 0f

    var done = false

    init{

    }

    override fun update(dt: Float) {
        accumulator += dt
        val alpha = accumulator / length
        if(alpha <= 1) {
            currentWorldX = interpolation.apply(startWorldX, endWorldX, alpha)
            currentWorldY = interpolation.apply(startWorldY, endWorldY, alpha)
        }
        else{
            currentWorldX = endWorldX
            currentWorldY = endWorldY
            done = true
            doneEvent()
        }
    }

    fun set(sx : Int, sy : Int, ex : Int, ey : Int, l : Float, yo : Float){
        startX = sx
        startY = sy
        endX = ex
        endY = ey
        length = l
        yOffset = yo

        currentWorldX = map.mapToWorldX(startX, 32)
        currentWorldY = map.mapToWorldY(startY, 0)

        startWorldX = currentWorldX
        startWorldY = currentWorldY

        endWorldX = map.mapToWorldX(endX, 32)
        endWorldY = map.mapToWorldY(endY, 0)

        accumulator = 0f
        done = false
    }

    override fun worldX(obj: MapCoordinates): Float {
        return currentWorldX
    }

    override fun worldY(obj: MapCoordinates): Float {
        return yOffset
    }

    override fun worldZ(obj: MapCoordinates): Float {
        return currentWorldY
    }

    override fun isDone() : Boolean{
        return done
    }


}