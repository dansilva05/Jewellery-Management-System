package ie.setu.jewelleryca.models;

import java.io.Serializable;

public class MaterialComponent implements Serializable {

    private String name;
    private String description;
    private double quantity;  // in grams
    private double quality;

    public MaterialComponent(String name, String description, double quantity, double quality) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.quality = quality;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getQuantity() { return quantity; }
    public double getQuality() { return quality; }

    // checks if the search term matches anything in this material
    public boolean matchesSearch(String term) {
        String lower = term.toLowerCase();
        return name.toLowerCase().contains(lower)
                || description.toLowerCase().contains(lower)
                || String.valueOf(quantity).contains(lower)
                || String.valueOf(quality).contains(lower);
    }

    // short label for combo boxes
    @Override
    public String toString() {
        return name + " (Qty: " + quantity + "g, Quality: " + quality + ")";
    }

    // full info for display
    public String getFullDetails() {
        return "Material: " + name + "\n"
                + "Description: " + description + "\n"
                + "Quantity: " + quantity + "g\n"
                + "Quality: " + quality;
    }
}
