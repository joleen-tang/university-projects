package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Action that allows a dinosaur to lay an egg after its pregnancy period
 * */
public class LayEggAction extends Action {
    /** The egg to be placed on the map */
    private final Egg EGG;
    /** The dinosaur to lay the egg */
    private final Dinosaur MOTHER;

    /**
     * Constructor
     * @param mother Mother dinosaur
     */
    public LayEggAction(Dinosaur mother){
        this.EGG=new Egg(mother.createNewCopy());
        this.MOTHER=mother;
    }

    /**
     * What is executed as part of this action
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string that explains the outcome of executing LayEggAction
     */
    @Override
    public String execute(Actor actor, GameMap map){
        map.locationOf(actor).addItem(EGG);
        MOTHER.resetEggCounter();
        return MOTHER+" laid an egg";
    }

    /**
     * Method to get menu description for this action
     * @param actor The actor performing the action.
     * @return a String description of what this Action is capable of
     */
    @Override
    public String menuDescription(Actor actor){
        return actor + "lays an egg";
    }
}
