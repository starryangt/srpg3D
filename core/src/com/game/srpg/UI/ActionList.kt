package com.game.srpg.UI

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.game.srpg.GlobalSystems.ActionListener
import com.game.srpg.GlobalSystems.Actions
import com.game.srpg.Units.Cursor.Cursor
import com.game.srpg.Units.Cursor.CursorAction
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/25/2016.
 */

class ActionList(skin : Skin,
                 val unit : GameUnit,
                 val cursor : Cursor,
                 previous : CursorAction,
                 x : Float,
                 y : Float) : KeyListenerList<CursorAction>(skin){
    val container = ScrollPane(list, skin)
    var changed = false

    override fun activate(item: CursorAction) {
        cursor.switch(item)
    }

    init{

        val actions = unit.actions.initialize(cursor, unit, previous).filter { it.condition() }.toTypedArray()
        val gdxArray = com.badlogic.gdx.utils.Array<CursorAction>(actions)
        list.setItems(gdxArray)
        //calculate position
        container.setPosition(x.toFloat(), y.toFloat())
        //list.setItems(DefaultAction(cursor))
        //list.setItems("Move", "Item", "End")
    }
}