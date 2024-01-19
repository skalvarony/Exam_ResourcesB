package org.vaadin.example;

import java.io.IOException;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    private TextField msCodeField;
    private TextField yearField;
    private TextField estCodeField;
    private NumberField estimateField;
    private NumberField seField;
    private NumberField lowerCIBField;
    private NumberField upperCIBField;
    private TextField flagField;

    private Grid<Observation2> observationGrid;
    private Button button;
    private Button button2;

    public MainView() {
        observationGrid = new Grid<>(Observation2.class);
        button = new Button("Añadir Observacion");
        button2 = new Button("Exportar a CSV");

        add(observationGrid, button, button2);

        // Opcional: Si también deseas centrar los componentes verticalmente en el layout
        setAlignItems(Alignment.CENTER);
        refreshGrid();


        /* -------- Aádir nueva observación -------- */

        button.addClickListener(event -> {
            setupNewDialog(); // Llama a la función para preparar y mostrar el Dialog
        });

        // Exportar CSV 
        button2.addClickListener(event -> {
            ExportCSV_POST();
        });

        // Descargar CSV
        DownloadViewCSV();
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /* ------- Todas las Observaciones en Grid  --------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------- */

    private void refreshGrid() {
        ObservationsService observationService = new ObservationsService();
        try {
            observationGrid.setItems(observationService.fetchObservations());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /* ---------- Dialog Nueva Observacion -------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------- */

    public void setupNewDialog() {
        // Crear un nuevo diálogo
        Dialog dialog = new Dialog();
    
        // Usar FormLayout para los campos de entrada
        FormLayout formLayout = new FormLayout();
    
        // Crear campos de formulario
        msCodeField = new TextField("MS Code");
        yearField = new TextField("Year");
        estCodeField = new TextField("Est Code");
        estimateField = new NumberField("Estimate");
        seField = new NumberField("SE");
        lowerCIBField = new NumberField("Lower CIB");
        upperCIBField = new NumberField("Upper CIB");
        flagField = new TextField("Flag");
    
        // Añadir campos al formulario
        formLayout.add(msCodeField, yearField, estCodeField, estimateField, seField, lowerCIBField, upperCIBField, flagField);
    
        // Botones
        Button saveButton = new Button("Guardar");
        Button cancelButton = new Button("Cancelar");
    
        /* ----------- Botón Guardar ----------- */
        saveButton.addClickListener(event -> { 
            // Crear una nueva instancia de Observation2 con los datos del formulario
            Observation2 observationToUpdate = new Observation2();
            //inicializamos
            observationToUpdate.setMscode(msCodeField.getValue());
            observationToUpdate.setYear(yearField.getValue());
            observationToUpdate.setEstCode(estCodeField.getValue());
            observationToUpdate.setEstimate(estimateField.getValue() != null ? estimateField.getValue().floatValue() : null);
            observationToUpdate.setSe(seField.getValue() != null ? seField.getValue().floatValue() : null);
            observationToUpdate.setLowerCIB(lowerCIBField.getValue() != null ? lowerCIBField.getValue().floatValue() : null);
            observationToUpdate.setUpperCIB(upperCIBField.getValue() != null ? upperCIBField.getValue().floatValue() : null);
            observationToUpdate.setFlag(flagField.getValue());

            // Enviar los datos al backend
            ObservationsService observationService = new ObservationsService();
            try {
                observationService.createObservation(observationToUpdate);
                refreshGrid();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            dialog.close();

         });
        
        /* ----------- Botón Cancelar ----------- */
        cancelButton.addClickListener(event -> dialog.close());
    
        // Añadir formLayout y botones al diálogo
        dialog.add(formLayout, saveButton, cancelButton);
    
        // Abrir el diálogo
        dialog.open();
    }
    
    /* ---------------------------------------------------------------------------------------------------------------- */
    /* ------ Exportar a CSV ------------------------------------------------------------------------------------------ */
    /* ---------------------------------------------------------------------------------------------------------------- */

    private void ExportCSV_POST() {
        ObservationsService observationService = new ObservationsService();
        try {
            observationService.exportCSV_POST();
            Notification.show("Exportacion CSV Completada POST", 3000, Notification.Position.BOTTOM_START);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Notification.show("Error en la Exportacion CSV POST", 3000, Notification.Position.BOTTOM_START);
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------- */
    /* ------ Downloads ----------------------------------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------------------------- */

    public void DownloadViewCSV() {
        Button downloadButton = new Button("Descargar CSV");
    
        // Agregar un listener al botón para abrir la URL de descarga
        downloadButton.addClickListener(event -> 
            UI.getCurrent().getPage().executeJs("window.location.href = $0", "http://localhost:8080/download-csv")
        );
    
        // Añadir el botón a la vista
        add(downloadButton);
    }

}



