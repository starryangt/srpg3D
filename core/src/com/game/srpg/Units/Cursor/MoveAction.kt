package com.game.srpg.Units.Cursor

import com.badlogic.gdx.math.Vector2
import com.game.srpg.AI.Action.AIMove
import com.game.srpg.GlobalSystems.Globals
import com.game.srpg.Units.Controller.PathAnimationController
import com.game.srpg.Units.Controller.UnitWorldController
import com.game.srpg.Units.GameUnit
import java.util.*

/**
 * Created by FlyingJam on 8/22/2016.
 */

class MoveAction(val cursor : Cursor, val unit : GameUnit) : ImplementedCursorAction(){
    val hold = unit
    internal lateinit var movableRegion : ArrayList<Int>
    var releaseX = 0
    var releaseY = 0

    val oldX = unit.mapX()
    val oldY = unit.mapY()

    var moving = false

    override fun onEnter() {
        unit.x = oldX
        unit.y = oldY
        movableRegion = cursor.parent.path.movableRegion(hold.mapX(), hold.mapY(), unit.stats.current.move)
        cursor.parent.playerHighlight.setPath(movableRegion)
        unit.drawing.requestState("down")
    }

    override fun onExit() {
        unit.drawing.requestState("idle")
    }


    override fun update(dt : Float){
        if(unit.controllers.peekFirst().isDone()){
            //releaseUnit()
        }
    }

    override fun activate() {
        if(isReleasable() && !moving){
            releaseX = cursor.x
            releaseY = cursor.y
            if(releaseX == unit.x && releaseY == unit.y){
                startAnimation(releaseX, releaseY)
                //cursor.switch(SelectAction(cursor, unit, this))
            }
            else {
                startAnimation(releaseX, releaseY)
            }
        }
    }

    fun isReleasable() : Boolean{
        val index = cursor.parent.path.pointToIndex(cursor.mapX(), cursor.mapY())
        val pos = movableRegion.find {it == index}
        if(pos != null){
            return true
        }
        return false
    }

    fun releaseUnit(){
        unit.popController()
        unit.x = releaseX
        unit.y = releaseY
        cursor.switch(SelectAction(cursor, unit, this))
        moving = false
    }

    fun startAnimation(x : Int, y : Int){
        val path = cursor.parent.path.pathfind(unit.mapX(), unit.mapY(), x, y)
        val animation = PathAnimationController(path, unit.drawing, cursor.parent, Globals.UnitMoveTime)
        animation.doneCallback { releaseUnit() }
        unit.addController(animation)
        moving = true
    }

    override fun cancel() {
        cursor.switch(DefaultAction(cursor))
    }
}