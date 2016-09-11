package com.game.srpg.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;

/**
 * Created by FlyingJam on 8/24/2016.
 */
public class HighlightAttribute extends Attribute {
    public final static String HighlightAlias = "Alias";
    public static long Highlight = register(HighlightAlias);
    public Color position;
    public Color attack;
    public Color ally;

    public static Boolean is(final long type){
        return (type & Highlight) != 0;
    }

    public HighlightAttribute(Color friends, Color enemies, Color allies){
        super(Highlight);
        position = friends;
        attack = enemies;
        ally = allies;
    }

    public HighlightAttribute(){
        this(Color.CYAN, Color.RED, Color.GREEN);
    }

    @Override
    public Attribute copy(){
        return new HighlightAttribute(position, attack, ally);
    }

    @Override
    public int hashCode(){
        final int prime = 100003;
        final long v = position.hashCode() + attack.hashCode() + ally.hashCode();
        return prime * super.hashCode() + (int)(v^(v>>>32));
    }

    @Override
    public int compareTo(Attribute o){
        if(type != o.type) return type < o.type ? -1 : 1;
        return 1;
    }
}
