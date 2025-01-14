/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean dug;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);

        dug = false;
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
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
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
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }
            return true;
        }
        if (terrain.getNeededItem().equals("boots")) {
            printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have " + Colors.PURPLE +
                    terrain.getNeededItem() + Colors.RESET + ".";
        }
        else{
            printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + Colors.PURPLE +
                    terrain.getNeededItem() + Colors.RESET + ".";
        }
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and received " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                hunter.changeGold(-goldDiff);
            }
        }
    }

    /**
     * Lets the hunter dig for gold. <br>
     * If they have a shovel, it's a 50/50 change to find nothing or for 1-20 gold <br>
     * If they don't a message is shown saying they can't dig
     */
    public void digForGold() {
        boolean shovel = hunter.hasItemInKit("shovel");
        printMessage = "";
        if (dug) {
            printMessage += "You already dug for gold in this town.";
            return;
        }

        if (shovel) {
            dug = true;
            if (Math.random() < 0.5)
                printMessage += "You dug but only found dirt.";
            else {
                int goldDug = (int) (Math.random() * 20) + 1;
                hunter.changeGold(goldDug);
                printMessage += "You dug up " + Colors.YELLOW + goldDug + " gold" + Colors.RESET + "!";
            }
        }
        else {
            printMessage += "You can't dig for gold without a " + Colors.PURPLE + "shovel" + Colors.RESET + ".";
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .2) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .4) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .6) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .8) {
            return new Terrain("Desert", "Water");
        } else {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}