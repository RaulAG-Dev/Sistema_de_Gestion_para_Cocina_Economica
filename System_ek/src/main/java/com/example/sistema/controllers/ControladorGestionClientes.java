package com.example.sistema.controllers;

import com.example.sistema.models.Cliente;
import com.example.sistema.models.Pedido; // Necesario para el historial
import com.example.sistema.services.ServicioCliente;
import com.example.sistema.services.ServicioVentas; // Necesario para obtener pedidos
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * **Controlador de la vista para la gestión de clientes (CRUD y consulta).**
 * <p>
 * Permite a los usuarios visualizar, editar y eliminar clientes, así como
 * consultar el historial de pedidos de un cliente seleccionado utilizando
 * {@link ServicioCliente} y {@link ServicioVentas}.
 *
 * @author Raul Aguayo , Eduardo Calán y Michelle Chuc
 * @version 4.0
 * @since 2025-11-22
 */
public class ControladorGestionClientes implements Initializable {
    // --- Componentes FXML de Navegación y Acciones Globales ---
    /** Botón FXML para regresar a la vista anterior. */
    @FXML private Button regresarButton;
    /** Botón FXML para abrir la vista de clientes frecuentes. */
    @FXML private Button clientesFrecuentesButton;

    // --- Componentes FXML de la Tabla de Clientes ---
    /** Tabla principal que muestra la lista de todos los clientes. */
    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, Integer> idClienteColumn;
    @FXML private TableColumn<Cliente, String> nombreClienteColumn;
    @FXML private TableColumn<Cliente, String> telefonoClienteColumn;
    /** Columna para botones de edición y eliminación. */
    @FXML private TableColumn<Cliente, Void> accionClienteColumn;

    // --- Componentes FXML del Formulario de Edición/Creación ---
    @FXML private TextField idClienteField;
    @FXML private TextField nombreClienteField;
    @FXML private TextField telefonoClienteField;
    /** Área de texto para la dirección o preferencias del cliente. */
    @FXML private TextArea direccionClienteArea;
    @FXML private Button guardarClienteButton;

    // --- Componentes FXML del Historial de Pedidos ---
    /** Tabla que muestra el historial de pedidos del cliente seleccionado. */
    @FXML private TableView<Pedido> pedidosClienteTable;
    @FXML private TableColumn<Pedido, Integer> pedidoIdColumn;
    @FXML private TableColumn<Pedido, String> pedidoFechaColumn;
    @FXML private TableColumn<Pedido, Float> pedidoTotalColumn;

    // --- Servicios ---
    /** Instancia del servicio para la gestión de clientes. */
    private final ServicioCliente servicioCliente = ServicioCliente.getInstance();
    /** Instancia del servicio de ventas para obtener el historial de pedidos. */
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance(); // 🔑 Instancia del ServicioVentas

    // --- Estado Interno ---
    /** El cliente actualmente seleccionado en la tabla o en edición. */
    private Cliente clienteActual;


    /**
     * Inicializa el controlador, configura las fábricas de celdas para las tablas,
     * configura la columna de acciones y carga los datos iniciales de los clientes.
     *
     * @param url La ubicación utilizada para resolver rutas relativas.
     * @param rb Los recursos utilizados para localizar el objeto raíz.
     * @Override
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Configuración de Cell Value Factories para la tabla de Clientes
        idClienteColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nombreClienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        telefonoClienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
        // Configuración de Cell Value Factories para la tabla de Pedidos
        pedidoIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        pedidoTotalColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
        pedidoFechaColumn.setCellValueFactory(data -> {
            java.util.Date fecha = data.getValue().getFechaHora();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yy");
            return new SimpleStringProperty(fecha != null ? sdf.format(fecha) : "N/A");
        });

        configurarColumnaAcciones();
        // Carga inicial de datos
        clientesTable.getItems().setAll(servicioCliente.obtenerTodos());
        // Listener para la selección de fila en la tabla de clientes
        clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                clienteActual = newSel;
                idClienteField.setText(String.valueOf(newSel.getId()));
                nombreClienteField.setText(newSel.getNombre());
                telefonoClienteField.setText(newSel.getTelefono());
                // Se asume que 'preferencias' se usa para almacenar la dirección u otros datos.
                direccionClienteArea.setText(newSel.getPreferencias());
                cargarHistorialPedidos(newSel);
            }
        });
    }


    /**
     * Carga y muestra el historial de pedidos asociados a un cliente específico.
     * Los pedidos se obtienen del {@link ServicioVentas} y se filtran por ID de cliente.
     * El historial se ordena de forma descendente por fecha.
     *
     * @param cliente El {@link Cliente} cuyo historial se desea mostrar.
     */
    private void cargarHistorialPedidos(Cliente cliente) {
        if (cliente == null || cliente.getId() <= 0) {
            pedidosClienteTable.getItems().clear();
            return;
        }

        List<Pedido> todosLosPedidos = servicioVentas.getPedidosMaestros();
        // Filtrar pedidos por ID de cliente
        List<Pedido> historial = todosLosPedidos.stream()
                .filter(p -> p.getCliente() != null && p.getCliente().getId() == cliente.getId())
                .collect(Collectors.toList());
        // Ordenar por fecha, el más reciente primero
        historial.sort(Comparator.comparing(Pedido::getFechaHora, Comparator.nullsLast(Comparator.reverseOrder())));

        pedidosClienteTable.getItems().setAll(historial);
    }

    /**
     * Configura la columna de acciones para incluir botones de "Editar" y "Eliminar"
     * dentro de cada fila de la tabla de clientes.
     */
    private void configurarColumnaAcciones() {
        accionClienteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button eliminarBtn = new Button("❌");
            private final Button editarBtn = new Button("...");

            {
                // Manejador para el botón Eliminar
                eliminarBtn.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente != null) {
                        servicioCliente.eliminar(cliente.getId());
                        clientesTable.getItems().setAll(servicioCliente.obtenerTodos());
                        prepararNuevoCliente(null); // Limpiar formulario después de eliminar
                    }
                });

                // Manejador para el botón Editar
                editarBtn.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente != null) {
                        // Cargar datos del cliente seleccionado al formulario
                        clienteActual = cliente;
                        idClienteField.setText(String.valueOf(cliente.getId()));
                        nombreClienteField.setText(cliente.getNombre());
                        telefonoClienteField.setText(cliente.getTelefono());
                        direccionClienteArea.setText(cliente.getPreferencias());
                        cargarHistorialPedidos(cliente);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(editarBtn, eliminarBtn);
                    box.setSpacing(5);
                    setGraphic(box);
                }
            }
        });
    }

    /**
     * Maneja el evento de clic del botón "Regresar". Cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }

    /**
     * Maneja el evento de clic del botón "Clientes Frecuentes".
     * Abre una nueva ventana modal para mostrar el ranking de clientes frecuentes.
     *
     * @param event El evento de acción.
     */
    @FXML
    void verClientesFrecuentes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/ClienteFrecuente.fxml"));
            Parent root = loader.load();
            Stage nuevaStage = new Stage();
            Scene scene = new Scene(root);
            nuevaStage.setScene(scene);
            nuevaStage.setTitle("Clientes frecuentes");
            nuevaStage.show();

        } catch(IOException e) {
            System.err.println("Error al cargar la vista de Corte de Caja.");
            e.printStackTrace();
        }
    }

    /**
     * Prepara el formulario para el ingreso de un **nuevo cliente**.
     * Genera un nuevo ID temporal y limpia todos los campos de entrada.
     *
     * @param event El evento de acción (puede ser {@code null} si se llama internamente).
     */
    @FXML
    void prepararNuevoCliente(ActionEvent event) {
        clienteActual = new Cliente();
        clienteActual.setId(servicioCliente.generarNuevoId());
        idClienteField.setText(String.valueOf(clienteActual.getId()));
        nombreClienteField.clear();
        telefonoClienteField.clear();
        direccionClienteArea.clear();
        pedidosClienteTable.getItems().clear();
    }

    /**
     * Maneja el evento de clic del botón "Guardar Cliente".
     * <p>
     * Este método realiza las siguientes acciones:
     * 1. Recolecta los datos de los campos de texto (nombre, teléfono, dirección/preferencias).
     * 2. Asigna los valores (limpiando espacios en blanco) al objeto {@link #clienteActual}.
     * <p>
     * NOTA: La lógica de persistencia real (llamada a {@code servicioCliente.guardar()})
     * debe ser añadida aquí para que los cambios se guarden en la lista maestra
     * y en el repositorio.
     *
     * @param event El evento de acción que disparó el guardado.
     */
    @FXML
    void guardarCliente(ActionEvent event) {
        String nombre = nombreClienteField.getText();
        clienteActual.setNombre(nombre != null ? nombre.trim() : "");

        String telefono = telefonoClienteField.getText();
        clienteActual.setTelefono(telefono != null ? telefono.trim() : "");

        String direccion = direccionClienteArea.getText();
        clienteActual.setPreferencias(direccion != null ? direccion.trim() : "");
    }
}