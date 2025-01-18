import java.awt.*;

/**
 * The Terrain class is designed to represent the zones between the towns in the Treasure Hunter game.
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class Terrain {
    private static OutputWindow WINDOW;

    // instance variables
    private String terrainName;
    private String neededItem;
    private String secondaryItem;

    /**
     * Sets the class member variables
     *
     * @param name The name of the zone.
     * @param item The item needed in order to cross the zone.
     */
    public Terrain(String name, String item, OutputWindow window) {
        terrainName = name;
        neededItem = item.toLowerCase();
        secondaryItem = null;
        WINDOW = window;
    }
    public Terrain(String name, String item, String altItem, OutputWindow window) {
        terrainName = name;
        neededItem = item.toLowerCase();
        secondaryItem = altItem.toLowerCase();
        WINDOW = window;
    }

    // accessors
    public String getTerrainName() {
        return terrainName;
    }

    public String getNeededItem() {
        return neededItem;
    }

    public String getSecondaryItem() {
        return secondaryItem;
    }

    /**
     * Guards against a hunter crossing the zone without the proper item.
     * Searches the hunter's inventory for the proper item and determines whether the hunter can cross.
     *
     * @param hunter The Hunter object trying to cross the terrain.
     * @return true if the Hunter has the proper item.
     */
    public boolean canCrossTerrain(Hunter hunter) {
        return hunter.hasItemInKit(neededItem) || hunter.hasItemInKit(secondaryItem);
    }

    /**
     * @return A string representation of the terrain and item to cross it.
     */
    public void infoString() {
        WINDOW.addTextToWindow("You are surrounded by ", Color.white);
        WINDOW.addTextToWindow(terrainName, Color.cyan);
        WINDOW.addTextToWindow(" which needs (a) ", Color.white);
        WINDOW.addTextToWindow(neededItem, Color.pink);
        WINDOW.addTextToWindow(" to cross.\n", Color.white);
    }
}