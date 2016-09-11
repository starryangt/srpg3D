package com.game.srpg.Units.Components

import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/20/2016.
 */

abstract class Component{
    abstract fun update(dt : Float, unit : GameUnit)
    abstract fun apply(unit : GameUnit)
}