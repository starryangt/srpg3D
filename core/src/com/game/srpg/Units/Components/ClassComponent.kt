package com.game.srpg.Units.Components

import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/27/2016.
 */

enum class UnitType { ALLY, ENEMY, NEUTRAL }

class ClassComponent(val type : UnitType = UnitType.ALLY) : Component(){

    override fun apply(unit: GameUnit) {
    }

    override fun update(dt: Float, unit: GameUnit) {
    }
}
