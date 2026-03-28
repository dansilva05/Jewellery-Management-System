package ie.setu.jewelleryca.models;

import ie.setu.jewelleryca.core.LinkedList;
import java.io.Serializable;

public class DisplayTray implements Serializable {

    private String trayID;
    private String inlayColour;
    private double width;
    private double depth;
    private LinkedList<JewelleryItem> items;

    public DisplayTray(String trayID, String inlayColour, double width, double depth) {
        this.trayID = trayID;
        this.inlayColour = inlayColour;
        this.width = width;
        this.depth = depth;
        items = new LinkedList<>();
    }

    public String getTrayID() { return trayID; }
    public String getInlayColour() { return inlayColour; }
    public double getWidth() { return width; }
    public double getDepth() { return depth; }
    public LinkedList<JewelleryItem> getItems() { return items; }

    // adds an item to this tray
    public void addItem(JewelleryItem item) {
        items.add(item);
    }

    // removes item at the index, returns true if it worked
    public boolean removeItem(int index) {
        return items.remove(index);
    }

    // adds up all item prices in this tray
    public double getTrayValue() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getRetailPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return trayID + " (" + inlayColour + ", " + width + " x " + depth + " cm)";
    }
}
