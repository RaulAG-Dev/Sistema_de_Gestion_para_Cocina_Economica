package com.example.sistema.services;

import com.example.sistema.models.*;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorPedido;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Proporciona los **servicios de gestión y análisis** para los objetos {@link Pedido}.
 * <p>
 * Implementa el patrón **Singleton** para centralizar la lógica de ventas, la creación
 * de pedidos, la persistencia, la interacción con {@link ServicioInventario} para la deducción
 * de stock y la generación de estadísticas (ranking de platillos).
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 4.0
 * @since 2025-11-23
 */
public class ServicioVentas {

    private static ServicioVentas instance;
    /** Repositorio utilizado para la persistencia de los objetos Pedido. */
    private final RepositorioJSON<Pedido> repositorio;
    /** Lista maestra de todos los pedidos cargados en memoria. */
    private final List<Pedido> pedidosMemoria;
    /** El pedido que se está creando o modificando actualmente por el usuario. */
    private Pedido pedidoActual;

    /** Instancia del servicio de inventario utilizada para la deducción de stock. */
    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();
    /** Instancia del servicio de cliente utilizada para la gestión de clientes asociados a pedidos. */
    private final ServicioCliente servicioCliente = ServicioCliente.getInstance();

    /**
     * Constructor privado para forzar el patrón Singleton.
     * Inicializa el repositorio y carga los pedidos existentes en memoria.
     */
    private ServicioVentas() {
        this.repositorio = new RepositorioJSON<Pedido>("System_ek/src/main/data/pedidos.json", new ConvertidorPedido());
        // Manejo de null en caso de problemas con el repositorio o archivo
        this.pedidosMemoria = this.repositorio != null ? this.repositorio.obtenerTodos() : new ArrayList<>();
    }

    /**
     * Devuelve la única instancia de ServicioVentas (patrón Singleton).
     *
     * @return La única instancia activa de ServicioVentas.
     */
    public static ServicioVentas getInstance() {
        if (instance == null) {
            instance = new ServicioVentas();
        }
        return instance;
    }

    /**
     * Obtiene la lista maestra de todos los pedidos cargados en memoria.
     *
     * @return La lista de objetos {@link Pedido}.
     */
    public List<Pedido> getPedidosMaestros() {
        return pedidosMemoria;
    }

    /**
     * Inicia un nuevo pedido, lo asigna como el pedido actual y le asigna un ID temporal.
     *
     * @return La nueva instancia de {@link Pedido}.
     */
    public Pedido crearPedido() {
        pedidoActual = new Pedido();
        pedidoActual.setFechaHora(new Date());
        pedidoActual.setItems(new ArrayList<>());
        pedidoActual.setPagado(false);
        pedidoActual.setTotal(0f);

        // Genera un ID basado en el repositorio o en la memoria si el repositorio es nulo
        int nuevoId = repositorio != null ? repositorio.generarNuevoId() : generarIdMemoria();
        pedidoActual.setId(nuevoId);

        return pedidoActual;
    }

    /**
     * Agrega un ítem al pedido actual. Si el platillo ya existe en el pedido,
     * incrementa la cantidad; de lo contrario, añade el nuevo ítem.
     *
     * @param item El {@link ItemPedido} a añadir.
     */
    public void agregarItemAlPedido(ItemPedido item) {
        if (pedidoActual == null) crearPedido();
        List<ItemPedido> items = pedidoActual.getItems();

        // Buscar si el platillo ya está en el pedido
        for (ItemPedido it : items) {
            if (it.getPlatillo().getId() == item.getPlatillo().getId()) {
                it.setCantidad(it.getCantidad() + item.getCantidad());
                pedidoActual.generarNombreDesdeItems();
                pedidoActual.calcularTotal();
                return;
            }
        }

        // Si no está, añadirlo
        items.add(item);
        pedidoActual.calcularTotal();
        pedidoActual.generarNombreDesdeItems();
    }

    /**
     * Valida, persiste el pedido, actualiza la lista maestra en memoria y ejecuta
     * la deducción de stock de ingredientes.
     *
     * @param pedido El {@link Pedido} a guardar y procesar.
     */
    public void guardarPedido(Pedido pedido) {
        pedido.calcularTotal();
        pedido.generarNombreDesdeItems();
        vincularYGuardarCliente(pedido); // 1. Gestionar Cliente

        reemplazarOMeterMemoria(pedido);// 2. Actualizar memoria
        deducirStock(pedido);// 3. Deducir inventario

        // 4. Persistir
        if (repositorio != null) {
            repositorio.guardar(pedido);
            // Recargar la lista maestra para asegurar que todos los datos estén sincronizados
            pedidosMemoria.clear();
            pedidosMemoria.addAll(repositorio.obtenerTodos());
        }
        // Si el pedido guardado era el actual, lo reiniciamos
        if (pedidoActual == pedido) pedidoActual = null;
    }

    /**
     * Registra o actualiza el cliente asociado al pedido, usando ServicioCliente.
     * Asigna un nuevo ID si es un cliente nuevo, o vincula un cliente existente
     * si el nombre coincide.
     *
     * @param pedido El pedido cuyo cliente será gestionado.
     */
    private void vincularYGuardarCliente(Pedido pedido) {
        Cliente clientePedido = pedido.getCliente();

        if (clientePedido != null) {
            if (clientePedido.getId() <= 0) { // Cliente nuevo o sin ID
                // Es realmente nuevo, asignamos ID
                Cliente clienteExistente = servicioCliente.buscarClientePorNombre(clientePedido.getNombre());

                if (clienteExistente == null) {
                    clientePedido.setId(servicioCliente.generarNuevoId());
                } else {
                    // Ya existe, usamos el cliente maestro
                    pedido.setCliente(clienteExistente);
                    clientePedido = clienteExistente;
                }
            }
            // Guardamos o actualizamos el cliente en el repositorio de clientes
            servicioCliente.guardar(clientePedido);
        }
    }

    /**
     * Deduce la cantidad de ingredientes del inventario necesaria para todos
     * los ítems del pedido. Si el stock es insuficiente para algún ingrediente,
     * imprime una advertencia pero procede con el resto.
     *
     * @param pedido El pedido que dispara la deducción de stock.
     */
    private void deducirStock(Pedido pedido) {
        boolean stockCambiado = false;

        for (ItemPedido item : pedido.getItems()) {
            int cantidadVendida = item.getCantidad();
            Platillo platillo = item.getPlatillo();

            if (platillo != null && platillo.getReceta() != null) {

                for (ItemReceta itemReceta : platillo.getReceta()) {

                    float cantidadRequeridaPorUnidad = itemReceta.getCantidadRequerida();
                    float totalARestar = cantidadRequeridaPorUnidad * cantidadVendida;
                    // Llamada al servicio de inventario para restar
                    boolean restado = servicioInventario.restarCantidad(
                            itemReceta.getIngrediente().getId(),
                            totalARestar
                    );

                    if (restado) {
                        stockCambiado = true;
                    } else {
                        System.err.println("ADVERTENCIA: Stock insuficiente para " + itemReceta.getIngrediente().getNombre() + " en pedido " + pedido.getId());
                    }
                }
            }
        }
        // Guardar la lista de ingredientes solo una vez si hubo deducciones
        if (stockCambiado) {
            servicioInventario.guardarStock();
        }
    }

    /**
     * Obtiene todos los pedidos realizados en un día específico (ignora la hora).
     *
     * @param fechaDia La fecha del día a buscar.
     * @return Una lista de objetos {@link Pedido} de ese día.
     */
    public List<Pedido> obtenerVentasPorFecha(Date fechaDia) {
        return pedidosMemoria.stream()
                .filter(p -> esMismoDia(p.getFechaHora(), fechaDia))
                .collect(Collectors.toList());
    }

    /**
     * Calcula el monto total de ventas generado en un día específico.
     *
     * @param fechaDia La fecha del día a calcular.
     * @return El total de las ventas (float).
     */
    public float obtenerTotalDelDia(Date fechaDia) {
        return (float) obtenerVentasPorFecha(fechaDia).stream()
                .mapToDouble(Pedido::calcularTotal)
                .sum();
    }

    /**
     * Obtiene una lista con todos los pedidos en el sistema.
     *
     * @return Una nueva lista que contiene todos los {@link Pedido}s.
     */
    public List<Pedido> obtenerTodasLasVentas() {
        return new ArrayList<>(pedidosMemoria);
    }

    /**
     * Obtiene el pedido que se está editando o creando actualmente.
     *
     * @return El {@link Pedido} actual, o {@code null} si no hay ninguno activo.
     */
    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    /**
     * Reemplaza un pedido existente en memoria por uno actualizado (si los ID coinciden),
     * o agrega el pedido si es nuevo.
     *
     * @param pedido El pedido a reemplazar o añadir.
     */
    private void reemplazarOMeterMemoria(Pedido pedido) {
        for (int i = 0; i < pedidosMemoria.size(); i++) {
            if (pedidosMemoria.get(i).getId() == pedido.getId()) {
                pedidosMemoria.set(i, pedido);
                return;
            }
        }
        pedidosMemoria.add(pedido);
    }

    /**
     * Genera un ID secuencial basado en el ID máximo actual en memoria.
     * Utilizado solo si el repositorio es nulo (para IDs temporales).
     *
     * @return El siguiente ID disponible.
     */
    private int generarIdMemoria() {
        int max = 0;
        for (Pedido p : pedidosMemoria) {
            if (p.getId() > max) max = p.getId();
        }
        return max + 1;
    }

    /**
     * Verifica si dos objetos {@code Date} representan el mismo día, ignorando la hora.
     *
     * @param a La primera fecha.
     * @param b La segunda fecha.
     * @return {@code true} si son el mismo día; {@code false} en caso contrario.
     */
    private boolean esMismoDia(Date a, Date b) {
        if (a == null || b == null) return false;
        return truncarDia(a).getTime() == truncarDia(b).getTime();
    }

    /**
     * Trunca la hora de un objeto {@code Date} a medianoche (00:00:00.000).
     *
     * @param d La fecha a truncar.
     * @return La fecha truncada.
     */
    private Date truncarDia(Date d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Identifica el platillo más vendido de todo el historial de pedidos.
     *
     * @return El objeto {@link Platillo} más vendido (con cantidades e ingresos acumulados), o {@code null} si no hay ventas.
     */
    public Platillo obtenerPlatilloMasVendido() {
        Map<Integer, Platillo> mapa = new HashMap<>();

        for (Pedido pedido : pedidosMemoria) {
            for (ItemPedido item : pedido.getItems()) {
                Platillo platillo = item.getPlatillo();
                if (platillo != null) {
                    Platillo acumulado = mapa.get(platillo.getId());
                    if (acumulado == null) {
                        // Usar constructor de copia para mantener precio original
                        acumulado = new Platillo(platillo); // copia precio correcto
                    }
                    // Acumular estadísticas
                    acumulado.setCantidadVendida(acumulado.getCantidadVendida() + item.getCantidad());
                    acumulado.setIngresosGenerados(acumulado.getIngresosGenerados() + (item.getCantidad() * item.getPrecioUnitario()));

                    mapa.put(platillo.getId(), acumulado);
                }
            }
        }
        // Encontrar el máximo por cantidad vendida
        return mapa.values().stream()
                .max(Comparator.comparingInt(Platillo::getCantidadVendida))
                .orElse(null);
    }

    /**
     * Devuelve un ranking de todos los platillos vendidos,
     * ordenados de mayor a menor por cantidad vendida.
     *
     * @return Una lista de objetos {@link Platillo} con estadísticas de venta acumuladas.
     */
    public List<Platillo> obtenerRankingPlatillos() {
        Map<Integer, Platillo> mapa = new HashMap<>();

        for (Pedido pedido : pedidosMemoria) {
            for (ItemPedido item : pedido.getItems()) {
                Platillo platillo = item.getPlatillo();
                if (platillo != null) {
                    Platillo acumulado = mapa.get(platillo.getId());
                    if (acumulado == null) {
                        acumulado = new Platillo(platillo); // copia precio correcto
                    }
                    acumulado.setCantidadVendida(acumulado.getCantidadVendida() + item.getCantidad());
                    acumulado.setIngresosGenerados(acumulado.getIngresosGenerados() + (item.getCantidad() * item.getPrecioUnitario()));

                    mapa.put(platillo.getId(), acumulado);

                }
            }
        }
        // Ordenar de forma descendente por cantidad vendida
        return mapa.values().stream()
                .sorted(Comparator.comparingInt(Platillo::getCantidadVendida).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un ranking de platillos vendidos en un rango de fechas específico,
     * ordenados de mayor a menor por cantidad vendida.
     *
     * @param inicio La fecha y hora de inicio del rango (inclusivo).
     * @param fin La fecha y hora de fin del rango (inclusivo).
     * @return Una lista de objetos {@link Platillo} con estadísticas de venta acumuladas en ese rango.
     */
    public List<Platillo> obtenerRankingPlatillosPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        Map<Integer, Platillo> mapa = new HashMap<>();

        for (Pedido pedido : pedidosMemoria) {
            // Conversión de Date a LocalDateTime para comparación
            LocalDateTime fechaPedido = pedido.getFechaHora().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();

            // Filtrar por rango de fechas
            if (!fechaPedido.isBefore(inicio) && !fechaPedido.isAfter(fin)) {
                for (ItemPedido item : pedido.getItems()) {
                    Platillo platillo = item.getPlatillo();
                    if (platillo != null) {
                        Platillo acumulado = mapa.get(platillo.getId());
                        if (acumulado == null) {
                            acumulado = new Platillo(platillo); // copia precio correcto
                        }
                        // Acumular estadísticas
                        acumulado.setCantidadVendida(acumulado.getCantidadVendida() + item.getCantidad());
                        acumulado.setIngresosGenerados(acumulado.getIngresosGenerados() + (item.getCantidad() * item.getPrecioUnitario()));

                        mapa.put(platillo.getId(), acumulado);

                    }
                }
            }
        }
        // Ordenar de forma descendente por cantidad vendida
        return mapa.values().stream()
                .sorted(Comparator.comparingInt(Platillo::getCantidadVendida).reversed())
                .collect(Collectors.toList());
    }

}