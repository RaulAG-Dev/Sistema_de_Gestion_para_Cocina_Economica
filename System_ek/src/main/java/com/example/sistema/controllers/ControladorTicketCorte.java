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
     * [Parámetros omitidos para brevedad]
     */
    public void inicializarTicket(List<Pedido> pedidos, float efectivo, float tarjeta, float totalSistema, float diferencia) {

        StringBuilder ticket = new StringBuilder();
        LocalDateTime horaCierre = LocalDateTime.now();
        String fechaInicioTexto = (!pedidos.isEmpty() && pedidos.get(0).getFechaHora() != null)
                ? pedidos.get(0).getFechaHora().toString() : "N/A";

        ticket.append("===================================================\n");
        ticket.append("         CORTE DE CAJA - COCINA EK\n");
        ticket.append("===================================================\n");
        ticket.append("\n");
        ticket.append(String.format("Fecha/Hora Cierre: %s\n", horaCierre.format(DATETIME_FORMAT)));
        ticket.append(String.format("Inicio de Periodo: %s\n", fechaInicioTexto));
        ticket.append("---------------------------------------------------\n");
        ticket.append("ID  DESCRIPCION                  CANT.       TOTAL\n");
        ticket.append("---------------------------------------------------\n");
        int ultimoId = -1;
        for (Pedido p : pedidos) {
            if (p.getId() != ultimoId) {
                ticket.append(String.format("ID PEDIDO: %d\n", p.getId()));
                ultimoId = p.getId();
            }
            for (ItemPedido item : p.getItems()) {
                String nombrePlatillo = item.getPlatillo().getNombre();
                String nombreCorto = nombrePlatillo.length() > 31 ? nombrePlatillo.substring(0, 31) : nombrePlatillo;
                ticket.append(String.format("  %-31s %-5d %10s\n",
                        nombreCorto,
                        item.getCantidad(),
                        currencyFormatter.format(item.calcularSubtotal())
                ));
            }
        }
        ticket.append("---------------------------------------------------\n");
        ticket.append(String.format("TOTAL VENTA (SISTEMA):     %20s\n\n\n", currencyFormatter.format(totalSistema)));
        ticket.append("===================================================\n");
        ticket.append(String.format("EFECTIVO CONTADO:          %20s\n", currencyFormatter.format(efectivo)));
        ticket.append(String.format("TARJETA CONTADA:           %20s\n", currencyFormatter.format(tarjeta)));
        ticket.append(String.format("TOTAL CONTADO:             %20s\n", currencyFormatter.format(efectivo + tarjeta)));
        ticket.append("---------------------------------------------------\n");

        String diferenciaTexto = diferencia >= 0 ? "SOBRANTE" : "FALTANTE";
        String diferenciaMontoAbs = currencyFormatter.format(diferencia);

        ticket.append(String.format(">> %s: %35s\n", diferenciaTexto, diferenciaMontoAbs));
        ticketTextArea.setText(ticket.toString());
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }

    @FXML
    private void imprimir(ActionEvent event) {
    }
}