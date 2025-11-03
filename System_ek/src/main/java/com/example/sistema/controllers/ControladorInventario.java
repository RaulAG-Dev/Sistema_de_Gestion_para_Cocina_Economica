package com.example.sistema.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista para la gestión y visualización del inventario de ingredientes.
 *
 * <p>Esta clase maneja la interfaz de usuario que muestra el stock de ingredientes,
 * permite la navegación al menú principal y podría mostrar alertas de stock mínimo.</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-01
 */
public class ControladorInventario implements Initializable {

    // --- Componentes FXML ---

    /**
     * Botón para regresar a la vista principal del sistema.
     */
    @FXML private Button regresarButton;
    /**
     * Tabla donde se visualizará la lista de ingredientes con su stock actual y mínimo.
     * <p>El tipo genérico {@code <?>} debe ser reemplazado por la clase Ingrediente.</p>
     */
    @FXML private TableView<?> inventarioTable;
    /**
     * Área de texto utilizada para mostrar notificaciones o alertas de stock mínimo.
     */
    @FXML private TextArea notificacionesArea;


    /**
     * Método de inicialización llamado automáticamente después de que se carga el archivo FXML.
     * <p>Se utiliza para configurar la tabla de inventario y cargar los datos iniciales (stock de ingredientes).</p>
     *
     * @param url La ubicación relativa del objeto raíz.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lógica: Configurar columnas, cargar datos de inventario.
    }

    /**
     * Maneja el evento de clic del botón "Regresar", navegando de vuelta a la vista principal.
     *
     * @param event El evento de acción (clic) que dispara la navegación.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/principal-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) regresarButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Principal");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal.");
            e.printStackTrace();
        }
    }
}