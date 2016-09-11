package com.game.srpg.Units.Cursor

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.srpg.GlobalSystems.addListener
import com.game.srpg.GlobalSystems.removeListener
import com.game.srpg.UI.ActionList
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/25/2016.
 */

class SelectAction(val cursor : Cursor, val unit : GameUnit, val previous : CursorAction) : ImplementedCursorAction(){

    val skin = cursor.parent.assets.assetManager.get("uiskin.json", Skin::class.java)
    val x = cursor.controllers.peekFirst().worldX(cursor)
    val y = cursor.controllers.peekFirst().worldZ(cursor)
    val ui = cursor.parent.screen.ui

    val coords = cursor.parent.screen.camera.project(Vector3(x, 0f, y))
    val list = ActionList(skin, unit, cursor, this, coords.x + 70, coords.y)

    init{

    }

    override fun onEnter() {
        cursor.moveCommand(cursor.mapX(), cursor.mapY(), unit.mapX(), unit.mapY())
        cursor.x = unit.x
        cursor.y = unit.y
        ui.stage.addActor(list.container)
        unit.drawing.requestState("idle")
        addListener(list)
    }

    override fun onExit() {
        cursor.disable = false
        list.container.remove()
        removeListener(list)
    }

    override fun update(dt: Float) {
    }

    override fun moved(x: Int, y: Int) {
        cursor.disable = true
    }

    override fun cancel() {
        cursor.switch(previous)
    }
}