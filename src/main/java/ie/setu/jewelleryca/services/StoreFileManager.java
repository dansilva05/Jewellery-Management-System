package ie.setu.jewelleryca.services;

import ie.setu.jewelleryca.models.JewelleryStore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// save and load using Java's ObjectOutputStream/ObjectInputStream (GPT)
public class StoreFileManager {

    private StoreFileManager() {}

    // saves the store to a file
    public static void saveStore(JewelleryStore store, String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(store);
        out.close();
    }

    // loads the store from a file
    public static JewelleryStore loadStore(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        JewelleryStore store = (JewelleryStore) in.readObject();
        in.close();
        return store;
    }
}
