package com.game.srpg.AI.Action

import com.game.srpg.Combat.Prediction
import com.game.srpg.GlobalSystems.*
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Controller.AttackAnimationController
import com.game.srpg.Units.Controller.PathAnimationController
import com.game.srpg.Units.Controller.UnitWorldController
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/3/2016.
 */

abstract class AIAttack(val prediction: Prediction, val x : Int, val y : Int) : ChainableAction(){

    val damage = prediction.attackerInfo.damage * prediction.attackerInfo.number
    val health = prediction.defender.stats.current.health
    val kill = damage >= health
    val length = Math.abs(prediction.attacker.mapX() - x) + Math.abs(prediction.attacker.mapY() - y)
    val value = calculateValue()

    override fun calculateValue(): Float {
        var value = 0f
        if(kill){
            value += Globals.AIFactors.Kill
        }
        value += prediction.attackerInfo.damage * Globals.AIFactors.PerDamage
        value += length * Globals.AIFactors.PerUnitMoved
        return value
    }

    override fun toString(): String {
        return "Moving to $x - $y to hit enemy for $damage, length: $length, worth: $value"
    }
}

class AIFastAttack(prediction: Prediction, x: Int, y: Int) : AIAttack(prediction, x, y){

    internal lateinit var attack : AttackProcess
    var death : DeathProcess? = null

    var ended = false
    override fun update(dt: Float) {
        if(!attack.hasEnded())
            attack.update(dt)
        death?.update(dt)
    }

    override fun begin(unit: GameUnit, map: GameMap) {
        attack = AttackProcess(map, prediction.attacker, prediction.defender)
        attack.doneCallback {
            val deadUnit = attack.deadUnits()
            if(deadUnit != null){
                val dead = DeathProcess(deadUnit, Globals.DeathFadeTime)
                dead.end.add { super.end(unit, map); ended = true }
                death = dead
            }
            else{
                ended = true
            }
        }
    }

    override fun end(unit: GameUnit, map: GameMap) {
        attack.end()
        super.end(unit, map)
    }

    override fun hasEnded(): Boolean {
        return ended
    }
}