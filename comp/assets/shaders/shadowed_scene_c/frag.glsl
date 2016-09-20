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

uniform sampler2D u_texture;
uniform vec4 u_diffuseColor;
uniform sampler2D u_shadows;
uniform float u_width;
uniform float u_height;
uniform vec4 u_color;


varying vec2 v_texCoords0;
varying float v_intensity;

void main()
{

	vec4 finalColor  = u_diffuseColor;
	finalColor.rgb  *= v_intensity;

	vec2 c = gl_FragCoord.xy;
	c.x /= u_width;
	c.y /= u_height;

	vec4 color = texture2D(u_shadows, c);

	finalColor.rgb *= color.z;
	//finalColor.rgb += color.z * 0.000001;

    gl_FragColor    = finalColor;
}

