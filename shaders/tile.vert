#version 330 core


layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0);
uniform mat4 ml_matrix = mat4(1.0);

const int MAX_LIGHTS = 8;

uniform vec3 point_position[MAX_LIGHTS];

out DATA
{
	vec2 tc;
	vec3 point_screen_position[MAX_LIGHTS];
} vs_out;

void main()
{
	for(int i = 0; i < MAX_LIGHTS; i++) {
		vec4 clip_space = pr_matrix * (vw_matrix * vec4(point_position[i], 1.0));
		vec3 ndc_space = clip_space.xyz / clip_space.w;
		vs_out.point_screen_position[i] = vec3((ndc_space.xy + 1.0), point_position[i].z);
	}
	
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	vs_out.tc = tc;
}
