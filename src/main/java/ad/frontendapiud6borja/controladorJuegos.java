package ad.frontendapiud6borja;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
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


public class controladorJuegos {

    @FXML
    private TableColumn<Map, String> jId;

    @FXML
    private TableColumn<Map, String> jNombre;

    @FXML
    private TableView<Map<String, Object>> juegos;

    @FXML
    private TableColumn<Map, String> pId;

    @FXML
    private TableColumn<Map, String> pIdJuego;

    @FXML
    private TableColumn<Map, String> pJugador;

    @FXML
    private TableColumn<Map, String> pPuntuacion;

    @FXML
    private TableView<Map<String, Object>> puntuaciones;

    protected static final String BASE_URL = "http://localhost:8080";

    public void initialize() {
        jId.setCellValueFactory(new MapValueFactory<>("id"));
        jNombre.setCellValueFactory(new MapValueFactory<>("nombre"));
        pId.setCellValueFactory(new MapValueFactory<>("id_puntuacion"));
        pPuntuacion.setCellValueFactory(new MapValueFactory<>("puntuacion"));
        pJugador.setCellValueFactory(new MapValueFactory<>("jugador"));
        pIdJuego.setCellValueFactory(new MapValueFactory<>("juego"));
        pIdJuego.setCellValueFactory(cellData -> {
            Object juegoObject = cellData.getValue().get("juego");
            if (juegoObject instanceof Map) {
                Object idJuego = ((Map<?, ?>) juegoObject).get("nombre");
                if (idJuego != null) {
                    return new SimpleStringProperty(String.valueOf(idJuego));
                }
            }
            return new SimpleStringProperty("");
        });
        llenarTablaJuegos();
        llenarTablaPuntuaciones();
    }

    private void llenarTablaJuegos() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/juego"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::procesarRespuestaJuegos)
                .join();
    }

    private void llenarTablaPuntuaciones() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/puntuacion"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::procesarRespuestaPuntuaciones)
                .join();
    }

    private void procesarRespuestaJuegos(String respuesta) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convertir la respuesta JSON a una lista de mapas
            ObservableList<Map<String, Object>> listaJuegos = FXCollections.observableArrayList(
                    objectMapper.readValue(respuesta, Map[].class)
            );

            // Llenar la tabla de juegos con los datos
            juegos.setItems(listaJuegos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarRespuestaPuntuaciones(String respuesta) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convertir la respuesta JSON a una lista de mapas
            ObservableList<Map<String, Object>> listaPuntuaciones = FXCollections.observableArrayList(
                    objectMapper.readValue(respuesta, Map[].class)
            );


            // Llenar la tabla de puntuaciones con los datos
            puntuaciones.setItems(listaPuntuaciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
