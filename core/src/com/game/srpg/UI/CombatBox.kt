package com.game.srpg.UI

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.game.srpg.Combat.CombatReport
import com.game.srpg.Combat.Prediction

/**
 * Created by FlyingJam on 8/27/2016.
 */

class CombatBox(val prediction : Prediction, val skin : Skin) : Container<Table>(){


    val allyColumn = VerticalGroup()
    val enemyColumn = VerticalGroup()

    val ally = prediction.attacker
    val allyName = Label(prediction.attacker.name, skin)
    val allyNumber = prediction.attackerInfo.number
    val allyDamage = Label(prediction.attackerInfo.damage.toString() + "x$allyNumber", skin)
    val allyHit = Label(prediction.attackerInfo.hit.toString(), skin)
    val allyHP = Healthbar(skin, prediction.attacker)

    val enemy = prediction.defender
    val enemyName = Label(prediction.defender.name, skin)
    val enemyHit = Label(prediction.defenderInfo.hit.toString(), skin)
    val enemyNumber = prediction.defenderInfo.number
    val enemyDamage = Label(prediction.defenderInfo.damage.toString() + "x$enemyNumber", skin)
    val enemyHP = Healthbar(skin, prediction.defender)

    val table = Table()

    val bothGroup = HorizontalGroup()
    val button = Button(skin)

    init{

        allyColumn.addActor(allyName)
        allyColumn.addActor(allyDamage)
        allyColumn.addActor(allyHit)

        enemyColumn.addActor(enemyName)
        enemyColumn.addActor(enemyDamage)
        enemyColumn.addActor(enemyHit)

        button.setSize(allyColumn.prefWidth, allyColumn.prefHeight)

        //table.add(allyColumn).left().padRight(1280f/2 - allyColumn.prefWidth)
        //table.add(enemyColumn).right().padLeft(1280f/2 - enemyColumn.prefWidth)
        allyHP.width = Gdx.graphics.width / 2 - allyColumn.prefWidth
        enemyHP.width = Gdx.graphics.width / 2 - enemyColumn.prefWidth
        table.add(allyColumn).left()
        table.add(allyHP).width(allyHP.width)
        table.add(enemyHP).width(enemyHP.width)
        table.add(enemyColumn).right()
        table.background = button.background
        //table.debug = true
        this.actor = table
        this.fill()
    }

}