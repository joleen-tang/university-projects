package game;

import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

/**
 * A tree that can drop fruit
 * */
public class Tree extends Ground {
	/** Number of turns since tree was created */
	private int age = 0;
	/** Probability of the tree dropping a fruit in the Location where Tree is */
	private static final int DROP_CHANCE = 5;

	/**
	 * Constructor for Ground type Tree
	 */
	public Tree() {
		super('+');
	}


	/**
	 * How Tree experiences time
	 * location is the where the particular Tree is located in the map.
	 * Calculates the probability of dropping Fruit. If probability is lower than random number then Fruit will be dropped.
	 *
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);

		age++;
		if (age == 10)
			displayChar = 't';
		if (age == 20)
			displayChar = 'T';

		//Getting a random number between 0 and 100
		//If the random number is less than or equal to DROP_CHANCE (5%) then add a new Fruit to the location (lying on ground)
		double probFruit = Math.random() * (100 + 1) + 0;
		if (probFruit <= DROP_CHANCE){
			location.addItem(new Fruit());
		}
	}

	/**
	 * Getting allowable actions at Tree
	 * @param actor the Actor acting
	 * @param location the current Location
	 * @param direction the direction of the Ground from the Actor
	 * @return Actions that can be performed at this location (with three as ground)
	 */
	@Override
	public Actions allowableActions(Actor actor, Location location, String direction) {
		Actions temp = new Actions();
		Fruit fruit = new Fruit();
		temp.add(new HarvestAction(location, fruit));
		return temp;
	}




}
