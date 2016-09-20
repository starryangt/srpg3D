package com.game.srpg.Map.Phase

import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.game.srpg.GlobalSystems.CallbackAction
import com.game.srpg.Map.GameMap

/**
 * Created by FlyingJam on 9/6/2016.
 */

class PlayerStartPhase(map : GameMap) : Phase(map){

    internal lateinit var label : Label

    override fun onEnter() {
        label = Label("Player Phase", parent.ui.skin)
        label.x = 600f
        label.y = 500f
        val action = AlphaAction()
        action.isReverse = true
        action.duration = 2f

        val action2 = AlphaAction()
        action2.duration = 1f

        val callback = CallbackAction()
        callback.callback = { parent.switch(PlayerPhase(parent)) }

        val sequence = SequenceAction()
        sequence.addAction(action)
        sequence.addAction(action2)
        sequence.addAction(callback)

        label.addAction(sequence)

        parent.ui.stage.addActor(label)
    }

    override fun onExit() {
        label.remove()
    }

    override fun update(dt: Float) {
    }

}