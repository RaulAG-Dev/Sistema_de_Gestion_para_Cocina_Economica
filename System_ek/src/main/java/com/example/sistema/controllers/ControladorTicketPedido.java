package com.example.sistema.controllers;

import com.example.sistema.models.ItemPedido;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ControladorTicketPedido {

    @FXML
    private TextArea ticketTextArea;
    private static final java.text.NumberFormat currencyFormatter =
            java.text.NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    private static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void inicializarTicketPedido(ObservableList<ItemPedido> pedidos, float total, String cliente) {

        StringBuilder ticket = new StringBuilder();
        LocalDateTime horaCierre = LocalDateTime.now();

        ticket.append("===================================================\n");
        ticket.append("         TICKET DE VENTA - COCINA EK\n");
        ticket.append("===================================================\n");
        ticket.append("\n\n");
        ticket.append(String.format("Fecha/Hora: %s\n", horaCierre.format(DATETIME_FORMAT)));
        ticket.append("Cliente: " + cliente);
        ticket.append("\n\n");
        ticket.append("---------------------------------------------------\n");
        ticket.append("DESCRIPCION                      CANT.       TOTAL\n");
        ticket.append("---------------------------------------------------\n");

        for (ItemPedido item : pedidos) {
            String nombrePlatillo = item.getPlatillo().getNombre();
            String nombreCorto = nombrePlatillo.length() > 32 ? nombrePlatillo.substring(0, 32) : nombrePlatillo;
            ticket.append(String.format(" %-32s %-7d %6s\n",
                    nombreCorto,
                    item.getCantidad(),
                    currencyFormatter.format(item.calcularSubtotal())
            ));
        }

        ticket.append("---------------------------------------------------\n");
        ticket.append(String.format("\n TOTAL: %40s", currencyFormatter.format(total)));
        ticketTextArea.setText(ticket.toString());
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }
}