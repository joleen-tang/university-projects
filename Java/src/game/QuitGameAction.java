package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Action to allow Player to quit the game
 * */
public class QuitGameAction extends Action {
    public QuitGameAction(){
    }

    /**
     * Method that removes the Actor from the map and ends the game
     *
     * @param actor Actor to execute the Action
     * @param map Map where actor is
     * */
    @Override
    public String execute(Actor actor, GameMap map){
        map.removeActor(actor);
        return "Quit game";
    }

    /**
     * Method to get menu description for this action
     * @param actor The actor performing the action.
     * @return a String description of what this Action is capable of
     */
    @Override
    public String menuDescription(Actor actor){
        return "Quit game";
    }
}
