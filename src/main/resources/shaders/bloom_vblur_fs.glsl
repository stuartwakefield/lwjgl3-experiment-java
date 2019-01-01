#version 410 core

in vec2 uv;

uniform sampler2D source;
uniform vec2 resolution;

out vec4 fragColor;

float w0_k9 = 0.382928;
float w1_k9 = 0.241732;
float w2_k9 = 0.060598;
float w3_k9 = 0.005977;
float w4_k9 = 0.000229;

void main(void) {

    float yres = resolution.y;

    vec2 p1 = vec2(0, yres);
    vec2 p2 = vec2(0, yres * 2);
    vec2 p3 = vec2(0, yres * 3);
    vec2 p4 = vec2(0, yres * 4);

    fragColor = clamp(
        texture(source, uv) * w0_k9 +
        texture(source, uv - p1) * w1_k9 +
        texture(source, uv + p1) * w1_k9 +
        texture(source, uv - p2) * w2_k9 +
        texture(source, uv + p2) * w2_k9 +
        texture(source, uv - p3) * w3_k9 +
        texture(source, uv + p3) * w3_k9 +
        texture(source, uv - p4) * w4_k9 +
        texture(source, uv + p4) * w4_k9
    , 0, 1);

}