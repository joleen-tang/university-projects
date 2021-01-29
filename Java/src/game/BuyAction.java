package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

/**
 * Action object class that allows the user to buy an item from a vending machine
 * */
public class BuyAction extends Action {
    /** Item to be bought from the vending machine */
    private final Item VM_ITEM;
    /** The amount of ecoPoints required to buy the target item */
    private final int PRICE;

    /**
     * Constructor. If desired item is something that has a non-empty tick method, creates
     * a new instance of the item with the same parameters instead
     *
     * @param VM_Item the item to purchase from the vending machine
     * @param price the amount of ecoPoints required to purchase the item
     */
    public BuyAction(Item VM_Item, int price){
        if (VM_Item instanceof BasicObjectFactory){
            this.VM_ITEM=(Item) ((BasicObjectFactory) VM_Item).createNewCopy();
        }
        else{
            this.VM_ITEM=VM_Item;
        }
        this.PRICE=price;
    }

    /**
     * This method will carry out the purchasing process deducting the player's ecoPoint balance
     * and adding the desired item to the inventory
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a String with purchase status
     */
    @Override
    public String execute(Actor actor, GameMap map){
        Player.decreaseEcoPoints(this.PRICE);
        actor.addItemToInventory(VM_ITEM);
        return actor + " bought " + VM_ITEM + "for " + this.PRICE +" ecopoints.";
    }

    /**
     * String that describes what BuyAction did
     * @param actor The actor performing the action.
     * @return String with purchase details
     */
    @Override
    public String menuDescription(Actor actor){
        return "Buy "+this.VM_ITEM+" for "+this.PRICE+" ecopoints";
    }
}
