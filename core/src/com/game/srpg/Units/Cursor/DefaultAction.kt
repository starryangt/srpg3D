package com.game.srpg.Units.Cursor

import com.game.srpg.Map.MapHighlight
import com.game.srpg.UI.StatBox
import com.game.srpg.Units.Components.ClassComponent
import com.game.srpg.Units.Components.UnitType
import com.game.srpg.Units.Items.Item

/**
 * Created by FlyingJam on 8/21/2016.
 */

class DefaultAction(val cursor : Cursor) : ImplementedCursorAction(){

    var stat : StatBox? = null

    override fun onEnter() {
        //if(!playerHighlight()){
            //val highlight = cursor.parent.playerHighlight
            //highlight.begin()
            //highlight.colorTile(cursor.mapX(), cursor.mapY())
            //highlight.end()
        //}
    }

    override fun onExit() {

        val s = stat
        if(s != null){
            cursor.parent.ui.removeFriendlyStat(s)
        }

        val highlight = cursor.parent.playerHighlight
        highlight.begin()
        highlight.end()
    }

    override fun activate() {
        val unit = cursor.parent.getUnit(cursor.mapX(), cursor.mapY())
        if(unit != null && unit.unitClass.type == UnitType.ALLY && !unit.dead && unit.enabled){
            cursor.switch(MoveAction(cursor, unit))
        }
    }

    fun playerHighlight() : Boolean{
        val unit = cursor.parent.getUnit(cursor.mapX(), cursor.mapY())
        if (unit != null){

            val highlight = cursor.parent.playerHighlight
            val region = cursor.parent.path.movableRegion(unit.mapX(),
                    unit.mapY(), unit.stats.current.move)

            val weapon = unit.inventory.equipped
            if(weapon is Item.Weapon){
                val attackRegion = cursor.parent.path.attackRegion(cursor.parent, region, unit.unitClass.type, weapon)

                highlight.begin()
                for(pair in attackRegion){
                    highlight.colorTile(pair.key, pair.value)
                }
                highlight.end()
            }

            else{
                highlight.begin()
                highlight.colorPath(region)
                highlight.end()
            }

            stat = cursor.parent.ui.spawnFriendlyStat(unit, true)
            return true
        }
        else{
            val s = stat
            if(s != null) {
                cursor.parent.ui.removeFriendlyStat(s)
            }
        }
        return false
    }

    override fun moved(x: Int, y: Int) {
        if(!playerHighlight()){
            cursor.parent.playerHighlight.begin()
            cursor.parent.playerHighlight.colorTile(cursor.mapX(), cursor.mapY())
            cursor.parent.playerHighlight.end()
        }
    }

    override fun condition(): Boolean {
        return true
    }

    override fun toString(): String {
        return "End"
    }
}