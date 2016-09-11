package com.game.srpg.Units

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.game.srpg.GlobalSystems.getFrames
import com.game.srpg.Map.GameMap
import com.game.srpg.Units.Controller.MapController
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.game.srpg.Cards.CardAnimation
import com.game.srpg.GlobalSystems.AssetWrapper
import com.game.srpg.Units.Components.*
import com.game.srpg.Units.Cursor.DefaultAction
import com.game.srpg.Units.Cursor.*
import com.game.srpg.Units.Items.WeaponFactory

/**
 * Created by FlyingJam on 8/20/2016.
 */

class UnitFactory{
    companion object factory{
        fun testUnit(assets : AssetWrapper, map : GameMap, x : Int = 0, y : Int = 0) : GameUnit{
            val texture = assets.masterAtlas.findRegion("fe").texture
            val frames = getFrames(texture, 64, 64, 4)
            val animation = Animation(0.3f, frames, Animation.PlayMode.LOOP_PINGPONG)
            val actions = ActionComponent(::AttackAction, ::EndAction)
            val dict = assets.animationDictionary.get("marth")
            val animationDict = dict ?: hashMapOf(Pair("idle", animation))
            val unit = GameUnit(x, y, "TestUnit", MapController(map), StatComponent(Stats(speed = 20, strength = 5)), AnimationComponent(animation, animationDict), ClassComponent(), actions)
            val weapon = WeaponFactory.TestSword()
            unit.inventory.add(weapon)
            unit.inventory.equip(0)
            return unit
        }

        fun testEnemy(assets : AssetWrapper, map : GameMap, x : Int = 0, y : Int = 0) : GameUnit{
            val texture = assets.masterAtlas.findRegion("fe").texture
            val frames = getFrames(texture, 64, 64, 4)
            val animation = Animation(0.6f, frames, Animation.PlayMode.LOOP_RANDOM)
            val dict = hashMapOf(Pair("idle", animation))
            val unit = GameUnit(x, y, "TestEnemy", MapController(map), StatComponent(Stats(health = 15, strength = 4, speed = 10)), AnimationComponent(animation, dict), ClassComponent(UnitType.ENEMY), ActionComponent())
            unit.inventory.add(WeaponFactory.TestSword())
            unit.inventory.equip(0)
            return unit
        }
    }
}