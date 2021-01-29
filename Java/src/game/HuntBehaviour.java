package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.DinoCapabilities;

import java.util.ArrayList;

/**
 * Behaviour used by meat-eating dinosaurs large enough to hunt
 * */
public class HuntBehaviour extends SearchBehaviour {
    /**
     * Constructor for HuntBehaviour
     */
    public HuntBehaviour() {
    }

    /**
     * This method will look for prey that are within the sight of the hunter.
     * @param currentLocation of the hunter dinosaur
     * @param dino the hunter
     * @return an action to get to a surrounding prey or null will be returned.
     */
    private Action checkSurroundings(Location currentLocation, Dinosaur dino){
        GameMap map=currentLocation.map();
        for (int i = 1; i <= dino.SIGHT_RANGE; i++) {
            ArrayList<Location> nearby = super.getSurroundings(i, currentLocation, map);
            for (Location location : nearby) {
                Dinosaur target = super.asDinosaur(location.getActor());
                if (dino.hasCapability(DinoCapabilities.HuntSome)) {
                    if (target != null) {
                        if (target.hasCapability(DinoCapabilities.Herbivorous)) {
                            return getHuntAction(i, dino, target, map);
                        }
                    }
                }
                //Dinosaurs such as Archaeopteryx can hunt down any dinosaur in the game apart from their own species.
                if (dino.hasCapability(DinoCapabilities.HuntAll)){
                    if(target != null){
                        if (!(target.SPECIES.equals(dino.SPECIES))){
                            return getHuntAction(i, dino, target, map);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an action to be performed by the dinosaur
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Dinosaur dino = super.asDinosaur(actor);
        return checkSurroundings(map.locationOf(actor), dino);
    }

    /**
     * What the Hunter should perform in order to attack or move towards the prey.
     * @param distance to the prey
     * @param dino the hunter dinosaur
     * @param target the prey the hunter is after
     * @param map GameMap
     * @return an Action that will make the hunter attack the prey or move towards it
     */
    private Action getHuntAction(int distance, Dinosaur dino, Actor target, GameMap map) {
        if (distance == 1) {
            return new AttackAction(target);
        } else {
            FollowBehaviour follow = new FollowBehaviour(target);
            return follow.getAction(dino, map);
        }
    }
}






