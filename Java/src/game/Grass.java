package game;

/**
 * Item that can be eaten by herbivores
 * */
public class Grass extends FoodItem {
    /***
     * Constructor for Grass
     */
    public Grass() {
        super("Grass", ',', 5, false);
        this.addCapability(FoodTypes.Herbivorous);
    }
}
