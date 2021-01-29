package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.dinosaurs.DinoCapabilities;

/**
 *BreedAction will increase the lay egg counter of the female.
 */
public class BreedAction extends Action {
    /** The dinosaur that has been impregnated */
    private final Dinosaur DINOSAUR;
    /** The dinosaur that did the impregnating */
    private final Dinosaur PARTNER;

    /**
     * Constructor
     * @param dinosaur the female dinosaur who is going to be pregnant.
     */
    public BreedAction(Dinosaur dinosaur, Dinosaur partner){
        this.DINOSAUR = dinosaur;
        this.PARTNER = partner;
    }

    /**
     * Execute method will increment the layEggCounter of the female dinosaur. This will make the female dinosaur pregnant.
     * @param actor The female dinosaur performing the action.
     * @param map The map the female dinosaur is on.
     * @return null
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        DINOSAUR.increaseLayEggCounter();
        DINOSAUR.addCapability(DinoCapabilities.Pregnant);
        return DINOSAUR +" was impregnated by "+ PARTNER;
    }

    /**
     * Returns a string to display in the menu
     * @param actor The female dinosaur performing the action.
     * @return a string with the description of the breeding process.
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Initiate Breed";
    }

}
