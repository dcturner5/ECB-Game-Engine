package com.gammarush.engine.entities.mobs.human;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class Human extends Mob {
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	public static final Model MODEL = new Model(new TextureArray("res/entities/mobs/human/human.png", 64));
	
	JSON json = JSONLoader.load("res/entities/mobs/human/data.json");
	
	protected Behavior travel, lumber;

	public Human(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
		
		addComponent(new AnimationComponent(this, json.getAnimationHashMap("mobs.human.animations")));
		
		ArrayList<Vector4f[]> colors = new ArrayList<Vector4f[]>();
		ArrayList<JSON> colorsJSON = json.getArray("mobs.human.colors");
		for(JSON color : colorsJSON) {
			colors.add(new Vector4f[] {color.getColor("primary"), color.getColor("secondary")});
		}
		
		ArrayList<Vector4f[]> hairColors = new ArrayList<Vector4f[]>();
		ArrayList<JSON> hairColorsJSON = json.getArray("mobs.human.hairColors");
		for(JSON hairColor : hairColorsJSON) {
			hairColors.add(new Vector4f[] {hairColor.getColor("primary"), hairColor.getColor("secondary")});
		}
		
		//race and hair
		color = colors.get((int) (Math.random() * colors.size()));
		hairColor = hairColors.get((int) (Math.random() * hairColors.size()));
		
		//hair
		//outfit.add(Game.clothings.getRandomByType(ClothingTemplate.TYPE_HAIR));
		//head
		//if(Math.random() < .25) outfit.add(Game.clothings.getRandomByType(ClothingTemplate.TYPE_HEAD));
		//body
		//if(Math.random() < .75) outfit.add(Game.clothings.getRandomByType(ClothingTemplate.TYPE_BODY))
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		AnimationComponent ac = ((AnimationComponent) getComponent("animation"));
		if(moving) ac.start("run");
		else ac.stop("run");

		if(!ac.isRunning()) ac.start("idle");
	}
	
	public void travel(int x, int y) {
		/*idle.queue.clear();
		
		behaviors.remove(travel);
		travel = new TravelBehavior(new Vector2i(x, y), this);
		behaviors.add(travel);*/
	}

}
