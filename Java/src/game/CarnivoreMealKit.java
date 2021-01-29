package game;

/**
 * FoodItem that will fill a meat-eater Dinosaur's belly
 * */
public class CarnivoreMealKit extends FoodItem {
    /**
     * Constructor
     */
    public CarnivoreMealKit(){
        super("Carnivore Meal Kit", 'c', 10000, true);
        this.addCapability(FoodTypes.Carnivorous);
    }
}
