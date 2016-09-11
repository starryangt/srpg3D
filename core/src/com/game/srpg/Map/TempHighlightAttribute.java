package com.game.srpg.Map;

import com.badlogic.gdx.graphics.g3d.Attribute;

/**
 * Created by FlyingJam on 8/24/2016.
 */
public class TempHighlightAttribute extends Attribute {
    public final static String HighlightAlias = "Alias";
    public static long Highlight = register(HighlightAlias);

    public static Boolean is(final long type){
        return (type & Highlight) != 0;
    }

    public TempHighlightAttribute(){
        super(Highlight);
    }

    @Override
    public Attribute copy(){
        return new TempHighlightAttribute();
    }

    @Override
    public int hashCode(){
        final int prime = 100003;
        return prime * super.hashCode();
    }

    @Override
    public int compareTo(Attribute o){
        if(type != o.type) return type < o.type ? -1 : 1;
        return 1;
    }
}
