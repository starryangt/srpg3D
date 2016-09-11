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

uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;

varying vec4 v_position;
varying vec2 v_texCoord0;
varying float v_intensity;

void main()
{
    v_position = u_worldTrans*vec4(a_position, 1.0);
    v_texCoord0 = a_texCoord0;
    gl_Position = u_projViewTrans *v_position;
}
