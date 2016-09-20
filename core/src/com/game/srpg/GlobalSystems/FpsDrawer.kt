package com.game.srpg.GlobalSystems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Created by FlyingJam on 8/13/2016.
 */

class FpsDrawer(){
    val batch = SpriteBatch()
    val font = BitmapFont()
    val builder = StringBuilder()

    fun draw(){
        val fps = Gdx.graphics.framesPerSecond
        batch.begin()
        font.draw(batch, "FPS: $fps", 0f, 50f)
        batch.end()
    }

    fun dispose(){
        batch.dispose()
        font.dispose()
    }

}