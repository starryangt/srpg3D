package com.game.srpg.Units.Cursor

import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/21/2016.
 */
abstract class CursorAction(){
    abstract fun update(dt : Float)
    abstract fun activate()
    abstract fun cancel()
    abstract fun onEnter()
    abstract fun onExit()
    abstract fun condition() : Boolean
    abstract fun moved(x : Int, y : Int)
    abstract fun move(direction : Cursor.Direction, amount : Int)
}

open class ImplementedCursorAction() : CursorAction(){

    override fun update(dt : Float){

    }

    override fun activate(){

    }

    override fun cancel() {
    }

    override fun onEnter(){

    }

    override fun onExit(){

    }

    override fun condition() : Boolean{
        return false
    }

    override fun moved(x : Int, y : Int){

    }

    override fun move(direction: Cursor.Direction, amount: Int) {

    }
}

open class SelectMenuAction(val cursor : Cursor, val unit : GameUnit, val previous : CursorAction) : ImplementedCursorAction(){

    override fun cancel() {
        println("we're switching?")
        cursor.switch(previous)
    }

}


