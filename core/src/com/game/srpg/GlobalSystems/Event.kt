package com.game.srpg.GlobalSystems

import com.badlogic.gdx.utils.Array

/**
 * Created by FlyingJam on 9/17/2016.
 */

class Event<T>{


    val listeners = Array<(T) -> Boolean>()

    fun addListener(listener : (T) -> Boolean){
        listeners.add(listener)
    }

    fun trigger(args : T){
        for(listener in listeners){
            if(listener(args)) return
        }
    }
}

