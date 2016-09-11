package com.game.srpg.Units

import com.badlogic.gdx.maps.MapObject
import com.game.srpg.Map.MapCoordinates
import com.game.srpg.Units.Components.ActionComponent
import com.game.srpg.Units.Components.ClassComponent
import com.game.srpg.Units.Components.DrawingComponent
import com.game.srpg.Units.Components.StatComponent
import com.game.srpg.Units.Controller.MapController
import com.game.srpg.Units.Controller.UnitWorldController
import com.game.srpg.Units.Items.Inventory
import java.util.*

/**
 * Created by FlyingJam on 8/15/2016.
 */

class GameUnit(var x : Int,
               var y : Int,
               val name : String,
               controller : MapController,
               val stats : StatComponent,
               val drawing : DrawingComponent,
               val unitClass : ClassComponent,
               val actions : ActionComponent) : MapCoordinates{

    val controllers = ArrayDeque<UnitWorldController>()
    val inventory = Inventory()

    var enabled = true

    init{
        controllers.addFirst(controller)
    }

    fun addController(controller: UnitWorldController){
        controllers.addFirst(controller)
    }

    fun popController(){
        if(controllers.size >= 1) {
            controllers.removeFirst()
        }
    }

    fun disable(){
        enabled = false
    }

    fun enable(){
        enabled = true
    }

    override fun mapX(): Int {
        return x
    }

    override fun mapY(): Int {
        return y
    }

    fun update(dt : Float){
        val controller = controllers.peekFirst()
        controller.update(dt)
        drawing.update(dt, this)
        drawing.apply(this)
    }

    override fun toString(): String {
        return name
    }
}

