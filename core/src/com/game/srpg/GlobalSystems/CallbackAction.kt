package com.game.srpg.GlobalSystems

import com.badlogic.gdx.scenes.scene2d.Action

/**
 * Created by FlyingJam on 9/6/2016.
 */

class CallbackAction : Action() {

    var callback : ((Unit) -> Unit)? = null

    override fun act(delta: Float): Boolean {
        val call = callback
        if (call != null){
            call(Unit)
        }
        return true
    }
}