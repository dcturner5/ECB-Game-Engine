package com.gammarush.engine.tiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.gammarush.engine.graphics.model.Texture;

public class TileLoader {

	public static void load(String path) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] tiles = line.split("\n");
				for(int i = 0; i < tiles.length; i++) {
					String[] data = tiles[i].split(";");
					int id = Integer.parseInt(data[0]);
					String name = data[1];
					Texture texture = new Texture(data[2]);
					boolean solid = Boolean.parseBoolean(data[3]);
					int blendType = Integer.parseInt(data[4]);
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + path + "'");
		}
	}

}
