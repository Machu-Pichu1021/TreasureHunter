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
        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, STARTING_GOLD, false);

        System.out.println("(E)asy");
        System.out.println("(N)ormal");
        System.out.println("(H)ard");
        System.out.print("Choose your difficulty: ");

        String diff = SCANNER.nextLine().toLowerCase();
        switch (diff) {
            case "test" -> {
                System.out.println("Test mode activated.");
                hunter = new Hunter(name, 100, false);
                hunter.buyItem("water", 0);
                hunter.buyItem("rope", 0);
                hunter.buyItem("machete", 0);
                hunter.buyItem("boots", 0);
                hunter.buyItem("horse", 0);
                hunter.buyItem("boat", 0);
                hunter.buyItem("shovel", 0);
            }
            case ("test lose") -> {
                System.out.println("Test Lose activated.");
                hunter = new Hunter(name, 0, false);
                hardMode = true;
            }
            case "s" -> {
                System.out.println("Hello, Samurai. It is an honor to see you.");
                samurai = true;
                hardMode = true;
                hunter = new Hunter(name, 20, true);
            }
            case "h" -> {
                System.out.println(Colors.RED + "Hard Mode it is then. Prepare for a challenge." + Colors.RESET);
                hardMode = true;
            }
            case "n" -> System.out.println(Colors.YELLOW + "Normal Mode. Good luck adventurer." + Colors.RESET);
            case "e" -> {
                System.out.println(Colors.GREEN + "Easy Mode. This be your first time?" + Colors.RESET);
                hunter = new Hunter(name, STARTING_GOLD * 2, false);
                easyMode = true;
            }
            default ->
                    System.out.println("Uhhh... I'm just gonna give you " + Colors.YELLOW + "Normal Mode..." + Colors.RESET);
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

        Shop shop = new Shop(markdown, samurai);

        currentTown = new Town(shop, toughness, breakChance);

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

            if (hunter.emptyPositionInTreasureInventory() == -1) {
                System.out.println();
                System.out.println(Colors.BLUE + "You Win!" + Colors.RESET);
                System.out.println("You found all 3 treasures!");
                break;
            }
            System.out.println();
            System.out.println(currentTown.getLatestNews());

            //Check for loss
            if (hunter.getGold() < 0) {
                System.out.println();
                System.out.println(Colors.RED + "GAME OVER" + Colors.RESET);
                System.out.println("You ran out of gold!");
                break;
            }

            System.out.println("***");
            System.out.println(hunter.infoString());
            System.out.println(currentTown.infoString());
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(E)xplore surrounding terrain.");
            System.out.println("(M)ove on to a different town.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(H)unt for treasure");
            System.out.println("(D)ig for gold");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        switch (choice) {
            case "b", "s" -> currentTown.enterShop(choice);
            case "e" -> System.out.println(currentTown.getTerrain().infoString());
            case "m" -> {
                if (currentTown.leaveTown()) {
                    // This town is going away so print its news ahead of time.
                    System.out.println(currentTown.getLatestNews());
                    enterTown();
                }
            }
            case "l" -> currentTown.lookForTrouble();
            case "h" -> currentTown.huntForTreasure();
            case "d" -> currentTown.digForGold();
            case "x" -> System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
            default -> System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}