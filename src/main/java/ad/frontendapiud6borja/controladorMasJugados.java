package ad.frontendapiud6borja;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static ad.frontendapiud6borja.controladorJuegos.BASE_URL;

public class controladorMasJugados {
    @FXML
    private TableColumn<Map, String> jmjJugadores;

    @FXML
    private TableColumn<Map, String> jmjNombre;

    @FXML
    private TableView<Map<String, Object>> tabla;

    public void initialize() {
        jmjNombre.setCellValueFactory(new MapValueFactory<>("nombre"));
        jmjJugadores.setCellValueFactory(new MapValueFactory<>("numJugadores"));
        llenarTabla();
    }

    private void llenarTabla() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/juego/masjugados"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::procesarRespuesta)
                .join();
    }

    private void procesarRespuesta(String respuesta) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convertir la respuesta JSON a una lista de mapas
            ObservableList<Map<String, Object>> listaJuegos = FXCollections.observableArrayList(
                    objectMapper.readValue(respuesta, Map[].class)
            );

            // Llenar la tabla de juegos con los datos
            tabla.setItems(listaJuegos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
