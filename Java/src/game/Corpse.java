package game;

/**
 * FoodItem that will fill a meat-eater Dinosaur's belly
 * */
public class Corpse extends FoodItem{
    /**
     * Constructor for Corpse which is a FoodItem suitable for carnivores.
     */
    public Corpse(String species, int foodPoints){
        super(species+" corpse", '%', foodPoints, true);
        this.addCapability(FoodTypes.Carnivorous);
    }
}
