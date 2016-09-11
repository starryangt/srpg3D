package com.game.srpg.Units.Components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.game.srpg.Cards.Card
import com.game.srpg.Cards.CardAnimation
import com.game.srpg.Units.GameUnit
import java.util.*

/**
 * Created by FlyingJam on 8/20/2016.
 */

abstract class DrawingComponent : Component(){
    abstract fun requestState(state : String)
}

abstract class CardDrawingComponent : DrawingComponent(){
    abstract fun card() : Card
    override fun apply(unit: GameUnit) {}
}

class AnimationComponent(val default : Animation, val animations : HashMap<String, Animation> = hashMapOf()) : CardDrawingComponent(){

    val potential = animations.get("idle")
    val animation = potential ?: default
    val cardAnimation = CardAnimation(animation)

    override fun apply(unit: GameUnit) {

    }

    override fun card(): Card {
        return cardAnimation.card
    }

    override fun update(dt: Float, unit : GameUnit) {
        val controller = unit.controllers.peekFirst()
        cardAnimation.card.setPosition(
                controller.worldX(unit),
                controller.worldY(unit),
                controller.worldZ(unit))
        cardAnimation.update(dt)
    }

    override fun requestState(state: String) {
        val potential = animations.get(state)
        println("Requesting $state")
        if(potential != null){
            cardAnimation.switch(potential)
        }
    }
}



class StaticCardComponent(sprite : Sprite) : CardDrawingComponent(){
    val card = Card(sprite)

    override fun card(): Card {
        return card
    }

    override fun update(dt: Float, unit: GameUnit) {
    }

    override fun requestState(state: String) {
    }
}