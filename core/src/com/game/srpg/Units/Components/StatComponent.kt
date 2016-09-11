package com.game.srpg.Units.Components

import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/22/2016.
 */


class Stats(
        var health : Int = 10,
        var move : Int = 5,
        var defense : Int = 5,
        var strength : Int = 7,
        var speed : Int = 5
) {

    fun copy() : Stats{
        return Stats(health, move, defense, strength, speed)
    }
}

class StatComponent(val base : Stats, val current : Stats = base.copy()) : Component(){

    override fun apply(unit: GameUnit) {}

    override fun update(dt: Float, unit: GameUnit) {}
}