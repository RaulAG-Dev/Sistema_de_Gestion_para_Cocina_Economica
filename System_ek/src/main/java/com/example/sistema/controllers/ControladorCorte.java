package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
import com.example.sistema.services.ServicioVentas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista para el registro final del Corte de Caja.
 * <p>
 * Gestiona la lógica para recibir los montos contados de efectivo y tarjeta,
 * compararlos con el total registrado en el sistema a través de {@link ServicioVentas},
 * calcular la diferencia (sobrante o faltante) y, finalmente, abrir la ventana del ticket
 * de corte ({@link ControladorTicketCorte}) con los resultados.
 * </p>
 *
 * @author Emiliano Avila
 * @version 1.0
 * @since 2025-11-24
 */
public class ControladorCorte implements Initializable {

    /**
     * Campo de texto para ingresar el monto contado en efectivo.
     */
    @FXML private TextField efectivoField;

    /**
     * Campo de texto para ingresar el monto contado con tarjeta.
     */
    @FXML private TextField tarjetaField;

    /**
     * Botón para confirmar el corte de caja e iniciar la verificación de montos.
     */
    @FXML private Button confirmarButton;

    /**
     * Botón para cancelar la operación y cerrar la ventana actual.
     */
    @FXML private Button cancelarButton;

    /**
     * Instancia del servicio de ventas para acceder a la lógica de negocio (obtener totales y pedidos del día).
     */
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    /**
     * Inicializa el controlador al cargar la vista FXML.
     * <p>
     * Actualmente no realiza ninguna configuración específica.
     * </p>
     *
     * @param url La ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si no se conoce.
     * @param resourceBundle  Los recursos utilizados para localizar el objeto raíz, o null si no se conoce.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Maneja el evento de clic del botón "Cancelar".
     * <p>
     * Obtiene el {@link Stage} actual a partir del {@link ActionEvent} y lo cierra.
     * </p>
     *
     * @param event El evento de acción generado por el botón.
     */
    @FXML
    void manejarCancelacion(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }

    /**
     * Abre una ventana modal para mostrar el ticket final del corte de caja.
     * <p>
     * Esta ventana utiliza el controlador {@link ControladorTicketCorte} e inicializa
     * la vista con los resultados del corte.
     * </p>
     *
     * @param pedidos Lista de {@link Pedido} realizados durante el día del corte.
     * @param efectivo Monto de efectivo contado.
     * @param tarjeta Monto de tarjeta contado.
     * @param totalSistema Total de ventas registrado por el sistema.
     * @param diferencia La diferencia (sobrante o faltante) entre el conteo manual y el total del sistema.
     * @throws IOException Si el archivo FXML para la ventana del ticket no puede ser cargado.
     */
    private void abrirVentanaTicket(List<Pedido> pedidos, float efectivo, float tarjeta, float totalSistema, float diferencia) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/TicketCorte.fxml"));
        Parent root = loader.load();

        ControladorTicketCorte controladorTicket = loader.getController();
        controladorTicket.inicializarTicket(pedidos, efectivo, tarjeta, totalSistema, diferencia);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Corte de caja");
        stage.show();
    }

    /**
     * Procesa la confirmación del corte de caja.
     * <p>
     * 1. Parsea los montos contados de efectivo y tarjeta.
     * 2. Consulta al {@link ServicioVentas} para obtener el total registrado y los pedidos del día.
     * 3. Calcula el total contado y la diferencia ({@code totalContado - totalSistema}).
     * 4. Abre la ventana del ticket de corte con todos los resultados.
     * 5. Cierra la ventana actual del controlador.
     * </p>
     *
     * @param event El evento de acción generado por el botón "Confirmar".
     */
    @FXML
    private void confirmarCorte(ActionEvent event) {
        try {
            // 1. Obtención y validación de datos contados
            float efectivoContado = Float.parseFloat(efectivoField.getText());
            float tarjetaContada = Float.parseFloat(tarjetaField.getText());

            // 2. Consulta al sistema
            java.util.Date hoy = new java.util.Date();
            float totalSistema = servicioVentas.obtenerTotalDelDia(hoy);
            List<Pedido> pedidosDelDia = servicioVentas.obtenerVentasPorFecha(hoy);

            // 3. Cálculo de resultados
            float totalContado = efectivoContado + tarjetaContada;
            float diferencia = totalContado - totalSistema;

            // 4. Apertura de ticket y cierre de ventana
            abrirVentanaTicket(pedidosDelDia, efectivoContado, tarjetaContada, totalSistema, diferencia);
            manejarCancelacion(event); // Cierra la ventana del corte

        } catch (NumberFormatException e) {
            // Manejo de error si los campos de texto no son números válidos.
            System.err.println("Error: Asegúrese de ingresar solo números en los campos de efectivo y tarjeta.");
            e.printStackTrace();
        } catch (Exception e) {
            // Manejo de otros errores (ej. error de I/O al cargar el FXML del ticket).
            System.err.println("Ocurrió un error inesperado durante el corte.");
            e.printStackTrace();
        }
    }
}