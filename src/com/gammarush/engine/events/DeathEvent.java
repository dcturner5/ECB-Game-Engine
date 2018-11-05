package com.gammarush.engine.events;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.actors.Actor;

public class DeathEvent extends Event {
	
	private Mob mob1, mob2;
	
	public DeathEvent(Mob mob1, Mob mob2) {
		this.mob1 = mob1;
		this.mob2 = mob2;
	}
	
	public void execute(EventManager eventManager) {
		if(mob1.isPlayer())
			eventManager.execute("death_player_by_mob", mob2.getTemplate().getName());
		
		if(mob1.isPlayer() && mob2.isActor())
			eventManager.execute("death_player_by_actor", ((Actor) mob2).getName());
		
		if(mob2.isPlayer())
			eventManager.execute("death_mob_by_player", mob1.getTemplate().getName());
		
		if(mob2.isPlayer() && mob1.isActor())
			eventManager.execute("death_actor_by_player", ((Actor) mob1).getName());
		
		if(mob1.isActor())
			eventManager.execute("death_actor_by_mob", ((Actor) mob1).getName(), mob2.getTemplate().getName());
		
		if(mob1.isActor() && mob2.isActor())
			eventManager.execute("death_actor_by_actor", ((Actor) mob1).getName(), ((Actor) mob2).getName());
		
		if(mob2.isActor())
			eventManager.execute("death_mob_by_actor", mob1.getTemplate().getName(), ((Actor) mob2).getName());
		
		eventManager.execute("death_mob_by_mob", mob1.getTemplate().getName(), mob2.getTemplate().getName());
	}

}
