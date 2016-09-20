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
uniform vec3 u_lightPos;

varying vec4 v_color;
varying vec3 v_frag;
varying vec3 v_normal;

float calcLight(){
    return 1;
}

void main()
{

	vec4 finalColor  = u_diffuseColor * v_color;

	vec2 c = gl_FragCoord.xy;
	c.x /= u_width;
	c.y /= u_height;

	vec4 color = texture2D(u_shadows, c);

	finalColor.rgb *= (color.z);
	//finalColor.rgb += color.z * 0.000001;

	vec3 norm = normalize(v_normal);
	vec3 lightDir = normalize(u_lightPos - v_frag);
	float diff = max(dot(norm, lightDir), 0.0);

    gl_FragColor    = finalColor * diff;
}

