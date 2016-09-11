package com.game.srpg.GlobalSystems

import com.badlogic.gdx.InputAdapter
import java.util.*

/**
 * Created by FlyingJam on 8/1/2016.
 */
enum class Actions { ACTIVATE, CANCEL, UP, DOWN, LEFT, RIGHT }

interface ActionListener{

    fun keyDown(input : Actions) : Boolean{
        return false
    }

    fun keyUp(input : Actions) : Boolean{
        return false
    }

    fun mouseMoved(screenX : Int, screenY : Int) : Boolean{
        return false
    }

    fun touchDown(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean{
        return false
    }

    fun touchUp(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean{
        return false
    }

}

val InputListeners = com.badlogic.gdx.utils.Array<ActionListener>()

val KeyMap = java.util.HashMap<Int, Actions>()

fun setKey(key : Int, action : Actions){
    KeyMap.set(key, action)
}

fun removeKey(key : Int){
    KeyMap.remove(key)
}

fun addListener(thing : ActionListener){
    InputListeners.add(thing)
}

fun removeListener(thing : ActionListener){
    InputListeners.removeValue(thing, true)
}

class InputConverter : InputAdapter() {

    override fun keyDown(input : Int) : Boolean{
        for(inputListeners in InputListeners){
            if(KeyMap.containsKey(input)){
                val action = KeyMap.get(input)
                if(action != null)
                    if(inputListeners.keyDown(action)) return true
            }
        }
        return false
    }

    override fun keyUp(input : Int) : Boolean{
        for(inputListeners in InputListeners){
            if(KeyMap.containsKey(input)){
                val action = KeyMap.get(input)
                if(action != null)
                    if(inputListeners.keyUp(action)) return true
            }
        }
        return false
    }

    override fun mouseMoved(screenX : Int, screenY : Int) : Boolean{
        for(inputListeners in InputListeners){
            if(inputListeners.mouseMoved(screenX, screenY)) return true
        }
        return false
    }

    override fun touchDown(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean{
        for(inputListeners in InputListeners){
            if(inputListeners.touchDown(screenX, screenY, pointer, button)) return true
        }
        return false
    }

    override fun touchUp(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean{
        for(inputListeners in InputListeners){
            if(inputListeners.touchUp(screenX, screenY, pointer, button)) return true
        }
        return false
    }

}

val inputConverter = InputConverter()
