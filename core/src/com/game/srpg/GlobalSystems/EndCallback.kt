package com.game.srpg.GlobalSystems

import com.badlogic.gdx.utils.Array
/**
 * Created by FlyingJam on 9/11/2016.
 */

class EndCallbackManager(){
    val callbacks = Array<() -> Unit>()

    fun add(callback : () -> Unit){
        callbacks.add(callback)
    }

    fun trigger(){
        for(callback in callbacks){
            callback()
        }
    }
}