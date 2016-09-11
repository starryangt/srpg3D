package com.game.srpg.Combat

import com.game.srpg.GlobalSystems.clamp
import com.game.srpg.Units.GameUnit
import com.game.srpg.Units.Items.Item
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException

/**
 * Created by FlyingJam on 8/26/2016.
 */

class Combat{
    companion object obj {

        val speedThreshold = 5


        fun attack(attacker: GameUnit, defender: GameUnit) : CombatReport{
            val report = CombatReport(attacker, defender)
            val faster =
                    if(attacker.stats.current.speed > defender.stats.current.speed)
                        attacker
                    else if (defender.stats.current.speed > attacker.stats.current.speed)
                        defender
                    else
                        null

            val slower =
                    if(attacker.stats.current.speed < defender.stats.current.speed)
                        attacker
                    else if (defender.stats.current.speed < attacker.stats.current.speed)
                        defender
                    else
                        null

            report.add(attackInstance(attacker, defender))
            if(report.defenderDead())
                return report

            report.add(attackInstance(defender, attacker))
            if(report.attackerDead())
                return report

            if(faster != null && slower != null){
                var fasterMargin = faster.stats.current.speed - slower.stats.current.speed
                while(fasterMargin > speedThreshold){
                    report.add(attackInstance(faster, slower))
                    if(report.queryDead(faster, slower))
                        return report
                    fasterMargin -= speedThreshold
                }
            }

            return report
        }

        fun prediction(attacker: GameUnit, defender: GameUnit) : Prediction{
            val faster =
                    if(attacker.stats.current.speed > defender.stats.current.speed)
                        attacker
                    else if (defender.stats.current.speed > attacker.stats.current.speed)
                        defender
                    else
                        null

            val slower =
                    if(attacker.stats.current.speed < defender.stats.current.speed)
                        attacker
                    else if (defender.stats.current.speed < attacker.stats.current.speed)
                        defender
                    else
                        null


            val damageToEnemy = predictionDamageCalculation(attacker, defender)
            val damageToAlly = predictionDamageCalculation(defender, attacker)
            if(faster != null && slower != null) {
                var fasterMargin = faster.stats.current.speed - slower.stats.current.speed
                val times = fasterMargin / speedThreshold
                if(faster == attacker){
                    val allyInstance = PredictionInstance(attacker, defender, damageToEnemy, times, 100, 10)
                    val enemyInstance = PredictionInstance(defender, attacker, damageToAlly, 1, 100, 10)
                    return Prediction(attacker, defender, allyInstance, enemyInstance)
                }
                if(faster == defender){
                    val allyInstance = PredictionInstance(attacker, defender, damageToEnemy, 1, 100, 10)
                    val enemyInstance = PredictionInstance(defender, attacker, damageToAlly, times, 100, 10)
                    return Prediction(attacker, defender, allyInstance, enemyInstance)
                }
            }

            return Prediction(attacker, defender,
                    PredictionInstance(attacker, defender, damageToEnemy, 1, 100, 10),
                    PredictionInstance(defender, attacker, damageToAlly, 1, 100, 10))


        }

        fun attackInstance(attacker : GameUnit, defender : GameUnit) : AttackInstance{
            val weapon = attacker.inventory.equipped
            if(weapon is Item.Weapon){
                val damage = clamp(attacker.stats.current.strength + weapon.damage - defender.stats.current.defense, 0, 99)
                return AttackInstance(attacker, defender, damage)
            }
            return AttackInstance(attacker, defender, 0)
        }

        fun damageCalculation(attacker : GameUnit, defender : GameUnit){

        }

        fun predictionDamageCalculation(attacker: GameUnit, defender: GameUnit) : Int{
            val weapon = attacker.inventory.equipped
            if(weapon is Item.Weapon){
                val damage = clamp(attacker.stats.current.strength + weapon.damage - defender.stats.current.defense, 0, 99)
                return damage
            }
            return 0
        }

        //Attacker attacks first
        //every time
    }

}