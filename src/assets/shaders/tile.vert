#version 130

in vec3 VertexPosition;
in vec4 VertexColor;
in vec2 VertexTexCoord;

out vec4 Color;
out vec2 TexCoord;

uniform mat4 transform;
uniform mat4 camera;

void main()
{
    Color = VertexColor;
    TexCoord = VertexTexCoord;

    gl_Position = camera*transform*vec4(VertexPosition, 1.0);
}
