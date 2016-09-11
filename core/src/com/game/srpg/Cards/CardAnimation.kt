package com.game.srpg.Cards

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

/**
 * Created by FlyingJam on 8/13/2016.
 */

class CardAnimation(var animation : Animation, width : Float = 32f, height : Float = 32f){

    constructor(frames : Array<out TextureRegion>, duration : Float, mode : Animation.PlayMode)
    : this(Animation(duration, frames, mode)){
    }
    val card = Card(animation.getKeyFrame(0f))

    init{
        card.setSize(width, height)
    }


    var time = 0f
    var lock = false

    fun update(dt : Float){
        lock = true
        time += dt
        val frame = animation.getKeyFrame(time)
        card.switch(frame)
        lock = false
    }

    fun switch(newAnimation: Animation){
        time = 0f
        if(!lock){
            animation = newAnimation
            val frame = animation.getKeyFrame(time)
            card.switch(frame)
        }
    }
}