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

/**
 * Controlador encargado de generar y mostrar un ticket de venta en formato de texto plano
 * dentro de un {@link TextArea}.
 * <p>
 * Se utiliza para presentar los detalles de un pedido finalizado, incluyendo la hora,
 * el cliente, y el desglose de los ítems con sus respectivos subtotales y el total general.
 * </p>
 *
 * @author Emiliano Avila
 * @version 1.0
 * @since 2025-11-24
 */
public class ControladorTicketPedido {

    /**
     * Área de texto de la interfaz gráfica donde se mostrará el contenido formateado del ticket.
     */
    @FXML
    private TextArea ticketTextArea;

    /**
     * Formateador estático de moneda para el estándar Mexicano (MXN).
     * Se usa para dar formato a los subtotales y al total final.
     */
    private static final java.text.NumberFormat currencyFormatter =
            java.text.NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    /**
     * Formateador estático para mostrar la fecha y hora en el formato "yyyy-MM-dd HH:mm:ss".
     */
    private static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Genera el contenido completo del ticket de venta y lo carga en el {@link TextArea}.
     * <p>
     * El método construye el ticket paso a paso, incluyendo encabezados, la hora de cierre
     * del pedido, el nombre del cliente y el desglose tabular de los ítems.
     * </p>
     *
     * @param pedidos Lista observable de {@link ItemPedido} que componen el pedido.
     * @param total El monto total final del pedido.
     * @param cliente El nombre del cliente al que se le hizo el pedido.
     */
    public void inicializarTicketPedido(ObservableList<ItemPedido> pedidos, float total, String cliente) {

        StringBuilder ticket = new StringBuilder();
        // Captura el momento exacto de la impresión del ticket
        LocalDateTime horaCierre = LocalDateTime.now();

        // --- ENCABEZADO Y DATOS GENERALES ---
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

        // --- DESGLOSE DE ITEMS ---
        for (ItemPedido item : pedidos) {
            String nombrePlatillo = item.getPlatillo().getNombre();
            // Trunca el nombre del platillo si es demasiado largo para mantener el formato tabular
            String nombreCorto = nombrePlatillo.length() > 32 ? nombrePlatillo.substring(0, 32) : nombrePlatillo;

            // Uso de String.format() para asegurar el alineamiento tabular:
            // %-32s: Nombre del platillo (alineado a la izquierda, 32 caracteres)
            // %-7d: Cantidad (alineado a la izquierda, 7 caracteres)
            // %6s: Subtotal (alineado a la derecha, 6 caracteres)
            ticket.append(String.format(" %-32s %-7d %6s\n",
                    nombreCorto,
                    item.getCantidad(),
                    currencyFormatter.format(item.calcularSubtotal())
            ));
        }

        // --- PIE DE TICKET ---
        ticket.append("---------------------------------------------------\n");
        ticket.append(String.format("\n TOTAL: %40s", currencyFormatter.format(total)));
        ticketTextArea.setText(ticket.toString());
    }

    /**
     * Cierra la ventana actual del ticket de pedido.
     * <p>
     * Este método se invoca al hacer clic en un botón de cierre dentro del FXML.
     * </p>
     *
     * @param event El evento de acción que dispara el cierre (ej. clic en un botón).
     */
    @FXML
    private void cerrar(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }
}