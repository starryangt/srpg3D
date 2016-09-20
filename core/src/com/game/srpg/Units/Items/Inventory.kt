package com.game.srpg.Units.Items

/**
 * Created by FlyingJam on 8/2/2016.
 */
import com.badlogic.gdx.utils.Array
import com.game.srpg.Units.Items.Item

class Inventory{
    val items = Array<Item>()

    var equipped : Item? = null

    fun add(item : Item){
        items.add(item)
    }

    fun remove(item : Item){
        items.removeValue(item, true)
    }

    fun equip(item : Item){
        val index = items.indexOf(item)
        if(index != -1){
            equip(index)
        }
    }

    fun equip(index : Int){
        val item = items.get(index)
        if (item != null){
            equipped = item
            if(index != 0) {
                println("$index, $item")
                items.removeIndex(index)
                items.insert(0, item)
            }
        }
    }

    fun unequip(){
        equipped = null
    }
}