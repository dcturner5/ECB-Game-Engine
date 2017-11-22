#version 330 core

layout(location = 0) out vec4 color;

uniform mat4 pr_matrix;

uniform sampler2DArray sprite;

uniform int sprite_index;

in DATA
{
	vec2 tc;
} fs_in;

void main()
{
	color = texture(sprite, vec3(fs_in.tc, sprite_index));
	if(color.w < 1) discard;
}
