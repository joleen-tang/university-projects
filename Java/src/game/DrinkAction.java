package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Action where dinosaur drinks water
 * */
public class DrinkAction extends Action {
    /** The dinosaur drinking water */
    private final Dinosaur DINO;

    /** Constructor */
    public DrinkAction(Dinosaur dino){
        this.DINO = dino;
    }

    /**
     * Execute method will make the dinosaur less thirsty
     * @param actor The dinosaur performing the action.
     * @param map The map the dinosaur is on.
     * @return null
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        DINO.increaseThirstLevel(DINO.MAX_THIRST_LEVEL/2);
        return DINO + " drank water.";
    }


    /**
     * Creates a menu description for this action
     * */
    @Override
    public String menuDescription(Actor actor) {
        return actor +" drinks water.";
    }
}
