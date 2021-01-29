package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.DinoCapabilities;

import java.util.TreeMap;

/**
 * Superclass for all dinosaurs
 * */
public abstract class Dinosaur extends Actor implements BasicObjectFactory {
    /** Static treemap storing all Behaviour objects used by all dinosaurs */
    protected static final TreeMap<String, Behaviour> BEHAVIOURS = initialiseBehaviours();
    /** Integer measuring how hungry the dinosaur is */
    private int foodLevel;
    /** Integer measuring how thirsty the dinosaur is */
    private int thirstLevel=20;
    /** Integer measuring how pregnant a dinosaur is */
    private int layEggCounter=0;
    /** Integer measuring how long a dinosaur has been unconscious from hunger */
    private int tooHungryCounter = 0;
    /** The foodLevel at which a dinosaur is capable of breeding */
    private static final int BREED_THRESHOLD = 30;
    /** The age at which a dinosaur is old enough to breed*/
    public static final int BREED_AGE=10;
    /** Integer measuring how old this dinosaur is */
    private int age;
    /** The radius around it that the dinosaur can see*/
    public final int SIGHT_RANGE;
    /** The limit of how full this dinosaur can be*/
    public final int MAX_FOOD_LEVEL;
    /** The limit of how much water this dinosaur can drink*/
    public final int MAX_THIRST_LEVEL;
    /** String describing what species of dinosaur this is*/
    public final String SPECIES;
    /** How many turns an egg this dinosaur lays takes to hatch*/
    public final int HATCH_NUM;
    /** How many eco points this dinosaur hatches from an egg, differs by SPECIES*/
    public final int HATCH_ECO_POINTS;
    /** String describe what gender this dinosaur is*/
    public final String GENDER;
    /** The display character this dinosaur will use as an adult */
    private final char ADULT_DISPLAY_CHAR;
    /** Integer measuring how many turns a dinosaur can stay unconscious before dying */
    private static final int TOO_HUNGRY=10;
    /** Integer measuring how many turns it takes for a dinosaur to give birth after getting pregnant */
    private static final int GIVE_BIRTH=10;
    /** Integer measuring how many hunger points this dinosaur will give a carnivore as a corpse */
    private final int CORPSE_POINTS;

    /**
     * Constructor for Dinosaur class; decides gender
     *  @param displayChar How the Dinosaur will be displayed on the map
     * @param hitPoints How much damage the Dinosaur can withstand
     * @param sightRange The range of what the Dinosaur can see
     * @param maxFoodLevel The amount of Food the Dinosaur can eat
     * @param species The type of the Dinosaur
     * @param currentFoodLevel How much Food is already in the Dinosaur
     * @param age Age of the Dinosaur
     * @param adult_display_char the display character of this dinosaur as an adult
     */
    public Dinosaur(char displayChar, int hitPoints, int sightRange, int maxFoodLevel,
                    int maxThirstLevel, String species, int currentFoodLevel, int age, char adult_display_char,
                    int hatchNum, int hatchEcoPoints, int corpseFoodPoints){
        super(species, displayChar, hitPoints);
        this.SIGHT_RANGE =sightRange;
        this.MAX_FOOD_LEVEL=maxFoodLevel;
        this.MAX_THIRST_LEVEL=maxThirstLevel;
        this.SPECIES=species;
        this.foodLevel=currentFoodLevel;
        this.age=age;
        this.ADULT_DISPLAY_CHAR=adult_display_char;
        this.HATCH_NUM=hatchNum;
        this.HATCH_ECO_POINTS=hatchEcoPoints;
        this.CORPSE_POINTS=corpseFoodPoints;
        if (this.age>BREED_AGE){
            //if dinosaur is an adult, mark as partly breedable
            this.addCapability(DinoCapabilities.BreedingAge);
        }
        //Determine gender
        if (Math.random() < 0.5) {
            this.GENDER="F";
        } else {
            this.GENDER="M";
        }
    }

    /**
     * This public method will allow other classes to increase the foodLevel of the Dinosaur
     * Ensures that foodLevel never goes over the maximum
     * @param foodPoints foodPoints of a given FoodItem
     */
    protected void increaseFoodLevel(int foodPoints){
        if (this.foodLevel+foodPoints>MAX_FOOD_LEVEL){
            this.foodLevel=MAX_FOOD_LEVEL;
        }
        else{
            this.foodLevel+=foodPoints;
        }
        if (this.foodLevel>BREED_THRESHOLD){
            this.addCapability(DinoCapabilities.WellFed);
        }
    }

    /**
     * Increases the thirstLevel of this dinosaur
     * */
    protected void increaseThirstLevel(int thirstPoints){
        if (this.thirstLevel+thirstPoints>MAX_THIRST_LEVEL){
            this.thirstLevel=MAX_THIRST_LEVEL;
        }
        else{
            this.thirstLevel+=thirstPoints;
        }
    }

    /**
     * Gets actions that other actions can perform on this dinosaur
     * @param otherActor the Actor that might be performing the allowable actions
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return a valid Action the other actor can perform on this dinosaur
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions temp=new Actions(new AttackAction(this));
        for (Item item: otherActor.getInventory()){
            if (this.SPECIES.equals("Allosaur") || this.SPECIES.equals("Archaeopteryx") || this.SPECIES.equals("Agilisaurus")){
                if (item.hasCapability(FoodTypes.Carnivorous)){
                    temp.add(new FeedAction((FoodItem)item, this));
                }
            }
            if (this.SPECIES.equals("Stegosaur") || this.SPECIES.equals("Agilisaurus")){
                if (item.hasCapability(FoodTypes.Herbivorous)){
                    temp.add(new FeedAction((FoodItem) item, this));
                }
            }
        }
        return temp;
    }

    /**
     * Method where dinosaur decides what Action() to execute this turn
     * First checks for things that are happening to it (see getPassiveAct), then tries
     * breeding, looking for food, and finally wandering in a random direction
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return new Action() this dinosaur will execute this turn
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display){
        this.name =this.displayLocation(map);
        Action passiveAction= getPassiveAction(map);
        if (passiveAction!=null){
            return passiveAction;
        }

        Action breedAct=BEHAVIOURS.get("breed").getAction(this, map);
        if (breedAct!=null){
            return breedAct;
        }

        Action huntAction = BEHAVIOURS.get("hunt").getAction(this, map);
        if (huntAction != null){
            return huntAction;
        }

        //Check if thirsty
        if (this.thirstLevel< 0.2*this.MAX_THIRST_LEVEL){
            Action waterAct = getWaterAction(map);
            if (waterAct != null){
                return waterAct;
            }
        }

        Action foodAct=getFoodAction(map);
        if (foodAct!=null){
            return foodAct;
        }

        return getAnAction(map);
    }

    /**
     * Ticks dinosaur and checks if that has affected any of its parameters such that
     * it is now forced into a certain action
     * @param map map where dinosaur is
     * @return a new Action() if dinosaur is dead, giving birth, or unconscious
     */
    private Action getPassiveAction(GameMap map){
        tick();
        //check if dead
        Action temp=this.checkDeath(map);
        if (temp!= null){
            return temp;
        }
        //check if giving birth
        if (checkPregnancy()){
            return layEgg();
        }

        //check if conscious
        if (!isConscious()){
            this.incrementTooHungryCounter();
            System.out.println(this +" is unconscious; please feed immediately");
            return new DoNothingAction();
        }
        return null;
    }

    /**
     * Returns an Action that accomplishes nothing
     * Used when dinosaur has nothing better to do
     * @param map map where dinosaur is
     * @return either MoveActorAction() in a random direction or DoNothingAction()
     */
    private Action getAnAction(GameMap map){
        //for dinos with nothing better to do
        Action wanderAct=BEHAVIOURS.get("wander").getAction(this, map);
        if (wanderAct!=null){
            return wanderAct;
        }
        System.out.println(this +" has nothing better to do");
        return new DoNothingAction();
    }

    /**
     * Default dinosaur function to get food-related actions (eat or move closer to food)
     * Used by stegosaurs; allosaurs override
     * @param map map where dinosaur is
     * @return either EatAction or MovetoItemAction or null if neither is available
     */
    protected Action getFoodAction(GameMap map){
        return BEHAVIOURS.get("eat").getAction(this, map);
    }
    /**
     * Function to find water. Common for all dinosaur types.
     * @param map the map the dinosaur is currently positioned in
     * @return either DrinkAction or MOvetoItemAction or null if there are locations with water.
     */
    protected Action getWaterAction(GameMap map){
        return BEHAVIOURS.get("drink").getAction(this, map);
    }

    /**
     * This method will reset EggCounter back to 0; used when dinosaur gives birth
     */
    protected void resetEggCounter(){
        this.layEggCounter=0;
        this.removeCapability(DinoCapabilities.Pregnant);
    }

    /**
     * Decrease foodLevel at start of every dinosaur turn. If not well-fed enough,
     * dinosaur cannot breed
     *
     * If foodLevel already at 0, cannot decrease further
     */
    private void decrementFoodLevel(){
        if (this.foodLevel>0) {
            this.foodLevel-=1;
            if (this.foodLevel<BREED_THRESHOLD){
                this.removeCapability(DinoCapabilities.WellFed);
            }
        }
    }

    /**
     * If dinosaur is unconscious from hunger, tick the value to keep track of how many turns
     * dinosaur has been unconscious
     */
    private void incrementTooHungryCounter(){
        if(this.tooHungryCounter>0) {
            this.tooHungryCounter += 1;
        }
    }

    /**
     * Checks if dinosaur is ready to give birth
     * @return true if it is ready to give birth
     */
    private boolean checkPregnancy(){
        return this.layEggCounter == GIVE_BIRTH;
    }

    /**
     * If layEggCounter is not 0, dinosaur must be pregnant; if so, this method advances
     * pregnancy
     */
    private void incrementLayEggCounter(){
        if (this.layEggCounter>0){
            this.increaseLayEggCounter();
        }
    }

    /**
     * Advances dinosaur age; if it hits BREED_AGE, it has become an adult and
     * will be capable of breeding, and have it's display character changed
     */
    private void incrementAge(){
        this.age+=1;
        if (this.age==BREED_AGE){
            this.addCapability(DinoCapabilities.BreedingAge);
            this.displayChar=ADULT_DISPLAY_CHAR;
        }
    }

    /**
     * Implements dying
     * @param map map where dinosaur is
     */
    protected void dies(GameMap map){
        map.locationOf(this).addItem(new Corpse(this.SPECIES, this.CORPSE_POINTS));
        Actions dropActions = new Actions();
        for (Item item : this.getInventory())
            dropActions.add(item.getDropAction());
        for (Action drop : dropActions)
            drop.execute(this, map);
        map.removeActor(this);
    }

    /**
     * Creates a new copy of this dinosaur
     * */
    @Override
    public abstract Dinosaur createNewCopy();

    /**
     * Advances pregnancy; used when first impregnated
     */
    protected void increaseLayEggCounter(){
        this.layEggCounter+=1;
    }

    /**
     * Initialises Treemap that stores behaviours that all game.dinosaurs have
     * (Follow is not here because it requires a target)
     * @return a TreeMap with static behaviours used by all game.dinosaurs
     */
    private static TreeMap<String, Behaviour> initialiseBehaviours(){
        TreeMap<String, Behaviour> temp= new TreeMap<>();
        temp.put("wander", new WanderBehaviour());
        temp.put("eat", new EatBehaviour());
        temp.put("breed", new BreedBehaviour());
        temp.put("hunt", new HuntBehaviour());
        temp.put("drink", new DrinkBehaviour());
        return temp;
    }

    /**
     * Allows game.dinosaurs to experience time; makes them hungry, advances age and pregnancy,
     * brings them closer to death
     */
    private void tick(){
        this.decrementFoodLevel();
        this.decrementThirstLevel();
        this.incrementAge();
        this.incrementTooHungryCounter();
        this.incrementLayEggCounter();
    }

    /**
     * Check if dinosaur is starved; if so, dinosaur dies and therefore does nothing
     * @param map map where dinosaur is
     * @return DieAction() or null if it should not be dead
     */
    private Action checkDeath(GameMap map){
        if (this.tooHungryCounter==TOO_HUNGRY || this.thirstLevel==0){
            this.dies(map);
            return new DieAction();
        }
        return null;
    }

    /**
     * Checks if dinosaur has fainted from hunger
     * @return true if dinosaur has not fainted from hunger
     */
    @Override
    public boolean isConscious(){
        return this.foodLevel>0;
    }

    /**
     * Creates new LayEggAction with species-specific eggs
     * @return LayEggAction for this dinosaur to give birth
     */
    private Action layEgg() {
        return new LayEggAction(this);
    }

    /**
     * Getter method for dinosaur's current HP
     * @return integer of current HP
     */
    protected int getHP(){
        int temp=this.hitPoints;
        return temp;
    }

    /**
     * Creates string for this dinosaur including its location
     * @param map map where dinosaur is
     * @return String combining dinosaur name and location
     */
    private String displayLocation(GameMap map){
        Location location=map.locationOf(this);
        return this.SPECIES+" at ("+location.x()+", "+location.y()+")";
    }

    /**
     * Method to decrement a dinosaur's thirstLevel; used in tick method
     * */
    private void decrementThirstLevel(){
        this.thirstLevel-=1;
    }
}
