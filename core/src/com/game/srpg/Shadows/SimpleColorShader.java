package com.game.srpg.Shadows;

/**
 * Created by FlyingJam on 8/5/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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

/**
 * Simple shader for an object with a texture
 */
public class SimpleColorShader extends BaseShader
{
    public Renderable	renderable;

    @Override
    public void end()
    {
        super.end();
    }

    public SimpleColorShader(final Renderable renderable, final ShaderProgram shaderProgramModelBorder)
    {
        this.renderable = renderable;
        this.program = shaderProgramModelBorder;
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
        return instance.material.has(ColorAttribute.Diffuse);
    }

    @Override
    public void render(final Renderable renderable, final Attributes combinedAttributes)
    {
        super.render(renderable, combinedAttributes);
    }

}