#version 410

in vec2 position;

out vec2 uv;

void main(void) {

    gl_Position = vec4(position, 0.1, 1);

    uv = position * 0.5 + 0.5;

}