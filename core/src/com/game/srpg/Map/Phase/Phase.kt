package com.game.srpg.Map.Phase

import com.game.srpg.Map.GameMap

/**
 * Created by FlyingJam on 9/3/2016.
 */

abstract class Phase(val parent : GameMap){
    abstract fun update(dt : Float)
    abstract fun onEnter()
    abstract fun onExit()
}