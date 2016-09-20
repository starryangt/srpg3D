package com.game.srpg.Units.Components

import com.badlogic.gdx.graphics.Color
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
    abstract fun requestColor(color : Color)
    abstract fun requestRotation(x : Float, y : Float, z : Float, angle : Float)
    abstract fun requestStop(stop : Boolean)
}

abstract class CardDrawingComponent : DrawingComponent(){
    abstract fun card() : Card
    override fun apply(unit: GameUnit) {}
}

class AnimationComponent(val default : Animation, val animations : HashMap<String, Animation> = hashMapOf()) : CardDrawingComponent(){

    val potential = animations.get("idle")
    val animation = potential ?: default
    val cardAnimation = CardAnimation(animation)
    var stop = false

    override fun apply(unit: GameUnit) {

    }

    override fun requestStop(stop : Boolean) {
        cardAnimation.pause(stop)
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

    override fun requestColor(color: Color) {
        cardAnimation.card.color = color
    }

    override fun requestRotation(x: Float, y: Float, z: Float, angle: Float) {
        cardAnimation.card.setRotation(x, y, z, angle)
    }
}



class StaticCardComponent(sprite : Sprite) : CardDrawingComponent(){
    init{
        sprite.color = Color(0.3f, 0.4f, 0.7f, 1f)
    }
    val card = Card(sprite)

    override fun card(): Card {
        return card
    }

    override fun requestStop(stop : Boolean) {
    }

    override fun update(dt: Float, unit: GameUnit) {
    }

    override fun requestState(state: String) {
    }


    override fun requestColor(color: Color) {
        card.color = color
    }

    override fun requestRotation(x: Float, y: Float, z: Float, angle: Float) {
        card.setRotation(x, y, z, angle)
    }
}