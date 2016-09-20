package com.game.srpg.Map

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils
//import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Pool
import com.game.srpg.Shaders.MapHighlightShader
import java.util.*

/**
 * Created by FlyingJam on 8/21/2016.
 */

val positionColor = Color(0f, 1f, 0f, 1f)
val attackColor = Color(0f, 0f, 1f, 1f)
val allyColor = Color(1f, 0f, 0f, 1f)
val blankColor = Color(0f, 0f, 0f, 0f)

var count = 0

class MapHighlight(val assetManager: AssetManager,
                   val map : GameMap,
                   var beginOpacity : Float = 0.3f,
                   var maxOpacity : Float = 0.7f,
                   var length : Float = 1.5f,
                   var colorSet : ColorSet = ColorSet(Color.CYAN, Color.CHARTREUSE, Color.CORAL)
                   ) : Disposable, RenderableProvider{

    enum class Type { POSITION, ATTACK, ALLY }

    data class ColorSet(var position : Color, var attack : Color, var ally : Color)

    private var highlightPixmap = createSelectTexture(ArrayList<Int>(), map)
    private var highlightTexture = Texture(highlightPixmap)
    private val highlightMesh = initMapModel()//createMapColor(map.width, map.height, map.tileWidth, map.tileHeight)
    val highlightInstance = ModelInstance(highlightMesh)

    val program = assetManager.get("tile_c", ShaderProgram::class.java)
    init{
        highlightInstance.transform.translate(
                map.tileWidth.toFloat() / 2,
                0f,
                -map.tileHeight.toFloat() / 2
        )
    }

    var timeAccumulator = 0f

    var opacity = 0f
    var goingUp = true

    fun update(dt : Float){
        val alpha = timeAccumulator / length
        opacity = MathUtils.lerp(beginOpacity, maxOpacity, alpha)
        when{
            alpha > 1 -> goingUp = false
            alpha < 0 -> goingUp = true
        }
        when(goingUp){
            true -> timeAccumulator += dt
            false -> timeAccumulator -= dt
        }
    }

    fun set(program: ShaderProgram){
        program.begin()
        highlightTexture.bind(0)
        program.setUniformi("u_tileMap", 0)
        program.setUniformf("u_mapFactorX", map.width * map.tileWidth.toFloat())
        program.setUniformf("u_mapFactorY", map.height * map.tileHeight.toFloat())
        program.setUniformf("u_alpha", opacity)
        program.end()
    }

    fun setPath(path : Array<Int>, type : Type = Type.POSITION){
        //setSelectTexture()
    }

    fun initMapModel() : Model{
        val builder = ModelBuilder()
        val mat = Material(
                BlendingAttribute(true, 0.5f),
                HighlightAttribute(colorSet.position, colorSet.attack, colorSet.ally)
        )
        builder.begin()
        val partBuilder = builder.part("color", GL20.GL_TRIANGLES,
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong(),
            mat)
        for(y in 0..map.height-1){
            for(x in 0..map.width-1){
                val realX = x * map.tileWidth.toFloat()
                val realY = -y * map.tileHeight.toFloat()
                partBuilder.box(realX, -3f, realY, map.tileWidth.toFloat(), 0f, map.tileHeight.toFloat())
            }
        }
        val model = builder.end()
        return model
    }

    fun begin(){
        highlightPixmap.setColor(blankColor)
        highlightPixmap.fill()
    }

    fun end(){
        //highlightTexture.dispose()
        highlightTexture.draw(highlightPixmap, 0, 0)
        //highlightTexture = Texture(highlightPixmap)
    }

    fun colorPath(path : Array<Int>, type : Type = Type.POSITION){
        for(tile in path){
            colorTile(tile, type)
        }
    }

    fun colorPath(path : Iterable<Int>, type : Type = Type.POSITION){
        for(tile in path){
            colorTile(tile, type)
        }
    }

    fun colorTile(x : Int, y : Int, type : Type = Type.POSITION){

         val color =
                when(type){
                    Type.POSITION -> positionColor
                    Type.ATTACK -> attackColor
                    Type.ALLY -> allyColor
                }

        highlightPixmap.setColor(color)
        highlightPixmap.drawPixel(x, y)
    }

    fun colorTile(index : Int, type : Type = Type.POSITION){
        val path = map.path

        val x = path.indexToX(index)
        val y = path.indexToY(index)

        colorTile(x, y, type)
    }


    fun setPath(path : ArrayList<Int>){
        setSelectTexture(highlightPixmap, path, map)
        highlightTexture.dispose()
        highlightTexture = Texture(highlightPixmap)
    }

    fun setTile(tile : Int){
        setSelectTexture(highlightPixmap, tile, map)
        highlightTexture.dispose()
        highlightTexture = Texture(highlightPixmap)
    }

    fun setTile(x : Int, y : Int){
        setSelectTexture(highlightPixmap, x, y, map)
        highlightTexture.dispose()
        highlightTexture = Texture(highlightPixmap)
    }

    override fun dispose() {
        highlightPixmap.dispose()
        highlightTexture.dispose()
        highlightMesh.dispose()
    }

    override fun getRenderables(renderables: com.badlogic.gdx.utils.Array<Renderable>, pool: Pool<Renderable>) {
        val renderable = pool.obtain()
        highlightInstance.getRenderable(renderable)
        renderable.userData = this
        renderables.add(renderable)
    }
}