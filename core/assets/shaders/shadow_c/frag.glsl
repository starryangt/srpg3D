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

uniform sampler2DShadow u_depthMap;
uniform samplerCube u_depthCube;
uniform float u_cameraFar;
uniform vec3 u_lightPosition;
uniform vec4 u_diffuseColor;
uniform float u_shadowIntensity;
uniform float u_lightScaling;
uniform float u_roundError;
uniform int u_type;

varying vec4 v_position;
varying vec4 v_positionLightTrans;
varying vec2 v_texCoord0;

void main()
{


    float intensity = 0.0;

	vec3 lightDirection=v_position.xyz-u_lightPosition;
    float lenToLight=length(lightDirection)/u_cameraFar;

    float lenDepthMap = -1.0;

    if(u_type == 1){
        vec3 depth = (v_positionLightTrans.xyz / v_positionLightTrans.w) * 0.5+0.5;
        if (v_positionLightTrans.z>=0.0 &&
             (depth.x >= 0.0) && (depth.x <= 1.0) &&
             (depth.y >= 0.0) && (depth.y <= 1.0) ) {
             lenDepthMap = shadow2D(u_depthMap, depth.xyz).z;
        }
    }
    else if(u_type == 2){
        lenDepthMap = textureCube(u_depthCube, lightDirection).z;
    }

    // If can not be viewed by light > shadows
    if(lenDepthMap<lenToLight-u_roundError){
        intensity = u_shadowIntensity;
    }else{
        intensity = u_shadowIntensity + u_lightScaling*(1.0-lenToLight);

    }
    gl_FragColor    = vec4(intensity);
}

