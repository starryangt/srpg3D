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

uniform sampler2D u_diffuseTexture;
uniform float u_cameraFar;

varying vec4 v_position;
varying vec2 v_texCoord0;
uniform vec3 u_lightPosition;
void main()

{
	// Simple depth calculation, just the length of the vector light-current position
	vec4 a = texture2D(u_diffuseTexture, v_texCoord0);
	if(a.a < 0.5){
        discard;
	}
	vec4 color = vec4(length(v_position.xyz-u_lightPosition)/u_cameraFar);
	//if(a.a < 0.3){
	   // discard;
	//}
	gl_FragColor     = color;
	
}

