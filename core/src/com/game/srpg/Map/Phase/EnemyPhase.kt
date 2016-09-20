package com.game.srpg.Map.Phase

import com.badlogic.gdx.utils.Array
import com.game.srpg.AI.AI
import com.game.srpg.AI.Action.AIAction
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/3/2016.
 */

class EnemyPhase(map : GameMap) : Phase(map){

    val enemies = map.getEnemies()
    val enemyIter = Array.ArrayIterator(enemies)
    internal lateinit var currentEnemy : GameUnit
    internal lateinit var currentActions : AIAction

    init{
    }

    override fun update(dt: Float) {
        val iter = enemyIter
        currentActions.update(dt)
        if(iter.hasNext() && currentActions.hasEnded()){
            currentActions.end(currentEnemy, parent)
            currentEnemy.disable()
            currentEnemy = iter.next()
            parent.camController.unit = currentEnemy
            currentActions = generateAIActions(currentEnemy)
            currentActions.begin(currentEnemy, parent)
        }
        else if (!iter.hasNext() && currentActions.hasEnded()){
            currentActions.end(currentEnemy, parent)
            currentEnemy.disable()
            parent.switch(PlayerStartPhase(parent))
        }
    }

    fun generateAIActions(enemy : GameUnit) : AIAction{
        val actions = AI.attackPhase(enemy, parent)
        actions.sort { a1, a2 ->  val v1 = a1.calculateValue(); val v2 = a2.calculateValue(); if(v1 > v2) -1 else if (v2 > v1) 1 else 0}
        return actions.first()
    }

    override fun onEnter() {
        currentEnemy = enemyIter.next()
        parent.camController.unit = currentEnemy
        currentActions = generateAIActions(currentEnemy)
        currentActions.begin(currentEnemy, parent)
    }

    override fun onExit() {
        for(unit in enemies){
            unit.enable()
        }
    }

}