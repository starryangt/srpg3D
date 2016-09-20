package com.game.srpg.GlobalSystems

import com.badlogic.gdx.utils.Array
import com.game.srpg.Combat.Combat
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Controller.AttackAnimationController
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/4/2016.
 */

class AttackProcess(val map : GameMap, val attacker : GameUnit, val defender : GameUnit) : Ends, EndCallback{

    val prediction = Combat.prediction(attacker, defender)
    val report = Combat.attack(attacker, defender)

    var attackIter = report.attacks.iterator()
    var currentInstance = attackIter.next()
    var box = map.ui.spawnCombatBox(prediction)
    var healthChange = map.ui.takeDamage(box, currentInstance.defender.stats.current.health, currentInstance.damage, currentInstance.defender)
    var animation = AttackAnimationController(map, attacker.mapX(), attacker.mapY(), defender.mapX(), defender.mapY(), 0.3f)
    var pair = ParallelStartEnd(animation, healthChange)

    init{
        currentInstance.attacker.addController(animation)
        //currentInstance.defender.stats.current.health -= currentInstance.damage
    }

    val callbacks = Array<() -> Unit>()

    var done = false

    init{
        println(report)
    }

    fun deadUnits() : GameUnit?{
        if(report.defenderDead())
            return report.defender
        else if (report.attackerDead())
            return report.attacker

        return null
    }

    fun update(dt : Float){
        if(pair.hasEnded() && attackIter.hasNext() && !done){
            currentInstance.attacker.popController()
            currentInstance.defender.stats.current.health -= currentInstance.damage
            currentInstance = attackIter.next()
            animation = AttackAnimationController(
                    map,
                    currentInstance.attacker.mapX(),
                    currentInstance.attacker.mapY(),
                    currentInstance.defender.mapX(),
                    currentInstance.defender.mapY(),
                    .3f)
            healthChange = map.ui.takeDamage(box, currentInstance.defender.stats.current.health,
                    currentInstance.damage, currentInstance.defender)
            currentInstance.attacker.addController(animation)
            pair.clear()
            pair.add(animation, healthChange)
        }
        else if (pair.hasEnded() && !attackIter.hasNext()){
            done = true
            doneEvent()
        }
    }

    fun end(){
        currentInstance.defender.stats.current.health -= currentInstance.damage
        currentInstance.attacker.popController()
        map.ui.removeCombatBox(box)
    }

    override fun doneCallback(callback: () -> Unit) {
        callbacks.add(callback)
    }

    override fun doneEvent() {
        callbacks.forEach { it() }
    }


    override fun hasEnded(): Boolean {
        return done
    }
}