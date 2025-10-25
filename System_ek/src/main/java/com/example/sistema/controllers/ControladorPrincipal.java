package com.example.sistema.controllers;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Platillo;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ControladorPrincipal {

    @FXML
    private GridPane menuGrid;
    @FXML
    private TextField nombreClienteField;


    @FXML
    private TableView<ItemPedido> pedidoTable; // La tabla ahora usa ItemPedido
    @FXML
    private TableColumn<ItemPedido, String> nombreColumn; // Columna de Nombre
    @FXML
    private TableColumn<ItemPedido, Integer> cantidadColumn; // Columna de Cantidad
    @FXML
    private TableColumn<ItemPedido, Float> precioColumn; // Columna de Precio
    @FXML
    private TableColumn<ItemPedido, Float> subtotalColumn; // Columna de Subtotal
    @FXML
    private TableColumn<ItemPedido, Void> accionesColumn; // Columna de Acciones
    // --- Fin de conexiones de tabla ---

    @FXML
    private Label totalPagarLabel;

    private ObservableList<Platillo> menuDelDia = FXCollections.observableArrayList();
    private ObservableList<ItemPedido> pedidoActual = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cargarMenuDelDia();
        mostrarMenuEnGrid();
        inicializarTablaPedido();
        actualizarTotalPagar();
    }


    private void cargarMenuDelDia() {
        menuDelDia.addAll(
                new Platillo(1, "Chile Poblano", "4pz de chiles poblanos", 135.00f, true, new ArrayList<>()),
                new Platillo(2, "Caldo de Verduras", "2 litros de caldo de verduras", 135.00f, true, new ArrayList<>()),
                new Platillo(3, "Tamales Horneados", "3 pz de tamales horneados", 60.00f, true, new ArrayList<>()),
                new Platillo(4, "Caldo de Camarón", "2 litros de caldo de camarón", 230.00f, true, new ArrayList<>()),
                new Platillo(5, "Mole Poblano", "2 litros de mole poblano", 150.00f, false, new ArrayList<>())
        );
    }


    private void mostrarMenuEnGrid() {
        menuGrid.getChildren().clear();
        int col = 0;
        int row = 0;

        for (Platillo platillo : menuDelDia) {
            if (platillo.isDisponible()) {
                VBox platilloCard = crearTarjetaPlatillo(platillo);
                menuGrid.add(platilloCard, col, row);

                col++;
                if (col == 2) { col = 0; row++; }
            }
        }
    }


    private VBox crearTarjetaPlatillo(Platillo platillo) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: #DDDDDD; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: white;");
        card.setPrefSize(200, 200);

        Label nombreLabel = new Label(platillo.getNombre());
        nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label descripcionLabel = new Label(platillo.getDescripcion());
        descripcionLabel.setWrapText(true);
        Label precioLabel = new Label("$" + String.format("%.2f", platillo.getPrecio()));
        precioLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #007bff;");
        Button addButton = new Button("Añadir al pedido");
        addButton.setOnAction(e -> añadirAlPedido(platillo));

        card.getChildren().addAll(nombreLabel, descripcionLabel, precioLabel, addButton);
        return card;
    }


    private void añadirAlPedido(Platillo platilloSeleccionado) {

        for (ItemPedido item : pedidoActual) {
            if (item.getPlatillo().getId() == platilloSeleccionado.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                pedidoTable.refresh();
                actualizarTotalPagar();
                return;
            }
        }


        ItemPedido nuevoItem = new ItemPedido(platilloSeleccionado, 1, platilloSeleccionado.getPrecio());
        pedidoActual.add(nuevoItem);

        actualizarTotalPagar();
    }


    private void inicializarTablaPedido() {


        nombreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlatillo().getNombre())
        );


        cantidadColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCantidad())
        );


        precioColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPrecioUnitario())
        );


        subtotalColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().calcularSubtotal())
        );


        accionesColumn.setCellFactory(param -> new TableCell<ItemPedido, Void>() {
            final Button btnMas = new Button("+");
            final Button btnMenos = new Button("-");
            final HBox pane = new HBox(5, btnMenos, btnMas);

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ItemPedido pedidoItem = getTableView().getItems().get(getIndex());

                    btnMas.setOnAction(event -> {
                        pedidoItem.setCantidad(pedidoItem.getCantidad() + 1);
                        getTableView().refresh();
                        actualizarTotalPagar();
                    });

                    btnMenos.setOnAction(event -> {
                        if (pedidoItem.getCantidad() > 1) {
                            pedidoItem.setCantidad(pedidoItem.getCantidad() - 1);
                        } else {
                            pedidoActual.remove(pedidoItem);
                        }
                        getTableView().refresh();
                        actualizarTotalPagar();
                    });
                    setGraphic(pane);
                }
            }
        });

        pedidoTable.setItems(pedidoActual);
    }

    private void actualizarTotalPagar() {
        float total = 0.0f;
        for (ItemPedido item : pedidoActual) {
            total += item.calcularSubtotal();
        }
        totalPagarLabel.setText(String.format("$%.2f", total));
    }

    @FXML
    private void confirmarPedido(ActionEvent event) {
        if (nombreClienteField.getText().isEmpty() || pedidoActual.isEmpty()) {
            System.out.println("No se puede confirmar el pedido: Cliente o pedido vacío.");
            return;
        }

        System.out.println("Pedido confirmado para el cliente: " + nombreClienteField.getText());
        for (ItemPedido item : pedidoActual) {
            System.out.println("- " + item.toString());
        }
        System.out.println("Total: " + totalPagarLabel.getText());


        pedidoActual.clear();
        nombreClienteField.clear();
        actualizarTotalPagar();
    }

    @FXML
    private void gestionarVentas(ActionEvent event) {
        System.out.println("Botón 'Gestión de ventas' presionado.");
    }

    @FXML
    private void gestionarInventario(ActionEvent event) {
        System.out.println("Botón 'Gestión Inventario' presionado.");
    }
}