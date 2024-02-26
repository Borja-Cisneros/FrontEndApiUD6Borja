package ad.frontendapiud6borja;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class controladorPuntosDeUnJuego {
    @FXML
    private ChoiceBox<String> cb;

    @FXML
    private TableColumn<Map, String> jJuego;

    @FXML
    private TableColumn<Map, String> jNombre;

    @FXML
    private TableColumn<Map, String> jPuntuacion;

    @FXML
    private TableView<Map<String, Object>> juegos;

    private final String BASE_URL = "http://localhost:8080/api/";
    private final String URL_JUEGOS = BASE_URL + "juego";
    private final String URL_PUNTUACIONES = BASE_URL + "puntuacion";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Long> juegosMap = new HashMap<>();

    @FXML
    public void initialize() {
        cargarJuegos();
        cb.setOnAction(event -> mostrarPuntuacionesPorJuego(juegosMap.get(cb.getValue())));
    }

    private void cargarJuegos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL_JUEGOS))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode juegosNode = objectMapper.readTree(response.body());
                ObservableList<String> nombresJuegos = FXCollections.observableArrayList();

                for (JsonNode juegoNode : juegosNode) {
                    String nombre = juegoNode.get("nombre").asText();
                    Long id = juegoNode.get("id").asLong();
                    nombresJuegos.add(nombre);
                    juegosMap.put(nombre, id);
                }

                cb.setItems(nombresJuegos);
            } else {
                System.out.println("Error al cargar juegos - Código de respuesta: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void mostrarPuntuacionesPorJuego(Long juegoId) {
        try {
            String urlPuntuacionesPorJuego = URL_PUNTUACIONES + "/juego/" + juegoId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlPuntuacionesPorJuego))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObservableList<Map<String, Object>> listaPuntuaciones = FXCollections.observableArrayList();

                JsonNode puntuacionesNode = objectMapper.readTree(response.body());
                Iterator<JsonNode> iterator = puntuacionesNode.elements();

                while (iterator.hasNext()) {
                    JsonNode puntuacionNode = iterator.next();
                    Map<String, Object> puntuacionMap = objectMapper.convertValue(puntuacionNode, Map.class);
                    listaPuntuaciones.add(puntuacionMap);
                }

                // Configurar las celdas de la tabla
                jPuntuacion.setCellValueFactory(new MapValueFactory<>("puntuacion"));
                jNombre.setCellValueFactory(new MapValueFactory<>("jugador"));
                jJuego.setCellValueFactory(cellData -> {
                    Object juegoObject = cellData.getValue().get("juego");
                    if (juegoObject instanceof Map) {
                        Object idJuego = ((Map<?, ?>) juegoObject).get("nombre");
                        if (idJuego != null) {
                            return new SimpleStringProperty(String.valueOf(idJuego));
                        }
                    }
                    return new SimpleStringProperty("");
                });

                // Llenar la tabla de puntuaciones con los datos
                juegos.setItems(listaPuntuaciones);
            } else {
                System.out.println("Error al cargar puntuaciones - Código de respuesta: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
