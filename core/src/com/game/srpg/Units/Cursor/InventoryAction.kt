package com.game.srpg.Units.Cursor

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.srpg.GlobalSystems.addListener
import com.game.srpg.GlobalSystems.removeListener
import com.game.srpg.UI.InventoryList
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 9/17/2016.
 */

class InventoryAction(cursor : Cursor, unit : GameUnit, previous : CursorAction) : SelectMenuAction(cursor, unit, previous){
    val skin = cursor.parent.assets.assetManager.get("uiskin.json", Skin::class.java)
    val list = InventoryList(skin, unit, 100f, 100f)

    override fun onEnter() {
        cursor.parent.ui.stage.addActor(list.container)
        addListener(list.itemList)
        cursor.disable = true
    }

    override fun onExit() {
        list.container.remove()
        removeListener(list.itemList)
    }

    override fun condition(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Inventory"
    }
}