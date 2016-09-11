package com.game.srpg.Units.Cursor

import com.game.srpg.Combat.AttackInstance
import com.game.srpg.Combat.Combat
import com.game.srpg.Combat.CombatReport
import com.game.srpg.Map.MapHighlight
import com.game.srpg.UI.CombatBox
import com.game.srpg.Units.Controller.AttackAnimationController
import com.game.srpg.Units.GameUnit
import com.game.srpg.Units.Items.Item

/**
 * Created by FlyingJam on 8/25/2016.
 */

class AttackAction(cursor : Cursor, unit : GameUnit, previous : CursorAction) : SelectMenuAction(cursor, unit, previous){

    internal lateinit var validLocations : Array<Int>
    val highlight = cursor.parent.playerHighlight
    var target : GameUnit? = null
    var report : CombatReport? = null

    var combatBox : CombatBox? = null


    init{
        val weapon = unit.inventory.equipped
        if(weapon != null && weapon is Item.Weapon){
            validLocations = weapon.range.eligibleSquares(cursor.parent, unit.mapX(), unit.mapY())
        }
    }

    var index = 0

    override fun onEnter() {
        cursor.disable = true
        highlight.begin()
        highlight.colorPath(validLocations, MapHighlight.Type.ATTACK)
        highlight.end()

        setToPosition(validLocations[index])
    }

    override fun onExit() {
        val box = combatBox
        if(box != null){
            cursor.parent.ui.removeCombatBox(box)
        }

        //temp
        val enemy = target
        if(enemy != null && enemy.stats.current.health < 0){
            cursor.parent.killUnit(enemy)
        }
        highlight.begin()
        highlight.end()
    }

    override fun condition(): Boolean {
        val weapon = unit.inventory.equipped
        if(weapon != null && weapon is Item.Weapon){
            return weapon.range.condition(cursor.parent, unit.mapX(), unit.mapY())
        }
        return false
    }

    override fun toString(): String {
        return "Attack"
    }

    fun getUnit(location : Int) : GameUnit?{
        val x = cursor.parent.path.indexToX(location)
        val y = cursor.parent.path.indexToY(location)
        val unit = cursor.parent.getUnit(x, y)
        return unit
    }

    override fun activate() {
        val enemy = getUnit(validLocations[index])
        if(enemy != null) {
            val report = Combat.attack(unit, enemy)
            val attackIter = report.attacks.iterator()
            val currentInstance = attackIter.next()

            val box = combatBox
            if(box != null){
                cursor.lightSwitch(FastAttackAnimationAction(cursor, unit, enemy, box, attackIter, currentInstance))
            }
        }
    }

    override fun move(direction: Cursor.Direction, amount: Int) {
        val dir =
            when(direction){
                Cursor.Direction.UP -> 1
                Cursor.Direction.RIGHT -> 1
                Cursor.Direction.DOWN -> -1
                Cursor.Direction.LEFT -> -1
                else -> 1
            }
        index =
                if(index + dir > validLocations.size - 1 || index + dir < 0)
                    if(dir > 0) 0 else validLocations.size - 1
                else
                    index + dir

        val location = validLocations[index]
        setToPosition(location)
    }

    fun setToPosition(location : Int){
        val x = cursor.parent.path.indexToX(location)
        val y = cursor.parent.path.indexToY(location)

        val oldX = cursor.x
        val oldY = cursor.y

        cursor.x = x
        cursor.y = y
        cursor.moveCommand(oldX, oldY, x, y)
        val enemy = cursor.parent.getUnit(x, y)
        if(enemy != null){
            val prediction = Combat.prediction(unit, enemy)
            combatBox = cursor.parent.ui.spawnCombatBox(prediction)
            target = enemy
        }
    }

}