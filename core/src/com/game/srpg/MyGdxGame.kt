package com.game.srpg

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.srpg.GlobalSystems.*
import com.game.srpg.Screens.LoadScreen
import com.game.srpg.Screens.MapScreen
import com.game.srpg.Screens.ShitFuck
import com.game.srpg.Shaders.ShaderParamater
import jdk.management.resource.ResourceRequest

class MyGdxGame : Game() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture

    override fun create() {
        Gdx.input.inputProcessor = inputConverter

        setKey(Input.Keys.UP, Actions.UP)
        setKey(Input.Keys.DOWN, Actions.DOWN)
        setKey(Input.Keys.LEFT, Actions.LEFT)
        setKey(Input.Keys.RIGHT, Actions.RIGHT)
        setKey(Input.Keys.X, Actions.ACTIVATE)
        setKey(Input.Keys.Z, Actions.CANCEL)

        val requests = AssetRequest()
        requests.add {
            it.load("tile_c", ShaderProgram::class.java, ShaderParamater())
            it.load("depthmap_c", ShaderProgram::class.java, ShaderParamater())
            it.load("depthmap_t", ShaderProgram::class.java, ShaderParamater())
            it.load("shadow_c", ShaderProgram::class.java, ShaderParamater())
            it.load("shadow_t", ShaderProgram::class.java, ShaderParamater())
            it.load("shadowed_scene_c", ShaderProgram::class.java, ShaderParamater())
            it.load("shadowed_scene_t", ShaderProgram::class.java, ShaderParamater())
            it.load("uiskin.atlas", TextureAtlas::class.java)
            it.load("uiskin.json", Skin::class.java, SkinLoader.SkinParameter("uiskin.atlas"))
        }
        requests.add(TexturePiece("fe.png", "fe"))
        requests.add(TexturePiece("cursor.png", "cursor"))
        requests.add(AnimationRequest("marth", "idle", 4, Animation.PlayMode.LOOP_PINGPONG, .17f))
        requests.add(AnimationRequest("marth", "up", 6, Animation.PlayMode.LOOP_PINGPONG, .2f))
        requests.add(AnimationRequest("marth", "right", 6, Animation.PlayMode.LOOP_PINGPONG, .2f))
        requests.add(AnimationRequest("marth", "left", 6, Animation.PlayMode.LOOP_PINGPONG, .2f))
        //requests.add(AnimationRequest("marth", "up", 6, Animation.PlayMode.LOOP_PINGPONG, .2f))
        screen = LoadScreen(this, requests, ::MapScreen)
    }

    override fun render() {
        super.render()
    }
}

