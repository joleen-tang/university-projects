package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.DinoCapabilities;

import java.util.ArrayList;

/**
 * This class implements EatBehaviour
 * EatBehaviour will make the dinosaur eat a food item at the current location or look for a location with food and eat it at the new location.
 */
public class EatBehaviour extends SearchBehaviour {
    private Action action;

    /**
     * Constructor
     */
    public EatBehaviour(){
    }


    /**
     * getAction will return the necessary action(s) the actor should take in order to complete the behaviour.
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an action(s) that makes the dinosaur eat something edible
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Location currentLocation=map.locationOf(actor);
        //Checking the dinosaur type. Useful to check what is edible.
        Dinosaur dino = super.asDinosaur(actor);
        //iterate through items in the current location.
        for(int i=0; i<currentLocation.getItems().size(); i++){
            if(currentLocation.getItems().get(i) instanceof FoodItem){
                Item item = currentLocation.getItems().get(i);
                FoodItem food = asFood(currentLocation.getItems().get(i));
                action = new EatAction(food, currentLocation, (Dinosaur) actor);
                if(dino.hasCapability(DinoCapabilities.Herbivorous)) {
                    if (food.hasCapability(FoodTypes.Herbivorous)) {
                        return action;
                    }
                }
                if(dino.hasCapability(DinoCapabilities.Carnivorous)){
                    if (food.hasCapability(FoodTypes.Carnivorous)){
                        return action;
                    }
                }
                if(dino.hasCapability(DinoCapabilities.Omnivorous)){
                    if (food.hasCapability(FoodTypes.Carnivorous)|| food.hasCapability(FoodTypes.Herbivorous)){
                        return action;
                    }
                }
            }
        }
        //If there is no foodItems nearby, then the herbivore should look for other locations.
        //We will use getSurroundings to achieve this
        return checkSurroundingforFood(map.locationOf(actor), (Dinosaur)actor);
    }


    /**
     * This method will change an Item into an instance of FoodItem.
     * @param itemtoFood The Item to be convereted to an instance of FoodItem
     * @return a FoodItem. Useful to check value of foodPoints etc.
     */
    private FoodItem asFood(Item itemtoFood) {
        return itemtoFood instanceof Item ? (FoodItem) itemtoFood : null;
    }

    /**
     * This method will check if the dinosaur is already in a location with food. If not, it will move towards food.
     * @param dino the dinosaur looking for food.
     * @param foodSource Where food is located
     * @param food the Food item
     * @param distance the int distance from the dinosaur to the food item. 0 if the dinosaur is already in the location.
     * @param item The item
     * @param map the current map the dinosaur is located
     * @return an EatAction or MovetoItemBehaviour if no edible food at the current location.
     */
    private Action getDinoAction(Dinosaur dino, Location foodSource, FoodItem food, int distance, Item item, GameMap map){
        if (distance ==0){
            return new EatAction(food, foodSource, dino);
        }
        else{
            MovetoItemBehaviour move = new MovetoItemBehaviour(item, foodSource);
            return move.getAction(dino, map);
        }

    }
    /**
     * Thismethod will look for surrounding locations for edible food for Dinosaurs.
     * If found, it will return an Eat action.
     * @param currentLocation where the dinosaur is positioned currently
     * @param dino the dinosaur
     * @return an action depending on edible food found or not.
     */
    private Action checkSurroundingforFood(Location currentLocation, Dinosaur dino) {
        GameMap map = currentLocation.map();
        for (int i = 0; i <= dino.SIGHT_RANGE; i++) {
            ArrayList<Location> nearby = super.getSurroundings(i, currentLocation, map);
            for (Location location : nearby) {
                if(location.getItems().size()==0){
                    continue;
                }
                for (int j = 0; j < location.getItems().size(); j++) {
                    if (location.getItems().get(j) instanceof FoodItem) {
                        Item item = location.getItems().get(j);
                        FoodItem food = asFood(item);
                        action =  getDinoAction(dino, location, food, i, item, map);
                        if (dino.hasCapability(DinoCapabilities.Herbivorous)) {
                            if (item.hasCapability(FoodTypes.Herbivorous)) {
                                return action;
                            }
                        }
                        if (dino.hasCapability(DinoCapabilities.Carnivorous)) {
                            if (item.hasCapability(FoodTypes.Carnivorous)) {
                                return action;
                            }
                        }
                        if (dino.hasCapability(DinoCapabilities.Omnivorous)) {
                            if (item.hasCapability(FoodTypes.Herbivorous)||item.hasCapability(FoodTypes.Carnivorous)) {
                                return action;
                            }
                        }
                    }
                }

            }
        }
        //No food can be found. Return null
        return null;
    }

}
