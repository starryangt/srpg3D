package com.game.srpg.Map.Phase

import com.game.srpg.GlobalSystems.addListener
import com.game.srpg.GlobalSystems.removeListener
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Components.CardDrawingComponent
import com.game.srpg.Units.GameUnit

import com.badlogic.gdx.utils.Array
import com.game.srpg.Units.Cursor.DefaultAction
import com.game.srpg.Units.Cursor.NullAction

/**
 * Created by FlyingJam on 9/3/2016.
 */

class PlayerPhase(map : GameMap) : Phase(map){

    var players = map.getAllies()

    fun disabled(){
        var enabled = false
        for(player in players){
            enabled = enabled or player.enabled
            print(player.enabled)
        }
        println(players.size)
        if(!enabled){
            println("we're switching? da fuck $enabled")
            parent.switch(EnemyStartPhase(parent))
        }
    }

    override fun update(dt: Float) {

    }

    override fun onEnter() {
        players = parent.getAllies()
        addListener(parent.cursor)
        val draw = parent.cursor.drawing
        if(draw is CardDrawingComponent){
            parent.batch.add(draw.card)
        }
        parent.cursor.switch(DefaultAction(parent.cursor))
        for(player in players){
            player.enable()
        }
    }

    override fun onExit() {
        removeListener(parent.cursor)
        val draw = parent.cursor.drawing
        if(draw is CardDrawingComponent){
            parent.batch.remove(draw.card)
        }
        parent.cursor.switch(NullAction(parent.cursor))
        for(player in players){
            player.enable()
        }
    }

}
