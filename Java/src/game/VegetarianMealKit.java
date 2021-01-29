package game;

/**
 * A food item that can be fed to herbivores
 * */
public class VegetarianMealKit extends FoodItem {
    /**
     * Constructor for VegetarianMealKit which is a FoodItem suitable for Stegosaur consumption
     */
    public VegetarianMealKit(){
        super("Herbivore Meal Kit", 'h', 10000, true);
        this.addCapability(FoodTypes.Herbivorous);
    }
}
