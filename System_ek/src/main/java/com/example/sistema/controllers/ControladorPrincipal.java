package com.example.sistema.controllers;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Platillo;
import com.example.sistema.models.Usuario;
import com.example.sistema.services.ServicioMenu;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorPrincipal implements Initializable {

    @FXML private ChoiceBox<String> selectorMenu;
    @FXML private GridPane menuGrid;
    @FXML private Button añadirPlatilloButton;
    @FXML private TextField nombreClienteField;
    @FXML private Label totalPagarLabel;

    @FXML private TableView<ItemPedido> pedidoTable;
    @FXML private TableColumn<ItemPedido, String> nombreColumn;
    @FXML private TableColumn<ItemPedido, Integer> cantidadColumn;
    @FXML private TableColumn<ItemPedido, Float> precioColumn; // Precio Unitario
    @FXML private TableColumn<ItemPedido, Float> subtotalColumn;
    @FXML private TableColumn<ItemPedido, Void> accionesColumn; // Columna para los botones


    private ObservableList<ItemPedido> itemsPedido = FXCollections.observableArrayList();
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

        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlatillo().getNombre())); // Usa el nombre del platillo
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));

        subtotalColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().calcularSubtotal()));
        pedidoTable.setItems(itemsPedido);
        configurarColumnaAcciones();
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
        card.setPrefSize(180, 180);
        card.setStyle("-fx-border-color: #DDDDDD; -fx-border-radius: 5; -fx-background-color: white;");

        HBox header = new HBox(5);

        Label nombre = new Label(platillo.getNombre());
        nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        MenuButton opciones = new MenuButton("⋮");
        opciones.setStyle("-fx-padding: 0; -fx-background-color: transparent; -fx-font-size: 14px;");

        MenuItem editarItem = new MenuItem("Editar");
        editarItem.setOnAction(e -> abrirVentanaPlatillo(platillo));

        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(e -> eliminarPlatillo(platillo));
        opciones.getItems().addAll(editarItem, eliminarItem);
        header.getChildren().addAll(nombre, spacer, opciones);


        Label descripcion = new Label(platillo.getDescripcion());
        descripcion.setWrapText(true);

        Label precio = new Label(String.format("$%.2f", platillo.getPrecio()));
        precio.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");


        Button botonAñadir = new Button("Añadir al pedido");
        botonAñadir.setMaxWidth(Double.MAX_VALUE);
        botonAñadir.setOnAction(e -> añadirItemAlPedido(platillo));


        card.getChildren().addAll(header, descripcion, precio, new Region(), botonAñadir);
        VBox.setVgrow(card.getChildren().get(3), Priority.ALWAYS);

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

    /**
     * Conecta el botón "Añadir al pedido" de la tarjeta con el pedido actual.
     * Este método será llamado por el botón dentro de la tarjeta del platillo.
     */
    public void añadirItemAlPedido(Platillo platillo) {
        for (ItemPedido item : itemsPedido) {
            if (item.getPlatillo().getId() == platillo.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                pedidoTable.refresh();
                actualizarTotal();
                return;
            }
        }

        ItemPedido nuevoItem = new ItemPedido(platillo, 1, platillo.getPrecio());
        itemsPedido.add(nuevoItem);
        actualizarTotal();
    }

    /**
     * Calcula y actualiza el label del total.
     */
    private void actualizarTotal() {
        float total = 0;
        for (ItemPedido item : itemsPedido) {
            total += item.calcularSubtotal();
        }
        // Formatear y mostrar el total
        totalPagarLabel.setText(String.format("$%.2f", total));
    }

    /**
     * Configura la columna de botones (+ y -) de la tabla de pedidos.
     */
    private void configurarColumnaAcciones() {
        accionesColumn.setCellFactory(tc -> new TableCell<ItemPedido, Void>() {
            final Button btnRestar = new Button("-");
            final Button btnSumar = new Button("+");
            final HBox pane = new HBox(5, btnRestar, btnSumar);

            {
                btnRestar.setOnAction(event -> {
                    ItemPedido item = getTableView().getItems().get(getIndex());
                    if (item.getCantidad() > 1) {
                        item.setCantidad(item.getCantidad() - 1);
                    } else {
                        itemsPedido.remove(item);
                    }
                    getTableView().refresh();
                    actualizarTotal();
                });

                btnSumar.setOnAction(event -> {
                    ItemPedido item = getTableView().getItems().get(getIndex());
                    item.setCantidad(item.getCantidad() + 1);
                    getTableView().refresh();
                    actualizarTotal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
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