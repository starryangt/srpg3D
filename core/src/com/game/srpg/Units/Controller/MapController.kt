package com.game.srpg.Units.Controller

import com.badlogic.gdx.maps.MapObject
import com.game.srpg.Map.GameMap
import com.game.srpg.Map.MapCoordinates
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/15/2016.
 */

class MapController(val map : GameMap, val yOffset : Float = 0f) : UnitWorldController(){

    override fun update(dt : Float) {}

    override fun worldX(obj : MapCoordinates) : Float{
        return map.mapToWorldX(obj.mapX(), 32)
    }

    override fun worldY(obj : MapCoordinates) : Float{
        return yOffset
    }

    override fun worldZ(obj : MapCoordinates): Float {
        return map.mapToWorldY(obj.mapY(), 0)
    }
}