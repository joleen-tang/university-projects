package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.Dinosaur;

public class Allosaur extends Dinosaur {
    /** The limit of how full this dinosaur can be*/
    private static final int MAX_FOOD_LEVEL=100;                       //This integer value specifies the maximum foodLevel value a stegosaur can accumulate. After this number, cannot increment foodLevel
    /** The limit of how much water this dinosaur can drink*/
    private static final int MAX_THIRST_LEVEL=100;
    /** The radius around it that the dinosaur can see*/
    private static final int sightRange=20;
    /** The default starting foodLevel for an adult Allosaur */
    private static final int DEF_ADULT_FOOD_LVL=50;
    /** The default starting foodLevel for a baby Allosaur */
    private static final int DEF_BABY_FOOD_LVL=10;
    /** The age at which an Allosaur is considered an adult */
    private static final int ADULT_AGE=30;
    /** The number of turns it takes an Allosaur egg to hatch */
    private static final int HATCH_NUM=5;                             //This integer value specifies the time taken for a stegosaur egg to be hatched in terms of number of rounds.
    /** The number of ecoPoints the player earns when an Allosaur egg hatches */
    private static final int HATCH_ECO_POINTS=1000;
    /** The maximum number of hitpoints an Allosaur can have */
    private static final int MAX_HP=100;
    /** The foodLevel increase a carnivore experiences when eating an Allosaur corpse */
    private static final int CORPSE_FOOD_POINTS=80;
    /** The character used to display an adult Allosaur */
    private static final char ADULT_DISPLAY_CHAR='A';
    /** The character used to display a baby Allosaur */
    private static final char BABY_DISPLAY_CHAR='a';
    /** A string denoting the species of Allosaurs */
    private static final String SPECIES="Allosaur";

    /**
     * constructor
     */
    public Allosaur(){
        super(ADULT_DISPLAY_CHAR, MAX_HP, sightRange, MAX_FOOD_LEVEL, MAX_THIRST_LEVEL, SPECIES,
                DEF_ADULT_FOOD_LVL, ADULT_AGE, ADULT_DISPLAY_CHAR, HATCH_NUM, HATCH_ECO_POINTS, CORPSE_FOOD_POINTS);
        this.addCapability(DinoCapabilities.Carnivorous);
        this.addCapability(DinoCapabilities.HuntSome);
    }
    /**
     * baby constructor
     */
    public Allosaur(boolean baby){
        super(BABY_DISPLAY_CHAR, MAX_HP, sightRange, MAX_FOOD_LEVEL, MAX_THIRST_LEVEL, SPECIES,
                DEF_BABY_FOOD_LVL, 0, ADULT_DISPLAY_CHAR, HATCH_NUM, HATCH_ECO_POINTS, CORPSE_FOOD_POINTS);
        this.addCapability(DinoCapabilities.Carnivorous);
        this.addCapability(DinoCapabilities.HuntSome);
    }

    /**
     * Action needed when allosaur is hungry
     * @param map GameMap
     * @return an Action for the hungry allosaur to perform
     */
    @Override
    protected Action getFoodAction(GameMap map){
        Action foodAct=super.getFoodAction(map);
        if (foodAct!=null){
            return foodAct;
        }
        else{
            return Dinosaur.BEHAVIOURS.get("hunt").getAction(this, map);
        }
    }

    /**
     * Clone an allosaur
     * @return a baby allosaur
     */
    public Allosaur createNewCopy(){
        return new Allosaur(true);
    }
}
