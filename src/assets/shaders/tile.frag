#version 130

in vec4 Color;
in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D sampler;

void main()
{
    vec4 texVal = texture(sampler, TexCoord);

    if(texVal.w == 0) discard;

    FragColor = vec4(Color.xyz, texVal.w);
}