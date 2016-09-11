package com.game.srpg.Units.Controller

import com.badlogic.gdx.graphics.Camera
import com.game.srpg.Units.Cursor.Cursor

/**
 * Created by FlyingJam on 8/24/2016.
 */

class CursorCameraController(val cursor : Cursor,
                             val camera : Camera,
                             val width : Int,
                             val height : Int){
    val speed = 2f
    fun update(dt : Float){
        val controller = cursor.controllers.peekFirst()
        val x = controller.worldX(cursor)
        val y = controller.worldZ(cursor) + height / 3
        val position = camera.position
        position.x += ((x - position.x) * speed * dt).toFloat()
        position.z += ((y - position.z) * speed * dt).toFloat()
        //camera.position.set(x, camera.position.y, y)
    }
}