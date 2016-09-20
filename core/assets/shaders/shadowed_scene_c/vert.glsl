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

uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_normalMatrix;
uniform mat4 u_lightTrans;

varying vec4 v_color;
varying vec3 v_normal;
varying vec3 v_frag;

void main()
{
	// Vertex position after transformation
    vec4 v_position = u_worldTrans * vec4(a_position, 1.0);
    gl_Position =   u_projViewTrans * v_position;
    v_frag = vec3(u_worldTrans * vec4(a_position, 1.0));
    v_color = a_color;
    v_normal = a_normal;
}
