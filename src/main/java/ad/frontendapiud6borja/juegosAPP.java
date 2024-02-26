package ad.frontendapiud6borja;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class juegosAPP extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(juegosAPP.class.getResource("inicial.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 730, 500);
        stage.setTitle("Juegos!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}