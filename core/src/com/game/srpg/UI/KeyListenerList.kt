package com.game.srpg.UI

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.srpg.GlobalSystems.ActionListener
import com.game.srpg.GlobalSystems.Actions
import com.game.srpg.GlobalSystems.Event

/**
 * Created by FlyingJam on 9/15/2016.
 */

open class KeyListenerList<T>(skin : Skin) : ActionListener {
    val list = List<T>(skin)
    val activateEvent = Event<T>()
    fun moveDown(){
        list.selectedIndex =
                if(list.selectedIndex + 1 < list.items.size)
                    list.selectedIndex + 1
                else
                    0
    }

    fun moveUp(){
        list.selectedIndex =
                if(list.selectedIndex - 1 >= 0)
                    list.selectedIndex - 1
                else
                    list.items.size - 1
    }

    open fun activate(item : T){
        activateEvent.trigger(item)
    }

    override fun keyDown(input: Actions): Boolean {
        when(input){
            Actions.DOWN -> moveDown()
            Actions.UP -> moveUp()
            Actions.ACTIVATE -> activate(list.selected)
        }
        return false
    }
}