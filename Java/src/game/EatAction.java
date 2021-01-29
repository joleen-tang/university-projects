package game;

import edu.monash.fit2099.engine.*;

/**
 * Action for dinosaurs to eat something
 * */
public class EatAction extends Action {
    /** The dinosaur eating the food */
    private final Dinosaur DINO;
    /** The location containing the item the dinosaur will eat */
    private final Location CURRENT_LOCATION;
    /** The food item that will be consumed by the dinosaur */
    private final FoodItem FOOD;

    /**
     * Constructor for EatAction
     * @param food to be consumed by the dinosaur
     * @param currentLocation current location of the dinosaur. Needed to get it out of the location.
     * @param dino dinosaur
     */
    public EatAction(FoodItem food, Location currentLocation, Dinosaur dino){
        this.DINO = dino;
        this.CURRENT_LOCATION = currentLocation;
        this.FOOD = food;
    }

    /**
     * What should happen when this Action is executed
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string of what the outcome of EatAction was
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        CURRENT_LOCATION.removeItem(FOOD);
        DINO.increaseFoodLevel(FOOD.getFoodPoints());
        return DINO + " ate"+ FOOD;
    }

    /**
     * A string description for the user to see
     * @param actor The actor performing the action.
     * @return a string description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " eats " + FOOD;
    }


}
