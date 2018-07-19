package com.gammarush.engine.entities.components;

import java.util.ArrayList;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.world.Chunk;
import com.gammarush.engine.world.World;

public class PhysicsComponent extends Component {
	
	public static final String NAME = "physics";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 3;
	
	public Vector2f velocity;
	public float acceleration;

	public PhysicsComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		this.velocity = new Vector2f();
		this.acceleration = 0;
	}
	
	public PhysicsComponent(Entity entity, float acceleration) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		this.velocity = new Vector2f();
		this.acceleration = acceleration * Renderer.SCALE;
	}

	@Override
	public void update(double delta) {
		Entity e = getEntity();
		
		Vector2f position = new Vector2f(e.position).add(velocity);
		Vector2f translation = collision(position);
		position = position.add(translation);
		
		e.position.x = position.x;
		e.position.y = position.y;
		e.position.z = Renderer.ENTITY_LAYER + (e.position.y / Tile.HEIGHT) / Chunk.HEIGHT;
		
		if(translation.x != 0) {
			e.position.x -= velocity.x;
			velocity.x = 0;
		}
		if(translation.y != 0) {
			e.position.y -= velocity.y;
			velocity.y = 0;
		}
	}
	
	public Vector2f collision(Vector2f position) {
		Entity e = getEntity();
		World w = e.getWorld();
		AABB box = e.getAABB();
		
		ArrayList<Vector2i> tiles = w.getSolidTiles((int) ((position.x + e.width / 2) / Tile.WIDTH), (int) ((position.y + e.height / 2) / Tile.HEIGHT), Math.max(e.width / Tile.WIDTH + 1, e.height / Tile.HEIGHT + 1));
		
		ArrayList<AABB> groups = new ArrayList<AABB>();
		for(int i = 0; i < tiles.size(); i++) {
			Vector2i tile = tiles.get(i);
			boolean success = false;
			for(int j = 0; j < groups.size(); j++) {
				AABB group = groups.get(j);
				if(tile.x == group.x && tile.y == group.y + group.height) {
					group.height += Tile.HEIGHT;
					success = true;
				}
			}
			if(!success) {
				AABB group = new AABB(tile.x, tile.y, Tile.WIDTH, Tile.HEIGHT);
				groups.add(group);
			}
		}
		
		for(Entity entity : e.getWorld().getEntities()) {
			AABB entityBox = entity.getAABB();
			if(entity.getSolid() && !e.equals(entity) && Physics.getCollision(box, entityBox)) {
				groups.add(entity.getAABB());
			}
		}
		
		Vector2f mtv = new Vector2f();
		for(int i = 0; i < groups.size(); i++) {
			AABB groupBox = groups.get(i);
			if(Physics.getCollision(box, groupBox)) {
				Vector2f v = Physics.getTranslationVector(box, groupBox);
				if(Math.abs(v.x) > Math.abs(mtv.x)) mtv.x = v.x;
				if(Math.abs(v.y) > Math.abs(mtv.y)) mtv.y = v.y;
			}
		}
		
		return mtv;
	}

}
