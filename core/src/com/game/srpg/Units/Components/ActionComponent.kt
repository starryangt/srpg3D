package com.game.srpg.Units.Components

import com.game.srpg.Units.Cursor.Cursor
import com.game.srpg.Units.Cursor.CursorAction
import com.game.srpg.Units.GameUnit
import java.util.*

/**
 * Created by FlyingJam on 8/25/2016.
 */

class ActionComponent(vararg constructors : (Cursor, GameUnit, CursorAction) -> CursorAction) : Component(){

    val actions = ArrayList<(Cursor, GameUnit, CursorAction) -> CursorAction>()

    init{
        for(constructor in constructors){
            actions.add(constructor)
        }
    }

    override fun apply(unit: GameUnit) {
    }

    override fun update(dt: Float, unit: GameUnit) {
    }

    fun initialize(cursor : Cursor, unit : GameUnit, previous : CursorAction) : Array<CursorAction>{
        return actions.map { it(cursor, unit, previous) }.toTypedArray()
    }

    fun add(vararg constructors : (Cursor, GameUnit, CursorAction) -> CursorAction){
        for(constructor in constructors){
            actions.add(constructor)
        }
    }
}