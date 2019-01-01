#version 410 core

in vec2 uv;

uniform sampler2D scene;

out vec4 fragColor;

float minThreshold = 0.9;
float maxThreshold = 1.0;
float subtractor = minThreshold;
float multiplicator = 1.0 / (maxThreshold - minThreshold);

void main(void) {

    vec3 texel = texture(scene, uv).rgb;
    float level = max(texel.r, max(texel.g, texel.b));
    vec3 color = clamp(vec3(level - subtractor) * multiplicator, 0, 1);

    fragColor = vec4(color, 1);

}