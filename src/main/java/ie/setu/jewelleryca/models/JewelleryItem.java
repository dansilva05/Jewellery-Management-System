package ie.setu.jewelleryca.models;

import ie.setu.jewelleryca.core.LinkedList;
import java.io.Serializable;

public class JewelleryItem implements Serializable {

    private String description;
    private String type;
    private String targetGender;
    private String imageUrl;
    private double retailPrice;
    private LinkedList<MaterialComponent> materials;

    public JewelleryItem(String description, String type, String targetGender,
                         String imageUrl, double retailPrice) {
        this.description = description;
        this.type = type;
        this.targetGender = targetGender;
        this.imageUrl = imageUrl;
        this.retailPrice = retailPrice;
        materials = new LinkedList<>();
    }

    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getTargetGender() { return targetGender; }
    public String getImageUrl() { return imageUrl; }
    public double getRetailPrice() { return retailPrice; }
    public LinkedList<MaterialComponent> getMaterials() { return materials; }

    // adds a material to this item
    public void addMaterial(MaterialComponent m) {
        materials.add(m);
    }

    // checks description, type, gender, price and all materials for what is being searched
    public boolean matchesSearch(String term) {
        String lower = term.toLowerCase();
        if (description.toLowerCase().contains(lower)
                || type.toLowerCase().contains(lower)
                || targetGender.toLowerCase().contains(lower)
                || String.valueOf(retailPrice).contains(lower)) {
            return true;
        }
        for (int i = 0; i < materials.size(); i++) {  // checks each material too
            if (materials.get(i).matchesSearch(term)) {
                return true;
            }
        }
        return false;
    }

    // builds the materials list as text
    private String materialsText() {
        if (materials.isEmpty()) {
            return "  No materials added yet.";
        }
        String result = "";
        for (int i = 0; i < materials.size(); i++) {
            result += "  " + (i + 1) + ". " + materials.get(i).getFullDetails() + "\n";
        }
        return result.trim();
    }

    // full item details for the item details panel
    public String getFullDetails() {
        return "Description: " + description + "\n"
                + "Type: " + type + "\n"
                + "Target Gender: " + targetGender + "\n"
                + "Image URL: " + imageUrl + "\n"
                + "Retail Price: €" + String.format("%.2f", retailPrice) + "\n"
                + "\n--- Materials / Components ---\n"
                + materialsText();
    }

    // short label for combo boxes
    @Override
    public String toString() {
        return description + " [" + type + "] - €" + String.format("%.2f", retailPrice);
    }
}
