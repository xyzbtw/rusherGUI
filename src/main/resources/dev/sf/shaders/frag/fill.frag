#version 330

in vec4 vertex_color;

out vec4 fragcolor;

void main() {
    fragcolor = vertex_color;
}