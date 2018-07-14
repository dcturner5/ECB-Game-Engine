#version 330 core

layout(location = 0) out vec4 color;

uniform mat4 pr_matrix;

uniform sampler2D sprite;

uniform int offset;
uniform int width;

uniform vec4 cutoff;

in DATA
{
	vec4 color;
	vec2 tc;
	vec2 ts;
} fs_in;

void main()
{
	if(fs_in.tc.x < cutoff.x || fs_in.tc.y < cutoff.y || fs_in.tc.x > cutoff.z || fs_in.tc.y > cutoff.w) {
		discard;
	}

	float uv_offset = offset / fs_in.ts.x;
	float uv_width = width / fs_in.ts.x;
	vec2 tc = vec2(fs_in.tc.x * uv_width + uv_offset, fs_in.tc.y);

	color = texture(sprite, tc);
	if(color == vec4(0, 0, 0, 1)) {
		color = fs_in.color;
	}
	if(color.w == 0) {
		discard;
	}
}
