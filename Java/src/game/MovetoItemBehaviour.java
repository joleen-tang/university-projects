package game;

import edu.monash.fit2099.engine.*;

public class MovetoItemBehaviour implements Behaviour {
    private final Item item;
    private Action action;
    private final Location source;

    /**
     * Constructor for MovetoItemBehaviour
     * @param item
     * @param source
     */
    public MovetoItemBehaviour(Item item, Location source) {
        this.item = item;
        this.source = source;

    }

    /**
     * What the game.dinosaurs next action should be in order to get to a location with the given item
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return an Action to get to the required item
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {


        for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                for (int i=0; i<destination.getItems().size(); i++){
                    if(destination.getItems().get(i) == item){
                        action=(new MoveActorAction(destination, "Towards food"));
                        return action;
                    }
                }

            }
        }
        Location here = map.locationOf(actor);
        Location there = this.source;
        int currentDistance = distance(here, there);
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = distance(destination, there);
                if (newDistance < currentDistance) {
                    return new MoveActorAction(destination, exit.getName());
                }
            }
        }
        return null;




    }
    /**
     * Compute the Manhattan distance between two locations.
     *
     * @param a the first location
     * @param b the first location
     * @return the number of steps between a and b if you only move in the four cardinal directions.
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

}
