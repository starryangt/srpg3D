package com.mygdx.game.Map

/**
 * Created by FlyingJam on 7/6/2016.
 */

class Array2D<T>(val width : Int, val height : Int) : Iterable<T?>{


    val internal : Array<T?>

    var count = 0

    init{
        val temp : Array<Any?> = arrayOfNulls(width * height)
        internal = temp as Array<T?>
    }

    override operator fun iterator() : Iterator<T?>{
        return internal.iterator()
    }

    fun set(x : Int, y : Int, value : T){
        val index = pointToIndex(x, y)
        internal[index] = value
    }

    fun set(index : Int, value : T) {
        count++
        internal[index] = value
    }

    fun get(x : Int, y : Int) : T?{
        val index = pointToIndex(x, y)
        return internal[index]
    }

    fun get(index : Int) : T?{
        return internal[index]
    }

    fun exist(x : Int, y : Int) : Boolean{
        if(x > width-1 || y > height-1 || x < 0 || y < 0) return false
        val index = pointToIndex(x, y)
        return internal[index] != null
    }

    fun exist(index : Int) : Boolean{
        val x = indexToX(index)
        val y = indexToY(index)
        return exist(x,y)
    }

    fun pointToIndex(x : Int, y : Int) :Int{
        return (x + y*(width))
    }

    fun indexToX(index : Int) : Int{
        return (index % width)
    }

    fun indexToY(index : Int) : Int{
        return (index / width)
    }

    override fun toString() : String{
        val s = StringBuilder()
        for(y in 0..height-1){
            for(x in 0..width-1){
                val index = pointToIndex(x,y)
                val data = internal[index]?.toString() ?: "NULL"
                s.append(data)
                s.append("|")
            }
            s.append("\n")
        }
        return s.toString()
    }


}