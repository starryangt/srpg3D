package com.game.srpg.Screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.PixmapPacker
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector3
import com.game.srpg.GlobalSystems.AnimationMap
import com.game.srpg.GlobalSystems.AssetRequest
import com.game.srpg.GlobalSystems.AssetWrapper
import com.game.srpg.Shaders.ShaderLoader
import com.game.srpg.Shadows.DirectionalLight
import java.util.*

/**
 * Created by FlyingJam on 8/13/2016.
 */

class LoadScreen(val parent : Game, val requests : AssetRequest,
                 val construct : ((Game, AssetWrapper) -> Screen)) : ScreenAdapter(){
    val assetManager = AssetManager()

    init{
        assetManager.setLoader(ShaderProgram::class.java, ShaderLoader(InternalFileHandleResolver()))
        requests.loadResources(assetManager)
    }

    val packer = PixmapPacker(512, 512, Pixmap.Format.RGBA8888, 2, true)
    val dict = AnimationMap()
    override fun render(dt : Float){
        if(assetManager.update()){
            requests.lateLoad(assetManager, packer)
            val atlas = packer.generateTextureAtlas(
                    Texture.TextureFilter.Linear,
                    Texture.TextureFilter.Nearest, false)
            requests.generateAnimations(atlas, dict)
            val assets = AssetWrapper(assetManager, atlas, dict)
            parent.screen = construct(parent, assets)
        }
    }

}