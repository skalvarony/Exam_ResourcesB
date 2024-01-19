package es.ufv.dis.mock.AFN;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ObservationController {

    /* ---------------------------------------------------------------------------------------------------------------- */
    /* ----------- DECLARACION VARIABLES ------------------------------------------------------------------------------ */
    /* ---------------------------------------------------------------------------------------------------------------- */

    private static String projectRoot = System.getProperty("user.dir");
    private static final String JSON_FILE_PATH2 = projectRoot + "/AFN/Datos/Original.json";
    private static final String CSV_FILE_PATH2 = projectRoot + "/AFN/Datos/Export_csv.csv";
  

    /* -------------------------------------------- */
    /* ------- LEER TODAS LAS OBSERVACIONES ------- */
    /* -------------------------------------------- */

    @GetMapping("/loadgrid")
    public List<Observation2> getObservations() {
        // Inicializar una lista vacía
        List<Observation2> allObservations = new ArrayList<>();

        try {
            // Crear una instancia de Gson
            Gson gson = new Gson();

            // Abrir el archivo JSON para lectura
            FileReader reader = new FileReader(JSON_FILE_PATH2);

            // Especificar el tipo del contenido del JSON
            Type listType = new TypeToken<List<Observation2>>(){}.getType();

            // Deserializar el contenido del archivo JSON a la lista
            List<Observation2> observationsFromFile = gson.fromJson(reader, listType);

            // Cerrar el lector de archivos
            reader.close();

            // Si el archivo está vacío, se devolverá la lista inicializada (vacía)
            if (observationsFromFile != null) {
                allObservations.addAll(observationsFromFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // No es necesario devolver null aquí, ya que allObservations es una lista vacía
        }

        // Devolver la lista de observaciones (vacía o con datos)
        return allObservations;
    }


    /* -------------------------------------------- */
    /* ------- AÑADIR UNA NUEVA OBSERVACION ------- */
    /* -------------------------------------------- */

    @PostMapping("/observations/add")
    public ResponseEntity<?> addObservation(@RequestBody Observation2 observation) throws IOException {
        // Crear una instancia de Gson
        Gson gson = new Gson();

        // Inicializar una lista vacía
        List<Observation2> observations = new ArrayList<>();

        // Verificar si el archivo JSON existe
        File file = new File(JSON_FILE_PATH2);
        if (file.exists()) {
            // El archivo existe, leer las observaciones existentes
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<List<Observation2>>(){}.getType();
                List<Observation2> observationsFromFile = gson.fromJson(reader, listType);
                if (observationsFromFile != null) {
                    observations.addAll(observationsFromFile);
                }
            }
        }

        // Añadir la nueva observación a la lista
        observations.add(observation);

        // Escribir las observaciones actualizadas de nuevo en el archivo JSON
        try (FileWriter writer = new FileWriter(JSON_FILE_PATH2)) {
            gson.toJson(observations, writer);
        }

        return ResponseEntity.ok(observation);
    }



    /* ---------------------------------------------------------------------------------------------------------------- */
    /* ------- EXPORT CSV --------------------------------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------------------------- */

    @PostMapping("/exportCSV")
    public ResponseEntity<?> exportCSV_POST() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Crear una instancia de Gson
        Gson gson = new Gson();

        // Abrir el archivo JSON para lectura
        FileReader reader = new FileReader(JSON_FILE_PATH2);

        // Especificar el tipo del contenido del JSON
        Type listType = new TypeToken<List<Observation2>>(){}.getType();

        // Deserializar el contenido del archivo JSON a una List
        List<Observation2> allObservations = gson.fromJson(reader, listType);

        // Cerrar el lector de archivos
        reader.close();
        
        // Escribir las observaciones en un archivo CSV
        try (Writer writer = new FileWriter(CSV_FILE_PATH2)) {
            ColumnPositionMappingStrategy<Observation2> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Observation2.class);
            String[] columns = new String[]{"mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag", "_id"}; // Ajusta según los campos de Observation2
            strategy.setColumnMapping(columns);

            StatefulBeanToCsv<Observation2> beanToCsv = new StatefulBeanToCsvBuilder<Observation2>(writer)
                    .withMappingStrategy(strategy)
                    .build();

            beanToCsv.write(allObservations);
        }

        // Devolver una respuesta HTTP
        return ResponseEntity.ok(allObservations);

    }

}

