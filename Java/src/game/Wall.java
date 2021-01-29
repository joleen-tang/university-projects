package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;

/**
 * Class that represents the wall of a building
 * */
public class Wall extends Ground {
	/**
	 * Constructor
	 * */
	public Wall() {
		super('#');
	}

	/**
	 * Wall does not allow actors to enter
	 * */
	@Override
	public boolean canActorEnter(Actor actor) {
		return false;
	}

	/**
	 * Wall blocks throws objects
	 * */
	@Override
	public boolean blocksThrownObjects() {
		return true;
	}
}
