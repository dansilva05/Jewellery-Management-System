package ie.setu.jewelleryca.pages;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Welcome extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ie/setu/jewelleryca/welcome-view.fxml")
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
