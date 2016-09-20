package com.game.srpg.GlobalSystems

/**
 * Created by FlyingJam on 8/13/2016.
 */

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.PixmapPacker
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import java.util.*

class AssetRequest(){

    val requests = Array<(AssetManager) -> Unit>()
    val texturePieces = Array<TexturePiece>()
    val lateRequest = Array<(AssetManager, TextureAtlas) -> Unit>()
    val animRequest = Array<AnimationRequest>()

    val empty = Pixmap(1, 1, Pixmap.Format.RGB565)
    init{
        empty.setColor(.5f, .5f, .5f, 1f)
        empty.drawPixel(1, 1)
    }

    fun add(request : (AssetManager) -> Unit){
        requests.add(request)
    }

    fun add(vararg request : TexturePiece){
        for(r in request){
            texturePieces.add(r)
        }
    }

    fun add(vararg requests : AnimationRequest){
        for(request in requests){
            animRequest.add(request)
        }
    }

    fun addLate(request : (AssetManager, TextureAtlas) -> Unit){
        lateRequest.add(request)
    }

    fun loadResources(manager : AssetManager){
        for(request in requests){
            request(manager)
        }

        for(tex in texturePieces){
            manager.load(tex.file, Pixmap::class.java)
        }

        for(anim in animRequest){
            for(x in 1..anim.number){
                val char = anim.character
                val animation = anim.animation
                manager.load("anim/$char/$animation/$x.png", Pixmap::class.java)
            }
        }
    }

    fun lateLoad(manager: AssetManager, packer : PixmapPacker){
        for(tex in texturePieces){
            val pixmap = manager.get(tex.file, Pixmap::class.java)
            packer.pack(tex.name, pixmap)
        }

        for(anim in animRequest) {
            for (x in 1..anim.number) {
                val char = anim.character
                val animation = anim.animation
                val pixmap = manager.get("anim/$char/$animation/$x.png", Pixmap::class.java)
                packer.pack("$char.$animation.$x", pixmap)
            }
        }
    }

    fun generateAnimations(atlas: TextureAtlas, dict : AnimationMap){
        println("at least trying")
        for(anim in animRequest){
            val frames = Array<TextureRegion>(anim.number)
            println("loading " + anim.animation + anim.character)
            for(x in 1..anim.number){
                val char = anim.character
                val animation = anim.animation
                val region = atlas.findRegion("$char.$animation.$x")
                if(anim.reverse){
                    region.flip(true, false)
                }
                frames.add(region)
            }

            val animation = Animation(anim.length, frames, anim.mode)
            if(!dict.containsKey(anim.character)){
                val newDict = hashMapOf(Pair(anim.animation, animation))
                dict.set(anim.character, newDict)
            }
            else{
                val animDict = dict.get(anim.character)
                animDict?.set(anim.animation, animation)
            }
        }
    }
}

data class TexturePiece(val file : String, val name : String)

data class AnimationRequest(val character : String,
                            val animation : String,
                            val number : Int,
                            val mode : Animation.PlayMode = Animation.PlayMode.NORMAL,
                            val length : Float = 0.1f,
                            val reverse : Boolean = false)

interface RequiresResources{
    fun getRequests() : AssetRequest
}
