package com.example.sistema;

import com.example.sistema.services.ServicioCliente;
import com.example.sistema.services.ServicioVentas;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {


    /*ServicioVentas servicioVentas = ServicioVentas.getInstance();
    ServicioCliente servicioCliente = ServicioCliente.getInstance();*/

    @Override
    public void start(Stage stage) throws IOException {
        /*servicioCliente.sincronizarClientesDesdePedidos();*/

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/sistema/LogginView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400, 480);
        stage.setTitle("Inicio de sesión a cocina economica");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }
}