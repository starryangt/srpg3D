package com.game.srpg.Units.Controller

import com.game.srpg.GlobalSystems.BackAndForth
import com.game.srpg.GlobalSystems.Ends
import com.game.srpg.GlobalSystems.Interop
import com.game.srpg.Map.GameMap
import com.game.srpg.Map.MapCoordinates

/**
 * Created by FlyingJam on 9/1/2016.
 */

class AttackAnimationController(
        val map : GameMap,
        var startX : Int,
        var startY : Int,
        var endX : Int,
        var endY : Int,
        var length : Float,
        var yOffset : Float = 0f
        ) : UnitWorldController(), Ends {

    val interop = Interop.BackAndForth

    var currentWorldX = map.mapToWorldX(startX, 32)
    var currentWorldY = map.mapToWorldY(startY, 0)

    var startWorldX = currentWorldX
    var startWorldY = currentWorldY

    var endWorldX = map.mapToWorldX(endX, 32)
    var endWorldY = map.mapToWorldY(endY, 0)

    var accumulator = 0f
    var done = false

    override fun update(dt: Float) {
        accumulator += dt
        val alpha = accumulator / length
        if(alpha <= 1){
            currentWorldX = interop.apply(startWorldX, endWorldX, alpha)
            currentWorldY = interop.apply(startWorldY, endWorldY, alpha)
        }
        else{
            currentWorldX = startWorldX
            currentWorldY = startWorldY
            done = true
        }
    }

    override fun hasEnded() : Boolean{
        return done
    }

    override fun isDone(): Boolean {
        return done
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

}