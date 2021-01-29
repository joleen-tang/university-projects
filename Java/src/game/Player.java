package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Menu;

/**
 * Class representing the Player.
 */
public class Player extends Actor {
	/** Number of turns that have passed since the start of the game */
	private static int turnCount=0;
	/** Menu object for bringing up the player menu every turn */
	private final Menu menu = new Menu();
	/** Number of ecoPoints player currently has */
	private static int ecoPoints=0;

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		if (Application.checkChallengeMode() && turnCount==Application.getTurnLimit()){
			return new QuitGameAction();
		}
		turnCount+=1;
		System.out.println(this +" has "+ecoPoints+" ecopoints.");
		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		actions.add(new QuitGameAction());
		return menu.showMenu(this, actions, display);
	}

	/**
	 * Method that resets the Player used at the start of a new game
	 * */
	protected static void resetPlayer(){
		ecoPoints=0;
		turnCount=0;
	}

	/**
	 * Method to allow other Objects to add ecoPoints
	 * */
	protected static void increaseEcoPoints(int points){
		ecoPoints+=points;
	}

	/**
	 * Method to allow other Objects to decrease ecoPoints
	 * */
	protected static void decreaseEcoPoints(int points){
		ecoPoints-=points;
	}

	/**
	 * Getter method for the player's current ecoPoints
	 * @return temp Integer representing how many ecoPoints the player current has
	 * */
	protected static int getEcoPoints(){
		int temp=ecoPoints;
		return temp;
	}
}
