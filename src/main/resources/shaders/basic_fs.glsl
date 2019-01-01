#version 410 core

in vec4 vertexLight;
in vec3 normalWorld;
in vec3 vertexWorld;
in vec2 fragCoord;

uniform vec3 eye;
uniform struct Light {
    vec3 direction;
    vec3 color;
    float ambience;
    float diffusion;
    float specularity;
    float power;
} light;
uniform sampler2D sampler;

out vec4 fragColor;

float specular(vec3 eye, vec3 pos, vec3 normal, vec3 light) {

    vec3 n = normalize(eye - pos);
    vec3 l = normalize(reflect(light, normal));
    float d = dot(n, l);

    return clamp(d, 0, 1);

}

vec4 specularLight(vec3 eye, vec3 pos, vec3 normal, Light light) {

    float s = specular(eye, pos, normal, light.direction);
    float p = pow(s, light.power);

    return vec4(light.color * light.specularity * p, 1);

}

void main(void) {

    vec4 l = (vertexLight + specularLight(eye, vertexWorld, normalize(normalWorld), light));

    //fragColor = texture(sampler, fragCoord) * l;
    fragColor = texture(sampler, fragCoord) * l;
    fragColor = vec4(0.1, 0.5, 0.9, 1) * l;

}