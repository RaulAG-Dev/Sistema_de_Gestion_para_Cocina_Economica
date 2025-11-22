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


public class ControladorHistorialVentas {
    @FXML
    private Button regresarButton;

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

    @FXML
    private TextArea detallesVentaArea;

    @FXML
    private Button reimprimirButton;
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();


    @FXML
    public void initialize() {
        idVentaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        fechaColumn.setCellValueFactory(data -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(sdf.format(data.getValue().getFechaHora()));
        });

        clienteColumn.setCellValueFactory(cellData -> {
            Pedido pedido = cellData.getValue();
            String nombreCliente = (pedido.getCliente() != null)
                    ? pedido.getCliente().getNombre()
                    : "N/A";
            return new javafx.beans.property.SimpleStringProperty(nombreCliente);
        });

        totalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));
        ventasTable.getItems().setAll(servicioVentas.obtenerTodasLasVentas());
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
                    detalles.append(item.toString()).append("\n");
                }
                detallesVentaArea.setText(detalles.toString());
            }
        });
        System.out.println("ControladorHistorialVentas inicializado.");
    }

    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Stage stageActual = (Stage) scene.getWindow();
        stageActual.close();
    }


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
                seleccionado.getCliente() != null ? seleccionado.getCliente().getNombre() : "motoo"
        ).append("\n");
        ticket.append("----------------------------\n");

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