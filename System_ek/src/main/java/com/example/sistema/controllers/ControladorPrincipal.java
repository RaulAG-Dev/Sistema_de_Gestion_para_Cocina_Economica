package com.example.sistema.controllers;

import com.example.sistema.models.Platillo;
import com.example.sistema.models.Usuario;
import com.example.sistema.services.ServicioMenu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorPrincipal implements Initializable {

    @FXML private ChoiceBox<String> selectorMenu;
    @FXML private GridPane menuGrid;
    @FXML private Button añadirPlatilloButton;
    @FXML private TextField nombreClienteField;
    @FXML private TableView<?> pedidoTable;
    @FXML private Label totalPagarLabel;

    private final ServicioMenu servicioMenu = new ServicioMenu();
    private final String[] TIPOS_MENU = {"Desayunos", "Almuerzos", "Cenas"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectorMenu.setItems(FXCollections.observableArrayList(TIPOS_MENU));
        selectorMenu.getSelectionModel().selectFirst();

        selectorMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarMenu(newValue);
            }
        });

        cargarMenu(selectorMenu.getSelectionModel().getSelectedItem());
    }

    public void inicializarDatos(Usuario usuario) {

    }


    private void cargarMenu(String tipoMenu) {

        menuGrid.getChildren().clear();

        List<Platillo> platillos = servicioMenu.obtenerPlatillosPorTipo(tipoMenu);

        if (platillos.isEmpty()) {
            menuGrid.add(new Label("No hay platillos disponibles para: " + tipoMenu), 0, 0);
            return;
        }


        int col = 0;
        int row = 0;
        final int MAX_COLUMNS = 3;

        for (Platillo platillo : platillos) {
            VBox tarjeta = crearTarjetaPlatillo(platillo);
            menuGrid.add(tarjeta, col, row);
            col++;
            if (col >= MAX_COLUMNS) {
                col = 0;
                row++;
            }
        }
    }


    private VBox crearTarjetaPlatillo(Platillo platillo) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #AAAAAA; -fx-border-radius: 5; -fx-background-color: white;");


        HBox header = new HBox();
        Label nombre = new Label(platillo.getNombre());
        nombre.setStyle("-fx-font-weight: bold;");


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        MenuButton opciones = new MenuButton("⋮");
        opciones.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        MenuItem editarItem = new MenuItem("Editar");
        editarItem.setOnAction(e -> abrirVentanaPlatillo(platillo));

        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(e -> eliminarPlatillo(platillo));

        opciones.getItems().addAll(editarItem, eliminarItem);
        header.getChildren().addAll(nombre, spacer, opciones);

        Label precio = new Label("$" + platillo.getPrecio());

        card.getChildren().addAll(header, new Label(platillo.getDescripcion()), precio);

        return card;
    }


    @FXML
    void abrirVentanaPlatillo() {
        abrirVentanaPlatillo(null);
    }


    private void abrirVentanaPlatillo(Platillo platilloAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/platillo-modificacion-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            ControladorPlatilloModal modalController = loader.getController();

            modalController.inicializar(platilloAEditar, servicioMenu, this);

            stage.setScene(new Scene(root));
            stage.setTitle(platilloAEditar == null ? "Crear nuevo platillo" : "editar Platillo");
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("FATAL: No se pudo cargar la vista modal. Revise la ruta del FXML.");
            e.printStackTrace();
        }
    }

    private void eliminarPlatillo(Platillo platillo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quieres eliminar el platillo " + platillo.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (servicioMenu.eliminarPlatillo(platillo.getId())) {
                System.out.println("Platillo eliminado: " + platillo.getNombre());
                cargarMenu(selectorMenu.getSelectionModel().getSelectedItem()); // Refrescar la vista
            } else {
                System.err.println("Error al eliminar platillo.");
            }
        }
    }


    public void refrescarVista() {
        cargarMenu(selectorMenu.getSelectionModel().getSelectedItem());
    }






    @FXML void gestionarVentas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/Gestion-corte.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) selectorMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Inventario");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error");
            e.printStackTrace();
        }

    }
    @FXML void gestionarInventario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/Gestion-inventario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) selectorMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Inventario");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error");
            e.printStackTrace();
        }
    }
    @FXML void confirmarPedido(ActionEvent event) {  }
}