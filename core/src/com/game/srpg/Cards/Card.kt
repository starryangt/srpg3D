package com.game.srpg.Cards

import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.game.srpg.Units.Controller.MapController
import com.game.srpg.Units.Controller.UnitWorldController

/**
 * Created by FlyingJam on 8/4/2016.
 */

val indices = arrayOf(0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4).map { it.toShort() }.toShortArray()
//val indices = arrayOf(0, 1, 2, 2, 3, 0).map { it.toShort() }.toShortArray()

fun convert(front : FloatArray, back : FloatArray) : FloatArray{

    return arrayOf(
        front[Batch.X2], front[Batch.Y2], 0f, 0f, 0f, 1f, front[Batch.U2], front[Batch.V2], 1f, 1f, 1f, 1f,
        front[Batch.X1], front[Batch.Y1], 0f, 0f, 0f, 1f, front[Batch.U1], front[Batch.V1], 0.3f, 1f, 1f, 1f,
        front[Batch.X4], front[Batch.Y4], 0f, 0f, 0f, 1f, front[Batch.U4], front[Batch.V4], 1f, 0.5f, 1f, 1f,
        front[Batch.X3], front[Batch.Y3], 0f, 0f, 0f, 1f, front[Batch.U3], front[Batch.V3], 0.3f, 1f, 1f, 1f,

        back[Batch.X1], back[Batch.Y1], 0f, 0f, 0f, -1f, back[Batch.U1], back[Batch.V1], 1f, 1f, 1f, 1f,
        back[Batch.X2], back[Batch.Y2], 0f, 0f, 0f, -1f, back[Batch.U2], back[Batch.V2], 1f, 1f, 1f, 1f,
        back[Batch.X3], back[Batch.Y3], 0f, 0f, 0f, -1f, back[Batch.U3], back[Batch.V3], 1f, 1f, 1f, 1f,
        back[Batch.X4], back[Batch.Y4], 0f, 0f, 0f, -1f, back[Batch.U4], back[Batch.V4], 1f, 1f, 1f, 1f
    ).toFloatArray()

}

class Test(val region : TextureRegion, builder : ModelBuilder){
    val sprite = Sprite(region)
    init{
    }
}

class Card(val sprite : Sprite){

    constructor (textureRegion : TextureRegion) : this(Sprite(textureRegion)){

    }

    val width = 10
    val height = 5
    val vertices = convert(sprite.vertices, sprite.vertices)
    val transform = Matrix4()
    val material = Material()
    var color = Color(1f, 1f, 1f, 1f)

    private val pos = Vector3()
    private val defaultQ = Quaternion()
    private val axis = Vector3()


    init{
        setSize(sprite.width, sprite.height)
        //transform.rotate(1f, 0f, 1f, 45f)
        //defaultQ.set(Vector3(0f, 1f, 0f), -90f)
        //defaultQ.set(1f, 0f, 0f, 5f)
        //switch(sprite)
        //sprite.setSize(width.toFloat(), height.toFloat())
        //sprite.setPosition((-sprite.width * 0.5).toFloat(), (-sprite.height * 0.5).toFloat())
    }

    fun setRotation(x : Float, y : Float, z : Float, angle : Float){
        axis.x = x
        axis.y = y
        axis.z = z
        defaultQ.set(axis, angle)
    }

    fun setPosition(x : Float, y : Float, z : Float){

        pos.x = x
        pos.y = y
        pos.z = z
        transform.set(pos, defaultQ)
        //transform.setToTranslation(pos)
        pos.x = 0f
        pos.y = 0f
        pos.z = 0f
    }


    fun update(){
        switch(sprite)
    }

    fun setVertices(front : FloatArray, back : FloatArray){

        vertices[0] = front[Batch.X2]
        vertices[1] = front[Batch.Y2]
        vertices[2] = 0f
        vertices[3] = 0f
        vertices[4] = 0f
        vertices[5] = 1f
        vertices[6] = front[Batch.U2]
        vertices[7] = front[Batch.V2]
        vertices[8] = color.r
        vertices[9] = color.g
        vertices[10] = color.b
        vertices[11] = color.a

        vertices[12] = front[Batch.X1]
        vertices[13] = front[Batch.Y1]
        vertices[14] = 0f
        vertices[15] = 0f
        vertices[16] = 0f
        vertices[17] = 1f
        vertices[18] = front[Batch.U1]
        vertices[19] = front[Batch.V1]
        vertices[20] = color.r
        vertices[21] = color.g
        vertices[22] = color.b
        vertices[23] = color.a

        vertices[24] = front[Batch.X4]
        vertices[25] = front[Batch.Y4]
        vertices[26] = 0f
        vertices[27] = 0f
        vertices[28] = 0f
        vertices[29] = 1f
        vertices[30] = front[Batch.U4]
        vertices[31] = front[Batch.V4]
        vertices[32] = color.r
        vertices[33] = color.g
        vertices[34] = color.b
        vertices[35] = color.a

        vertices[36] = front[Batch.X3]
        vertices[37] = front[Batch.Y3]
        vertices[38] = 0f
        vertices[39] = 0f
        vertices[40] = 0f
        vertices[41] = 1f
        vertices[42] = front[Batch.U3]
        vertices[43] = front[Batch.V3]
        vertices[44] = color.r
        vertices[45] = color.g
        vertices[46] = color.b
        vertices[47] = color.a

        vertices[48] = back[Batch.X1]
        vertices[49] = back[Batch.Y1]
        vertices[50] = 0f
        vertices[51] = 0f
        vertices[52] = 0f
        vertices[53] = -1f
        vertices[54] = back[Batch.U1]
        vertices[55] = back[Batch.V1]
        vertices[56] = color.r
        vertices[57] = color.g
        vertices[58] = color.b
        vertices[59] = color.a

        vertices[60] = back[Batch.X2]
        vertices[61] = back[Batch.Y2]
        vertices[62] = 0f
        vertices[63] = 0f
        vertices[64] = 0f
        vertices[65] = -1f
        vertices[66] = back[Batch.U2]
        vertices[67] = back[Batch.V3]
        vertices[68] = color.r
        vertices[69] = color.g
        vertices[70] = color.b
        vertices[71] = color.a


        vertices[72] = back[Batch.X3]
        vertices[73] = back[Batch.Y3]
        vertices[74] = 0f
        vertices[75] = 0f
        vertices[76] = 0f
        vertices[77] = -1f
        vertices[78] = back[Batch.U3]
        vertices[79] = back[Batch.V3]
        vertices[80] = color.r
        vertices[81] = color.g
        vertices[82] = color.b
        vertices[83] = color.a


        vertices[84] = back[Batch.X4]
        vertices[85] = back[Batch.Y4]
        vertices[86] = 0f
        vertices[87] = 0f
        vertices[88] = 0f
        vertices[89] = -1f
        vertices[90] = back[Batch.U4]
        vertices[91] = back[Batch.V4]
        vertices[92] = color.r
        vertices[93] = color.g
        vertices[94] = color.b
        vertices[95] = color.a
    }

    fun updateColor(color : Color){
        vertices[8] = color.r
        vertices[9] = color.g
        vertices[10] = color.b
        vertices[11] = color.a

        vertices[20] = color.r
        vertices[21] = color.g
        vertices[22] = color.b
        vertices[23] = color.a

        vertices[32] = color.r
        vertices[33] = color.g
        vertices[34] = color.b
        vertices[35] = color.a

        vertices[44] = color.r
        vertices[45] = color.g
        vertices[46] = color.b
        vertices[47] = color.a

        vertices[56] = color.r
        vertices[57] = color.g
        vertices[58] = color.b
        vertices[59] = color.a

        vertices[68] = color.r
        vertices[69] = color.g
        vertices[70] = color.b
        vertices[71] = color.a

        vertices[80] = color.r
        vertices[81] = color.g
        vertices[82] = color.b
        vertices[83] = color.a

        vertices[92] = color.r
        vertices[93] = color.g
        vertices[94] = color.b
        vertices[95] = color.a
    }

    fun setSize(width : Float, height : Float){
        sprite.setSize(width, height)
        switch(sprite)
    }

    fun switch(region : TextureRegion){
        sprite.setRegion(region)
        switch(sprite)
    }

    fun switch(s : Sprite){
        setVertices(s.vertices, s.vertices)
    }



}
