package ie.setu.jewelleryca.models;

public class SearchResult {

    private DisplayCase displayCase;
    private DisplayTray displayTray;
    private JewelleryItem item;

    public SearchResult(DisplayCase displayCase, DisplayTray displayTray, JewelleryItem item) {
        this.displayCase = displayCase;
        this.displayTray = displayTray;
        this.item = item;
    }

    public DisplayCase getDisplayCase() { return displayCase; }
    public DisplayTray getDisplayTray() { return displayTray; }
    public JewelleryItem getItem() { return item; }

    // shown in the search results list
    @Override
    public String toString() {
        return item.getDescription() + " -> Case " + displayCase.getCaseID()
                + ", Tray " + displayTray.getTrayID();
    }
}
