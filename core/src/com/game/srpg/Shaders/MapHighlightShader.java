package com.game.srpg.Shaders;

/**
 * Created by FlyingJam on 8/5/2016.
 */
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Inputs;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Setters;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.game.srpg.Map.HighlightAttribute;
import com.game.srpg.Map.MapHighlight;

/**
 * Simple shader for an object with a texture
 */
public class MapHighlightShader extends BaseShader
{
    public Renderable	renderable;
    public MapHighlight mapHighlight;
    public Color position;
    public Color enemy;
    public Color ally;


    @Override
    public void end()
    {
        super.end();
    }

    public MapHighlightShader(final Renderable renderable, final MapHighlight highlight)
    {
        this.renderable = renderable;
        this.program = highlight.getProgram();
        this.mapHighlight = highlight;
        this.position = ((HighlightAttribute)renderable.material.get(HighlightAttribute.Highlight)).position;
        this.enemy = ((HighlightAttribute)renderable.material.get(HighlightAttribute.Highlight)).attack;
        this.ally = ((HighlightAttribute)renderable.material.get(HighlightAttribute.Highlight)).ally;

        register(Inputs.worldTrans, Setters.worldTrans);
        register(Inputs.projViewTrans, Setters.projViewTrans);
        register(Inputs.normalMatrix, Setters.normalMatrix);
        register(Inputs.diffuseColor, Setters.diffuseColor);
    }

    @Override
    public void begin(final Camera camera, final RenderContext context)
    {
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
        //context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_CONSTANT_ALPHA);
        program.begin();
        program.setUniformf("u_position", position.r, position.g, position.b);
        program.setUniformf("u_attack", enemy.r, enemy.g, enemy.b);
        program.setUniformf("u_ally", ally.r, ally.g, ally.b);
        program.end();
        mapHighlight.set(this.program);
        super.begin(camera, context);

    }

    @Override
    public void render(final Renderable renderable)
    {
        if (renderable.material.has(BlendingAttribute.Type))
		{
			context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
		else
		{
            context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            //context.setDepthTest(GL20.GL_LEQUAL);
            //context.setCullFace(GL20.GL_BACK);
		}
        super.render(renderable);
    }

    @Override
    public void init()
    {
        final ShaderProgram program = this.program;
        this.program = null;
        init(program, renderable);
        renderable = null;
    }

    @Override
    public int compareTo(final Shader other)
    {
        return 0;
    }

    @Override
    public boolean canRender(final Renderable instance)
    {
        if(instance.userData instanceof MapHighlight){
            if (instance.userData == mapHighlight) {
                return true;
            }
        }
        return false;//instance.material.has(HighlightAttribute.Highlight);
    }

    @Override
    public void render(final Renderable renderable, final Attributes combinedAttributes)
    {
        super.render(renderable, combinedAttributes);
    }

}