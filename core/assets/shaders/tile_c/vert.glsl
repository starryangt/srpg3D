#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

attribute vec3 a_position;
attribute vec4 a_color;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;


uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_normalMatrix;

varying vec4 v_color;
varying vec4 v_opos;
varying vec2 v_tex;

void main()
{
	// Vertex position after transformation
    vec4 v_position = u_worldTrans * vec4(a_position, 1.0);
    v_opos = v_position;
    gl_Position =   u_projViewTrans * v_position;
    v_color = a_color;
    v_tex = a_texCoord0;

    // Just add some basic self shadow
}
