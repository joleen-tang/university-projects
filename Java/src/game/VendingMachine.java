package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.Agilisaurus;
import game.dinosaurs.Allosaur;
import game.dinosaurs.Archaeopteryx;
import game.dinosaurs.Stegosaur;

import java.util.HashMap;
import java.util.Map;

/**
 * Vending machine from which player can buy various items
 * */
public class VendingMachine extends Ground {
    /**
     * Hashmap containing all items that can be bought from this vending machine and their prices as keys
     * */
    private final HashMap<Item, Integer> ITEM_PRICES = initialiseItemPrices();

    /**
     * Constructor
     *
     */
    public VendingMachine(){
        super('V');
    }

    /**
     * Initialises HashMap of items that can be brought from the vending machine
     *
     * @return temp HashMap with items that can be bought from vending machines and their prices
     */
    private static HashMap<Item, Integer> initialiseItemPrices(){
        HashMap<Item, Integer> temp = new HashMap<>();
        temp.put(new CarnivoreMealKit(), 500);
        temp.put(new VegetarianMealKit(), 100);
        temp.put(new Hay(), 20);
        temp.put(new Egg(new Stegosaur(true)), 200);
        temp.put(new Egg(new Allosaur(true)), 1000);
        temp.put(new Egg(new Archaeopteryx(true)), 500);
        temp.put(new Egg(new Agilisaurus(true)), 500);
        temp.put(new Fruit(), 30);
        temp.put(new LaserGun(), 500);
        return temp;
    }

    /**
     * Gets allowable actions
     *
     * Allows actor to buy items that have a price less than or equal to their current ecoPoints balance
     */
    @Override
    public Actions allowableActions(Actor actor, Location location, String direction){
        Actions temp=new Actions();
        for (Map.Entry<Item, Integer> buyable: ITEM_PRICES.entrySet()){
            int price= buyable.getValue();
            if (price<=Player.getEcoPoints()){
            temp.add(new BuyAction(buyable.getKey(), buyable.getValue()));
            }
        }
        return temp;
    }

    /**
     * Overridden to make vending machine impassable terrain
     */
    @Override
    public boolean canActorEnter(Actor actor){
        return false;
    }
}
