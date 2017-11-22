#version 330 core

layout(location = 0) out vec4 color;

uniform mat4 pr_matrix;
uniform sampler2D sprite;

uniform sampler2D blend_sprite;
uniform sampler2DArray blend_map;
uniform int blend_indices[8];

const int MAX_LIGHTS = 8;
uniform float point_radius[MAX_LIGHTS];
uniform vec4 point_color[MAX_LIGHTS];
uniform vec4 ambient_color;
uniform vec3 global_direction;
uniform vec4 global_color;
uniform vec2 resolution;
uniform sampler2D normal_map;

in DATA
{
	vec2 tc;
	vec3 point_screen_position[MAX_LIGHTS];
} fs_in;

void main()
{
	int blend_index = -1;
	float blend_percent = 0;
	for(int i = 0; i < 8; i++) {
		float blend_sample = texture(blend_map, vec3(fs_in.tc, i)).r;
		if(blend_indices[i] != 0 && blend_sample > blend_percent) {
			blend_index = i;
			blend_percent = blend_sample;
		}
	}
	
	vec4 diffuse_color;
	vec3 normal = vec3(0, 0, 1);
	if(blend_percent > 0) {
		diffuse_color = texture(blend_sprite, fs_in.tc);
	}
	else {
		diffuse_color = texture(sprite, fs_in.tc);
		normal = texture(normal_map, fs_in.tc).rgb * 2.0 - 1.0;
	}
	
	vec3 ambient = ambient_color.rgb * ambient_color.a;
	vec3 global_diffuse = (global_color.rgb * global_color.a) * max(dot(normal, normalize(global_direction)), 0);
	vec3 sum = diffuse_color.rgb * (global_diffuse + ambient);
	
	for(int i = 0; i < MAX_LIGHTS; i++) {
		vec3 light_direction = vec3(fs_in.point_screen_position[i].xy - (gl_FragCoord.xy / (resolution.xy / 2)), fs_in.point_screen_position[i].z);
		light_direction.x *= resolution.x / resolution.y;
		float distance = length(light_direction);
		vec3 light_direction_normalized = normalize(light_direction);
		vec3 diffuse = (point_color[i].rgb * point_color[i].a) * max(dot(normal, light_direction_normalized), 0);
		float attenuation = clamp(1.0 - distance * distance / (point_radius[i] * point_radius[i]), 0.0, 1.0);
		attenuation *= attenuation;
		vec3 intensity = diffuse * attenuation;
		vec3 final_color = diffuse_color.rgb * intensity;
		sum += final_color;
	}
	
	color = vec4(sum, diffuse_color.a);
}
