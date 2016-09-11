package com.game.srpg.Units.Cursor

import com.game.srpg.Combat.AttackInstance
import com.game.srpg.GlobalSystems.AttackProcess
import com.game.srpg.GlobalSystems.ParallelStartEnd
import com.game.srpg.UI.CombatBox
import com.game.srpg.UI.HealthChange
import com.game.srpg.Units.Controller.AttackAnimationController
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/2/2016.
 */

class FastAttackAnimationAction(
        val cursor : Cursor,
        val unit : GameUnit,
        val enemy : GameUnit,
        val box : CombatBox,
        val attackIter : Iterator<AttackInstance>,
        var currentInstance : AttackInstance) : ImplementedCursorAction(){

        val attack = AttackProcess(cursor.parent, unit, enemy)
        init{
            //attack.doneCallback { attack.end(); cursor.switch(EndAction(cursor, unit, this)) }
        }

        override fun update(dt: Float) {
                attack.update(dt)
                if(attack.hasEnded()){
                        attack.end()
                        cursor.switch(EndAction(cursor, unit, this))
                }
        }
    /*
    init{
        currentInstance.attacker.controllers.addFirst(animation)
        currentInstance.defender.stats.current.health -= currentInstance.damage
    }

    var pair = ParallelStartEnd(animation, healthChange)

    override fun update(dt: Float) {
        if(pair.hasEnded() && attackIter.hasNext()){
            currentInstance.attacker.popController()
            currentInstance.defender.stats.current.health -= currentInstance.damage
            currentInstance = attackIter.next()
            animation = AttackAnimationController(
                    cursor.parent,
                    currentInstance.attacker.mapX(),
                    currentInstance.attacker.mapY(),
                    currentInstance.defender.mapX(),
                    currentInstance.defender.mapY(), .3f)
            healthChange = cursor.parent.ui.takeDamage(box,
                    currentInstance.defender.stats.current.health,
                    currentInstance.damage, currentInstance.defender)
            currentInstance.attacker.controllers.addFirst(animation)
            pair = ParallelStartEnd(animation, healthChange)
        }
        else if (pair.hasEnded() && !attackIter.hasNext()){
            cursor.switch(EndAction(cursor, unit, this))
        }
    }

    override fun onExit() {
        currentInstance.attacker.popController()
        //unit.controllers.removeFirst()
        cursor.parent.ui.removeCombatBox(box)
        if(currentInstance.defender.stats.current.health < 0){
            cursor.parent.killUnit(currentInstance.defender)
        }
        cursor.parent.playerHighlight.begin()
        cursor.parent.playerHighlight.end()
    }

*/

}