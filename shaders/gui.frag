#version 330 core

layout(location = 0) out vec4 color;

uniform mat4 pr_matrix;

uniform sampler2D sprite;

uniform int use_sprite;
uniform float sprite_alpha;

uniform vec4 cutoff;

in DATA
{
	vec4 color;
	vec2 tc;
} fs_in;

void main()
{
	if(fs_in.tc.x < cutoff.x || fs_in.tc.y < cutoff.y || fs_in.tc.x > cutoff.z || fs_in.tc.y > cutoff.w) {
		discard;
	}
	
	if(use_sprite == 1) {
		color = texture(sprite, fs_in.tc);
		if(color.w > 0) color.w = sprite_alpha;
		if(color.w == 0) discard;
	}
	else {
		color = fs_in.color;
	}
}
