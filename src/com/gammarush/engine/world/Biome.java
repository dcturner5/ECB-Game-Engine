package com.gammarush.engine.world;

import com.gammarush.engine.math.vector.Vector2f;

public class Biome {
	
	public static final Biome PLAINS = new Biome(0, new Vector2f(-1.0f, 1.0f), new Vector2f(-1.0f, 1.0f), 5, 1f, 1/64f);
	public static final Biome FOREST = new Biome(1, new Vector2f(-.05f, .05f), new Vector2f(-.1f, .05f), 5, 1f, 1/64f);
	public static final Biome DESERT = new Biome(2, new Vector2f(-.05f, 1.0f), new Vector2f(-1.0f, -.1f), 5, 1f, 1/64f);
	
	public int id;
	
	public Vector2f temperatureRange;
	public Vector2f humidityRange;
	
	public int octaves;
	public float persistance;
	public float frequency;
	
	public Biome(int id, Vector2f temperatureRange, Vector2f humidityRange, int octaves, float persistance, float frequency) {
		this.id = id;
		this.temperatureRange = temperatureRange;
		this.humidityRange = temperatureRange;
		this.octaves = octaves;
		this.persistance = persistance;
		this.frequency = frequency;
	}
	
	public boolean valid(float temperature, float humidity) {
		return temperature >= temperatureRange.x && temperature < temperatureRange.y && humidity >= humidityRange.x && humidity < humidityRange.y;
	}

}
