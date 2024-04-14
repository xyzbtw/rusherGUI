#version 330

in vec4 color;
in vec3 pos;

uniform mat4 modelView;
uniform mat4 projection;
out vec4 vertex_color;

void main() {
    gl_Position = modelView * projection * vec4(pos, 1.0);
    vertex_color = color;
}
