package com.game.srpg.Map

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import java.util.*

/**
 * Created by FlyingJam on 9/24/2016.
 */

class MapInformation(){
    class InstanceData{
        var model = ""
        var position = Vector3()
        var rotation = Quaternion()
    }
    var width = 0
    var height = 0
    var tileWidth = 0
    var tileHeight = 0
    var walls = ArrayList<Int>()
    var instances = ArrayList<InstanceData>()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.appendln("Instances")
        for(instance in instances){
            builder.appendln(instance.model)
            builder.appendln(instance.position)
        }

        builder.appendln("Walls")
        for(tile in walls){
            builder.appendln(tile)
        }

        builder.appendln("Width : $width, height : $height, tW : $tileWidth, th : $tileHeight")
        return builder.toString()
    }
}
