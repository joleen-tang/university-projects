package game;

import edu.monash.fit2099.engine.Location;

/**
 * Fruit item
 * */
public class Fruit extends FoodItem implements BasicObjectFactory {
    /** An integer variable that keeps track of when the Fruit will be rotten */
    private int rotCounter = 0;
    /** The maximum number of turns the Fruit can survive without being rot */
    private static final int ROT_MAX = 20;

    /**
     * Constructor of Fruit which is a portable item.
     */
    public Fruit() {
        super("Fruit", 'F', 20, true);
        this.addCapability(FoodTypes.Herbivorous);
    }

    /**
     *This method lets Fruit experience Time.
     * Also checks if the Fruit is rotten. If it is, then remove the Fruit from the Location's items list.
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation){
        incrementRotCount();
        if (rotCounter == ROT_MAX){
            currentLocation.removeItem(this);
        }

    }

    /**
     * This method will increase the rot counter
     */
    private void incrementRotCount(){
        rotCounter += 1;
    }

    /**
     * Calls fruit constructor
     * */
    @Override
    public Fruit createNewCopy(){
        return new Fruit();
    }
}
