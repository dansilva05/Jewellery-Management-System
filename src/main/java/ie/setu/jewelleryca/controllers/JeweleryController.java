package ie.setu.jewelleryca.controllers;

import ie.setu.jewelleryca.core.LinkedList;
import ie.setu.jewelleryca.models.DisplayCase;
import ie.setu.jewelleryca.models.DisplayTray;
import ie.setu.jewelleryca.models.JewelleryItem;
import ie.setu.jewelleryca.models.JewelleryStore;
import ie.setu.jewelleryca.models.MaterialComponent;
import ie.setu.jewelleryca.models.SearchResult;
import ie.setu.jewelleryca.services.AppData;
import ie.setu.jewelleryca.services.StoreFileManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JeweleryController {

    @FXML
    private TextField addCaseID;
    @FXML
    private ChoiceBox<String> choiceCaseLighting;
    @FXML
    private ChoiceBox<String> choiceCaseType;

    @FXML
    private ChoiceBox<String> displayCaseTray;
    @FXML
    private TextField addTrayID;
    @FXML
    private ColorPicker pickTrayColour;
    @FXML
    private TextField addTrayDepth;
    @FXML
    private TextField addTrayWidth;

    @FXML
    private ChoiceBox<String> displayCaseItem;
    @FXML
    private ChoiceBox<String> displayTrayItem;
    @FXML
    private ChoiceBox<String> choiceTarget;
    @FXML
    private TextField addItemDesc;
    @FXML
    private ChoiceBox<String> choiceItemType;
    @FXML
    private TextField addItemPrice;
    @FXML
    private TextField addItemURL;

    @FXML
    private ChoiceBox<String> displayCaseMaterial;
    @FXML
    private ChoiceBox<String> displayTrayMaterial;
    @FXML
    private ChoiceBox<String> itemMaterial;
    @FXML
    private TextField addMaterials;
    @FXML
    private TextField addMaterialDesc;
    @FXML
    private TextField addWeight;
    @FXML
    private TextField addQuality;

    @FXML
    private ChoiceBox<String> displayCaseRemove;
    @FXML
    private ChoiceBox<String> displayTrayRemove;
    @FXML
    private ChoiceBox<String> itemRemove;

    @FXML
    private ComboBox<String> choiceDisplayCase;
    @FXML
    private ComboBox<String> choiceDisplayTray;
    @FXML
    private ComboBox<String> choiceItem;
    @FXML
    private ImageView itemImage;
    @FXML
    private TextArea itemDetails;
    @FXML
    private TextArea allStockDetails;
    @FXML
    private TextField searchBar;
    @FXML
    private ListView<String> searchResults;

    @FXML
    private Label messages;
    @FXML
    private TextArea reportArea;

    private LinkedList<SearchResult> lastSearchResults = new LinkedList<>();  // keeps the last search so clicking a result works

    @FXML
    public void initialize() {
        choiceCaseLighting.getItems().addAll("Lit", "Unlit");
        choiceCaseType.getItems().addAll("Freestanding", "Wall-mounted");
        choiceTarget.getItems().addAll("Female", "Male", "Unisex");
        choiceItemType.getItems().addAll("accessories", "anklets", "body jewellery", "bracelets", "brooches/pins", "earrings", "head/hair jewellery", "necklaces", "rings");

        searchResults.setOnMouseClicked(event -> searchResultClicked());

        fillAllChoiceBoxes();
        allStockDetails.setText(AppData.getStore().getAllStockReport());
        itemDetails.setText("Select a case, tray, and item to see details here.");
        reportArea.setText("Use the buttons above to generate reports, save, load, or reset.");
    }


    // Called when the user picks a case in the Add Item section. Fills the trays ChoiceBox.
    @FXML
    private void caseItemChanged() {
        fillTraysForCase(displayCaseItem, displayTrayItem);
    }

    // Called when the user picks a case in the Add Material section. Fills the trays ChoiceBox.
    @FXML
    private void caseMaterialChanged() {
        fillTraysForCase(displayCaseMaterial, displayTrayMaterial);
        fillItemsForMaterial();
    }

    // Called when the user picks a tray in the Add Material section. Fills the Items ChoiceBox.
    @FXML
    private void trayMaterialChanged() {
        fillItemsForMaterial();
    }

    // Called when the user picks a case in the Remove section. Fills the trays ChoiceBox.
    @FXML
    private void caseRemoveChanged() {
        fillTraysForCase(displayCaseRemove, displayTrayRemove);
        fillItemsForRemove();
    }

    // Called when the user picks a tray in the Remove section. Fills the items ChoiceBox.
    @FXML
    private void trayRemoveChanged() {
        fillItemsForRemove();
    }

    // Adds Display Case
    @FXML
    private void addDisplayCase() {
        String caseId = addCaseID.getText().trim();
        String type = choiceCaseType.getValue();
        String lighting = choiceCaseLighting.getValue();

        // Checks if all the fields are filled
        if (caseId.isEmpty() || type == null || lighting == null) {
            reportMessage("Please fill in Case ID, Type, and Lighting before adding a case.");
            return;
        }

        // Tries to add the display case
        boolean added = AppData.getStore().addDisplayCase(new DisplayCase(caseId, type, lighting));
        if (added) { // it worked, so I clear the fields for a new case to be added
            addCaseID.clear();
            choiceCaseType.getSelectionModel().clearSelection();
            choiceCaseLighting.getSelectionModel().clearSelection();
            fillAllChoiceBoxes();
            refreshAllStock();
            reportMessage("Display case '" + caseId + "' added successfully.");
        } else { // it didn't work
            reportMessage("A display case with ID '" + caseId + "' already exists. Choose a different ID.");
        }
    }

    // Adds a Display Tray to a case
    @FXML
    private void addDisplayTray() {
        String caseId = displayCaseTray.getValue();
        String trayId = addTrayID.getText().trim();
        String colour = pickTrayColour.getValue().toString();
        String widthTxt = addTrayWidth.getText().trim();
        String depthTxt = addTrayDepth.getText().trim();

        // checks if all the fields were filled
        if (caseId == null || trayId.isEmpty() || colour.isEmpty()
                || widthTxt.isEmpty() || depthTxt.isEmpty()) {
            reportMessage("Please fill in all tray fields before adding.");
            return;
        }

        // tries to parse the number required fields
        double width, depth;
        try {
            width = Double.parseDouble(widthTxt);
            depth = Double.parseDouble(depthTxt);
        } catch (NumberFormatException e) { // it didn't work. They didn't pass only numbers.
            reportMessage("Width and Depth must be numbers (e.g. 30.0).");
            return;
        }

        // Tries to add the tray to the linked list
        boolean added = AppData.getStore().addTrayToCase(caseId, new DisplayTray(trayId, colour, width, depth));
        if (added) { // it worked, so I clear all the fields to add a new tray.
            addTrayID.clear();
            pickTrayColour.setValue(null);
            addTrayWidth.clear();
            addTrayDepth.clear();
            fillAllChoiceBoxes();
            refreshAllStock();
            reportMessage("Tray '" + trayId + "' added to case '" + caseId + "' successfully.");
        } else { // it didn't work
            reportMessage("Could not add tray. A tray with ID '" + trayId + "' already exists in the system.");
        }
    }

    // adds item
    @FXML
    private void addItem() {
        String caseId = displayCaseItem.getValue();
        String trayId = displayTrayItem.getValue();
        String desc = addItemDesc.getText().trim();
        String type = choiceItemType.getValue();
        String gender = choiceTarget.getValue();
        String url = addItemURL.getText().trim();
        String priceStr = addItemPrice.getText().trim();

        // checks if all the fields were filled
        if (caseId == null || trayId == null || desc.isEmpty()
                || type.isEmpty() || gender == null || priceStr.isEmpty()) {
            reportMessage("Please fill in all item fields and select a case and tray.");
            return;
        }

        // tries to parse number required fields
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) { // it didn't work
            reportMessage("Retail Price must be a number (e.g. 199.99).");
            return;
        }

        // tries to add item to a tray in a case
        boolean added = AppData.getStore().addItemToTray(caseId, trayId,
                new JewelleryItem(desc, type, gender, url, price));
        if (added) { //it worked, so I clear the fields to add new items
            addItemDesc.clear();
            choiceItemType.getSelectionModel().clearSelection();
            addItemURL.clear();
            addItemPrice.clear();
            choiceTarget.getSelectionModel().clearSelection();
            fillAllChoiceBoxes();
            refreshAllStock();
            reportMessage("Item '" + desc + "' added to tray '" + trayId + "' in case '" + caseId + "'.");
        } else { // it didn't work
            reportMessage("Could not add item. Make sure you have selected a valid case and tray.");
        }
    }

    // smart add item for user
    @FXML
    private void smartAdd() {
        String desc = addItemDesc.getText().trim();
        String type = choiceItemType.getValue();
        String gender = choiceTarget.getValue();
        String url = addItemURL.getText().trim();
        String priceStr = addItemPrice.getText().trim();

        // checks if all the fields were filled
        if (desc.isEmpty() || type.isEmpty() || gender == null || priceStr.isEmpty()) {
            reportMessage("Please fill in all item fields for smart add.");
            return;
        }

        // tries to parse number required fields
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) { // it didn't work
            reportMessage("Retail Price must be a number (e.g. 199.99).");
            return;
        }

        // calls smart add in JewelleryStore model.
        SearchResult placement = AppData.getStore().smartAdd(new JewelleryItem(desc, type, gender, url, price));
        if (placement != null) { // it worked, so I clear the fields to add a new item
            addItemDesc.clear();
            choiceItemType.getSelectionModel().clearSelection();
            addItemURL.clear();
            addItemPrice.clear();
            choiceTarget.getSelectionModel().clearSelection();
            fillAllChoiceBoxes();
            refreshAllStock();
            reportMessage("Item '" + desc + "' smart-added to tray '"
                    + placement.getDisplayTray().getTrayID() + "' in case '"
                    + placement.getDisplayCase().getCaseID() + "'.");
        } else { // it didn't work. There are no trays in the system.
            reportMessage("Could not smart-add item. There are no trays in the system yet.");
        }
    }

    // adds material to item
    @FXML
    private void addItemMaterial() {
        String caseId = displayCaseMaterial.getValue();
        String trayId = displayTrayMaterial.getValue();
        int itemIndex = itemMaterial.getSelectionModel().getSelectedIndex();
        String name = addMaterials.getText().trim();
        String desc = addMaterialDesc.getText().trim();
        String weightStr = addWeight.getText().trim();
        String qualityStr = addQuality.getText().trim();

        // checks if all the fields are filled
        if (caseId == null || trayId == null || itemIndex < 0
                || name.isEmpty() || desc.isEmpty() || weightStr.isEmpty() || qualityStr.isEmpty()) {
            reportMessage("Please select a case, tray, and item, then fill in all material fields.");
            return;
        }

        // tries to parse
        double weight, quality;
        try {
            weight = Double.parseDouble(weightStr);
            quality = Double.parseDouble(qualityStr);
        } catch (NumberFormatException e) {
            reportMessage("Weight and Quality must be numbers.");
            return;
        }

        // tries to add material
        boolean added = AppData.getStore().addMaterialToItem(caseId, trayId, itemIndex,
                new MaterialComponent(name, desc, weight, quality));
        if (added) {
            addMaterials.clear();
            addMaterialDesc.clear();
            addWeight.clear();
            addQuality.clear();
            refreshAllStock();
            reportMessage("Material '" + name + "' added to the selected item.");
        } else {
            reportMessage("Could not add material. Make sure you have selected a valid item.");
        }
    }

    // removes item
    @FXML
    private void removeItem() {
        String caseId = displayCaseRemove.getValue();
        String trayId = displayTrayRemove.getValue();
        int itemIndex = itemRemove.getSelectionModel().getSelectedIndex();

        // checks if all fields were filled
        if (caseId == null || trayId == null || itemIndex < 0) {
            reportMessage("Please select a case, tray, and item to remove.");
            return;
        }

        // grab the name before removing so we can show it in the message
        JewelleryItem toRemove = AppData.getStore().findItem(caseId, trayId, itemIndex);
        String name = (toRemove != null) ? toRemove.getDescription() : "unknown";

        // tries to remove item
        boolean removed = AppData.getStore().removeItem(caseId, trayId, itemIndex);
        if (removed) {
            fillAllChoiceBoxes();
            refreshAllStock();
            itemDetails.setText("Item removed. Select another item to view details.");
            itemImage.setImage(null);
            reportMessage("Item '" + name + "' removed from tray '" + trayId + "'.");
        } else {
            reportMessage("Could not remove item. Please re-select and try again.");
        }
    }


    // called when the user picks a case in the browse combo box
    @FXML
    private void searchDisplayCase() {
        String caseId = choiceDisplayCase.getValue();
        choiceDisplayTray.getItems().clear();
        choiceItem.getItems().clear();
        itemDetails.setText("Select a tray, then an item to see details.");
        itemImage.setImage(null);

        DisplayCase dc = AppData.getStore().findCase(caseId);

        // gets the trays of the case selected
        for (int i = 0; i < dc.getTrays().size(); i++) {
            choiceDisplayTray.getItems().add(dc.getTrays().get(i).getTrayID());
        }
        // fills the tray area with the first tray of this case
        if (!choiceDisplayTray.getItems().isEmpty()) {
            choiceDisplayTray.getSelectionModel().selectFirst();
        }
    }

    // called when the user picks a tray in the browse combo box
    @FXML
    private void searchDisplayTray() {
        String caseId = choiceDisplayCase.getValue();
        String trayId = choiceDisplayTray.getValue();
        choiceItem.getItems().clear();
        itemDetails.setText("Select an item to see its full details.");
        itemImage.setImage(null);

        DisplayTray tray = AppData.getStore().findTray(caseId, trayId);

        // gets all the items in that tray
        for (int i = 0; i < tray.getItems().size(); i++) {
            choiceItem.getItems().add(tray.getItems().get(i).toString());
        }
        // fills item area with the first item in the tray
        if (!choiceItem.getItems().isEmpty()) {
            choiceItem.getSelectionModel().selectFirst();
        }
    }

    // called when the user picks an item in the browse combo box
    @FXML
    private void searchItem() {
        String caseId = choiceDisplayCase.getValue();
        String trayId = choiceDisplayTray.getValue();
        int itemIndex = choiceItem.getSelectionModel().getSelectedIndex();

        JewelleryItem item = AppData.getStore().findItem(caseId, trayId, itemIndex);

        // shows full details and image
        itemDetails.setText(item.getFullDetails());
        showImage(item.getImageUrl());
    }

    // view all stock
    @FXML
    private void viewAllStock() {
        refreshAllStock();
    }

    // search bar
    @FXML
    private void search() {
        // gets what is in search bar
        String term = searchBar.getText().trim();
        if (term.isEmpty()) { // if it's empty
            reportMessage("Please type something in the search box before searching.");
            return;
        }

        // remembers last result
        lastSearchResults = AppData.getStore().search(term);
        searchResults.getItems().clear();

        if (lastSearchResults.isEmpty()) {
            itemDetails.setText("No items found matching '" + term + "'.");
            itemImage.setImage(null);
        } else {
            for (int i = 0; i < lastSearchResults.size(); i++) {
                searchResults.getItems().add(lastSearchResults.get(i).toString());
            }
            itemDetails.setText("Click a result in the list to see full details.");
        }
    }

    // runs when the user clicks something in the search results list (GPT)
    private void searchResultClicked() {
        // gets the index of what was clicked in the list
        int selectedIndex = searchResults.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= lastSearchResults.size()) return; // checks if the index is reasonable

        SearchResult result = lastSearchResults.get(selectedIndex);
        // prints selected item to item details field
        itemDetails.setText(
                "Found in: Case " + result.getDisplayCase().getCaseID()
                        + ", Tray " + result.getDisplayTray().getTrayID()
                        + "\n\n"
                        + result.getItem().getFullDetails()
        );
        showImage(result.getItem().getImageUrl());
    }

    // stock report
    @FXML
    private void stockReport() {
        reportArea.setText(AppData.getStore().getValueReport());
    }

    // save store
    @FXML
    private void saveStore() {
        try {
            StoreFileManager.saveStore(AppData.getStore(), AppData.getSaveFile());
            reportArea.setText("Store saved successfully to: " + AppData.getSaveFile());
        } catch (Exception e) {
            reportArea.setText("Save failed: " + e.getMessage());
        }
    }

    // load store
    @FXML
    private void loadStore() {
        try {
            AppData.setStore(StoreFileManager.loadStore(AppData.getSaveFile()));
            fillAllChoiceBoxes();
            refreshAllStock();
            reportArea.setText("Store loaded successfully from: " + AppData.getSaveFile());
        } catch (Exception e) {
            reportArea.setText("Load failed. Have you saved the store at least once?\nError: " + e.getMessage());
        }
    }

    // reset system
    @FXML
    private void resetSystem() {
        // clears everything
        AppData.resetStore();
        lastSearchResults = new LinkedList<>();
        searchResults.getItems().clear();
        fillAllChoiceBoxes();
        refreshAllStock();
        itemDetails.setText("System reset. All data cleared.");
        itemImage.setImage(null);
        reportArea.setText("All data has been cleared.");
    }

    // fills a case ChoiceBox with all the current case IDs
    private void fillCaseChoiceBox(ChoiceBox<String> box) {
        box.getItems().clear();
        JewelleryStore store = AppData.getStore();

        // goes through the linked list
        for (int i = 0; i < store.getDisplayCases().size(); i++) {
            // fills choice boxes
            box.getItems().add(store.getDisplayCases().get(i).getCaseID());
        }
        if (!box.getItems().isEmpty()) {
            box.getSelectionModel().selectFirst();
        }
    }

    // fills a tray ChoiceBox based on which case is selected in caseBox
    private void fillTraysForCase(ChoiceBox<String> caseBox, ChoiceBox<String> trayBox) {
        trayBox.getItems().clear();
        // gets Display Case
        String caseId = caseBox.getValue();

        DisplayCase dc = AppData.getStore().findCase(caseId);

        // goes through the trays in the display case
        for (int i = 0; i < dc.getTrays().size(); i++) {
            // fills with the trays
            trayBox.getItems().add(dc.getTrays().get(i).getTrayID());
        }
        if (!trayBox.getItems().isEmpty()) {
            trayBox.getSelectionModel().selectFirst();
        }
    }

    // fills the item ChoiceBox in the Add Material section
    private void fillItemsForMaterial() {
        itemMaterial.getItems().clear();
        String caseId = displayCaseMaterial.getValue();
        String trayId = displayTrayMaterial.getValue();

        DisplayTray tray = AppData.getStore().findTray(caseId, trayId);

        // goes through the items in the tray
        for (int i = 0; i < tray.getItems().size(); i++) {
            // fills with the items
            itemMaterial.getItems().add(tray.getItems().get(i).toString());
        }
        if (!itemMaterial.getItems().isEmpty()) {
            itemMaterial.getSelectionModel().selectFirst();
        }
    }

    // fills the item ChoiceBox in the Remove section
    private void fillItemsForRemove() {
        itemRemove.getItems().clear();
        String caseId = displayCaseRemove.getValue();
        String trayId = displayTrayRemove.getValue();

        DisplayTray tray = AppData.getStore().findTray(caseId, trayId);

        // goes through the items in the tray
        for (int i = 0; i < tray.getItems().size(); i++) {
            // fills with the items
            itemRemove.getItems().add(tray.getItems().get(i).toString());
        }
        if (!itemRemove.getItems().isEmpty()) {
            itemRemove.getSelectionModel().selectFirst();
        }
    }

    // re-fills all ChoiceBoxes with the current data in the system
    private void fillAllChoiceBoxes() {
        fillCaseChoiceBox(displayCaseTray);
        fillCaseChoiceBox(displayCaseItem);
        fillCaseChoiceBox(displayCaseMaterial);
        fillCaseChoiceBox(displayCaseRemove);

        fillTraysForCase(displayCaseItem, displayTrayItem);
        fillTraysForCase(displayCaseMaterial, displayTrayMaterial);
        fillTraysForCase(displayCaseRemove, displayTrayRemove);

        fillItemsForMaterial();
        fillItemsForRemove();


        choiceDisplayCase.getItems().clear();
        JewelleryStore store = AppData.getStore();
        for (int i = 0; i < store.getDisplayCases().size(); i++) {
            choiceDisplayCase.getItems().add(store.getDisplayCases().get(i).getCaseID());
        }
        choiceDisplayTray.getItems().clear();
        choiceItem.getItems().clear();
    }

    // updates all the stock text area
    private void refreshAllStock() {
        allStockDetails.setText(AppData.getStore().getAllStockReport());
    }

    // loads and shows the image from the URL, clears it if there's no URL, or it breaks
    private void showImage(String url) {
        if (url == null || url.isBlank()) {
            itemImage.setImage(null);
            return;
        }
        try {
            itemImage.setImage(new Image(url, true));
        } catch (Exception e) {
            itemImage.setImage(null);
        }
    }

    //prints a message to the top, below title
    private void reportMessage(String message) {
        messages.setText(message);
    }
}
