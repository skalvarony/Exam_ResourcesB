package org.vaadin.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class ObservationsService {

    /* ------------------------------------------------------------------------------ */
    /* ----------- Inicializacion de Variables -------------------------------------- */
    /* ------------------------------------------------------------------------------ */

    private static final String BACKEND_URL = "http://localhost:8080";
    private HttpClient client = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    /* ------------------------------------------------------------------------------ */
    /* ------- EXPORT CSV ----------------------------------------------------------- */
    /* ------------------------------------------------------------------------------ */

    public void exportCSV_POST() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/exportCSV"))
                .POST(HttpRequest.BodyPublishers.noBody()) // Utilizando POST sin cuerpo
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /* ------------------------------------------------------------------------------ */
    /* ----------------- Leer todas las Observaciones (Observation) ----------------- */
    /* ------------------------------------------------------------------------------ */

    public List<Observation2> fetchObservations() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/loadgrid"))
                .GET()
                .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
        Type listType = new TypeToken<List<Observation2>>() {}.getType();
        List<Observation2> observations = new Gson().fromJson(response.body(), listType);
    
        return observations;
    }

    /* ----------------------------------------------------------------------------- */
    /* ------------------------ Crear una nueva Observacion ------------------------ */
    /* ----------------------------------------------------------------------------- */

    public void createObservation(Observation2 observation) throws IOException, InterruptedException {
        String json = gson.toJson(observation);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/observations/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    /* ---------------------------------------------------------------------------------------------------------------- */
}
