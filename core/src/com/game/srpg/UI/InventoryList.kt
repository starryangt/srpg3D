package com.game.srpg.UI

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.srpg.Units.GameUnit
import com.game.srpg.Units.Items.Inventory
import com.game.srpg.Units.Items.Item
import com.badlogic.gdx.utils.Array

/**
 * Created by FlyingJam on 9/15/2016.
 */

class InventoryList(skin : Skin, val unit : GameUnit, x : Float, y : Float){
    val itemList = KeyListenerList<Item>(skin)
    val equippedNotice = KeyListenerList<String>(skin)
    val container2 = HorizontalGroup()
    init{
        container2.addActor(itemList.list)
        container2.addActor(equippedNotice.list)
    }

    val container = ScrollPane(container2, skin)

    init{
        itemList.activateEvent.addListener { activate(it) }
    }

    fun activate(item: Item) : Boolean{
        if(item is Item.Weapon){
            if(item != unit.inventory.equipped){
                unit.inventory.equip(item)
                updateDisplay(unit.inventory)
            }
            else {
                unit.inventory.unequip()
                updateDisplay(unit.inventory)
            }
        }
        else{

        }
        return false
    }

    fun updateDisplay(inventory : Inventory){
        val items = inventory.items
        val equipped = Array(items.map {
            if (inventory.equipped == it)
                "E"
            else
                ""
        }.toTypedArray())
        println(equipped.size)
        itemList.list.setItems(items)
        equippedNotice.list.setItems(equipped)
        equippedNotice.list.selectedIndex = -1
    }

    init{
        updateDisplay(unit.inventory)
        //container.scrollTo(0f, 30f, container.prefWidth, container.prefHeight)
        container2.space(10f)
        container2.pad(10f)
        equippedNotice.list.selectedIndex = -1
        container.setPosition(x, y)
        container.height = container2.prefHeight
        container.width = container2.prefWidth
    }
}