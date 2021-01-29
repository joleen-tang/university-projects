package game;

import edu.monash.fit2099.engine.*;

import java.util.ArrayList;

public class DrinkBehaviour extends SearchBehaviour{
    private Action action;

    /**
     *Constructor for DrinkBehaviour
     */
    public DrinkBehaviour(){}

    /**
     * getAction will return the required action(s) that are needed to be taken in order to complete DrinkBehaviour
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an Action which will ultimately make the dinosaur drink water if there is a water source in the map.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        ArrayList<Action> actions = new ArrayList<Action>();
        Location currentLocation=map.locationOf(actor);
        for(int i=0; i<currentLocation.getItems().size(); i++){
            if(currentLocation.getItems().get(i) instanceof WaterItem){
                action = new DrinkAction((Dinosaur) actor);
                return action;

            }
        }

        return checkSurroundings(map.locationOf(actor), (Dinosaur)actor);
    }

    /**
     * This method will look for surrounding locations for a water source so the dinosaur can drink.
     * If found, it will return  DrinkAction.
     * @param currentLocation where the dinosaur is positioned currently
     * @param dino the dinosaur
     * @return an action depending on water source found or not.
     */
    private Action checkSurroundings(Location currentLocation, Dinosaur dino) {
        GameMap map = currentLocation.map();
        int tempSight = dino.SIGHT_RANGE;
        for (int i = 0; i <= dino.SIGHT_RANGE; i++) {
            ArrayList<Location> nearby = super.getSurroundings(i, currentLocation, map);
            for (Location location : nearby) {
                if(location.getItems().size()==0){
                    continue;
                }
                for (int j = 0; j < location.getItems().size(); j++) {
                    if (location.getItems().get(j) instanceof WaterItem) {
                        Item item = location.getItems().get(j);
                        return getDinoAction(dino, location, i, item, map);

                    }
                }
            }
        }
        WanderBehaviour wander = new WanderBehaviour();
        return wander.getAction(dino, map);

    }

    /**
     * This method will determine if the dinosaur is right next to a water source or not. If not it will move towards a water source.
     * If the dinosaur is next to a water source, DrinkAction will be called so the dinosaur can drink.
     * @param dino the dinosaur who is thirsty
     * @param waterSource the location with water
     * @param distance distance in int from the dinosaur to the water source
     * @param item water item.
     * @param map the map the dinosaur is currently at
     * @return a DrinkAction or MovetoItemAction if there is no water source next to the dinosaur.
     */
    private Action getDinoAction(Dinosaur dino, Location waterSource, int distance, Item item, GameMap map){
        if (distance == 0 || distance == 1){
            return new DrinkAction(dino);
        }
        else{

            MovetoItemBehaviour move = new MovetoItemBehaviour(item, waterSource);
            return move.getAction(dino, map);
        }

    }
}
