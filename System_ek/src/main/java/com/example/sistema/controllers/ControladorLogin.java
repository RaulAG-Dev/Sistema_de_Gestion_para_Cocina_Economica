package com.example.sistema.controllers;

import com.example.sistema.models.Usuario;
import com.example.sistema.services.ServicioUsuarios;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controlador de la vista de inicio de sesión (Login).
 *
 * <p>Gestiona la interacción del usuario para la autenticación, validando las credenciales
 * a través de {@code ServicioUsuarios} y manejando la transición a la vista principal
 * tras un login exitoso.</p>
 *
 * @author Eduardo calan canche
 * @version 2.0
 * @since 2025-11-02
 */
public class ControladorLogin {
    // --- Componentes FXML ---

    /**
     * Campo de texto para ingresar el nombre de usuario.
     */
    @FXML
    private TextField usuarioField;
    /**
     * Campo de contraseña para ingresar la clave del usuario.
     */
    @FXML
    private PasswordField passField;
    /**
     * Botón utilizado para iniciar el proceso de autenticación.
     */
    @FXML
    private Button loginButton;
    /**
     * Etiqueta (Label) utilizada para mostrar mensajes de error durante la autenticación.
     */
    @FXML
    private Label mensajeError;
    /**
     * Instancia del servicio que contiene la lógica de negocio para la gestión de usuarios.
     */
    private ServicioUsuarios servicioUsuarios;

    /**
     * Constructor del controlador de Login.
     * <p>Se encarga de inicializar las dependencias necesarias, como el servicio de usuarios.</p>
     */
    public ControladorLogin(){
        this.servicioUsuarios = new ServicioUsuarios();
    }

    /**
     * Maneja el evento de acción (clic en el botón de login) para iniciar la autenticación.
     *
     * <p>Llama al método de autenticación del servicio de usuarios. Si tiene éxito,
     * cambia la escena a la vista principal. Si falla, muestra el mensaje de error.</p>
     *
     * @param event El evento de acción (clic) que dispara el login.
     */
    @FXML
    protected void manejarLogin(ActionEvent event) {
        String usuario = usuarioField.getText().trim();
        String password = passField.getText().trim();

        try{
            Usuario usuario1 = servicioUsuarios.autenticar(usuario, password);
            cambiarEscena();
        }catch (Exception e){
            mensajeError.setText(e.getMessage());
        }
    }

    /**
     * Realiza la transición de la ventana de login a la vista principal del sistema.
     *
     * <p>Carga el archivo FXML de la vista principal, obtiene la etapa actual y establece la nueva escena.</p>
     *
     * @throws IOException Si el archivo FXML de la vista principal no puede ser cargado.
     */
    private void cambiarEscena() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/PrincipalVew.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Cocina EK");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal después del login.");
            e.printStackTrace();
        }
    }
}