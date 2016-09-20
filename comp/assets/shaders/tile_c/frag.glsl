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

uniform sampler2D u_tileMap;
uniform float u_mapFactorX;
uniform float u_mapFactorY;
uniform float u_alpha;
uniform vec4 u_diffuseColor;
uniform mat4 u_projTrans;

uniform vec3 u_position;
uniform vec3 u_attack;
uniform vec3 u_ally;

varying vec4 v_opos;
varying vec4 v_color;
varying vec2 v_tex;

void main()
{
    float x = v_opos.x / u_mapFactorX;
    float y = -v_opos.z / u_mapFactorY;
    vec4 color = texture2D(u_tileMap, vec2(x, y));
    //vec4 color = texture2D(u_tileMap, v_tex);
	vec4 finalColor  = vec4(0, 0, 0, u_alpha);
    if(color.g > 0.5){
        finalColor.rgb = u_position;
    }
    else if(color.b > 0.5){
        finalColor.rgb = u_attack;
    }
    else if (color.r > 0.5){
        finalColor.rgb = u_ally;
    }
    else{
        discard;
    }
	//finalColor.rgb = u_color.rgb;
	//finalColor.a = u_alpha;
	//finalColor.rgb   = finalColor.rgb*v_intensity;
    gl_FragColor    = finalColor;
}

