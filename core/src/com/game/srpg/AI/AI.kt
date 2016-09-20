package com.game.srpg.AI

import com.game.srpg.Map.GameMap
import com.game.srpg.Units.GameUnit
import com.badlogic.gdx.utils.Array
import com.game.srpg.AI.Action.*
import com.game.srpg.Combat.Combat
import com.game.srpg.Map.MapHighlight
import com.game.srpg.Units.Components.UnitType
import com.game.srpg.Units.Items.Item

/**
 * Created by FlyingJam on 9/3/2016.
 */

class AI{
    companion object static{
        fun attackPhase(unit : GameUnit, map : GameMap) : Array<AIAction>{
            val possible = map.path.movableRegion(unit.mapX(), unit.mapY(), unit.stats.current.move)
            val weapon = unit.inventory.equipped
            val attacks = Array<AIAction>()
            attacks.add(NothingAction())
            if(weapon is Item.Weapon){
                val attackRegion = map.path.attackRegion(map, possible, UnitType.ALLY, weapon)
                for(pair in attackRegion){
                    val enemy = map.getUnit(pair.key, { it.unitClass.type == UnitType.ALLY && !it.dead })
                    if(enemy != null) {
                        val possibilities = weapon.range.unfilteredEligibleSquares(map, enemy.mapX(), enemy.mapY())
                        for(possibility in possibilities){
                            if(attackRegion.containsKey(possibility) && attackRegion[possibility] == MapHighlight.Type.POSITION) {
                                val prediction = Combat.prediction(unit, enemy)
                                val x = map.path.indexToX(possibility)
                                val y = map.path.indexToY(possibility)
                                val action = AIMove(unit.mapX(), unit.mapY(), x, y)
                                val attack = AIFastAttack(prediction, x, y)
                                attacks.add(SequenceAction(unit, map, action, attack))
                                //attacks.add(action)
                            }
                        }
                    }
                }

            }
            return attacks
        }
    }
}