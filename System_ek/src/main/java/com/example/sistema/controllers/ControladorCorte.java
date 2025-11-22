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
 */
public class ControladorCorte implements Initializable {

    // --- Componentes FXML Nuevos ---
    @FXML private TextField efectivoField;
    @FXML private TextField tarjetaField;
    @FXML private Button confirmarButton;
    @FXML private Button cancelarButton;

    // --- Componentes Antiguos Eliminados (Solo dejo el Button de ejemplo) ---
    // @FXML private TableView<Pedido> ventasTable; <--- ELIMINADO
    // ...

    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lógica de inicialización simplificada.
        // Aquí podrías cargar la fecha actual o el total esperado si fuera necesario.
    }

    /**
     * Maneja el evento de clic del botón "Cancelar" o el cierre de la ventana.
     */
    @FXML
    void manejarCancelacion(ActionEvent event) {
        // Obtiene la Stage actual (la ventana modal) y la cierra
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }

    private void abrirVentanaTicket(List<Pedido> pedidos, float efectivo, float tarjeta, float totalSistema, float diferencia) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/TicketCorte.fxml"));
        Parent root = loader.load();

        ControladorTicketCorte controladorTicket = loader.getController();

        // Inyectar todos los datos necesarios para construir el ticket
        controladorTicket.inicializarTicket(pedidos, efectivo, tarjeta, totalSistema, diferencia);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle("Corte de caja");
        stage.show();
    }

    @FXML
    private void confirmarCorte(ActionEvent event) {
        try {
            float efectivoContado = Float.parseFloat(efectivoField.getText());
            float tarjetaContada = Float.parseFloat(tarjetaField.getText());

            // 1. Obtener el Total Esperado del Sistema (Necesitas la lógica aquí, ej: totalVentasHoy)
            java.util.Date hoy = new java.util.Date();
            float totalSistema = servicioVentas.obtenerTotalDelDia(hoy);
            List<Pedido> pedidosDelDia = servicioVentas.obtenerVentasPorFecha(hoy); // Lista de pedidos del día

            float totalContado = efectivoContado + tarjetaContada;
            float diferencia = totalContado - totalSistema;

            // 2. Lógica para guardar el registro de corte de caja...
            // ...

            //3. ABRIR LA VENTANA DEL TICKET E INYECTAR DATOS
            abrirVentanaTicket(pedidosDelDia, efectivoContado, tarjetaContada, totalSistema, diferencia);

            // Cierra la ventana del Corte de Caja
            manejarCancelacion(event);

        } catch (NumberFormatException e) {
            // ... manejo de error ...
        } catch (Exception e) {
            // ... manejo de error ...
        }
    }
}