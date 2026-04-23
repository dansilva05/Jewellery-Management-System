package ie.setu.jewelleryca.models;

import ie.setu.jewelleryca.core.LinkedList;
import java.io.Serializable;

public class JewelleryStore implements Serializable {

    private LinkedList<DisplayCase> displayCases;

    public JewelleryStore() {
        displayCases = new LinkedList<>();
    }

    public LinkedList<DisplayCase> getDisplayCases() {
        return displayCases;
    }

    // adds a display case if the ID isn't already taken
    public boolean addDisplayCase(DisplayCase dc) {
        if (findCase(dc.getCaseID()) != null) {
            return false;  // already exists
        }
        displayCases.add(dc);
        return true;
    }

    // adds a tray to a case and checks that tray ID is unique across all cases
    public boolean addTrayToCase(String caseID, DisplayTray tray) {
        if (findTrayByID(tray.getTrayID()) != null) {
            return false;  // tray ID already used somewhere
        }
        DisplayCase dc = findCase(caseID);
        if (dc == null) {
            return false;
        }
        dc.addTray(tray);
        return true;
    }

    // adds an item to a specific tray in a specific case
    public boolean addItemToTray(String caseID, String trayID, JewelleryItem item) {
        DisplayTray tray = findTray(caseID, trayID);
        if (tray == null) {
            return false;
        }
        tray.addItem(item);
        return true;
    }

    // automatically finds a tray to add an item to (smart add)
    // tries to find a tray with another item of the same type, otherwise picks the tray with the fewest items
    public SearchResult smartAdd(JewelleryItem item) {
        DisplayTray bestTray = null;
        DisplayCase bestCase = null;
        int fewestItems = -1;

        for (int i = 0; i < displayCases.size(); i++) {
            DisplayCase dc = displayCases.get(i);
            for (int j = 0; j < dc.getTrays().size(); j++) {
                DisplayTray tray = dc.getTrays().get(j);

                // first try: a tray with at least one item of the same type
                for (int k = 0; k < tray.getItems().size(); k++) {
                    if (tray.getItems().get(k).getType().equalsIgnoreCase(item.getType())) {
                        tray.addItem(item);
                        return new SearchResult(dc, tray, item);
                    }
                }

                // otherwise remember the tray with the fewest items as a fallback
                if (fewestItems == -1 || tray.getItems().size() < fewestItems) {
                    fewestItems = tray.getItems().size();
                    bestTray = tray;
                    bestCase = dc;
                }
            }
        }

        // fallback: add to the tray with the fewest items if there's any tray at all
        if (bestTray != null) {
            bestTray.addItem(item);
            return new SearchResult(bestCase, bestTray, item);
        }
        return null;  // no trays in the system at all
    }

    // adds a material to an item (found by index)
    public boolean addMaterialToItem(String caseID, String trayID, int itemIndex, MaterialComponent material) {
        JewelleryItem item = findItem(caseID, trayID, itemIndex);
        if (item == null) {
            return false;
        }
        item.addMaterial(material);
        return true;
    }

    // finds a case by its ID, returns null if not found
    public DisplayCase findCase(String caseID) {
        for (int i = 0; i < displayCases.size(); i++) {
            if (displayCases.get(i).getCaseID().equalsIgnoreCase(caseID)) {
                return displayCases.get(i);
            }
        }
        return null;
    }

    // searches all cases for a tray with this ID
    public DisplayTray findTrayByID(String trayID) {
        for (int i = 0; i < displayCases.size(); i++) {
            DisplayCase dc = displayCases.get(i);
            for (int j = 0; j < dc.getTrays().size(); j++) {
                if (dc.getTrays().get(j).getTrayID().equalsIgnoreCase(trayID)) {
                    return dc.getTrays().get(j);
                }
            }
        }
        return null;
    }

    // finds a tray inside a specific case
    public DisplayTray findTray(String caseID, String trayID) {
        DisplayCase dc = findCase(caseID);
        if (dc == null) return null;
        for (int i = 0; i < dc.getTrays().size(); i++) {
            if (dc.getTrays().get(i).getTrayID().equalsIgnoreCase(trayID)) {
                return dc.getTrays().get(i);
            }
        }
        return null;
    }

    // finds an item by index inside a specific tray
    public JewelleryItem findItem(String caseID, String trayID, int itemIndex) {
        DisplayTray tray = findTray(caseID, trayID);
        if (tray == null || itemIndex < 0 || itemIndex >= tray.getItems().size()) {
            return null;
        }
        return tray.getItems().get(itemIndex);
    }

    // removes an item by index from a tray
    public boolean removeItem(String caseID, String trayID, int itemIndex) {
        DisplayTray tray = findTray(caseID, trayID);
        if (tray == null) return false;
        return tray.removeItem(itemIndex);
    }

    // searches every item and its materials for anything matching the term
    public LinkedList<SearchResult> search(String term) {
        LinkedList<SearchResult> results = new LinkedList<>();
        for (int i = 0; i < displayCases.size(); i++) {
            DisplayCase dc = displayCases.get(i);
            for (int j = 0; j < dc.getTrays().size(); j++) {
                DisplayTray tray = dc.getTrays().get(j);
                for (int k = 0; k < tray.getItems().size(); k++) {
                    JewelleryItem item = tray.getItems().get(k);
                    if (item.matchesSearch(term)) {
                        results.add(new SearchResult(dc, tray, item));
                    }
                }
            }
        }
        return results;
    }

    // adds up prices across all cases
    public double getTotalValue() {
        double total = 0;
        for (int i = 0; i < displayCases.size(); i++) {
            total += displayCases.get(i).getCaseValue();
        }
        return total;
    }

    // builds a text report of the value of each case and tray (GPT)
    public String getValueReport() {
        if (displayCases.isEmpty()) {
            return "No stock in the store to value.";
        }
        String report = "=== Stock Value Report ===\n\n";
        for (int i = 0; i < displayCases.size(); i++) {
            DisplayCase dc = displayCases.get(i);
            report += "Case " + dc.getCaseID() + "  ->  €" + String.format("%.2f", dc.getCaseValue()) + "\n";
            for (int j = 0; j < dc.getTrays().size(); j++) {
                DisplayTray tray = dc.getTrays().get(j);
                report += "   Tray " + tray.getTrayID() + "  ->  €" + String.format("%.2f", tray.getTrayValue()) + "\n";
            }
            report += "\n";
        }
        report += "Total store value:  €" + String.format("%.2f", getTotalValue());
        return report;
    }

    // lists everything in the store as text
    public String getAllStockReport() {
        if (displayCases.isEmpty()) {
            return "No stock in the store yet.";
        }
        String report = "";
        for (int i = 0; i < displayCases.size(); i++) {
            DisplayCase dc = displayCases.get(i);
            report += dc.toString() + "\n";
            if (dc.getTrays().isEmpty()) {
                report += "   (no trays)\n";
            } else {
                for (int j = 0; j < dc.getTrays().size(); j++) {
                    DisplayTray tray = dc.getTrays().get(j);
                    report += "   " + tray.toString() + "\n";
                    if (tray.getItems().isEmpty()) {
                        report += "      (no items)\n";
                    } else {
                        for (int k = 0; k < tray.getItems().size(); k++) {
                            report += "      - " + tray.getItems().get(k).toString() + "\n";
                        }
                    }
                }
            }
            report += "\n";
        }
        return report.trim();
    }

    // clears everything
    public void clear() {
        displayCases.clear();
    }
}
