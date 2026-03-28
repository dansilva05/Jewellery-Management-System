package ie.setu.jewelleryca.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WelcomeController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void enterStore(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ie/setu/jewelleryca/jewelery-system.fxml")
        );
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/ie/setu/jewelleryca/stylesheet.css").toExternalForm()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Jewellery Management System");
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
