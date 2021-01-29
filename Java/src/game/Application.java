package game;

import java.util.*;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.Allosaur;
import game.dinosaurs.Stegosaur;

/**
 * The main class for the Jurassic World game.
 *
 */
public class Application {
	/** Hashmap containing a set of hotkeys and strings for displaying the main menu */
	private static final HashMap<Character, String> KEY_TO_OPTION_MAP =setOptions();
	/** Display object for easy printing */
	private static final Display DISPLAY =new Display();
	/** Scanner object for taking user input for menu navigation */
	private static final Scanner KEYBOARD = new Scanner(System.in);
	/** Integer storing the user defined turn limit for Challenge Mode */
	private static int turnLimit=0;
	/** Boolean showing whether the current game mode is challenge or sandbox */
	private static boolean challengeMode=false;

	/**
	 * Main method for the Jurassic World game.
	 */
	public static void main(String[] args) {
		char key;
		do{
		key=getPlayOption();
		World world=initialiseWorld();

		if (key=='1'){
			sandboxMode(world);
		}
		else if(key=='2'){
			challengeMode(world);
			}
		} while (key!='3');
	}

	/**
	 * Runs sandbox mode of the game; only ends when user quits
	 * */
	private static void sandboxMode(World world){
		challengeMode=false;
		world.run();
	}

	/**
	 * Runs challenge mode of the game; user sets turn limit and ecoPoints target
	 * Game ends when user quits or when turn limit is reached
	 * Prints an end game message after game session runs
	 * */
	private static void challengeMode(World world){
		challengeMode=true;

		DISPLAY.println("Enter a turn limit: ");
		turnLimit=KEYBOARD.nextInt();

		DISPLAY.println("Enter an ecoPoints target: ");
		int ecoPointTarget= KEYBOARD.nextInt();

		world.run();

		if (Player.getEcoPoints()>=ecoPointTarget){
			DISPLAY.println("Congratulations! You beat challenge mode!");
		}
		else{
			DISPLAY.println("You failed challenge mode. Better luck next time!");
		}
	}

	/**
	 * Initialises World to use each time a new game is started
	 *
	 * @return World object to be used in next game
	 * */
	private static World initialiseWorld(){
		World world = new World(new Display());

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(),
				new VendingMachine(), new Water());

		List<String> map = Arrays.asList(
				"................................................................................",
				"....................~~..........................................................",
				".....#######.......~~~~...............................~~~~~~....................",
				".....#____V#........~~~.................................~~~~~...................",
				".....#_____#.............................................~~~....................",
				".....###.###....................................................................",
				"................................................................................",
				"......................................+++.......................................",
				".......................................++++.....................................",
				"...................................+++++........................................",
				".....................................++++++.....................................",
				"......................................+++.......................................",
				".....................................+++........................................",
				"................................................................................",
				"............+++.................................................................",
				".............+++++.............~~~..............................................",
				"...............++...........~~~~~~~~.....................+++++..................",
				".............+++..........~~~~~~~~~~~...............++++++++....................",
				"............+++..............~~~~~~~~.................+++.......................",
				"................................~~~~............................................",
				".........................................................................++.....",
				"........................................................................++.++...",
				".........................................................................++++...",
				"..........................................................................++....",
				"................................................................................");
		GameMap gameMap = new GameMap(groundFactory, map );
		world.addGameMap(gameMap);

		//Adding the second GameMap
		//The second GameMap will be constructed in a different way to the initial GameMap
		//However, the second GameMap will still have the same size as the initial
		//Added to the world.
		GameMap gameMap2 = new GameMap(groundFactory, '.', 80, 25);
		world.addGameMap(gameMap2);

		Actor player = new Player("Player", '@', 100);
		world.addPlayer(player, gameMap.at(8, 4));

		// Place a pair of stegosaurs in the middle of the map
		gameMap.at(30, 12).addActor(new Stegosaur());
		gameMap.at(32, 12).addActor(new Stegosaur());
		gameMap.at(32,13).addActor(new Allosaur());
		gameMap.at(33,12).addActor(new Allosaur());

		//This section will add exits to the initial GameMap, allowing the player to traverse the second GameMap smoothly.
		//Checks are in place to make sure that the Player cannot make an invalid move.
		NumberRange widths = gameMap.getXRange();
		for (int x : widths){
			gameMap.at(x,0).addExit(new Exit("North", gameMap2.at(x,24), "8"));
			if (x != 0){
				gameMap.at(x-1, 0).addExit(new Exit("North West", gameMap2.at(x-1, 24), "7"));
			}
			if (x != 79){
				gameMap.at(x+1, 0).addExit(new Exit("North East", gameMap2.at(x+1, 24), "9"));
			}
		}

		//This section will add exits to the second GameMap, allowing the player tp traverse from second to initial GameMap smoothly.
		//Checks are in place to make sure that the Player cannot make an invalid move.
		NumberRange widths2 = gameMap2.getXRange();
		for (int x2 : widths2){
			gameMap2.at(x2, 24).addExit(new Exit("South", gameMap.at(x2, 0),"2"));
			if (x2 != 0){
				gameMap2.at(x2-1, 24).addExit(new Exit("South West",gameMap.at(x2-1, 0), "1"));
			}
			if (x2 != 79){
				gameMap2.at(x2+1, 24).addExit(new Exit("South East", gameMap.at(x2+1, 0), "3"));
			}
		}


		Player.resetPlayer();

		return world;
	}

	/**
	 * Method to display the start game menu and allow user to select game mood or quit
	 * */
	private static Character getPlayOption(){
		for (Map.Entry<Character, String> option: KEY_TO_OPTION_MAP.entrySet())
			DISPLAY.println(option.getKey() + ": " + option.getValue());

		DISPLAY.println("Please select a game mode.");

		char key;
		do {
			key = DISPLAY.readChar();
		} while (!KEY_TO_OPTION_MAP.containsKey(key));

		return key;
	}

	/**
	 * Initialises the start menu options for the game
	 * */
	private static HashMap<Character, String> setOptions() {
		HashMap<Character, String> keyToOptionMap = new HashMap<>();
		keyToOptionMap.put('1', "Sandbox Mode");
		keyToOptionMap.put('2', "Challenge Mode");
		keyToOptionMap.put('3', "Quit");

		return keyToOptionMap;
	}

	/**
	 * Check whether or not the current game session is in challenge mode
	 *
	 * @return true if challenge mode, false if not
	 */
	protected static boolean checkChallengeMode(){
		return challengeMode;
	}

	/**
	 * Gets the turn limit
	 *
	 * @return turn limit set by the player for challenge mode
	 */
	protected static int getTurnLimit(){
		int temp=turnLimit;
		return temp;
	}
}
