package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.Location;

import java.util.List;

/**
 * Dinosaur egg that hatches into a dinosaur after a set number of turns
 * */
public class Egg extends FoodItem implements BasicObjectFactory {
    /** The dinosaur that will hatch from this egg */
    private final Dinosaur UNBORN_BABY;
    /** The number of turns it takes for this egg to hatch */
    private final int HATCH_NUM;
    /** The number of turns it has been since this egg was constructed */
    private int hatchCounter=0;

    /**
     * Constructor.
     *  @param baby the Actor that will be placed on the map when egg hatches
     * */
    public Egg(Dinosaur baby){
        super(baby.SPECIES+" Egg", 'e', 10, true);
        this.UNBORN_BABY=baby;
        this.HATCH_NUM=baby.HATCH_NUM;
        this.addCapability(FoodTypes.Carnivorous);
    }

    /**
     * Inform a carried Egg of the passage of time.
     *
     * This method is called once per turn, if the Item is being carried.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        this.incrementHatchCounter();
        if (hatchCounter==HATCH_NUM){
            if (this.hatch(currentLocation)){
            actor.removeItemFromInventory(this);
            }
        }
    }

    /**
     * Inform an Egg on the ground of the passage of time.
     * This method is called once per turn, if the item rests upon the ground.
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        this.incrementHatchCounter();
        if (hatchCounter==HATCH_NUM) {
            if (this.hatch(currentLocation)) {
                currentLocation.removeItem(this);
            }
        }
    }

    /**
     * Brings an egg closer to hatching.
     *
     * This method is called every turn in the tick method
     */
    private void incrementHatchCounter(){
        hatchCounter+=1;
    }

    /**
     * Brings an egg one step further from hatching.
     *
     * This method is only called when the egg should have hatched but there was no available space
     */
    private void decrementHatchCounter(){
        hatchCounter-=1;
    }

    /**
     * Implements egg hatching
     *
     * This method is called when the hatchCounter reaches HATCH_NUM
     * @param currentLocation the Location the egg is in
     */
    private Boolean hatch(Location currentLocation){
        //if baby cannot be born on current location
        if (!currentLocation.canActorEnter(this.UNBORN_BABY)){
            List<Exit> exits=currentLocation.getExits();
            for (Exit exit:exits){
                Location temp=exit.getDestination();
                //if baby can be born on temp
                if (temp.canActorEnter(this.UNBORN_BABY)){
                    addBaby(temp);
                    return true;
                }
            }
            //if no available spaces, decrement counter and try again next turn
            this.decrementHatchCounter();
            return false;
        }
        else{
            addBaby(currentLocation);
            return true;
        }
    }

    /**
     * Make a new Egg
     * @return a new Egg
     */
    @Override
    public Egg createNewCopy(){
        return new Egg(this.UNBORN_BABY.createNewCopy());
    }

    /**
     * Adding a baby of the dinosaur type to the breeding location
     * @param birthLocation location where the baby will be
     */
    private void addBaby(Location birthLocation){
        birthLocation.addActor(UNBORN_BABY);
        System.out.println(UNBORN_BABY+" born at ("+birthLocation.x()+", "+birthLocation.y()
        +")");
        Player.increaseEcoPoints(UNBORN_BABY.HATCH_ECO_POINTS);
    }
}
