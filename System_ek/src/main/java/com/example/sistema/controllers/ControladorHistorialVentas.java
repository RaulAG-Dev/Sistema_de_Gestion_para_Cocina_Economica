package com.example.sistema.controllers;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Pedido;
import com.example.sistema.services.ServicioVentas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * **Controlador de la vista para la consulta del Historial de Ventas (Pedidos).**
 * <p>
 * Permite a los usuarios visualizar todos los pedidos registrados en el sistema,
 * ver los detalles de un pedido seleccionado y generar una simulación de
 * reimpresión de ticket.
 *
 * @author Raul Aguayo , Eduardo Calán y Michelle Chuc
 * @version 6.0
 * @since 2025-11-22
 */
public class ControladorHistorialVentas {

    // --- Componentes FXML de la Vista ---
    @FXML
    private Button regresarButton;

    /** Tabla principal que muestra la lista de todos los pedidos. */
    @FXML
    private TableView<Pedido> ventasTable;

    @FXML
    private TableColumn<Pedido, Integer> idVentaColumn;

    @FXML
    private TableColumn<Pedido, String> fechaColumn;

    @FXML
    private TableColumn<Pedido, String> clienteColumn;

    @FXML
    private TableColumn<Pedido, String> nombreColumn;

    @FXML
    private TableColumn<Pedido, Float> totalColumn;

    /** Área de texto FXML para mostrar los detalles del pedido seleccionado. */
    @FXML
    private TextArea detallesVentaArea;

    /** Botón FXML para generar la simulación de reimpresión de ticket. */
    @FXML
    private Button reimprimirButton;

    // --- Servicios ---
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    /**
     * Inicializa el controlador, configura las fábricas de celdas para la tabla
     * y carga la lista de pedidos al inicio. También añade el listener para
     * mostrar los detalles del pedido seleccionado.
     *
     * @Override
     */
    @FXML
    public void initialize() {
        // Configuración de Cell Value Factories
        idVentaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        // Formateo de fecha y hora
        fechaColumn.setCellValueFactory(data -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(sdf.format(data.getValue().getFechaHora()));
        });
        // Mostrar nombre del cliente (o N/A si no tiene cliente asociado)
        clienteColumn.setCellValueFactory(cellData -> {
            Pedido pedido = cellData.getValue();
            String nombreCliente = (pedido.getCliente() != null)
                    ? pedido.getCliente().getNombre()
                    : "N/A";
            return new javafx.beans.property.SimpleStringProperty(nombreCliente);
        });

        totalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));
        // Carga inicial de datos desde el servicio
        ventasTable.getItems().setAll(servicioVentas.obtenerTodasLasVentas());
        // Listener para la selección de fila
        ventasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("Venta ID: ").append(newSel.getId()).append("\n");
                detalles.append("Fecha: ").append(newSel.getFechaHora()).append("\n");
                detalles.append("cliente: ").append(newSel.getCliente()).append("\n");
                detalles.append("Nombre: ").append(newSel.getNombre()).append("\n");
                detalles.append("Total: $").append(newSel.getTotal()).append("\n\n");
                detalles.append("Items:\n");
                for (var item : newSel.getItems()) {
                    // Usar la representación en cadena de ItemPedido o un formato específico
                    detalles.append(item.toString()).append("\n");
                }
                detallesVentaArea.setText(detalles.toString());
            }
        });
        System.out.println("ControladorHistorialVentas inicializado.");
    }

    /**
     * Maneja el evento de clic del botón "Regresar". Cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Stage stageActual = (Stage) scene.getWindow();
        stageActual.close();
    }

    /**
     * Genera un formato de ticket de venta para el pedido seleccionado en la tabla
     * y muestra el resultado en el área de detalles.
     *
     * @param event El evento de acción.
     */
    @FXML
    void reimprimirTicket(ActionEvent event) {
        Pedido seleccionado = ventasTable.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            detallesVentaArea.setText("No se ha seleccionado ninguna venta para reimprimir.");
            return;
        }

        StringBuilder ticket = new StringBuilder();
        ticket.append("=== Ticket de Venta ===\n");
        ticket.append("ID Venta: ").append(seleccionado.getId()).append("\n");
        ticket.append("Fecha: ").append(seleccionado.getFechaHora()).append("\n");
        ticket.append("Cliente: ").append(
                seleccionado.getCliente() != null ? seleccionado.getCliente().getNombre() : "N/A"
        ).append("\n");
        ticket.append("----------------------------\n");

        // Listado de ítems
        for (ItemPedido item : seleccionado.getItems()) {
            ticket.append(item.getPlatillo().getNombre())
                    .append(" x").append(item.getCantidad())
                    .append("  $").append(item.calcularSubtotal())
                    .append("\n");
        }

        ticket.append("----------------------------\n");
        ticket.append("TOTAL: $").append(seleccionado.getTotal()).append("\n");
        ticket.append(seleccionado.isPagado() ? "Estado: PAGADO\n" : "Estado: PENDIENTE\n");
        ticket.append("============================\n");

        detallesVentaArea.setText(ticket.toString());

        System.out.println("Ticket reimpreso para venta ID: " + seleccionado.getId());
    }

}