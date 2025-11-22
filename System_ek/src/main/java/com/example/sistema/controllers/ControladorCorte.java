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

    @FXML private TextField efectivoField;
    @FXML private TextField tarjetaField;
    @FXML private Button confirmarButton;
    @FXML private Button cancelarButton;

    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Maneja el evento de clic del botón "Cancelar" o el cierre de la ventana.
     */
    @FXML
    void manejarCancelacion(ActionEvent event) {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }

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

    @FXML
    private void confirmarCorte(ActionEvent event) {
        try {
            float efectivoContado = Float.parseFloat(efectivoField.getText());
            float tarjetaContada = Float.parseFloat(tarjetaField.getText());
            java.util.Date hoy = new java.util.Date();
            float totalSistema = servicioVentas.obtenerTotalDelDia(hoy);
            List<Pedido> pedidosDelDia = servicioVentas.obtenerVentasPorFecha(hoy);

            float totalContado = efectivoContado + tarjetaContada;
            float diferencia = totalContado - totalSistema;
            abrirVentanaTicket(pedidosDelDia, efectivoContado, tarjetaContada, totalSistema, diferencia);
            manejarCancelacion(event);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}