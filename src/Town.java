import java.awt.*;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class Town {
    private static OutputWindow WINDOW;

    // instance variables
    private Hunter hunter;

    private Shop shop;

    private Terrain terrain;

    private String printMessage;

    private boolean toughTown;
    private double toughness;

    private boolean dug;

    private double breakChance;

    private boolean alreadySearched;
    private String treasure;
    private double randomTreasure;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, double breakChance, OutputWindow window) {
        this.shop = shop;

        randomTreasure = Math.random();
        if (randomTreasure < .25)
            treasure = "a crown";
        else if (randomTreasure < .5)
            treasure = "a gem";
        else if (randomTreasure < .75)
            treasure = "a trophy";
        else
            treasure = "dust";

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = Math.random() < toughness;
        this.toughness = toughness;

        this.breakChance = breakChance;

        alreadySearched = false;
        dug = false;

        WINDOW = window;

        this.terrain = getNewTerrain();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        WINDOW.addTextToWindow("Welcome to town, " + hunter.getHunterName() + ".", Color.white);
        if (toughTown)
            WINDOW.addTextToWindow("\nIt's pretty rough around here, so watch yourself.", Color.white);
        else
            WINDOW.addTextToWindow("\nWe're just a sleepy little town with mild mannered folk.", Color.white);
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            String altItem = terrain.getSecondaryItem();
            if (altItem != null && altItem.equals("katana") && hunter.hasItemInKit("katana")) {
                WINDOW.addTextToWindow("You expertly slice your way through the dense bamboo of the ", Color.white);
                WINDOW.addTextToWindow(terrain.getTerrainName(), Color.cyan);
                WINDOW.addTextToWindow(" with your ", Color.white);
                WINDOW.addTextToWindow("katana", Color.red);
                WINDOW.addTextToWindow(".\n", Color.white);
                return true;
            }
            WINDOW.addTextToWindow("You used your ", Color.white);
            WINDOW.addTextToWindow(item, Color.pink);
            WINDOW.addTextToWindow(" to cross the ", Color.white);
            WINDOW.addTextToWindow(terrain.getTerrainName(), Color.cyan);
            WINDOW.addTextToWindow(".\n", Color.white);
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                WINDOW.addTextToWindow("Unfortunately, you lose your ", Color.white);
                WINDOW.addTextToWindow(item, Color.pink);
                WINDOW.addTextToWindow(" when travelling the ", Color.white);
                WINDOW.addTextToWindow(terrain.getTerrainName(), Color.cyan);
                WINDOW.addTextToWindow(".\n", Color.white);
            }
            WINDOW.addTextToWindow("\n", Color.white);
            return true;
        }

        WINDOW.addTextToWindow("You can't leave town, " + hunter.getHunterName() + ".", Color.white);
        if (terrain.getNeededItem().equals("boots") || terrain.getNeededItem().equals("water"))
            WINDOW.addTextToWindow(" You don't have ", Color.white);
        else
            WINDOW.addTextToWindow(" You don't have a ", Color.white);
        WINDOW.addTextToWindow(terrain.getNeededItem(), Color.pink);
        WINDOW.addTextToWindow(".\n", Color.white);
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        double fightDifficulty;
        if (toughTown) {
            noTroubleChance = 0.4;
            fightDifficulty = toughness;
        } else {
            noTroubleChance = 0.7;
            fightDifficulty = 0.8 * toughness;
        }
        if (Math.random() < noTroubleChance)
            WINDOW.addTextToWindow("You couldn't find any trouble\n", Color.white);
        else {
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (hunter.hasItemInKit("katana")) {
                WINDOW.addTextToWindow("You want trouble, stranger?! You g-\n", Color.white);
                WINDOW.addTextToWindow("Oh my god that's a ", Color.blue);
                WINDOW.addTextToWindow("katana", Color.red);
                WINDOW.addTextToWindow(". Yeah nope I'm out. I am NOT fighting a samurai. Just take my gold man.\n", Color.BLUE);
                WINDOW.addTextToWindow("That was... interesting. Well, at least you got ", Color.white);
                WINDOW.addTextToWindow(goldDiff + " gold", Color.orange);
                WINDOW.addTextToWindow(".\n", Color.white);
                hunter.changeGold(goldDiff);
            }
            else {
                WINDOW.addTextToWindow("You want trouble, stranger?! You got it!\nOof! Umph! Ow!\n", Color.red);
                if (Math.random() > fightDifficulty) {
                    WINDOW.addTextToWindow("Okay, stranger! You proved yer mettle. Here, take my gold.\n", Color.blue);
                    WINDOW.addTextToWindow("You won the brawl and received ", Color.white);
                    WINDOW.addTextToWindow(goldDiff + " gold", Color.orange);
                    WINDOW.addTextToWindow(".\n", Color.white);
                    hunter.changeGold(goldDiff);
                } else {
                    WINDOW.addTextToWindow("That'll teach you to go lookin' fer trouble in MY town! Now pay up!\n", Color.red);
                    WINDOW.addTextToWindow("You lost the brawl and pay ", Color.white);
                    WINDOW.addTextToWindow(goldDiff + " gold", Color.orange);
                    WINDOW.addTextToWindow(".\n", Color.white);
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }

    public void huntForTreasure() {
        if (!alreadySearched) {
            if (!treasure.equals("dust")) {
                WINDOW.addTextToWindow("You found ", Color.white);
                WINDOW.addTextToWindow(treasure, Color.blue);
                WINDOW.addTextToWindow("!\n", Color.white);
                if (!hunter.hasItemInTreasureInventory(treasure)) {
                    hunter.addTreasure(treasure);
                    WINDOW.addTextToWindow("You add it to your collection.\n", Color.white);
                }
                else
                    WINDOW.addTextToWindow("It seems you already have that treasure. You decide to leave this one here for the next adventurer.\n", Color.white);
            }
            else
                WINDOW.addTextToWindow("All you could find was dust.\n", Color.white);
            alreadySearched = true;
        }
        else
            WINDOW.addTextToWindow("You have already searched this town!\n", Color.white);
    }

    /**
     * Lets the hunter dig for gold. <br>
     * If they have a shovel, it's a 50/50 change to find nothing or for 1-20 gold <br>
     * If they don't a message is shown saying they can't dig
     */
    public void digForGold() {
        boolean shovel = hunter.hasItemInKit("shovel");
        if (dug) {
            WINDOW.addTextToWindow("You already dug for gold in this town.\n", Color.white);
            return;
        }

        if (shovel) {
            dug = true;
            if (Math.random() < 0.5)
                WINDOW.addTextToWindow("You dug but only found dirt.\n", Color.white);
            else {
                int goldDug = (int) (Math.random() * 20) + 1;
                hunter.changeGold(goldDug);
                WINDOW.addTextToWindow("You dug up ", Color.white);
                WINDOW.addTextToWindow(goldDug + " gold", Color.orange);
                WINDOW.addTextToWindow("!\n", Color.white);
            }
        }
        else {
            WINDOW.addTextToWindow("You can't dig for gold without a ", Color.white);
            WINDOW.addTextToWindow("shovel", Color.pink);
            WINDOW.addTextToWindow(".\n", Color.white);
        }
    }

    public void infoString() {
        WINDOW.addTextToWindow("This nice little town is surrounded by ", Color.white);
        WINDOW.addTextToWindow(terrain.getTerrainName(), Color.cyan);
        WINDOW.addTextToWindow(".\n", Color.white);
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < 1.0/6)
            return new Terrain("Mountains", "Rope", WINDOW);
        else if (rnd < 2.0/6)
            return new Terrain("Ocean", "Boat", WINDOW);
        else if (rnd < 3.0/6)
            return new Terrain("Plains", "Horse", WINDOW);
        else if (rnd < 4.0/6)
            return new Terrain("Desert", "Water", WINDOW);
        else if (rnd < 5.0/6)
            return new Terrain("Marsh", "Boots", WINDOW);
        else
            return new Terrain("Jungle", "Machete", "Katana", WINDOW);
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        return Math.random() < breakChance;
    }
}