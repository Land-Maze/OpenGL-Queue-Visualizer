#version 330 core

in vec3 FragPos;
in vec3 Normal;

out vec4 FragColor;

uniform vec4 color;
uniform vec3 lightPos;
uniform vec3 viewPos;

void main() {
    // ambient lighting
    float ambientStrength = 0.2;
    vec3 ambient = ambientStrength * vec3(color);

    // diffuse lighting
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * vec3(color) * 2.0;

    vec3 result = ambient + diffuse;
    FragColor = vec4(result, color.a);
}
