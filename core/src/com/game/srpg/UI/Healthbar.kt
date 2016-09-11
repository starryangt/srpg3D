package com.game.srpg.UI

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.game.srpg.GlobalSystems.Ends
import com.game.srpg.Units.GameUnit

/**
 * Created by FlyingJam on 8/31/2016.
 */

open class HealthChange : Action(), Ends {

    var newValue = 0f
    var oldValue = 0f
    var timeAccumulator = 0f
    var time = 1f
    var interop = Interpolation.circleOut

    var ended = false

    override fun setActor(actor: Actor?) {
        val act = actor
        if(act is Healthbar){
            oldValue = act.value
        }
        super.setActor(actor)
    }

    override fun act(delta: Float): Boolean {
        timeAccumulator += delta
        val alpha = timeAccumulator / time
        if(alpha > 1){
            ended = true
            return true
        }
        else{
            ended = false
        }
        val act = target
        if (act is Healthbar){
            val previous = act.value.toInt()
            act.value = interop.apply(oldValue.toFloat(), newValue.toFloat(), alpha)
            if(act.value < 0){
                ended = true
                return true
            }
            val new = act.value.toInt()
            if(previous != new){
                act.changed(new)
            }
        }
        return false
    }

    override fun reset() {
        newValue = 0f
        oldValue = 0f
        timeAccumulator = 0f
        time = 1f
        super.reset()
    }

    override fun restart() {
        timeAccumulator = 0f
        super.restart()
    }

    override fun hasEnded(): Boolean {
        return ended
    }

}

class HealthDamage : HealthChange(){

    var damage = 0f
    var unit : GameUnit? = null
}

class Healthbar(skin : Skin, val min : Float, val max : Float, var value : Float) : Table(skin){

    val patch = skin.getPatch("health_bar")
    val hp = Label("HP: ", skin)
    val hpValue = Label(value.toInt().toString(), skin)

    constructor(skin : Skin, unit : GameUnit) : this(
            skin,
            0f,
            unit.stats.base.health.toFloat(),
            unit.stats.current.health.toFloat())


    init{
        this.width = 100f
        this.add(hp)
        this.add(hpValue).expand().left()
        //this.debug = true
        patch.scale(0.01f, 1f)
    }

    override fun act(dt : Float){
        super.act(dt)
    }

    fun changed(newValue : Int){
        hpValue.setText(newValue.toString())
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val alpha = (value - min)/max.toFloat()
        val combined = hp.prefWidth + hpValue.prefWidth
        val width = MathUtils.lerp(0f, width - combined, alpha) + patch.leftWidth
        if(width > patch.leftWidth + 1){
            patch.draw(batch, this.x + combined, this.y, width, this.height)
        }
        super.draw(batch, parentAlpha)
    }

}