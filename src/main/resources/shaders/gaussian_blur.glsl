#version 120

float w0_k9 = 0.382928;
float w1_k9 = 0.241732;
float w2_k9 = 0.060598;
float w3_k9 = 0.005977;
float w4_k9 = 0.000229;

vec4 hblur_k9(sampler2D source, vec2 uv, float xres) {

    vec2 p1 = vec2(xres, 0);
    vec2 p2 = vec2(xres * 2, 0);
    vec2 p3 = vec2(xres * 3, 0);
    vec2 p4 = vec2(xres * 4, 0);

    return vec4(
        texture(source, uv).rgb * w0_k9 +
        texture(source, uv - p1).rgb * w1_k9 +
        texture(source, uv + p1).rgb * w1_k9 +
        texture(source, uv - p2).rgb * w2_k9 +
        texture(source, uv + p2).rgb * w2_k9 +
        texture(source, uv - p3).rgb * w3_k9 +
        texture(source, uv + p3).rgb * w3_k9 +
        texture(source, uv - p4).rgb * w4_k9 +
        texture(source, uv + p4).rgb * w4_k9, 1);

}

vec4 vblur_k9(sampler2D source, vec2 uv, float yres) {

    vec2 p1 = vec2(0, xres);
    vec2 p2 = vec2(0, xres * 2);
    vec2 p3 = vec2(0, xres * 3);
    vec2 p4 = vec2(0, xres * 4);

    return vec4(
        texture(source, uv) * w0_k9 +
        texture(source, uv - p1) * w1_k9 +
        texture(source, uv + p1) * w1_k9 +
        texture(source, uv - p2) * w2_k9 +
        texture(source, uv + p2) * w2_k9 +
        texture(source, uv - p3) * w3_k9 +
        texture(source, uv + p3) * w3_k9 +
        texture(source, uv - p4) * w4_k9 +
        texture(source, uv + p4) * w4_k9
    , 1);

}
