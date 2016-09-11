package com.game.srpg.Cards

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
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
        front[Batch.X2], front[Batch.Y2], 0f, 0f, 0f, 1f, front[Batch.U2], front[Batch.V2],
        front[Batch.X1], front[Batch.Y1], 0f, 0f, 0f, 1f, front[Batch.U1], front[Batch.V1],
        front[Batch.X4], front[Batch.Y4], 0f, 0f, 0f, 1f, front[Batch.U4], front[Batch.V4],
        front[Batch.X3], front[Batch.Y3], 0f, 0f, 0f, 1f, front[Batch.U3], front[Batch.V3],

        back[Batch.X1], back[Batch.Y1], 0f, 0f, 0f, -1f, back[Batch.U1], back[Batch.V1],
        back[Batch.X2], back[Batch.Y2], 0f, 0f, 0f, -1f, back[Batch.U2], back[Batch.V2],
        back[Batch.X3], back[Batch.Y3], 0f, 0f, 0f, -1f, back[Batch.U3], back[Batch.V3],
        back[Batch.X4], back[Batch.Y4], 0f, 0f, 0f, -1f, back[Batch.U4], back[Batch.V4]
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


    init{
        setSize(sprite.width, sprite.height)
        switch(sprite)
        //sprite.setSize(width.toFloat(), height.toFloat())
        //sprite.setPosition((-sprite.width * 0.5).toFloat(), (-sprite.height * 0.5).toFloat())
    }

    private val pos = Vector3()
    private val defaultQ = Quaternion()

    fun setPosition(x : Float, y : Float, z : Float){
        pos.x = x
        pos.y = y
        pos.z = z
        transform.set(pos, defaultQ)
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

        vertices[8] = front[Batch.X1]
        vertices[9] = front[Batch.Y1]
        vertices[10] = 0f
        vertices[11] = 0f
        vertices[12] = 0f
        vertices[13] = 1f
        vertices[14] = front[Batch.U1]
        vertices[15] = front[Batch.V1]

        vertices[16] = front[Batch.X4]
        vertices[17] = front[Batch.Y4]
        vertices[18] = 0f
        vertices[19] = 0f
        vertices[20] = 0f
        vertices[21] = 1f
        vertices[22] = front[Batch.U4]
        vertices[23] = front[Batch.V4]

        vertices[24] = front[Batch.X3]
        vertices[25] = front[Batch.Y3]
        vertices[26] = 0f
        vertices[27] = 0f
        vertices[28] = 0f
        vertices[29] = 1f
        vertices[30] = front[Batch.U3]
        vertices[31] = front[Batch.V3]

        vertices[32] = back[Batch.X1]
        vertices[33] = back[Batch.Y1]
        vertices[34] = 0f
        vertices[35] = 0f
        vertices[36] = 0f
        vertices[37] = -1f
        vertices[38] = back[Batch.U1]
        vertices[39] = back[Batch.V1]

        vertices[40] = back[Batch.X2]
        vertices[41] = back[Batch.Y2]
        vertices[42] = 0f
        vertices[43] = 0f
        vertices[44] = 0f
        vertices[45] = -1f
        vertices[46] = back[Batch.U2]
        vertices[47] = back[Batch.V3]


        vertices[48] = back[Batch.X3]
        vertices[49] = back[Batch.Y3]
        vertices[50] = 0f
        vertices[51] = 0f
        vertices[52] = 0f
        vertices[53] = -1f
        vertices[54] = back[Batch.U3]
        vertices[55] = back[Batch.V3]


        vertices[56] = back[Batch.X4]
        vertices[57] = back[Batch.Y4]
        vertices[58] = 0f
        vertices[59] = 0f
        vertices[60] = 0f
        vertices[61] = -1f
        vertices[62] = back[Batch.U4]
        vertices[63] = back[Batch.V4]

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
