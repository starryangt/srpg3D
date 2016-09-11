package com.game.srpg.Cards

import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.Pool
import javafx.scene.effect.Blend

/**
 * Created by FlyingJam on 8/4/2016.
 */

val maxDrawable = 50
val maxVertices = maxDrawable * 8
val maxIndices = maxDrawable * 12

class CardBatch(texture : Texture) : RenderableProvider, Disposable {

    val mesh = Mesh(false, maxVertices, maxIndices, VertexAttribute.Position(),
            VertexAttribute.Normal(), VertexAttribute.TexCoords(0))

    val builder = MeshBuilder()
    val cards = ObjectSet<Card>()
    val renderable = Renderable()
    var mat = Material(
            TextureAttribute.createDiffuse(texture),
            BlendingAttribute(false, 1f))

    init{
        renderable.material = mat
    }

    fun add(card : Card){
        cards.add(card)
    }

    fun remove(card : Card){
        cards.remove(card)
    }

    override fun getRenderables(renderables: Array<Renderable>, pool: Pool<Renderable>) {
        renderable.shader = null
        renderables.add(renderable)
    }

    fun build(r : Renderable = renderable){
        r.material = mat
        builder.begin(mesh.vertexAttributes)
        builder.part("cards", GL20.GL_TRIANGLES,
                r.meshPart)
        for(card in cards){
            builder.setVertexTransform(card.transform)
            builder.addMesh(card.vertices, indices)
        }
        builder.end(mesh)
    }

    override fun dispose(){
        mesh.dispose()
    }

}