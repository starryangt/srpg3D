package com.game.srpg.Units.Cursor

import com.game.srpg.Map.Phase.PlayerPhase
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/25/2016.
 */

class EndAction(cursor : Cursor, unit : GameUnit, previous : CursorAction) : SelectMenuAction(cursor, unit, previous){
    override fun condition(): Boolean {
        return true
    }

    override fun onEnter() {
        unit.disable()
        cursor.switch(DefaultAction(cursor))
        val phase = cursor.parent.phase
        if(phase is PlayerPhase){
            phase.disabled()
        }
    }

    override fun toString(): String {
        return "End"
    }
}