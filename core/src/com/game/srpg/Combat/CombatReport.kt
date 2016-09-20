package com.game.srpg.Combat

import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/26/2016.
 */

class Effect{

}

class AttackInstance(
        val attacker : GameUnit,
        val defender : GameUnit,
        val damage : Int,
        val ally : Boolean = true,
        vararg effect : Effect
){
    val effects : Array<Effect> = effect.map{ it }.toTypedArray()

    fun apply(){
    }

    override fun toString(): String {
        return "$attacker attacked $defender for $damage"
    }
}

class CombatReport(val attacker : GameUnit, val defender : GameUnit, vararg attack : AttackInstance){
    val attacks = attack.toMutableList()

    val attackerHealth = attacker.stats.current.health
    val defenderHealth = defender.stats.current.health

    fun applyToUnits(){
        for(attack in attacks){
            attack.apply()
        }
    }

    fun add(attack : AttackInstance){
        attacks.add(attack)
    }

    fun attackerDamage() : Int{
        return attacks.filter { it.attacker == attacker }.sumBy { it.damage }
    }

    fun defenderDamage() : Int{
        return attacks.filter { it.attacker == defender }.sumBy { it.damage }
    }

    fun defenderDead() : Boolean{
        return attackerDamage() >= defenderHealth
    }

    fun attackerDead() : Boolean{
        return defenderDamage() >= attackerHealth
    }

    fun queryDead(a: GameUnit, d: GameUnit) : Boolean{
        val damage = attacks.filter {it.attacker == a }.sumBy { it.damage }
        return damage >= d.stats.current.health
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for(attack in attacks){
            builder.appendln(attack.toString())
        }
        return builder.toString()
    }
}

data class PredictionInstance(val attacker : GameUnit, val defender : GameUnit, val damage : Int, val number : Int, val hit : Int, val crit : Int){

}

class Prediction(val attacker : GameUnit, val defender : GameUnit, val attackerInfo : PredictionInstance, val defenderInfo : PredictionInstance)