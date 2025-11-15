package com.example.sistema.controllers;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Pedido;
import com.example.sistema.services.ServicioVentas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

//hola juan calos

public class ControladorHistorialVentas {
// en los privates debes de cmabiar lo que esta dentro de los ¿? por los modelos corresponientes
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
    private TableColumn<Pedido, Float> totalColumn;

    @FXML
    private TextArea detallesVentaArea;

    @FXML
    private Button reimprimirButton;
    private final ServicioVentas servicioVentas = new ServicioVentas(
            new com.example.sistema.persistencia.RepositorioJSON<>("pedidos.json",
                    new com.example.sistema.persistencia.ConvertidorPedido())
    );


    @FXML
    public void initialize() {
        // Configurar columnas y cargar ventas
        idVentaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        fechaColumn.setCellValueFactory(data -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(sdf.format(data.getValue().getFechaHora()));
        });
        clienteColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCliente() != null ? data.getValue().getCliente().getNombre() : "N/A"
        ));
        totalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));

        ventasTable.getItems().setAll(servicioVentas.obtenerTodasLasVentas());

        // Listener para mostrar detalles en el TextArea
        ventasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("Venta ID: ").append(newSel.getId()).append("\n");
                detalles.append("Fecha: ").append(newSel.getFechaHora()).append("\n");
                detalles.append("Cliente: ").append(newSel.getCliente() != null ? newSel.getCliente().getNombre() : "N/A").append("\n");
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


    @FXML
    void reimprimirTicket(ActionEvent event) {
        // Obtener el pedido seleccionado en la tabla
        Pedido seleccionado = ventasTable.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            detallesVentaArea.setText("⚠️ No se ha seleccionado ninguna venta para reimprimir.");
            return;
        }

        // Construir el ticket en formato texto
        StringBuilder ticket = new StringBuilder();
        ticket.append("=== Ticket de Venta ===\n");
        ticket.append("ID Venta: ").append(seleccionado.getId()).append("\n");
        ticket.append("Fecha: ").append(seleccionado.getFechaHora()).append("\n");
        ticket.append("Cliente: ").append(
                seleccionado.getCliente() != null ? seleccionado.getCliente().getNombre() : "N/A"
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

        // Mostrar el ticket en el TextArea
        detallesVentaArea.setText(ticket.toString());

        System.out.println("Ticket reimpreso para venta ID: " + seleccionado.getId());
    }

}