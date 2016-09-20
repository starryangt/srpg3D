package com.game.srpg.AI.Action

import com.game.srpg.GlobalSystems.Ends
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.GameUnit
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Queue

/**
 * Created by FlyingJam on 9/3/2016.
 */

abstract class AIAction : Ends {
    abstract fun calculateValue(): Float
    abstract fun begin(unit: GameUnit, map: GameMap)
    abstract fun end(unit: GameUnit, map: GameMap)
    abstract fun update(dt : Float)
}

abstract class ChainableAction(vararg action : AIAction) : AIAction(){

    var next : AIAction? = null

    open fun chain(action : AIAction){
        next = action
    }

    override fun end(unit: GameUnit, map: GameMap) {
        next?.begin(unit, map)
    }

}

class NothingAction() : AIAction(){
    override fun begin(unit: GameUnit, map: GameMap) {
    }

    override fun end(unit: GameUnit, map: GameMap) {
    }

    override fun calculateValue(): Float {
        return 0f
    }

    override fun update(dt: Float) {
    }

    override fun hasEnded(): Boolean {
        return true
    }
}
class SequenceAction(val unit : GameUnit, val map : GameMap, vararg action : AIAction) : AIAction(){

    val actions = Queue<AIAction>()

    init{
        for(act in action){
            actions.addFirst(act)
        }
    }

    var done = false

    override fun begin(unit: GameUnit, map: GameMap) {
        actions.last().begin(unit, map)
    }

    override fun end(unit: GameUnit, map: GameMap) {
    }

    override fun update(dt: Float) {
        actions.last()?.update(dt)
        if(actions.last().hasEnded() && actions.size >= 1){
            actions.last().end(unit, map)
            actions.removeLast()
            if(actions.size >= 1){
                actions.last().begin(unit, map)
            }
            else{
                done = true
            }
        }
    }

    override fun hasEnded(): Boolean {
        return done
    }

    override fun calculateValue(): Float {
        return actions.fold(0f) {r : Float , current -> r + current.calculateValue()}
    }
}

