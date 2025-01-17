import java.awt.*;
import java.util.Scanner;

/**
 * The Shop class controls the cost of the items in the Treasure Hunt game. <p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method. <p>
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class Shop {
    // constants
    private static final int KATANA_COST = 0;
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int SHOVEL_COST = 8;
    private static final int BOOTS_COST = 10;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;

    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);
    private static OutputWindow WINDOW;

    // instance variables
    private double markdown;
    private Hunter customer;
    private boolean isSamurai;

    /**
     * The Shop constructor takes in a markdown value and leaves customer null until one enters the shop.
     *
     * @param markdown Percentage of markdown for selling items in decimal format.
     */
    public Shop(double markdown, boolean samurai, OutputWindow window) {
        this.markdown = markdown;
        isSamurai = samurai;
        customer = null; // customer is set in the enter method
        WINDOW = window;
    }

    /**
     * Method for entering the shop.
     *
     * @param hunter the Hunter entering the shop
     * @param buyOrSell String that determines if hunter is "B"uying or "S"elling
     * @return a String to be used for printing in the latest news
     */
    public void enter(Hunter hunter, String buyOrSell) {
        customer = hunter;
        if (buyOrSell.equals("b")) {
            WINDOW.addTextToWindow("Welcome to the shop! We have the finest wares in town.\n", Color.white);
            WINDOW.addTextToWindow("Currently we have the following items:\n", Color.white);
            inventory();
            WINDOW.addTextToWindow("What're you lookin' to buy?", Color.white);
            String item = SCANNER.nextLine().toLowerCase();
            WINDOW.clear();
            int cost = checkMarketPrice(item, true);
            if (cost == -1)
                WINDOW.addTextToWindow("We ain't got none of those.\n", Color.white);
            else {
                WINDOW.addTextToWindow("It'll cost you ", Color.white);
                WINDOW.addTextToWindow(cost + " gold", Color.orange);
                WINDOW.addTextToWindow(". Buy it (y/n)?\n", Color.white);
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y"))
                    buyItem(item);
            }
        } else {
            WINDOW.addTextToWindow("What're you lookin' to sell?\n", Color.white);
            WINDOW.addTextToWindow("You currently have the following items:\n", Color.white);
            customer.printInventory();
            WINDOW.addTextToWindow("\n", Color.white);
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, false);
            if (cost == 0 || cost == -1)
                WINDOW.addTextToWindow("We don't want none of those.\n", Color.white);
            else {
                WINDOW.addTextToWindow("It'll get you ", Color.white);
                WINDOW.addTextToWindow(cost + " gold", Color.orange);
                WINDOW.addTextToWindow(". Sell it (y/n)?\n", Color.white);
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y"))
                    sellItem(item);
            }
        }
        WINDOW.clear();
        WINDOW.addTextToWindow("You left the shop.\n", Color.white);
    }

    /**
     * A method that returns a string showing the items available in the shop
     * (all shops sell the same items).
     *
     * @return the string representing the shop's items available for purchase and their prices.
     */
    public void inventory() {
        if (isSamurai) {
            WINDOW.addTextToWindow("KATANA: ", Color.red);
            WINDOW.addTextToWindow(KATANA_COST + " gold\n", Color.orange);
        }
        WINDOW.addTextToWindow("Water: ", Color.pink);
        WINDOW.addTextToWindow(WATER_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("Rope: ", Color.pink);
        WINDOW.addTextToWindow(ROPE_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("Machete: ", Color.pink);
        WINDOW.addTextToWindow(MACHETE_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("Boots: ", Color.pink);
        WINDOW.addTextToWindow(BOOTS_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("Shovel: ", Color.pink);
        WINDOW.addTextToWindow(SHOVEL_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("Horse: ", Color.pink);
        WINDOW.addTextToWindow(HORSE_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("Boat: ", Color.pink);
        WINDOW.addTextToWindow(BOAT_COST + " gold\n", Color.orange);
        WINDOW.addTextToWindow("\n", Color.white);
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     *
     * @param item The item being bought.
     */
    public void buyItem(String item) {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem)) {
            if (customer.hasItemInKit("katana")) {
                WINDOW.addTextToWindow("Nice ", Color.white);
                WINDOW.addTextToWindow("katana", Color.red);
                WINDOW.addTextToWindow(" you have there sir... Just this once I can give you the ", Color.white);
                WINDOW.addTextToWindow(item, Color.pink);
                WINDOW.addTextToWindow(" for free. Just don't start swinging...\n", Color.white);
                customer.changeGold(costOfItem);
            }
            else {
                WINDOW.addTextToWindow("Ye' got yerself a ", Color.white);
                WINDOW.addTextToWindow(item, Color.pink);
                WINDOW.addTextToWindow(". Come again soon.\n", Color.white);
            }
        } else {
            if (customer.getGold() < costOfItem) {
                if (customer.hasItemInKit("katana")) {
                    WINDOW.addTextToWindow("It seems you don't have enough gold for that. B-but your ", Color.white);
                    WINDOW.addTextToWindow("katana", Color.red);
                    WINDOW.addTextToWindow(" looks mighty frightening, just take the ", Color.white);
                    WINDOW.addTextToWindow(item, Color.pink);
                    WINDOW.addTextToWindow(" and don't hurt me!\n", Color.white);
                    customer.buyItem(item, 0);
                }
                else
                    WINDOW.addTextToWindow("I'm afraid you don't have enough gold, come back when you're a little, mmmm richer.", Color.white);
            }
            else {
                WINDOW.addTextToWindow("It seems you already have a ", Color.white);
                WINDOW.addTextToWindow(item, Color.pink);
                WINDOW.addTextToWindow(".", Color.white);
            }
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     *
     * @param item The item being sold.
     */
    public void sellItem(String item) {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice))
            WINDOW.addTextToWindow("Pleasure doin' business with you.\n", Color.white);
        else
            WINDOW.addTextToWindow("Stop stringin' me along!\n", Color.white);
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     *
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying) {
        return isBuying ? getCostOfItem(item) : getBuyBackCost(item);
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or -1 if the item is not found.
     */
    public int getCostOfItem(String item) {
        return switch (item) {
            case "water" -> WATER_COST;
            case "rope" -> ROPE_COST;
            case "machete" -> MACHETE_COST;
            case "boots" -> BOOTS_COST;
            case "shovel" -> SHOVEL_COST;
            case "horse" -> HORSE_COST;
            case "boat" -> BOAT_COST;
            case "katana" -> isSamurai ? KATANA_COST : -1;
            default -> -1;
        };
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item) {
        return (int) (getCostOfItem(item) * markdown);
    }
}