attribute vec3 a_position;
attribute vec4 a_color;
attribute vec3 a_normal;

uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
void main() {
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}
