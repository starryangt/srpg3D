package com.game.srpg.AI.Action

import com.game.srpg.GlobalSystems.Globals
import com.game.srpg.GlobalSystems.ParallelStartEnd
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Controller.MoveAnimationController
import com.game.srpg.Units.Controller.PathAnimationController
import com.game.srpg.Units.Controller.UnitWorldController
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/4/2016.
 */

class AIMove(val oldX : Int = 0, val oldY : Int = 0, val newX : Int = 0, val newY : Int = 0) : ChainableAction(){

    val pair = ParallelStartEnd()
    var anim : UnitWorldController? = null

    override fun end(unit: GameUnit, map: GameMap) {
        unit.popController()
        unit.x = newX
        unit.y = newY
        super.end(unit, map)
    }

    override fun begin(unit: GameUnit, map: GameMap) {
        val path = map.path.pathfind(oldX, oldY, newX, newY)
        val animation = PathAnimationController(path, unit.drawing, map, Globals.UnitMoveTime)
        anim = animation
        unit.addController(animation)
    }

    override fun calculateValue(): Float {
        var value = 0f
        val dist = Math.abs(oldX - newX) + Math.abs(oldY - newY)
        value += -dist * Globals.AIFactors.PerUnitMoved
        return value
    }

    override fun update(dt: Float) {
    }

    override fun hasEnded(): Boolean {
        return anim?.isDone() ?: false
    }
}
