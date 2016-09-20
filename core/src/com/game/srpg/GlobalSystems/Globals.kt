package com.game.srpg.GlobalSystems

/**
 * Created by FlyingJam on 9/11/2016.
 */

class Globals{
    class AIFactors{
        companion object static{
            val Kill = 120f
            val PerDamage = 2f
            val PerUnitMoved = -1f
        }
    }

    class Shadow{
        companion object static{
            val LightCameraWidth = 1024f
            val LightCameraHeight = 1024f
            val LightDirectionalDefaultFOV = 120f
            val LightPointDefaultFOV = 90f
            val LightDefaultFar = 500f

            val ShadowMapWidth = 1024
            val ShadowMapHeight = 1024
        }

    }

    companion object static{
        val UnitMoveTime = .2f
        val CursorMoveTime = .125f
        val DeathFadeTime = .9f
    }

}