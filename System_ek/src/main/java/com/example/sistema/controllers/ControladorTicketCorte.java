package com.example.sistema.controllers;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Pedido;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ControladorTicketCorte {

    @FXML private TextArea ticketTextArea;
    static final java.text.NumberFormat currencyFormatter =
            java.text.NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Inicializa el ticket con la información del corte de caja.
     * @param pedidos Lista de pedidos del día (o periodo).
     * @param efectivo Monto contado en efectivo.
     * @param tarjeta Monto contado en tarjeta.
     * @param totalSistema Monto total esperado según el sistema.
     * @param diferencia Monto faltante o sobrante.
     */
    public void inicializarTicket(List<Pedido> pedidos, float efectivo, float tarjeta, float totalSistema, float diferencia) {

        StringBuilder ticket = new StringBuilder();
        ticket.append("===================================================\n");
        ticket.append("         CORTE DE CAJA - COCINA EK\n");
        ticket.append("===================================================\n");
        LocalDateTime horaCierre = LocalDateTime.now();
        ticket.append("\n\n");

        ticket.append(String.format("Fecha/Hora Cierre: %s\n", horaCierre.format(DATETIME_FORMAT)));
        if (!pedidos.isEmpty()) {
            ticket.append(String.format("Inicio de Periodo: %s\n", pedidos.get(0).getFechaHora().toString()));
        }
        ticket.append("---------------------------------------------------\n");
        ticket.append("DESCRIPCION                      CANT.     TOTAL\n");
        ticket.append("---------------------------------------------------\n");
        for (Pedido p : pedidos) {
            for (ItemPedido item : p.getItems()) {
                String nombrePlatillo = item.getPlatillo().getNombre();
                String nombreCorto = nombrePlatillo.length() > 27 ? nombrePlatillo.substring(0, 27) : nombrePlatillo;
                ticket.append(String.format("ID: %d %-28s %-5d %7s\n",p.getId() , nombreCorto, item.getCantidad(), currencyFormatter.format(item.calcularSubtotal())));
            }
        }
        ticket.append("---------------------------------------------------\n");
        ticket.append(String.format("TOTAL VENTA (SISTEMA):     %21s\n", currencyFormatter.format(totalSistema)));
        ticket.append("\n\n");
        ticket.append("===================================================\n");
        ticket.append(String.format("EFECTIVO CONTADO:          %20s\n", currencyFormatter.format(efectivo)));
        ticket.append(String.format("TARJETA CONTADA:           %20s\n", currencyFormatter.format(tarjeta)));
        ticket.append(String.format("TOTAL VENTAS:             %20s\n", currencyFormatter.format(efectivo + tarjeta)));
        ticket.append("---------------------------------------------------\n");

        String diferenciaTexto = diferencia >= 0 ? "SOBRANTE" : "FALTANTE";
        String diferenciaMonto = currencyFormatter.format(diferencia);

        ticket.append(String.format("\n\n%s:   %36s\n", diferenciaTexto, diferenciaMonto));
        ticketTextArea.setText(ticket.toString());
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }
}