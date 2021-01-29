package game;

/**
 * Object that player can harvest from grass and feed to herbivores
 * */
public class Hay extends FoodItem {
    /**
     * Constructor for Hay
     */
    public Hay(){
        super("Hay", 'h', 20, true);
        this.addCapability(FoodTypes.Herbivorous);
    }
}
