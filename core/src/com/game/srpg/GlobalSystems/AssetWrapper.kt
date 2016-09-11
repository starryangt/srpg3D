package com.game.srpg.GlobalSystems

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import java.util.*

/**
 * Created by FlyingJam on 8/22/2016.
 */

class AnimationMap : HashMap<String, HashMap<String, Animation>>(){

}

data class AssetWrapper(val assetManager : AssetManager,
                        val masterAtlas : TextureAtlas,
                        val animationDictionary: AnimationMap)
