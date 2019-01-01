#version 410 core

in vec3 vertex;
in vec3 normal;
in vec2 textureCoord;

uniform mat4 model;
uniform mat4 view;

uniform struct Light {
    vec3 direction;
    vec3 color;
    float ambience;
    float diffusion;
    float specularity;
    float power;
} light;

out vec4 vertexLight;
out vec3 normalWorld;
out vec3 vertexWorld;
out vec2 fragCoord;

float diffuse(vec3 normal, vec3 light) {

    vec3 n = normalize(normal);
    vec3 l = normalize(light);
    float d = dot(n, -l);

    return clamp(d, 0, 1);

}

vec4 ambientLight(Light light) {

    vec3 l = light.ambience * light.color;

    return vec4(l, 1);

}

vec4 diffuseLight(Light light, vec3 normalWorld) {
    return vec4(light.color * light.diffusion * diffuse(normalWorld, light.direction), 1);
}

void main(void) {

    vertexWorld = (model * vec4(vertex, 1)).xyz;
    normalWorld = (model * vec4(normal, 0)).xyz;
    fragCoord = textureCoord;

    vertexLight = ambientLight(light) + diffuseLight(light, normalWorld);

    gl_Position = view * model * vec4(vertex, 1);

}
