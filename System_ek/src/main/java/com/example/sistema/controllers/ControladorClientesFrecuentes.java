package com.example.sistema.controllers;

import com.example.sistema.models.ClienteFrecuente;
import com.example.sistema.services.ServicioCliente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador encargado de gestionar la vista de Clientes Frecuentes.
 * <p>
 * Esta clase se encarga de inicializar la tabla de visualización, configurar las columnas
 * para mostrar los datos del modelo {@link ClienteFrecuente} y gestionar la lógica de cierre de la ventana.
 * Se comunica con el {@link ServicioCliente} para obtener los datos actualizados.
 * </p>
 *
 * @author Emiliano Avila
 * @version 1.0
 * @since 2025-11-24
 */
public class ControladorClientesFrecuentes implements Initializable {

    /**
     * Tabla principal que muestra la lista de clientes frecuentes.
     */
    @FXML private TableView<ClienteFrecuente> frecuentesTable;

    /**
     * Columna que muestra la posición (ranking) del cliente basado en su número de pedidos.
     */
    @FXML private TableColumn<ClienteFrecuente, Integer> rankingColumn;

    /**
     * Columna que muestra el nombre del cliente.
     */
    @FXML private TableColumn<ClienteFrecuente, String> nombreColumn;

    /**
     * Columna que muestra el número de teléfono del cliente.
     */
    @FXML private TableColumn<ClienteFrecuente, String> telefonoColumn;

    /**
     * Columna que muestra la cantidad total de pedidos realizados por el cliente.
     */
    @FXML private TableColumn<ClienteFrecuente, Long> pedidosColumn;

    /**
     * Instancia del servicio de clientes para acceder a la lógica de negocio y datos.
     */
    private final ServicioCliente servicioCliente = ServicioCliente.getInstance();

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * <p>
     * Configura las "CellValueFactory" de las columnas de la tabla para enlazar los datos
     * del objeto {@link ClienteFrecuente} con la interfaz gráfica.
     * </p>
     * <ul>
     * <li>Configura el nombre y teléfono como propiedades de texto.</li>
     * <li>Configura la cantidad de pedidos.</li>
     * <li>Calcula dinámicamente el ranking basado en el índice de la fila + 1.</li>
     * </ul>
     *
     * @param url La ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si no se conoce.
     * @param rb  Los recursos utilizados para localizar el objeto raíz, o null si no se conoce.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        telefonoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));

        pedidosColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCantidadPedidos()));

        // Calcula el ranking dinámicamente basado en la posición en la lista visual
        rankingColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(frecuentesTable.getItems().indexOf(data.getValue()) + 1)
        );

        cargarRankingClientes();
    }

    /**
     * Obtiene la lista de clientes frecuentes desde el servicio y actualiza la tabla.
     * <p>
     * Este método llama a {@link ServicioCliente#obtenerClientesFrecuentes()} y reemplaza
     * todos los ítems actuales de la tabla con la nueva lista ordenada.
     * </p>
     */
    private void cargarRankingClientes() {
        List<ClienteFrecuente> clientesOrdenados = servicioCliente.obtenerClientesFrecuentes();
        frecuentesTable.getItems().setAll(clientesOrdenados);
    }

    /**
     * Maneja el evento de cierre de la ventana actual.
     * <p>
     * Este método se invoca típicamente desde un botón "Cerrar" o "Volver".
     * Obtiene el {@link Stage} actual a partir del evento y lo cierra.
     * </p>
     *
     * @param event El evento de acción generado por el componente de la UI (ej. botón).
     */
    public void manejarCierre(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }
}