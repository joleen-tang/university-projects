package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

/**
 * Action to feed the Dinosaur
 * */
public class FeedAction extends Action {
    /** Food item to be fed to the dinosaur */
    private final FoodItem FOOD_ITEM;
    /** Dinosaur to be fed */
    private final Dinosaur TARGET;

    /**
     * Constructor for FeedAction where a player can feed a dinosaur
     * @param foodItem foodItem to be fed to the dinosaur
     * @param target the dinosaur to be fed
     */
    public FeedAction(FoodItem foodItem, Dinosaur target){
        this.FOOD_ITEM=foodItem;
        this.TARGET=target;
    }

    /**
     * What the action will execute
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string of what was performed by FeedAction
     */
    @Override
    public String execute(Actor actor, GameMap map){
        this.TARGET.increaseFoodLevel(FOOD_ITEM.getFoodPoints());
        actor.removeItemFromInventory(FOOD_ITEM);
        return actor+" feeds "+this.TARGET+" "+this.FOOD_ITEM;
    }

    /**
     * A string description of what the action does as seen by the user before selecting.
     * @param actor The actor performing the action.
     * @return string with a what the action does
     */
    @Override
    public String menuDescription(Actor actor){
        return "Feed "+this.TARGET+" "+this.FOOD_ITEM;
    }
}
