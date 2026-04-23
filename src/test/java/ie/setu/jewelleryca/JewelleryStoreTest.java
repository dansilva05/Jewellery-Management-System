package ie.setu.jewelleryca;

import ie.setu.jewelleryca.models.*;
import ie.setu.jewelleryca.core.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JewelleryStoreTest {

    private JewelleryStore store;

    @BeforeEach
    public void setUp() {
        store = new JewelleryStore();
    }

    @Test
    public void addDisplayCaseWorks() {
        boolean added = store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        assertTrue(added);
        assertEquals(1, store.getDisplayCases().size());
    }

    @Test
    public void addDisplayCaseRejectsDuplicates() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        boolean addedAgain = store.addDisplayCase(new DisplayCase("C1", "Freestanding", "Unlit"));
        assertFalse(addedAgain);
        assertEquals(1, store.getDisplayCases().size());
    }

    @Test
    public void addTrayChecksUniquenessAcrossCases() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addDisplayCase(new DisplayCase("C2", "Freestanding", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));

        // same tray ID should be rejected even in a different case
        boolean added = store.addTrayToCase("C2", new DisplayTray("A1", "Red", 40, 30));
        assertFalse(added);
    }

    @Test
    public void addItemToTrayWorks() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        boolean added = store.addItemToTray("C1", "A1",
                new JewelleryItem("Gold ring", "Ring", "Female", "", 500.0));
        assertTrue(added);
        assertEquals(1, store.findTray("C1", "A1").getItems().size());
    }

    @Test
    public void removeItemReturnsFalseOnBadIndex() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        boolean removed = store.removeItem("C1", "A1", 99);
        assertFalse(removed);
    }

    @Test
    public void searchFindsByDescription() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        store.addItemToTray("C1", "A1",
                new JewelleryItem("Diamond ring", "Ring", "Female", "", 999.0));

        LinkedList<SearchResult> results = store.search("diamond");
        assertEquals(1, results.size());
    }

    @Test
    public void searchFindsByMaterialName() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        store.addItemToTray("C1", "A1",
                new JewelleryItem("Plain ring", "Ring", "Female", "", 100.0));
        store.addMaterialToItem("C1", "A1", 0,
                new MaterialComponent("Platinum", "shiny", 5.0, 950));

        LinkedList<SearchResult> results = store.search("platinum");
        assertEquals(1, results.size());
    }

    @Test
    public void totalValueAddsUpAcrossEverything() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        store.addItemToTray("C1", "A1",
                new JewelleryItem("Item1", "Ring", "Female", "", 100.0));
        store.addItemToTray("C1", "A1",
                new JewelleryItem("Item2", "Ring", "Male", "", 250.50));

        assertEquals(350.50, store.getTotalValue(), 0.001);
    }

    @Test
    public void resetClearsEverything() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        store.clear();
        assertEquals(0, store.getDisplayCases().size());
    }

    @Test
    public void smartAddPicksTrayWithSameType() {
        store.addDisplayCase(new DisplayCase("C1", "Wall-mounted", "Lit"));
        store.addTrayToCase("C1", new DisplayTray("A1", "Green", 30, 20));
        store.addTrayToCase("C1", new DisplayTray("A2", "Red", 40, 30));
        store.addItemToTray("C1", "A1",
                new JewelleryItem("Gold Ring", "Ring", "Female", "", 500.0));

        // smart-add another Ring, should land in A1 because A1 has a Ring already
        SearchResult placement = store.smartAdd(
                new JewelleryItem("Silver Ring", "Ring", "Male", "", 300.0));

        assertNotNull(placement);
        assertEquals("A1", placement.getDisplayTray().getTrayID());
    }
}