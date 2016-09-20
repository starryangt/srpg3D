package com.game.srpg.Map

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import java.util.*

/**
 * Created by FlyingJam on 8/20/2016.
 */

fun createMap(builder : ModelBuilder, width : Int, height : Int, tileWidth : Int, tileHeight : Int) : Model {
    builder.begin()
    val partBuilder = builder.part("grid", GL20.GL_LINES,
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.TextureCoordinates or VertexAttributes.Usage.ColorUnpacked).toLong(),
            Material())
    val maxHeight = -height * tileHeight.toFloat()
    val maxWidth = width * tileWidth.toFloat()
    for(x in 0..width-1){
        //vertical lines
        val realX = x * tileWidth.toFloat()
        partBuilder.line(Vector3(realX, 0f, 0f), Vector3(realX, 0f, maxHeight))
    }

    for(y in 0..height-1){
        val realY = -y * tileWidth.toFloat()
        partBuilder.line(Vector3(0f, 0f, realY), Vector3(maxWidth, 0f, realY))
    }
    val model = builder.end()
    return model
}


fun createMapColor(width : Int, height : Int, tileWidth : Int, tileHeight : Int) : Model {
    val builder = ModelBuilder()
    val blue = Color(.46f, .61f, .79f, 1f)
    val red = Color(1f, .41f, .38f, 1f)
    val mat = Material(
            BlendingAttribute(true, 0.5f),
            HighlightAttribute(blue, red, blue)
    )

    builder.begin()
    val partBuilder = builder.part("color", GL20.GL_TRIANGLES,
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong(),
            mat)
    for(y in 0..height-1){
        for(x in 0..width-1){
            val realX = x * tileWidth.toFloat()
            val realY = -y * tileHeight.toFloat()
            partBuilder.box(realX, -3f, realY, tileWidth.toFloat(), 0f, tileHeight.toFloat())
        }
    }
    val model = builder.end()
    return model
}


val friendColor = Color(1f, 1f, 0f, 1f)
val enemyColor = Color(1f, 0f, 1f, 1f)

fun createSelectTexture(tiles : ArrayList<Int>,
                        map : GameMap) : Pixmap{
    val tex = Pixmap(map.width, map.height, Pixmap.Format.RGB565)
    setSelectTexture(tex, tiles, map, friendColor)
    return tex
}

fun setSelectTexture(
        tex : Pixmap,
        tiles : ArrayList<Int>,
        map : GameMap,
        color : Color = Color(1f, 1f, 1f, 1f)) : Pixmap{
    tex.setColor(0f, 0f, 0f, 0f)
    tex.fill()

    val path = map.path
    for(tile in tiles){
        tex.setColor(friendColor)
        val x = path.indexToX(tile)
        val y = path.indexToY(tile)
        tex.drawPixel(x, y)
    }

    return tex
}


fun setSelectTexture(
        tex : Pixmap,
        tiles : ArrayList<Int>,
        attackTiles : ArrayList<Int>,
        map : GameMap,
        color : Color = Color(1f, 1f, 1f, 1f)) : Pixmap{
    tex.setColor(0f, 0f, 0f, 0f)
    tex.fill()

    val path = map.path
    for(tile in tiles){
        tex.setColor(color)
        val x = path.indexToX(tile)
        val y = path.indexToY(tile)
        tex.drawPixel(x, y)
    }

    for(tile in attackTiles){
        tex.setColor(enemyColor)
    }

    return tex
}

fun setSelectTexture(
        tex : Pixmap,
        tile : Int,
        map : GameMap,
        friendly : Boolean = true) : Pixmap{
    tex.setColor(0f, 0f, 0f, 0f)
    tex.fill()

    val path = map.path
    if(friendly)
        tex.setColor(friendColor)
    else
        tex.setColor(enemyColor)

    val x = path.indexToX(tile)
    val y = path.indexToY(tile)
    tex.drawPixel(x, y)

    return tex
}


fun setSelectTexture(
        tex : Pixmap,
        x : Int,
        y : Int,
        map : GameMap) : Pixmap{
    tex.setColor(0f, 0f, 0f, 0f)
    tex.fill()

    tex.setColor(friendColor)
    tex.drawPixel(x, y)

    return tex
}

