package com.gammarush.engine.entities.interactives.static_vehicles;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.vehicles.Mercury;
import com.gammarush.engine.math.vector.Vector3f;

public class StaticMercury extends StaticVehicle {
	
	public StaticMercury(Vector3f position, int direction, Game game) {
		super(position, Mercury.WIDTH, Mercury.HEIGHT, Mercury.MODEL, game);
		this.direction = direction;
	}
	
	@Override
	public void activate(Mob mob) {
		Mercury e = new Mercury(position, direction, game);
		mob.setVehicle(e);
		game.world.vehicles.add(e);
		game.world.interactives.remove(this);
	}

}
