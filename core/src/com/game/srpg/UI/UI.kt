package com.game.srpg.UI

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Pool
import com.game.srpg.Combat.Prediction
import com.game.srpg.Units.GameUnit
import javafx.scene.shape.MoveTo

/**
 * Created by FlyingJam on 8/27/2016.
 */

class UI(val skin : Skin){

    val stage = Stage()
    var friendlyStat : StatBox? = null
    var combatBox : CombatBox? = null
    val duration = .2f

    val movePool = object: Pool<MoveToAction>(){
        override fun newObject(): MoveToAction {
            return MoveToAction()
        }
    }

    init{
        //skin.getFont("default-font").data.setScale(2.5f, 2.5f)
    }

    fun update(dt : Float){
        stage.act(dt)
    }

    fun render(dt : Float){
        stage.draw()
    }

    fun spawnSelectMenu(){
    }

    fun spawnFriendlyStat(unit : GameUnit, unique : Boolean = false) : StatBox{
        val stat = friendlyStat
        if(unique && stat != null){
            removeFriendlyStat(stat)
        }
        val table = StatBox(unit, skin)
        friendlyStat = table
        table.left().bottom()

        val move = movePool.obtain()
        move.setPosition(table.x, table.y)
        move.duration = duration
        table.x = table.x - table.prefWidth
        table.addAction(move)

        stage.addActor(table)
        return table
    }

    fun removeFriendlyStat(stat : StatBox){
        val action = MoveToAction()
        action.setPosition(stat.x - stat.prefWidth, stat.y)
        action.duration = duration
        val delete = RemoveActorAction()
        val sequence = SequenceAction()
        sequence.addAction(action)
        sequence.addAction(delete)
        stat.addAction(sequence)
    }

    fun spawnCombatBox(prediction: Prediction) : CombatBox{
        val box = combatBox
        if(box != null){
            removeCombatBox(box)
        }
        val combat = CombatBox(prediction, skin)
        combat.bottom().left()
        combatBox = combat
        stage.addActor(combat)
        return combat
    }

    fun takeDamage(combatBox : CombatBox, change : Int, ally : Boolean = false){
        val actor = HealthChange()
        actor.newValue = change.toFloat()
        if(ally){
            combatBox.allyHP.addAction(actor)
        }
        else{
            combatBox.enemyHP.addAction(actor)
        }
    }


    fun takeDamage(combatBox : CombatBox, oldValue : Int, damage : Int, unit : GameUnit) : HealthChange{
        val actor = HealthChange()
        actor.newValue = oldValue - damage.toFloat()
        actor.oldValue = oldValue.toFloat()
        actor.time = 2f
        if(unit == combatBox.ally){
            combatBox.allyHP.addAction(actor)
        }
        else{
            combatBox.enemyHP.addAction(actor)
        }
        return actor
    }



    fun removeCombatBox(combatBox: CombatBox){
        combatBox.remove()
    }
}