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

    // instance variables
    private double markdown;
    private Hunter customer;
    private boolean isSamurai;

    /**
     * The Shop constructor takes in a markdown value and leaves customer null until one enters the shop.
     *
     * @param markdown Percentage of markdown for selling items in decimal format.
     */
    public Shop(double markdown, boolean samurai) {
        this.markdown = markdown;
        isSamurai = samurai;
        customer = null; // customer is set in the enter method
    }

    /**
     * Method for entering the shop.
     *
     * @param hunter the Hunter entering the shop
     * @param buyOrSell String that determines if hunter is "B"uying or "S"elling
     * @return a String to be used for printing in the latest news
     */
    public String enter(Hunter hunter, String buyOrSell) {
        customer = hunter;
        if (buyOrSell.equals("b")) {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, true);
            if (cost == -1)
                System.out.println("We ain't got none of those.");
            else {
                System.out.print("It'll cost you " + Colors.YELLOW + cost + " gold" + Colors.RESET + ". Buy it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y"))
                    buyItem(item);
            }
        } else {
            System.out.println("What're you lookin' to sell? ");
            System.out.println("You currently have the following items: " + customer.getInventory());
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, false);
            if (cost == -1)
                System.out.println("We don't want none of those.");
            else {
                System.out.print("It'll get you " + Colors.YELLOW + cost + " gold" + Colors.RESET + ". Sell it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y"))
                    sellItem(item);
            }
        }
        return "You left the shop";
    }

    /**
     * A method that returns a string showing the items available in the shop
     * (all shops sell the same items).
     *
     * @return the string representing the shop's items available for purchase and their prices.
     */
    public String inventory() {
        String str = "";
        if (isSamurai)
            str += Colors.RED + "KATANA: " + Colors.YELLOW + KATANA_COST + " gold\n" + Colors.RESET;

        str += Colors.PURPLE + "Water: " + Colors.YELLOW + WATER_COST + " gold\n" + Colors.RESET;
        str += Colors.PURPLE + "Rope: " + Colors.YELLOW + ROPE_COST + " gold\n" + Colors.RESET;
        str += Colors.PURPLE + "Machete: " + Colors.YELLOW + MACHETE_COST + " gold\n" + Colors.RESET;
        str += Colors.PURPLE + "Boots: " + Colors.YELLOW + BOOTS_COST + " gold\n" + Colors.RESET;
        str += Colors.PURPLE + "Shovel: " + Colors.YELLOW + SHOVEL_COST + " gold\n" + Colors.RESET;
        str += Colors.PURPLE + "Horse: " + Colors.YELLOW + HORSE_COST + " gold\n" + Colors.RESET;
        str += Colors.PURPLE + "Boat: " + Colors.YELLOW + BOAT_COST + " gold\n" + Colors.RESET;
        return str;
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
                System.out.println("Nice " + Colors.RED + "katana" + Colors.RESET + " you have there sir... Just this once I can " +
                        "give you the " + Colors.PURPLE + item + Colors.RESET + " for free. Just don't start swinging...");
                customer.changeGold(costOfItem);
            }
            else
                System.out.println("Ye' got yerself a " + Colors.PURPLE + item + Colors.RESET + ". Come again soon.");
        } else {
            if (customer.getGold() < costOfItem) {
                if (customer.hasItemInKit("katana")) {
                    System.out.println("It seems you don't have enough gold for that. B-but your " + Colors.RED + "katana" +
                            Colors.RESET + " looks mighty frightening, just take the " + Colors.PURPLE + item + Colors.RESET +
                            " and don't hurt me!");
                    customer.buyItem(item, 0);
                }
                else
                    System.out.println("I'm afraid you don't have enough gold, come back when you're a little, mmmm richer.");
            }
            else
                System.out.println("It seems you already have a " + Colors.PURPLE + item + Colors.RESET + ".");
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
            System.out.println("Pleasure doin' business with you.");
        else
            System.out.println("Stop stringin' me along!");
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