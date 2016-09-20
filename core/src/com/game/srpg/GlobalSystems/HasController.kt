package com.game.srpg.GlobalSystems

import com.game.srpg.Map.MapCoordinates
import com.game.srpg.Units.Controller.MapController
import com.game.srpg.Units.Controller.UnitWorldController

/**
 * Created by FlyingJam on 9/11/2016.
 */

interface HasController : MapCoordinates {
    abstract fun getActiveController() : UnitWorldController
}