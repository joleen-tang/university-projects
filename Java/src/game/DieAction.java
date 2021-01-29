package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Action that implements dying
 * */
public class DieAction extends Action {

    /**
     * Constructor
     */
    public DieAction(){
    }

    /**
     * Execute method occurs after dinosaur has been removed from map
     *
     * @param actor The dying dinosaur
     * @param map The map the  dinosaur is on.
     * @return a string describing where the dinosaur died
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return actor+" died";
    }

    /**
     * Method to get string to represent this action to printed on menus
     * @param actor THe dying dinosaur
     * @return a string stating that the dinosaur dies
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Dinosaur dies";
    }
}
