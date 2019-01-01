#version 410 core

in vec2 uv;

uniform sampler2D scene;
uniform sampler2D highlight;

out vec4 fragColor;

void main(void) {

    vec3 texel = texture(scene, uv).rgb;
    vec3 level = texture(highlight, uv).rgb;

    fragColor = vec4(clamp(texel + level * 2, 0, 1), 1);

}