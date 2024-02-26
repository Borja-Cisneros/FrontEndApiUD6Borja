package ad.frontendapiud6borja;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class controladorInicial {
    @FXML
    private VBox contenido;

    @FXML
    private Menu ver;

    public void initialize() {
        verAmbasTablas(null);
    }

    @FXML
    void juegosMasJugados(ActionEvent event) {
        cargarContenido("vistaMasJugados.fxml");

    }

    @FXML
    void puntosPorJuego(ActionEvent event) {
        cargarContenido("puntosDeUnJuego.fxml");
    }

    @FXML
    void verAmbasTablas(ActionEvent event) {
        cargarContenido("vistaJuegos.fxml");
    }

    private void cargarContenido(String vista) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(juegosAPP.class.getResource(vista));
            contenido.getChildren().clear();
            contenido.getChildren().add(fxmlLoader.load());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
