package com.game.srpg.Units.Items

import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Components.UnitType
import com.game.srpg.Units.GameUnit


/**
 * Created by FlyingJam on 8/2/2016.
 */

abstract class Range{
    abstract fun condition(map : GameMap, x : Int, y : Int, target: UnitType = UnitType.ENEMY) : Boolean
    abstract fun eligibleSquares(map : GameMap, x : Int, y : Int, target : UnitType = UnitType.ENEMY) : Array<Int>
    abstract fun unfilteredEligibleSquares(map : GameMap, x : Int, y : Int, target : UnitType = UnitType.ENEMY) : Array<Int>
}

class StrictRange(val range : Int = 1) : Range(){

    override fun condition(map : GameMap, x : Int, y : Int, target : UnitType) : Boolean{

        val check = {x : GameUnit -> x.unitClass.type == target}
        val north = y - range
        val south = y + range
        val east = x + range
        val west = x - range

        return (map.isUnit(x, north, check)
                || map.isUnit(x, south, check)
                || map.isUnit(east, y, check)
                || map.isUnit(west, y, check))
    }


    override fun eligibleSquares(map : GameMap, x : Int, y : Int, target : UnitType) : Array<Int>{

        val check = {x : GameUnit -> x.unitClass.type == target}
        val north = y - range
        val south = y + range
        val east = x + range
        val west = x - range

        val possible = arrayOf(Pair(x, north), Pair(x, south), Pair(east, y), Pair(west, y)).filter{ map.isUnit(it.first, it.second, check) }.map { map.path.pointToIndex(it.first, it.second)}
        return possible.toTypedArray()
    }

    override fun unfilteredEligibleSquares(map: GameMap, x: Int, y: Int, target: UnitType): Array<Int> {
        val check = {x : GameUnit -> x.unitClass.type == target}
        val north = y - range
        val south = y + range
        val east = x + range
        val west = x - range

        val possible = arrayOf(Pair(x, north), Pair(x, south), Pair(east, y), Pair(west, y)).map { map.path.pointToIndex(it.first, it.second)}
        return possible.toTypedArray()
    }

}

