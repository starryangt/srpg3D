package com.game.srpg.Units.Cursor

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.game.srpg.GlobalSystems.ActionListener
import com.game.srpg.GlobalSystems.Actions
import com.game.srpg.Map.GameMap
import com.game.srpg.Map.MapCoordinates
import com.game.srpg.Units.Components.StaticCardComponent
import com.game.srpg.Units.Controller.MapController
import com.game.srpg.Units.Controller.MoveAnimationController
import com.game.srpg.Units.Controller.UnitWorldController
import java.util.*

/**
 * Created by FlyingJam on 8/20/2016.
 */

class Cursor(val parent : GameMap,
             masterAtlas : TextureAtlas,
             var x : Int = 0,
             var y : Int = 0) : MapCoordinates, ActionListener {

    enum class Direction {UP, DOWN, LEFT, RIGHT}

    val cursorSprite = masterAtlas.createSprite("cursor")
    val drawing = StaticCardComponent(cursorSprite)

    val controllers = ArrayDeque<UnitWorldController>(2)
    var disable = false

    val moveAnimationCache = MoveAnimationController(parent, 0, 0, 0, 0, 0f, 0f)

    var action : CursorAction = DefaultAction(this)

    init{
        controllers.addFirst(MapController(parent, 32f))
        drawing.card.setSize(32f, 32f)
    }


    fun update(dt : Float){
        val controller = controllers.peekFirst()
        controller.update(dt)
        drawing.card.setPosition(
                controller.worldX(this),
                controller.worldY(this),
                controller.worldZ(this))

        if(controller.isDone()){
            controllers.removeFirst()
            disable = false
            moved(x, y)
        }

        action.update(dt)
    }

    override fun mapX(): Int {
        return x
    }

    override fun mapY(): Int {
        return y
    }

    fun moved(x : Int, y : Int){
        //parent.playerHighlight.setTile(parent.path.pointToIndex(x, y))
        action.moved(x, y)
    }

    fun move(direction : Direction, amount : Int = 1){
        action.move(direction, amount)
        if(!disable) {
            val oldX = x
            val oldY = y
            when (direction) {
                Direction.UP -> y += amount
                Direction.DOWN -> y -= amount
                Direction.LEFT -> x -= amount
                Direction.RIGHT -> x += amount
            }
            moveCommand(oldX, oldY, x, y)
        }
    }

    fun moveCommand(oldX : Int, oldY : Int,  newX : Int, newY : Int){
        moveAnimationCache.set(oldX, oldY, newX, newY, 0.125f, 32f)
        controllers.addFirst(moveAnimationCache)
        disable = true
    }

    fun activate(){
        action.activate()
    }

    fun switch(newState : CursorAction){
        action.onExit()
        action = newState
        action.onEnter()
    }

    fun lightSwitch(newState: CursorAction){
        action = newState
    }

    fun cancel(){
        action.cancel()
    }

    override fun keyDown(input: Actions): Boolean {
        when(input){
            Actions.UP -> move(Direction.UP)
            Actions.DOWN -> move(Direction.DOWN)
            Actions.LEFT -> move(Direction.LEFT)
            Actions.RIGHT -> move(Direction.RIGHT)
            Actions.ACTIVATE -> activate()
            Actions.CANCEL -> cancel()
        }
        return false
    }

}