package com.game.srpg.GlobalSystems
import com.badlogic.gdx.utils.Array

/**
 * Created by FlyingJam on 9/1/2016.
 */

interface Ends{
    abstract fun hasEnded() : Boolean
}

abstract class StartEnd{
    abstract fun hasEnded() : Boolean
}

class ParallelStartEnd(vararg endable : Ends) : StartEnd(){

    val endables = Array<Ends>()
    init{
        for(end in endable){
            endables.add(end)
        }
    }

    fun add(vararg endable : Ends){
        for(end in endable){
            endables.add(end)
        }
    }

    fun clear(){
        endables.clear()
    }

    override fun hasEnded() : Boolean{

        var ended = true
        for(end in endables){
            val e = end.hasEnded()
            ended = ended and end.hasEnded()
        }
        return ended
    }
}


class SequentialStartEnd(vararg endable : Ends) : StartEnd(){

    val endables = Array<Ends>()

    init{
        for(end in endable){
            endables.add(end)
        }
    }

    override fun hasEnded(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

interface EndCallback{
    fun doneCallback(callback : () -> Unit)
    fun doneEvent()
}