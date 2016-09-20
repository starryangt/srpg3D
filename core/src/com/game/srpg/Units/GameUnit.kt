package com.game.srpg.Units

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.game.srpg.GlobalSystems.HasController
import com.game.srpg.Map.MapCoordinates
import com.game.srpg.Units.Components.*
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
               val actions : ActionComponent) : HasController{

    val controllers = ArrayDeque<UnitWorldController>()
    val inventory = Inventory()

    var enabled = true
    var dead = false

    init{
        controllers.addFirst(controller)
    }

    fun addController(controller: UnitWorldController){
        controllers.addFirst(controller)
    }

    fun popController(){
        if(controllers.size > 1) {
            controllers.removeFirst()
        }
    }

    fun disable(){
        drawing.requestColor(Color(0.3f, 0.3f, 0.3f, 1.0f))
        enabled = false
    }

    fun enable(){
        drawing.requestColor(Color(1f, 1f, 1f, 1f))
        enabled = true
    }

    override fun mapX(): Int {
        return x
    }

    override fun mapY(): Int {
        return y
    }

    override fun getActiveController(): UnitWorldController {
        return controllers.peekFirst()
    }

    fun update(dt : Float){
        val controller = getActiveController()
        controller.update(dt)
        drawing.update(dt, this)
        drawing.apply(this)
    }

    override fun toString(): String {
        return name
    }
}

