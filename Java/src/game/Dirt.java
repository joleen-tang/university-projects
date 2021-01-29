package game;
import edu.monash.fit2099.engine.*;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

import java.util.List;

/**
 * A class that represents bare dirt.
 */
public class Dirt extends Ground {
	/** How many turns it has been since the game started */
	private Integer tickCounter = 0;
	/** Probability that grass will grow in the Location where this Dirt is */
	private Integer grassProb;
	/** Number of ecoPoints that the player earns when this Dirt object creates a Grass object */
	private static final int GRASS_GROW_ECO = 1;

	/**
	 * Constructor
	 * */
	public Dirt() {
		super('.');
	}

	/**
	 * This method will check if the current location has grass in it
	 * @param currentLocation  Location where Dirt is located
	 * @return   returns True or False depending on the availability of Grass in the location
	 */
	private boolean grassStatus(Location currentLocation){
		for(int i=0; i<currentLocation.getItems().size(); i++){
			if (currentLocation.getItems().get(i) instanceof Grass){
				return true;
			}
		}
		return false;
	}

	/**
	 * tick is how Dirt experiences time
	 * @param currentLocation is the location where this particular Dirt is situated in
	 * This method will look for the different probabilities of growing grass and add grass to the Location's items list if the probability criteria is met
	 */
	public void tick(Location currentLocation){
		tickCounter += 1;
		if (!this.grassStatus(currentLocation)) {
			//At the start of the game, dirt will have a 2% chance of growing grass. This will check if this is the initial tick()
			if (tickCounter == 1) {
				grassProb = 2;
			}
			int treeCount = 0;
			int grassCount = 0;
			List<Exit> exits = currentLocation.getExits();
			for (Exit exit : exits) {
				Location surroundingLocation = exit.getDestination();
				if (surroundingLocation.getGround() instanceof Tree) {
					treeCount += 1;
				}
				if (surroundingLocation.getGround() instanceof Dirt) { //dirt or this
					for (int i=0; i<surroundingLocation.getItems().size(); i++){
						if (surroundingLocation.getItems().get(i) instanceof Grass){
							grassCount += 1;
						}
					}
				}
			}
			if (treeCount >1){
				grassProb = 1;
			}
			if (grassCount >= 2){
				grassProb = 5;
			}

			//Getting a random number between 0 and 100
			//If the random number is less than or equal to grass growing probability then add a new Grass item to the location (lying on ground)
			double random = Math.random() * (100 - 0 + 1) + 0;
			if (random <= grassProb){
				currentLocation.addItem(new Grass());
				Player.increaseEcoPoints(GRASS_GROW_ECO);
			}
		}



	}

	/**
	 * If there is Grass in the ground, let the actor harvest Grass to obtain hay
	 * @param actor the Actor acting
	 * @param location the current Location
	 * @param direction the direction of the Ground from the Actor
	 * @return
	 */
	@Override
	public Actions allowableActions(Actor actor, Location location, String direction) {
		Actions temp = new Actions();
		for (int i =0; i<location.getItems().size(); i++){
			if (location.getItems().get(i) instanceof Grass){
				Hay hay = new Hay();
				temp.add(new HarvestAction(location, location.getItems().get(i), hay));
				return temp;
			}
		}
		return temp;
	}

}
