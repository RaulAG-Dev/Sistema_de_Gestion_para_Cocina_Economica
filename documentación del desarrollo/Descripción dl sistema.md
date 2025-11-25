# Descripción del codigo

Este documento contiene una descripción detallada de todos los archivos Java encontrados en `src/main/java`, incluyendo una breve explicación de cada método.

## Estructura del Proyecto
El proyecto está organizado en los siguientes paquetes:
- `com.example.sistema.models`: Clases de dominio que representan los datos del sistema.
- `com.example.sistema.services`: Lógica de negocio y gestión de datos.
- `com.example.sistema.persistencia`: Clases para guardar y cargar datos (JSON).
- `com.example.sistema.controllers`: Controladores de la interfaz gráfica (JavaFX).
- `com.example.sistema`: Clases principales de inicio de la aplicación.

---

## 1. Modelos (`com.example.sistema.models`)

### `Cliente`
Representa un cliente del restaurante.
- **Métodos**:
  - `Cliente()`: Constructor vacío.
  - `Cliente(int, String, String, String)`: Constructor completo para inicializar todos los atributos.
  - `equals(Object)`: Compara si dos clientes son iguales basándose en su ID.
  - `hashCode()`: Genera un código hash basado en el ID.
  - `getTelefono()`: Retorna el número de teléfono del cliente.
  - `setTelefono(String)`: Actualiza el número de teléfono.
  - `getPreferencias()`: Retorna las preferencias o restricciones dietéticas.
  - `setPreferencias(String)`: Actualiza las preferencias.
  - `toString()`: Retorna una cadena con el nombre, teléfono y preferencias.

### `ClienteFrecuente`
Extiende de `Cliente`, usado para reportes de clientes con más pedidos.
- **Métodos**:
  - `ClienteFrecuente(Cliente, long)`: Constructor que copia datos de un `Cliente` base y añade la cantidad de pedidos.
  - `getCantidadPedidos()`: Retorna el número total de pedidos realizados por este cliente.

### `CorteCaja`
Representa el registro de cierre de caja.
- **Métodos**:
  - `CorteCaja()`: Constructor vacío.
  - `CorteCaja(int, Date, Usuario, float, float, float)`: Constructor completo.
  - `calcularDiferencia()`: Calcula y retorna la diferencia entre el monto final en caja y el esperado (`montoFinal - (montoInicial + totalVentas)`).
  - `getFecha()`, `setFecha(Date)`: Gestionan la fecha del corte.
  - `getMontoInicial()`, `setMontoInicial(float)`: Gestionan el dinero base en caja.
  - `getMontoFinal()`, `setMontoFinal(float)`: Gestionan el dinero contado al cierre.
  - `getVentas()`, `setVentas(float)`: Gestionan el total de ventas registradas por el sistema.
  - `getUsuario()`, `setUsuario(Usuario)`: Gestionan el usuario responsable del corte.

### `Ingrediente`
Representa un insumo para los platillos.
- **Métodos**:
  - `Ingrediente()`: Constructor vacío.
  - `Ingrediente(int, String, float, float, String)`: Constructor completo.
  - `actualizarStock(float)`: Suma (o resta si es negativo) la cantidad dada al stock actual. Lanza excepción si el resultado es negativo.
  - `getStockActual()`, `setStockActual(float)`: Gestionan la cantidad actual. Valida que no sea negativa.
  - `getStockMinimo()`, `setStockMinimo(float)`: Gestionan el nivel de alerta.
  - `getUnidades()`, `setUnidades(String)`: Gestionan la unidad de medida (ej. "kg").
  - `estaEnAlerta()`: Retorna `true` si el stock actual es menor o igual al mínimo.

### `ItemPedido`
Representa una línea en un pedido (Platillo + Cantidad).
- **Métodos**:
  - `ItemPedido()`: Constructor vacío.
  - `ItemPedido(Platillo, int, float)`: Constructor completo.
  - `getPlatillo()`, `setPlatillo(Platillo)`: Gestionan el platillo asociado.
  - `getCantidad()`, `setCantidad(int)`: Gestionan la cantidad solicitada.
  - `getPrecioUnitario()`, `setPrecioUnitario(float)`: Gestionan el precio al momento de la venta.
  - `calcularSubtotal()`: Retorna el resultado de `precioUnitario * cantidad`.
  - `toString()`: Retorna una cadena formateada "Nombre xCantidad $Subtotal".

### `ItemReceta`
Define qué ingrediente y cuánto se necesita para un platillo.
- **Métodos**:
  - `ItemReceta()`: Constructor vacío.
  - `ItemReceta(Ingrediente, float)`: Constructor completo.
  - `getIngrediente()`, `setIngrediente(Ingrediente)`: Gestionan el ingrediente requerido.
  - `getCantidadRequerida()`, `setCantidadRequerida(float)`: Gestionan la cantidad necesaria para una porción.

### `ModelManagement`
Clase base abstracta para modelos con ID y Nombre.
- **Métodos**:
  - `getId()`: Retorna el ID único.
  - `setId(int)`: Establece el ID.
  - `getNombre()`: Retorna el nombre descriptivo.
  - `setNombre(String)`: Establece el nombre.

### `Pedido`
Representa una venta realizada.
- **Métodos**:
  - `Pedido()`: Constructor vacío.
  - `getId()`, `setId(int)`: Gestionan el ID del pedido.
  - `getFechaHora()`, `setFechaHora(Date)`: Gestionan la fecha y hora de la venta.
  - `getNombre()`, `setNombre(String)`: Gestionan una descripción corta del pedido.
  - `getCliente()`, `setCliente(Cliente)`: Gestionan el cliente asociado.
  - `getItems()`, `setItems(List)`: Gestionan la lista de ítems vendidos.
  - `getTotal()`, `setTotal(float)`: Gestionan el monto total.
  - `isPagado()`, `setPagado(boolean)`: Gestionan el estado de pago.
  - `generarNombreDesdeItems()`: Construye un string con los nombres y cantidades de los platillos en el pedido.
  - `calcularTotal()`: Suma los subtotales de todos los ítems y actualiza el atributo `total`.

### `Platillo`
Representa un producto del menú.
- **Métodos**:
  - `Platillo()`: Constructor vacío.
  - `Platillo(int, String, String, float, boolean, List, String)`: Constructor completo.
  - `Platillo(Platillo)`: Constructor de copia.
  - `getReceta()`, `setReceta(List)`: Gestionan la lista de ingredientes requeridos.
  - `getTipoMenu()`, `setTipoMenu(String)`: Gestionan la categoría (Desayuno, etc.).
  - `getDescripcion()`, `setDescripcion(String)`: Gestionan la descripción del platillo.
  - `getPrecio()`, `setPrecio(float)`: Gestionan el precio de venta. Valida que sea positivo.
  - `getCantidadVendida()`, `setCantidadVendida(int)`: Gestionan estadísticas de venta.
  - `getIngresosGenerados()`, `setIngresosGenerados(float)`: Gestionan estadísticas de ingresos.
  - `isDisponible()`, `setDisponible(boolean)`: Gestionan si el platillo se puede vender.
  - `toString()`: Retorna una representación en cadena del platillo.

### `Usuario`
Representa un usuario del sistema (empleado/admin).
- **Métodos**:
  - `Usuario()`: Constructor vacío.
  - `Usuario(int, String, String, String)`: Constructor completo.
  - `validarContrasena(String)`: Retorna `true` si la contraseña dada coincide con la almacenada.
  - `getContrasena()`, `setContrasena(String)`: Gestionan la contraseña.
  - `getRol()`, `setRol(String)`: Gestionan el rol del usuario (ej. "Administrador").

---

## 2. Servicios (`com.example.sistema.services`)

### `ServicioCliente`
Gestión de clientes (Singleton).
- **Métodos**:
  - `getInstance()`: Retorna la única instancia de la clase.
  - `buscarClientePorNombre(String)`: Busca en la lista maestra un cliente que coincida con el nombre (ignora mayúsculas).
  - `obtenerClientesFrecuentes()`: Analiza los pedidos y retorna una lista de clientes con más de 5 pedidos, ordenada por frecuencia.
  - `obtenerTodos()`: Retorna la lista completa de clientes.
  - `guardar(Cliente)`: Agrega o actualiza un cliente en la lista y guarda en JSON. Genera ID si es nuevo.
  - `eliminar(int)`: Elimina un cliente por ID y actualiza el JSON.
  - `generarNuevoId()`: Calcula el siguiente ID disponible.
  - `buscarPorId(int)`: Busca un cliente específico por su ID numérico.

### `ServicioInventario`
Gestión de ingredientes (Singleton).
- **Métodos**:
  - `getInstance()`: Retorna la instancia Singleton.
  - `obtenerInventario()`: Retorna una copia de la lista de todos los ingredientes.
  - `obtenerAlertasStock()`: Retorna una lista de ingredientes cuyo stock es menor o igual al mínimo.
  - `actualizarIngrediente(Ingrediente)`: Actualiza los datos de un ingrediente existente y guarda.
  - `agregarIngrediente(Ingrediente)`: Asigna ID, agrega a la lista y guarda un nuevo ingrediente.
  - `eliminarIngrediente(int)`: Elimina un ingrediente por ID y guarda.
  - `restarCantidad(int, float)`: Busca un ingrediente y reduce su stock si hay suficiente. Retorna `true` si tuvo éxito.
  - `guardarStock()`: Fuerza el guardado de la lista actual al archivo JSON.
  - `buscarIngredientePorId(int)`: Busca un ingrediente por su ID.

### `ServicioMenu`
Gestión de platillos.
- **Métodos**:
  - `ServicioMenu()`: Constructor que inicializa el repositorio de platillos.
  - `obtenerPlatillosPorTipo(String)`: Retorna una lista de platillos filtrada por la categoría dada.
  - `guardarPlatillo(Platillo)`: Agrega o actualiza un platillo y persiste los cambios.
  - `eliminarPlatillo(int)`: Elimina un platillo por ID.

### `ServicioReportes`
Generación de reportes de ventas.
- **Métodos**:
  - `obtenerVentas(LocalDateTime, LocalDateTime)`: Filtra la lista maestra de pedidos para incluir solo los que están dentro del rango de fechas.
  - `obtenerReporteHoy()`: Retorna las ventas desde el inicio del día actual hasta el momento presente.
  - `obtenerReporteSemanal()`: Retorna las ventas desde el lunes de la semana actual.
  - `obtenerReporteMensual()`: Retorna las ventas desde el día 1 del mes actual.

### `ServicioUsuarios`
Autenticación de usuarios.
- **Métodos**:
  - `ServicioUsuarios()`: Constructor que carga los usuarios del JSON.
  - `autenticar(String, String)`: Itera sobre los usuarios y verifica nombre y contraseña. Retorna el `Usuario` si es válido, o lanza una excepción.

### `ServicioVentas`
Núcleo de la lógica de ventas (Singleton).
- **Métodos**:
  - `getInstance()`: Retorna la instancia Singleton.
  - `getPedidosMaestros()`: Retorna la lista de todos los pedidos en memoria.
  - `crearPedido()`: Inicializa un nuevo objeto `Pedido` vacío y lo establece como actual.
  - `agregarItemAlPedido(ItemPedido)`: Añade un ítem al pedido actual. Si el platillo ya existe, suma la cantidad.
  - `guardarPedido(Pedido)`: Orquesta el proceso de venta: calcula total, vincula cliente, deduce stock de ingredientes y guarda el pedido en JSON.
  - `obtenerVentasPorFecha(Date)`: Retorna los pedidos de un día específico.
  - `obtenerTotalDelDia(Date)`: Calcula la suma de los totales de los pedidos de un día.
  - `obtenerTodasLasVentas()`: Retorna una copia de todos los pedidos.
  - `getPedidoActual()`: Retorna el pedido que se está editando.
  - `obtenerPlatilloMasVendido()`: Analiza el historial y retorna el platillo con mayor cantidad vendida.
  - `obtenerRankingPlatillos()`: Retorna una lista de todos los platillos ordenados por ventas descendentes.
  - `obtenerRankingPlatillosPorFechas(LocalDateTime, LocalDateTime)`: Retorna el ranking de ventas filtrado por un rango de fechas.

---

## 3. Persistencia (`com.example.sistema.persistencia`)

### `RepositorioJSON<T>`
Clase genérica para guardar/cargar listas de objetos en archivos JSON.
- **Métodos**:
  - `RepositorioJSON(String, ConvertidorJSON)`: Constructor que carga los datos iniciales.
  - `cargarDatos()`: Lee el archivo JSON, parsea el contenido y llena la lista en memoria usando el convertidor.
  - `guardarDatos()`: Serializa la lista en memoria y escribe en el archivo JSON.
  - `guardarTodos(List)`: Reemplaza la lista en memoria con una nueva y guarda.
  - `buscarPorId(int)`: Busca un objeto por ID en la lista en memoria.
  - `guardar(T)`: Agrega o actualiza un objeto en la lista y guarda.
  - `eliminar(int)`: Elimina un objeto por ID y guarda.
  - `obtenerTodos()`: Retorna una copia de la lista de datos.
  - `generarNuevoId()`: Encuentra el ID más alto y retorna `max + 1`.

### Convertidores (`ConvertidorJSON<T>`)
- **Métodos comunes**:
  - `aJSON(T)`: Convierte un objeto Java a `JSONObject`.
  - `deJSON(JSONObject)`: Convierte un `JSONObject` a un objeto Java.
- **Detalles específicos**:
  - `ConvertidorCliente`: Mapea ID, nombre, teléfono, preferencias.
  - `ConvertidorIngrediente`: Mapea ID, nombre, stocks, unidades.
  - `ConvertidorPedido`: Mapea datos del pedido y anida el Cliente (ID/Nombre) y la lista de Items.
  - `ConvertidorPlatillo`: Mapea datos del platillo y la receta (guardando solo ID de ingrediente y cantidad). Al leer, usa `ServicioInventario` para recuperar el objeto `Ingrediente` completo.
  - `ConvertidorUsuario`: Mapea ID, nombre, contraseña, rol.

---

## 4. Controladores (`com.example.sistema.controllers`)

### `ControladorLogin`
Maneja la vista de inicio de sesión.
- **Métodos**:
  - `manejarLogin(ActionEvent)`: Lee usuario/pass, llama a `ServicioUsuarios.autenticar()` y carga la vista principal correspondiente al rol.
  - `cambiarEscena()`, `cambiarEscenaEmpleado()`: Cargan el FXML de la vista principal y cambian el Stage.

### `ControladorPrincipal`
Vista principal del Punto de Venta (PDV).
- **Métodos**:
  - `initialize()`: Configura el selector de menú y las columnas de la tabla de pedido.
  - `cargarMenu(String)`: Limpia y rellena el Grid con tarjetas de platillos según la categoría seleccionada.
  - `crearTarjetaPlatillo(Platillo)`: Genera el nodo gráfico (VBox) para un platillo.
  - `abrirVentanaPlatillo()`: Abre el modal para crear/editar platillos.
  - `eliminarPlatillo(Platillo)`: Pide confirmación y elimina el platillo.
  - `añadirItemAlPedido(Platillo)`: Agrega el platillo seleccionado a la tabla de pedido actual.
  - `actualizarTotal()`: Recalcula y muestra el total a pagar en la etiqueta.
  - `confirmarPedido()`: Crea el objeto Pedido final, lo guarda vía servicio y abre el ticket.
  - Métodos de navegación (`gestionar...`): Abren las ventanas secundarias (Inventario, Clientes, Corte, Historial).

### `ControladorInventario`
CRUD de Ingredientes.
- **Métodos**:
  - `initialize()`: Configura columnas y carga datos.
  - `cargarDatosYAlertas()`: Refresca la tabla y verifica stock bajo.
  - `mostrarAlertas()`: Genera un mensaje de texto con los ingredientes en alerta.
  - `agregarIngrediente()`, `guardarIngrediente()`: Leen los campos, validan y llaman al servicio.
  - `eliminarIngrediente()`: Elimina el ingrediente seleccionado.

### `ControladorGestionClientes`
CRUD de Clientes e historial.
- **Métodos**:
  - `initialize()`: Configura tabla de clientes y tabla de historial.
  - `cargarHistorialPedidos(Cliente)`: Filtra los pedidos del cliente seleccionado y los muestra.
  - `configurarColumnaAcciones()`: Añade botones de editar/eliminar a cada fila.
  - `guardarCliente()`: Actualiza el objeto cliente con los datos del formulario (nota: falta llamada explícita a guardar en servicio en el código analizado).
  - `prepararNuevoCliente()`: Limpia el formulario para un nuevo registro.

### `ControladorHistorialVentas`
Consulta de ventas pasadas.
- **Métodos**:
  - `initialize()`: Carga todos los pedidos en la tabla.
  - `reimprimirTicket()`: Genera una cadena de texto simulando el ticket del pedido seleccionado y lo muestra en el área de detalles.

### `ControladorCorte`
Realiza el corte de caja.
- **Métodos**:
  - `confirmarCorte()`: Lee los montos ingresados, obtiene el total del sistema del día, calcula la diferencia y abre la ventana del ticket de corte.
  - `abrirVentanaTicket(...)`: Carga el FXML del ticket y le pasa los datos calculados.

### `ControladorReportes`
Vista de reportes de ventas.
- **Métodos**:
  - `generarReporteHoy()`, `generarReporteMensualSeleccionado()`, `generarReportePersonalizado()`: Llaman a `ServicioReportes` con los rangos de fecha correspondientes y actualizan la tabla.
  - `mostrarReporte(List)`: Llena la tabla con los pedidos y calcula la suma total.
  - `mostrarPopularidad()`: Abre la ventana de ranking de platillos.

### `ControladorPopularidad`
Vista de ranking de platillos.
- **Métodos**:
  - `initialize()`: Carga combos de fecha y muestra ranking global.
  - `cargarRanking(List)`: Muestra la lista de platillos ordenada por ventas.
  - `generarReporte...()`: Filtra el ranking por fechas seleccionadas.

### `ControladorPlatilloModal`
Modal para platillos.
- **Métodos**:
  - `inicializar(Platillo, ...)`: Prepara el formulario con los datos del platillo (si es edición) o vacío.
  - `agregarIngrediente()`: Añade un ingrediente y cantidad a la lista temporal de la receta.
  - `guardar()`: Valida campos, crea/actualiza el objeto Platillo y llama al servicio.

### `ControladorTicketCorte` y `ControladorTicketPedido`
Visualización de tickets.
- **Métodos**:
  - `inicializarTicket...()`: Reciben los datos y construyen un `StringBuilder` con el formato de texto del ticket para mostrarlo en el `TextArea`.

---

## 5. Main (`com.example.sistema`)

### `Launcher`
- **Métodos**:
  - `main(String[])`: Método estático que llama a `Application.launch(MainApp.class)`.

### `MainApp`
- **Métodos**:
  - `start(Stage)`: Método de entrada de JavaFX. Carga el FXML de `LogginView`, configura la ventana (título, icono) y la muestra.
