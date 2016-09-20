package com.game.srpg.Units.Items


/**
 * Created by FlyingJam on 8/2/2016.
 */

open class Item{

    class Weapon(val name : String, val range : Range,
                 val type : WeaponTypes,
                 val damage : Int,
                 val hit : Int) : Item() {
        override fun toString(): String {
            return name
        }
    }

}



enum class WeaponTypes { SWORD, SPEAR, BOW}

class WeaponFactory{
    companion object Factory{
        fun TestSword() : Item.Weapon{
            return Item.Weapon("Test Sword", StrictRange(), WeaponTypes.SWORD, 10, 90)
        }

        fun OtherSword() : Item.Weapon{
            return Item.Weapon("Hooo", StrictRange(), WeaponTypes.SWORD, 5, 60)
        }
    }
}


