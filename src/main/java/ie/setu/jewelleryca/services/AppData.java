package ie.setu.jewelleryca.services;

import ie.setu.jewelleryca.models.JewelleryStore;

public class AppData {

    private static final String SAVE_FILE = "jewellery-store.dat";
    private static JewelleryStore store = new JewelleryStore();  // the one store used everywhere

    private AppData() {}  // shouldn't be instantiated

    public static JewelleryStore getStore() {
        return store;
    }

    // replaces the store after loading from file
    public static void setStore(JewelleryStore newStore) {
        store = newStore;
    }

    // creates a fresh empty store for the reset
    public static void resetStore() {
        store = new JewelleryStore();
    }

    public static String getSaveFile() {
        return SAVE_FILE;
    }
}
