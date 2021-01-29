package game.dinosaurs;

import game.Dinosaur;

public class Agilisaurus extends Dinosaur {
    /** The limit of how full this dinosaur can be*/
    private static final int MAX_FOOD_LEVEL=100;                       //This integer value specifies the maximum foodLevel value a stegosaur can accumulate. After this number, cannot increment foodLevel
    /** The limit of how much water this dinosaur can drink*/
    private static final int MAX_THIRST_LEVEL=100;
    /** The radius around it that the dinosaur can see*/
    private static final int sightRange=16;
    /** The default starting foodLevel for an adult Agilisaurus */
    private static final int DEF_ADULT_FOOD_LVL=50;
    /** The default starting foodLevel for a baby Agilisaurus */
    private static final int DEF_BABY_FOOD_LVL=10;
    /** The age at which an Agilisaurus is considered an adult */
    private static final int ADULT_AGE=30;
    /** The number of turns it takes an Agilisaurus egg to hatch */
    private static final int HATCH_NUM=6;                             //This integer value specifies the time taken for a stegosaur egg to be hatched in terms of number of rounds.
    /** The number of ecoPoints the player earns when an Agilisaurus egg hatches */
    private static final int HATCH_ECO_POINTS=500;
    /** The maximum number of hitpoints an Agilisaur can have */
    private static final int MAX_HP=100;
    /** The foodLevel increase a carnivore experiences when eating an Agilisaurus corpse */
    private static final int CORPSE_FOOD_POINTS=50;
    /** The character used to display an adult Agilisaurus */
    private static final char ADULT_DISPLAY_CHAR='G';
    /** The character used to display a baby Agilisaurus */
    private static final char BABY_DISPLAY_CHAR='g';
    /** A string denoting the species of Agilisaurus */
    private static final String SPECIES="Agilisaurus";

    /**
     * Constructor
     * */
    public Agilisaurus(){
        super(ADULT_DISPLAY_CHAR, MAX_HP, sightRange, MAX_FOOD_LEVEL, MAX_THIRST_LEVEL ,SPECIES, DEF_ADULT_FOOD_LVL, ADULT_AGE,
                ADULT_DISPLAY_CHAR, HATCH_NUM, HATCH_ECO_POINTS, CORPSE_FOOD_POINTS);
        this.addCapability(DinoCapabilities.Omnivorous);
    }

    /**
     * Baby constructor
     * */
    public Agilisaurus(boolean baby){
        super(BABY_DISPLAY_CHAR, MAX_HP, sightRange, MAX_FOOD_LEVEL, MAX_THIRST_LEVEL ,SPECIES, DEF_BABY_FOOD_LVL, 0,
                ADULT_DISPLAY_CHAR, HATCH_NUM, HATCH_ECO_POINTS, CORPSE_FOOD_POINTS);
        this.addCapability(DinoCapabilities.Omnivorous);
    }

    /**
     * MAke a new agilisaurus
     * @return a baby agilisaurus
     */
    @Override
    public Agilisaurus createNewCopy(){
        return new Agilisaurus(true);
    }
}
