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

varying float v_intensity;
varying vec4 v_color;

void main()
{
	// Vertex position after transformation
    vec4 v_position = u_worldTrans * vec4(a_position, 1.0);
    gl_Position =   u_projViewTrans * v_position;
    v_color = a_color;


    // Just add some basic self shadow
    vec3 normal = normalize(u_normalMatrix * a_normal);
	v_intensity=1.1;
   	if(normal.y<0.5){
		if(normal.x>0.5 || normal.x<-0.5)
			v_intensity*=0.8;
		if(normal.z>0.5 || normal.z<-0.5)
			v_intensity*=0.6;
	}
}
