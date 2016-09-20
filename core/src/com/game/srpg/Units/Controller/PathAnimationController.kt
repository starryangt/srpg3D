package com.game.srpg.Units.Controller

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.game.srpg.GlobalSystems.Ends
import com.game.srpg.Map.GameMap
import com.game.srpg.Map.MapCoordinates
import com.game.srpg.Units.Components.DrawingComponent
import com.game.srpg.Units.GameUnit
import java.util.*

/**
 * Created by FlyingJam on 8/22/2016.
 */

class PathAnimationController(
        val path : ArrayList<Int>,
        val draw : DrawingComponent,
        val map : GameMap,
        val time : Float = 0.5f
        ) : UnitWorldController(), Ends {

    val originalPos = Vector2()
    val originalMapPos = Vector2()
    val goalPos = Vector2()
    val screenCoordinates = Vector2()

    var index = 2
    var timeAccumulator = 0f
    var direction = "idle"

    var done = false

    init{
        if(path.size > 1){
            val x = map.path.indexToX(path[0])
            val y = map.path.indexToY(path[0])

            goalPos.x = map.mapToWorldX(x, 32)
            goalPos.y = map.mapToWorldY(y, 0)
            move(path[1])
        }
        else{
            done = true
        }
    }

    fun move(x : Int, y : Int){
        originalPos.x = goalPos.x
        originalPos.y = goalPos.y

        goalPos.x = map.mapToWorldX(x, 32)
        goalPos.y = map.mapToWorldY(y, 0)
        val xDiff = goalPos.x - originalPos.x
        val yDiff = -1 * (goalPos.y - originalPos.y)
        val newDirection =
                if(xDiff > 0){
                    "right"
                }
                else if (xDiff < 0){
                    "left"
                }
                else if (yDiff > 0){
                    "up"
                }
                else if (yDiff < 0){
                    "down"
                }
                else{
                    "idle"
                }

        if(direction != newDirection){
            direction = newDirection
            draw.requestState(direction)
        }
    }


    fun move(index : Int){
        val x = map.path.indexToX(index)
        val y = map.path.indexToY(index)
        move(x, y)
    }

    override fun isDone(): Boolean {
        return done
    }

    override fun hasEnded(): Boolean {
        return done
    }

    override fun update(dt: Float) {
        val size = path.size
        if(index <= size){
            timeAccumulator += dt
            val alpha = timeAccumulator / time
            if(alpha <= 1){
                screenCoordinates.x = MathUtils.lerp(originalPos.x, goalPos.x, alpha)
                screenCoordinates.y = MathUtils.lerp(originalPos.y, goalPos.y, alpha)
            }
            else{
                screenCoordinates.x = goalPos.x
                screenCoordinates.y = goalPos.y

                timeAccumulator = 0f
                if(index >= size){
                    move(path[size - 1])
                }
                else{
                    move(path[index])
                }
                index++
            }

        }
        else{
            done = true
            doneEvent()
        }
    }

    override fun worldX(obj: MapCoordinates): Float {
        return screenCoordinates.x
    }

    override fun worldY(obj: MapCoordinates): Float {
        return 0f
    }

    override fun worldZ(obj: MapCoordinates): Float {
        return screenCoordinates.y
    }
}