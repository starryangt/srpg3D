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
attribute vec2 a_texCoord0;
attribute vec4 a_color;
attribute vec3 a_normal;


uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
uniform mat3 u_normalMatrix;
uniform mat4 u_lightTrans;

varying vec4 v_position;
varying vec4 v_positionLightTrans;
varying vec2 v_texCoord0;


void main()
{
	// Vertex position after transformation
    v_position = u_worldTrans * vec4(a_position, 1.0);
    v_positionLightTrans = u_lightTrans * v_position;
    gl_Position =   u_projViewTrans * v_position;
    v_texCoord0 = a_texCoord0;
}
