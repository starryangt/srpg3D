package com.game.srpg.Map

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.game.srpg.GlobalSystems.Event
import java.util.*

/**
 * Created by FlyingJam on 9/24/2016.
 */

class MapGeometry() : RenderableProvider{
    val models = HashMap<String, Model>()
    val activeInstances = Array<ModelInstance>()
    val instances = Array<ModelInstance>()
    val cache = ModelCache()

    val addedEvent = Event<Unit>()

    override fun getRenderables(renderables: Array<Renderable>, pool: Pool<Renderable>) {
        cache.getRenderables(renderables, pool)
        for(instance in activeInstances){
            instance.getRenderables(renderables, pool)
        }
    }

    fun loadModel(model : Model, name : String){
        models[name] = model
        addedEvent.trigger(Unit)
    }

    fun removeModel(name : String){
        models.remove(name)
    }

    fun getModel(name : String) : Model?{
        return models[name]
    }

    fun getFileName(model : Model) : String{
        var name : String = ""
        models.forEach { s, mo -> if(mo == model) name = s }
        return name
    }

    fun addInstance(instance : ModelInstance){
        instances.add(instance)
    }

    fun addInstance(name : String, pos : Vector3, rot : Quaternion = Quaternion(), active : Boolean = false){
        val model = getModel(name)
        if(model != null){
            val instance = ModelInstance(model)
            instance.transform.set(pos, rot)

            if(active) {
                activeInstances.add(instance)
            }
            else {
                instances.add(instance)
                buildCache()
            }
        }
    }

    fun removeInstance(instance: ModelInstance){
        instances.removeValue(instance, true)
        activeInstances.removeValue(instance, true)
    }

    fun buildCache() {
        cache.begin()
        for (instance in instances) {
            cache.add(instance)
        }
        cache.end()
    }
}