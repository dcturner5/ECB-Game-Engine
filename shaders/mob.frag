#version 330 core

layout(location = 0) out vec4 color;

uniform mat4 pr_matrix;
uniform sampler2DArray sprite;
uniform int sprite_index;

uniform vec4 primary_color;
uniform vec4 secondary_color;

in DATA
{
	vec2 tc;
} fs_in;

void main()
{
	color = texture(sprite, vec3(fs_in.tc, sprite_index));
	
	if(color == vec4(1, 0, 0, 1)) color = primary_color;
	if(color == vec4(0, 1, 0, 1)) color = secondary_color;
	
	if(color.w < 1) discard;
}
