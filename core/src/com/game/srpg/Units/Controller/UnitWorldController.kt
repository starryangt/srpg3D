package com.game.srpg.Units.Controller

import com.game.srpg.Map.MapCoordinates
import com.badlogic.gdx.utils.Array
import com.game.srpg.GlobalSystems.EndCallback

/**
 * Created by FlyingJam on 8/15/2016.
 */

abstract class UnitWorldController : EndCallback{
    val callbacks = Array<() -> Unit>()
    abstract fun worldX(obj : MapCoordinates) : Float
    abstract fun worldY(obj : MapCoordinates) : Float
    abstract fun worldZ(obj : MapCoordinates) : Float
    abstract fun update(dt : Float)
    open fun isDone() : Boolean { return false }

    override fun doneEvent(){
        for(callback in callbacks){
            callback()
        }
    }

    override fun doneCallback(callback : () -> Unit){
        callbacks.add(callback)
    }
}

