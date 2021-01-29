package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.DinoCapabilities;

import java.util.ArrayList;

/**
 * BreedBehaviour returns the action(s) required to breed when necessary requirements are met.
 */
public class BreedBehaviour extends SearchBehaviour {

    /**
     * Constructor
     */
    public BreedBehaviour() {
    }

    /**
     * This method will look for surrounding locations to see if the dinosaur can find a suitable partner to breed.
     * @param currentLocation Location where the dinosaur is currently located
     * @param dino The dinosaur looking to breed
     * @return an Action required for the dinosaur to breed. If a suitable partner is not found the dinosaur will DoNothing.
     */
    private Action checkSurroundings(Location currentLocation, Dinosaur dino) {
        GameMap map=currentLocation.map();
        for (int i = 1; i <= dino.SIGHT_RANGE; i++) {
            //Getting nearby locations within the sight of the dinosaur.
            ArrayList<Location> nearby = super.getSurroundings(i, currentLocation, map);
            for (Location location : nearby) {
                Dinosaur target = super.asDinosaur(location.getActor());
                //if target is dinosaur
                if (target!=null){
                    //if target is same species, opposite gender, and can breed
                    if (checkSuitableMate(dino, target)){
                        if (dino.GENDER.equals("F")){
                            //If dinosaur is female call getFemAction()
                            return getFemAction(i, dino, target, map);
                        }
                        else{
                            //If dinosaur is male, call getMaleAction()
                            return getMaleAction(i, dino, target, map);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method will return Action if the dinosaur is female and there is a suitable partner to breed in the map
     * @param distance between the dinosaur and the suitable partner
     * @param dino the dinosaur trying to breed
     * @param subject the suitable partner
     * @param map GameMap
     * @return an Action. If the female dinosaur is 1 square away from the male BreedAction. Else, FollowBehaviour until the partner is reached within 1 square.
     */
    private Action getFemAction(int distance, Dinosaur dino, Actor subject, GameMap map){
        if (distance==1){
            return new BreedAction(dino, (Dinosaur)subject);
        }
        else{
            FollowBehaviour temp=new FollowBehaviour(subject);
            return temp.getAction(dino, map);
        }
    }

    /**
     * This method will return Action if the dinosaur is male and there is a suitable partner to breed in the map
     * @param distance between the dinosaur and the suitable partner
     * @param dino the dinosaur trying to breed
     * @param subject the suitable partner
     * @param map GameMap
     * @return an Action. If the male dinosaur is 1 square away from the female DoNothingAction(). Else, FollowBehaviour until the partner is reached within 1 square.
     */
    private Action getMaleAction(int distance, Dinosaur dino, Actor subject, GameMap map){
        if (distance==1){
            return new DoNothingAction();
        }
        else{
            FollowBehaviour temp=new FollowBehaviour(subject);
            return temp.getAction(dino, map);
        }
    }

    /**
     * A method that will look for surroundings for a suitable breeding partner.
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an Action
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Dinosaur dino=super.asDinosaur(actor);
        if (checkBreedingState(dino)){
            return checkSurroundings(map.locationOf(dino), dino);
        }
        return null;
    }

    /**
     * Check if the given dinosaur is in a state where it can breed
     * @param dino dinosaur to check
     * @return a boolean value indicating if the dinosaur is a suitable partner or not.
     */
    private boolean checkBreedingState(Dinosaur dino){
        return dino.hasCapability(DinoCapabilities.WellFed) && dino.hasCapability(DinoCapabilities.BreedingAge)
                && !dino.hasCapability(DinoCapabilities.Pregnant);
    }

    /**
     * Checks if the target dinosaur can mate with the current dinosaur
     * */
    private boolean checkSuitableMate(Dinosaur dino, Dinosaur target){
        return checkBreedingState(target) && !dino.GENDER.equals(target.GENDER)
                && dino.SPECIES.equals(target.SPECIES);
    }
}