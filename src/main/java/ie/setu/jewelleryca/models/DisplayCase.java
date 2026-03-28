package ie.setu.jewelleryca.models;

import ie.setu.jewelleryca.core.LinkedList;
import java.io.Serializable;

public class DisplayCase implements Serializable {

    private String caseID;
    private String type;
    private String lighting;
    private LinkedList<DisplayTray> trays;

    public DisplayCase(String caseID, String type, String lighting) {
        this.caseID = caseID;
        this.type = type;
        this.lighting = lighting;
        trays = new LinkedList<>();
    }

    public String getCaseID() { return caseID; }
    public String getType() { return type; }
    public String getLighting() { return lighting; }
    public LinkedList<DisplayTray> getTrays() { return trays; }

    // adds a tray to this case
    public void addTray(DisplayTray tray) {
        trays.add(tray);
    }

    // adds up the value of all trays in this case
    public double getCaseValue() {
        double total = 0;
        for (int i = 0; i < trays.size(); i++) {
            total += trays.get(i).getTrayValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Case " + caseID + " (" + type + ", " + lighting + ")";
    }
}
