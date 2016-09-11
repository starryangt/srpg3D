package com.game.srpg.UI

import com.badlogic.gdx.scenes.scene2d.ui.*
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/27/2016.
 */

class StatBox(val unit : GameUnit, val skin : Skin) : Container<Table>(){
    val table = Table(skin)
    val name = Label(unit.name, skin)

    val health = Label("Health: ", skin)
    val healthValue = Label(unit.stats.current.health.toString(), skin)

    val defense = Label("Defense: ", skin)
    val defenseValue = Label(unit.stats.current.defense.toString(), skin)

    val move = Label("Move: ", skin)
    val moveValue = Label(unit.stats.current.move.toString(), skin)

    val strength = Label("Strength: ", skin)
    val strengthValue = Label(unit.stats.current.strength.toString(), skin)

    val button = Button(skin)

    init{
        table.add(name).left()
        table.row()
        table.add(health).left()
        table.add(healthValue)
        table.row()
        table.add(defense).left()
        table.add(defenseValue)
        table.row()
        table.add(move).left()
        table.add(moveValue)
        table.row()
        table.add(strength).left()
        table.add(strengthValue)

        button.width = 300f
        button.height = 200f

        table.background = button.background

        table.width = 300f
        table.height = 200f
        table.y = table.y + 100
        //table.width = 600f
        //table.height = 900f
        //table.setDebug(true)
        this.actor = table
    }
}