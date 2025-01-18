import java.awt.*;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final OutputWindow WINDOW = new OutputWindow();

    private static final int STARTING_GOLD = 20;

    // instance variables
    private Town currentTown;
    private Hunter hunter;

    private boolean hardMode;
    private boolean easyMode;
    private boolean samurai;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        WINDOW.addTextToWindow("Welcome to TREASURE HUNTER!\n", Color.white);
        WINDOW.addTextToWindow("Going hunting for the big treasure, eh?\n", Color.white);
        WINDOW.addTextToWindow("What's your name, Hunter?\n", Color.white);
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, STARTING_GOLD, false, WINDOW);

        WINDOW.clear();
        WINDOW.addTextToWindow("(E)asy\n", Color.white);
        WINDOW.addTextToWindow("(N)ormal\n", Color.white);
        WINDOW.addTextToWindow("(H)ard\n", Color.white);
        WINDOW.addTextToWindow("Choose your difficulty:", Color.white);

        String diff = SCANNER.nextLine().toLowerCase();
        WINDOW.clear();
        switch (diff) {
            case "test" -> {
                WINDOW.addTextToWindow("Test mode activated.\n", Color.white);
                hunter = new Hunter(name, 100, false, WINDOW);
                hunter.buyItem("water", 0);
                hunter.buyItem("rope", 0);
                hunter.buyItem("machete", 0);
                hunter.buyItem("boots", 0);
                hunter.buyItem("horse", 0);
                hunter.buyItem("boat", 0);
                hunter.buyItem("shovel", 0);
            }
            case ("test lose") -> {
                WINDOW.addTextToWindow("Test Lose activated.\n", Color.white);
                hunter = new Hunter(name, 0, false, WINDOW);
                hardMode = true;
            }
            case "s" -> {
                WINDOW.addTextToWindow("Hello, Samurai. It is an honor to see you.\n", Color.white);
                samurai = true;
                hardMode = true;
                hunter = new Hunter(name, 20, true, WINDOW);
            }
            case "h" -> {
                WINDOW.addTextToWindow("Hard Mode it is then. Prepare for a challenge.\n", Color.red);
                hardMode = true;
            }
            case "n" -> WINDOW.addTextToWindow("Normal Mode. Good luck adventurer.\n", Color.orange);
            case "e" -> {
                WINDOW.addTextToWindow("Easy Mode. This be your first time?\n", Color.green);
                hunter = new Hunter(name, STARTING_GOLD * 2, false, WINDOW);
                easyMode = true;
            }
            default ->
                    WINDOW.addTextToWindow("Uhhh... I'm just gonna give you Normal Mode...\n", Color.orange);
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        double breakChance = 0.5;
        if (hardMode) {
            markdown = 0.25;
            toughness = 0.75;
            breakChance = 0.65;
        }
        else if (easyMode) {
            markdown = 1;
            toughness = 0.2;
            breakChance = 0;
        }

        Shop shop = new Shop(markdown, samurai, WINDOW);

        currentTown = new Town(shop, toughness, breakChance, WINDOW);

        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {

            //Check for win
            if (hunter.emptyPositionInTreasureInventory() == -1) {
                WINDOW.addTextToWindow("\n", Color.white);
                WINDOW.addTextToWindow("You Win!\n", Color.blue);
                WINDOW.addTextToWindow("You found all 3 treasures!", Color.white);
                break;
            }

            WINDOW.addTextToWindow("\n", Color.white);
            //System.out.println(currentTown.getLatestNews());

            //Check for loss
            if (hunter.getGold() < 0) {
                WINDOW.addTextToWindow("\n", Color.white);
                WINDOW.addTextToWindow("GAME OVER\n", Color.red);
                WINDOW.addTextToWindow("You ran out of gold!", Color.white);
                break;
            }

            WINDOW.addTextToWindow("***\n", Color.white);
            hunter.infoString();
            currentTown.infoString();
            WINDOW.addTextToWindow("(B)uy something at the shop.\n", Color.white);
            WINDOW.addTextToWindow("(S)ell something at the shop.\n", Color.white);
            WINDOW.addTextToWindow("(E)xplore surrounding terrain.\n", Color.white);
            WINDOW.addTextToWindow("(M)ove on to a different town.\n", Color.white);
            WINDOW.addTextToWindow("(L)ook for trouble!\n", Color.white);
            WINDOW.addTextToWindow("(H)unt for treasure\n", Color.white);
            WINDOW.addTextToWindow("(D)ig for gold\n", Color.white);
            WINDOW.addTextToWindow("Give up the hunt and e(X)it.\n", Color.white);
            WINDOW.addTextToWindow("\n", Color.white);
            WINDOW.addTextToWindow("What's your next move?", Color.white);
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        WINDOW.clear();
        switch (choice) {
            case "b", "s" -> currentTown.enterShop(choice);
            case "e" -> currentTown.getTerrain().infoString();
            case "m" -> {
                if (currentTown.leaveTown()) {
                    // This town is going away so print its news ahead of time.
                    //System.out.println(currentTown.getLatestNews());
                    enterTown();
                }
            }
            case "l" -> currentTown.lookForTrouble();
            case "h" -> currentTown.huntForTreasure();
            case "d" -> currentTown.digForGold();
            case "x" -> {
                WINDOW.clear();
                WINDOW.addTextToWindow("Fare thee well, " + hunter.getHunterName() + "!\n", Color.white);
                WINDOW.addTextToWindow("Close the window to exit...", Color.white);
            }
            default -> {
                WINDOW.clear();
                WINDOW.addTextToWindow("Yikes! That's an invalid option! Try again.\n\n", Color.white);
            }
        }
    }
}