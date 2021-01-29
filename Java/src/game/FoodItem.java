package game;

import edu.monash.fit2099.engine.Item;

/**
 * Item that can be eaten by dinosaurs
 */
public class FoodItem extends Item {
    /** Number of points a dinosaur's foodLevel will increase by when eating this item */
    private final int FOOD_POINTS;

    /**
     * Constructor.
     * @param foodPoints the amount of points a dinosaur's food level will increase by when it eats the food item
     */
    public FoodItem(String name, char displayChar, int foodPoints, boolean portable){
        super(name, displayChar, portable);
        this.FOOD_POINTS=foodPoints;
    }

    /**
     * Getter method for FOOD_POINTS attribute
     * */
    protected int getFoodPoints(){
        int temp=this.FOOD_POINTS;
        return temp;
    }
}
