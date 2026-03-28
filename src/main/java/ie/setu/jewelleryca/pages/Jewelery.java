package ie.setu.jewelleryca.pages;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Jewelery extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ie/setu/jewelleryca/jewelery-system.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(
                getClass().getResource("/ie/setu/jewelleryca/stylesheet.css").toExternalForm()
        );
        stage.setTitle("Jewellery App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
