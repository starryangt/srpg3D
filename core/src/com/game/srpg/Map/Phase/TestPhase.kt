package com.game.srpg.Map.Phase

import com.game.srpg.AI.Action.AIMove
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Cursor.MoveAction
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/13/2016.
 */

class TestPhase(val unit : GameUnit, map : GameMap) : Phase(map){

    init{
        val action = AIMove(0, 0, 10, 10)
        action.begin(unit, parent)
    }

    override fun update(dt: Float) {
    }

    override fun onExit() {
    }

    override fun onEnter() {
    }
}