package game;

import edu.monash.fit2099.engine.*;

/**
 * Action that allowed player to get an Item from a Location
 * */
public class HarvestAction extends Action {
    /** Location to be harvested */
    private final Location currentLocation;
    /** Item to be harvested */
    private final Item harvestOutcome;
    private final Item harvesting;
    private static final int PICK_CHANCE = 60;

    /**
     * Constructor
     * @param currentLocation of where the harvest is going to take place
     * @param harvest what is used to harvest
     * @param harvestOutcome what is the output of the harvest
     */
    public HarvestAction(Location currentLocation, Item harvest, Item harvestOutcome){
        this.currentLocation = currentLocation;
        this.harvestOutcome = harvestOutcome;
        this.harvesting = harvest;

    }

    /**
     * same as above with less parameters
     * @param currentLocation
     * @param harvestOutcome
     */
    public HarvestAction(Location currentLocation, Item harvestOutcome) {
        this.currentLocation = currentLocation;
        this.harvestOutcome = harvestOutcome;
        this.harvesting = null;
    }

    /**
     * executing different methods to harvest
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string of the completed or incomplete harvest action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (currentLocation.getGround() instanceof Dirt){
            if(grassStatus(currentLocation)){
                currentLocation.removeItem(harvesting);
                actor.addItemToInventory(harvestOutcome);
                return "Player Harvested Hay";
            }
        }
        if(currentLocation.getGround() instanceof Tree){
            double probFruit = Math.random() * (100 + 1) + 0;
            if (probFruit <= PICK_CHANCE){
                actor.addItemToInventory(harvestOutcome);
                return "Player Picked Fruit";
            }
            return "Oops fruit was not picked successfully. Better luck next time";
        }
        return null;
    }

    /**
     * Description of what harvest action does as seen by the user
     * @param actor The actor performing the action.
     * @returns a string of what can be performed by HarvestAction
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Player harvests "+harvestOutcome;
    }
    private boolean grassStatus(Location currentLocation){
        for(int i=0; i<currentLocation.getItems().size(); i++){
            if (currentLocation.getItems().get(i) instanceof Grass){
                return true;
            }
        }
        return false;
    }
}
