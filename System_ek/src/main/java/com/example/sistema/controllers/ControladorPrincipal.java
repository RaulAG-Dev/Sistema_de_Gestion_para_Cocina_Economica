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
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista principal del sistema (PDV y Gestión).
 *
 * <p>Esta clase gestiona la interfaz principal de toma de pedidos, incluyendo la
 * visualización dinámica del menú, la gestión de la tabla de pedidos, el cálculo del
 * total a pagar y la navegación a otras vistas de gestión (Inventario, Corte de Caja).</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-02
 */
public class ControladorPrincipal implements Initializable {

    // --- Componentes FXML ---

    /** Selector para filtrar los platillos por tipo de menú (Desayunos, Almuerzos, Cenas). */
    @FXML private ChoiceBox<String> selectorMenu;
    /** Grid donde se insertarán dinámicamente las tarjetas de los platillos. */
    @FXML private GridPane menuGrid;
    /** Botón para abrir el modal de adición/modificación de platillos. */
    @FXML private Button añadirPlatilloButton;
    /** Campo de texto para ingresar el nombre del cliente asociado al pedido. */
    @FXML private TextField nombreClienteField;
    /** Etiqueta (Label) que muestra el monto total a pagar del pedido actual. */
    @FXML private Label totalPagarLabel;

    /** Tabla que lista todos los {@code ItemPedido} agregados al pedido actual. */
    @FXML private TableView<ItemPedido> pedidoTable;
    /** Columna de la tabla para el nombre del platillo. */
    @FXML private TableColumn<ItemPedido, String> nombreColumn;
    /** Columna de la tabla para la cantidad de unidades del platillo. */
    @FXML private TableColumn<ItemPedido, Integer> cantidadColumn;
    /** Columna de la tabla para el precio unitario del platillo. */
    @FXML private TableColumn<ItemPedido, Float> precioColumn;
    /** Columna de la tabla para el subtotal por item (cantidad * precio unitario). */
    @FXML private TableColumn<ItemPedido, Float> subtotalColumn;
    /** Columna de la tabla que contiene botones para modificar la cantidad del item. */
    @FXML private TableColumn<ItemPedido, Void> accionesColumn;

    // --- Variables de Estado y Servicios ---

    /** Lista observable de los items que componen el pedido actual. */
    private ObservableList<ItemPedido> itemsPedido = FXCollections.observableArrayList();
    /** Instancia del servicio para interactuar con la lógica de negocio de platillos. */
    private final ServicioMenu servicioMenu = new ServicioMenu();
    /** Arreglo estático que contiene los tipos de menú disponibles. */
    private final String[] TIPOS_MENU = {"Desayunos", "Almuerzos", "Cenas"};


    /**
     * Método de inicialización estándar de JavaFX.
     * <p>Configura el selector de menú, inicializa la tabla de pedidos y carga el menú inicial.</p>
     *
     * @param url La ubicación utilizada para resolver las rutas relativas.
     * @param resourceBundle Los recursos localizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Configuración del selector de menú y su listener
        selectorMenu.setItems(FXCollections.observableArrayList(TIPOS_MENU));
        selectorMenu.getSelectionModel().selectFirst();
        selectorMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarMenu(newValue);
            }
        });

        // 2. Carga inicial del menú
        cargarMenu(selectorMenu.getSelectionModel().getSelectedItem());

        // 3. Configuración de la tabla de pedidos
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlatillo().getNombre()));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        subtotalColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().calcularSubtotal()));
        pedidoTable.setItems(itemsPedido);
        configurarColumnaAcciones();
    }

    /**
     * Inicializa datos específicos pasados desde la escena de Login (ej. el usuario autenticado).
     * @param usuario El objeto {@code Usuario} que ha iniciado sesión.
     */
    public void inicializarDatos(Usuario usuario) {
        // Lógica para establecer el usuario actual, si es necesario.
    }


    /**
     * Carga y muestra las tarjetas de platillos en el {@code menuGrid} filtradas por el tipo de menú.
     *
     * @param tipoMenu La categoría de menú a cargar (ej. "Desayunos").
     */
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

    /**
     * Crea un componente visual (tarjeta VBox) para mostrar la información de un {@code Platillo}.
     * <p>Incluye botones para añadir al pedido y un menú de opciones para editar/eliminar.</p>
     *
     * @param platillo El objeto Platillo con los datos a mostrar.
     * @return El componente VBox que representa la tarjeta del platillo.
     */
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


    /**
     * Maneja el evento de clic del botón "Añadir Platillo" para abrir el modal de creación.
     */
    @FXML
    void abrirVentanaPlatillo() {
        abrirVentanaPlatillo(null);
    }


    /**
     * Abre la ventana modal para crear o editar un platillo.
     *
     * @param platilloAEditar El platillo a editar, o {@code null} si es un nuevo platillo.
     */
    private void abrirVentanaPlatillo(Platillo platilloAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/platillo-modificacion-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            ControladorPlatilloModal modalController = loader.getController();

            // Pasa las referencias necesarias al controlador del modal
            modalController.inicializar(platilloAEditar, servicioMenu, this);

            stage.setScene(new Scene(root));
            stage.setTitle(platilloAEditar == null ? "Crear nuevo platillo" : "editar Platillo");
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("FATAL: No se pudo cargar la vista modal. Revise la ruta del FXML.");
            e.printStackTrace();
        }
    }

    /**
     * Muestra una confirmación y elimina el platillo seleccionado si el usuario acepta.
     *
     * @param platillo El platillo a ser eliminado.
     */
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


    /**
     * Refresca la vista del menú recargando los platillos de la categoría actualmente seleccionada.
     * <p>Es llamado desde el modal de edición/creación.</p>
     */
    public void refrescarVista() {
        cargarMenu(selectorMenu.getSelectionModel().getSelectedItem());
    }

    /**
     * Añade un {@code Platillo} al pedido actual como un {@code ItemPedido}.
     * <p>Si el platillo ya existe en la lista, incrementa la cantidad; de lo contrario, añade un nuevo item.</p>
     *
     * @param platillo El platillo que se va a añadir al pedido.
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
     * Calcula la suma de todos los subtotales en el pedido y actualiza la etiqueta {@code totalPagarLabel}.
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
     * Configura el renderizado de la columna de acciones en la tabla de pedidos,
     * añadiendo botones de (+) y (-) para modificar la cantidad de cada item.
     */
    private void configurarColumnaAcciones() {
        accionesColumn.setCellFactory(tc -> new TableCell<ItemPedido, Void>() {
            final Button btnRestar = new Button("-");
            final Button btnSumar = new Button("+");
            final HBox pane = new HBox(5, btnRestar, btnSumar);

            {
                // Listener para el botón Restar
                btnRestar.setOnAction(event -> {
                    ItemPedido item = getTableView().getItems().get(getIndex());
                    if (item.getCantidad() > 1) {
                        item.setCantidad(item.getCantidad() - 1);
                    } else {
                        itemsPedido.remove(item); // Elimina el item si la cantidad es 1
                    }
                    getTableView().refresh();
                    actualizarTotal();
                });

                // Listener para el botón Sumar
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

    /**
     * Maneja el evento para navegar a la vista de Gestión de Ventas/Corte de Caja.
     */
    @FXML void gestionarVentas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/Gestion-corte.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) selectorMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Corte de Caja");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Corte de Caja.");
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento para navegar a la vista de Gestión de Inventario.
     */
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
            System.err.println("Error al cargar la vista de Inventario.");
            e.printStackTrace();
        }
    }


    // --- MÉTODO FALTANTE AÑADIDO ---
    // Este método era el que buscaba tu FXML (principal-view.fxml:29) y causaba el error.

    /**
     * Maneja el evento para navegar a la vista de Gestión de Clientes.
     * @param event El evento de acción.
     */
    @FXML
    void gestionarClientes(ActionEvent event) {
        System.out.println("Navegando a la vista de gestión de clientes...");

        // (Opcional) Aquí puedes poner la lógica para cargar la nueva vista
        try {
            // Asegúrate de que el FXML se llame "Gestion-clientes.fxml" o como sea
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/Gestion-clientes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) selectorMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Clientes");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Clientes.");
            e.printStackTrace();
        }
    }


    // --- MÉTODO AÑADIDO --- (Este ya lo tenías)
    /**
     * Maneja el evento para navegar a la vista de Historial de Ventas.
     * @param event El evento de acción.
     */
    @FXML
    void verHistorialVentas(ActionEvent event) {
        try {
            // Asegúrate de que el FXML se llame "Historial-ventas.fxml" o como lo hayas guardado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/Historial-ventas.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual
            Stage stage = (Stage) selectorMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Historial de Ventas");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Historial de Ventas.");
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic para confirmar el pedido actual.
     * <p>Este método contendría la lógica para guardar el pedido completo en la base de datos
     * (Pedido, ItemsPedido, Cliente, etc.) y limpiar la vista para un nuevo pedido.</p>
     *
     * @param event El evento de acción.
     */
    @FXML void confirmarPedido(ActionEvent event) {
        // Lógica para guardar el pedido (Pedido, ItemsPedido, Cliente, etc.)
        System.out.println("Pedido confirmado para el cliente: " + nombreClienteField.getText());

        // (Opcional) Limpiar la vista después de confirmar
        // itemsPedido.clear();
        // nombreClienteField.clear();
        // actualizarTotal();
    }
}