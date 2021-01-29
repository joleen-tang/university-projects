package game.dinosaurs;

import game.Dinosaur;

/**
 * A herbivorous dinosaur.
 *
 */
public class Stegosaur extends Dinosaur {
	/** The limit of how full this dinosaur can be*/
	private static final int MAX_FOOD_LEVEL=100;                       //This integer value specifies the maximum foodLevel value a stegosaur can accumulate. After this number, cannot increment foodLevel
	/** The limit of how much water this dinosaur can drink*/
	private static final int MAX_THIRST_LEVEL=100;
	/** The radius around it that the dinosaur can see*/
	private static final int sightRange=10;
	/** The default starting foodLevel for an adult Stegosaur */
	private static final int DEF_ADULT_FOOD_LVL=50;
	/** The default starting foodLevel for a baby Stegosaur */
	private static final int DEF_BABY_FOOD_LVL=10;
	/** The age at which an Stegosaur is considered an adult */
	private static final int ADULT_AGE=30;
	/** The number of turns it takes an Stegosaur egg to hatch */
	private static final int HATCH_NUM=5;                             //This integer value specifies the time taken for a stegosaur egg to be hatched in terms of number of rounds.
	/** The number of ecoPoints the player earns when an Stegosaur egg hatches */
	private static final int HATCH_ECO_POINTS=100;
	/** The maximum number of hitpoints a Stegosaur can have */
	private static final int MAX_HP=100;
	/** The foodLevel increase a carnivore experiences when eating a Stegosaur corpse */
	private static final int CORPSE_FOOD_POINTS=80;
	/** The character used to display an adult Stegosaur */
	private static final char ADULT_DISPLAY_CHAR='S';
	/** The character used to display a baby Stegosaur */
	private static final char BABY_DISPLAY_CHAR='s';
	/** A string denoting the species of Stegosaurs */
	private static final String SPECIES="Stegosaur";

	/** 
	 * Constructor; creates an adult stegosaur
	 * Adult Stegosaurs are represented by ADULT_DISPLAY_CHAR and have MAX_HP hitpoints.
	 *
	 */
	public Stegosaur() {
		super(ADULT_DISPLAY_CHAR, MAX_HP, sightRange, MAX_FOOD_LEVEL, MAX_THIRST_LEVEL ,SPECIES, DEF_ADULT_FOOD_LVL,
				ADULT_AGE, ADULT_DISPLAY_CHAR, HATCH_NUM, HATCH_ECO_POINTS, CORPSE_FOOD_POINTS);
		this.addCapability(DinoCapabilities.Herbivorous);
	}

	/**
	 * Constructor; creates a baby stegosaur
	 * Adult Stegosaurs are represented by BABY_DISPLAY_CHAR and have MAX_HP hitpoints.
	 *
	 */
	public Stegosaur(boolean baby){
		super(BABY_DISPLAY_CHAR, MAX_HP, sightRange, MAX_FOOD_LEVEL, MAX_THIRST_LEVEL ,SPECIES, DEF_BABY_FOOD_LVL,
				0, ADULT_DISPLAY_CHAR, HATCH_NUM, HATCH_ECO_POINTS, CORPSE_FOOD_POINTS);
		this.addCapability(DinoCapabilities.Herbivorous);
	}

	/**
	 * MAke a new stegosaur
	 * @return a baby Stegosaur
	 */
	@Override
	public Stegosaur createNewCopy(){
		return new Stegosaur(true);
	}
}
