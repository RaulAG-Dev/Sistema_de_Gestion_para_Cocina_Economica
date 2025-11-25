/**
 * package com.example.sistema.controllers;
 *
 * Controlador para la ventana que muestra el ticket (reporte) de Corte de Caja.
 * Se encarga de formatear la lista de pedidos, los totales contados y los totales del sistema
 * en un formato de ticket de caja legible para su visualización e impresión.
 *
 * @author Emiliano Ávila
 * @version 1.0
 * @since 2025-11-24
 */
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

    /** Componente FXML: Área de texto donde se muestra el ticket. */
    @FXML private TextArea ticketTextArea;

    /** Formateador estático para la moneda local (Peso Mexicano por la configuración "es", "MX"). */
    static final java.text.NumberFormat currencyFormatter =
            java.text.NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    /** Formateador estático para la fecha y hora. */
    static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Inicializa el ticket con la información detallada del corte de caja.
     * Construye una representación de texto del ticket con la lista de transacciones
     * y el resumen de la caja.
     *
     * @param pedidos Lista de {@link Pedido} incluidos en el corte.
     * @param efectivo Monto de efectivo contado físicamente.
     * @param tarjeta Monto de tarjeta contado físicamente.
     * @param totalSistema Total de ventas registrado por el sistema.
     * @param diferencia Diferencia entre el total contado (efectivo + tarjeta) y el totalSistema.
     */
    public void inicializarTicket(List<Pedido> pedidos, float efectivo, float tarjeta, float totalSistema, float diferencia) {

        StringBuilder ticket = new StringBuilder();
        LocalDateTime horaCierre = LocalDateTime.now();
        // Intenta obtener la fecha del primer pedido como inicio del periodo
        String fechaInicioTexto = (!pedidos.isEmpty() && pedidos.get(0).getFechaHora() != null)
                ? pedidos.get(0).getFechaHora().toString() : "N/A";
        // Se añade .format(DATETIME_FORMAT) para coincidir con el formato de horaCierre.

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

        // Detalle de los pedidos
        for (Pedido p : pedidos) {
            if (p.getId() != ultimoId) {
                ticket.append(String.format("ID PEDIDO: %d\n", p.getId()));
                ultimoId = p.getId();
            }
            for (ItemPedido item : p.getItems()) {
                String nombrePlatillo = item.getPlatillo().getNombre();
                // Limita el nombre a 31 caracteres para mantener el formato tabular
                String nombreCorto = nombrePlatillo.length() > 31 ? nombrePlatillo.substring(0, 31) : nombrePlatillo;
                ticket.append(String.format("  %-31s %-5d %10s\n",
                        nombreCorto,
                        item.getCantidad(),
                        currencyFormatter.format(item.calcularSubtotal())
                ));
            }
        }

        // Resumen de Totales
        ticket.append("---------------------------------------------------\n");
        ticket.append(String.format("TOTAL VENTA (SISTEMA):     %20s\n\n\n", currencyFormatter.format(totalSistema)));
        ticket.append("===================================================\n");
        ticket.append(String.format("EFECTIVO CONTADO:          %20s\n", currencyFormatter.format(efectivo)));
        ticket.append(String.format("TARJETA CONTADA:           %20s\n", currencyFormatter.format(tarjeta)));
        ticket.append(String.format("TOTAL CONTADO:             %20s\n", currencyFormatter.format(efectivo + tarjeta)));
        ticket.append("---------------------------------------------------\n");

        // Cálculo de Sobrante/Faltante
        String diferenciaTexto = diferencia >= 0 ? "SOBRANTE" : "FALTANTE";
        // Se usa el valor absoluto si es FALTANTE para mostrarlo sin el signo menos
        String diferenciaMontoAbs = currencyFormatter.format(Math.abs(diferencia));

        ticket.append(String.format(">> %s: %35s\n", diferenciaTexto, diferenciaMontoAbs));
        ticketTextArea.setText(ticket.toString());
    }

    /**
     * Maneja la acción de cerrar la ventana del ticket de corte.
     *
     * @param event El evento de acción (e.g., clic en el botón Cerrar).
     */
    @FXML
    private void cerrar(ActionEvent event) {
        // Obtiene la referencia al Stage (ventana) actual y la cierra
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }

    /**
     * Maneja la acción de imprimir el ticket de corte.
     * (Método actualmente vacío, esperando la implementación de la lógica de impresión).
     *
     * @param event El evento de acción (e.g., clic en el botón Imprimir).
     */
    @FXML
    private void imprimir(ActionEvent event) {
        // Lógica de impresión pendiente
    }
}