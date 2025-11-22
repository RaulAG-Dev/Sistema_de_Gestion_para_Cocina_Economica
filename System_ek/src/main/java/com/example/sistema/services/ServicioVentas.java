package com.example.sistema.services;

import com.example.sistema.models.*;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorPedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioVentas {

    private static ServicioVentas instance;
    private final RepositorioJSON<Pedido> repositorio;
    private final List<Pedido> pedidosMemoria;
    private Pedido pedidoActual;

    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();
    private final ServicioCliente servicioCliente = ServicioCliente.getInstance();

    /**
     * Constructor privado para forzar el patrón Singleton.
     */
    private ServicioVentas() {
        this.repositorio = new RepositorioJSON<Pedido>("System_ek/src/main/data/pedidos.json", new ConvertidorPedido());
        this.pedidosMemoria = this.repositorio != null ? this.repositorio.obtenerTodos() : new ArrayList<>();
    }

    /**
     * Devuelve la única instancia de ServicioVentas (patrón Singleton).
     */
    public static ServicioVentas getInstance() {
        if (instance == null) {
            instance = new ServicioVentas();
        }
        return instance;
    }

    public List<Pedido> getPedidosMaestros() {
        return pedidosMemoria;
    }

    public Pedido crearPedido() {
        pedidoActual = new Pedido();
        pedidoActual.setFechaHora(new Date());
        pedidoActual.setItems(new ArrayList<>());
        pedidoActual.setPagado(false);
        pedidoActual.setTotal(0f);

        int nuevoId = repositorio != null ? repositorio.generarNuevoId() : generarIdMemoria();
        pedidoActual.setId(nuevoId);

        return pedidoActual;
    }

    public void agregarItemAlPedido(ItemPedido item) {
        if (pedidoActual == null) crearPedido();
        List<ItemPedido> items = pedidoActual.getItems();
        for (ItemPedido it : items) {
            if (it.getPlatillo().getId() == item.getPlatillo().getId()) {
                it.setCantidad(it.getCantidad() + item.getCantidad());
                pedidoActual.generarNombreDesdeItems();
                pedidoActual.calcularTotal();
                return;
            }
        }
        items.add(item);
        pedidoActual.calcularTotal();
        pedidoActual.generarNombreDesdeItems();
    }

    /**
     * Valida, persiste el pedido y ejecuta la deducción de stock.
     */
    public void guardarPedido(Pedido pedido) {
        pedido.calcularTotal();
        pedido.generarNombreDesdeItems();
        vincularYGuardarCliente(pedido);

        reemplazarOMeterMemoria(pedido);
        deducirStock(pedido);

        if (repositorio != null) {
            repositorio.guardar(pedido);
            pedidosMemoria.clear();
            pedidosMemoria.addAll(repositorio.obtenerTodos());
        }
        if (pedidoActual == pedido) pedidoActual = null;
    }

    /**
     * Registra o actualiza el cliente asociado al pedido, usando ServicioCliente.
     */
    private void vincularYGuardarCliente(Pedido pedido) {
        Cliente clientePedido = pedido.getCliente();

        if (clientePedido != null) {
            if (clientePedido.getId() <= 0) {

                Cliente clienteExistente = servicioCliente.buscarClientePorNombre(clientePedido.getNombre());

                if (clienteExistente == null) {
                    clientePedido.setId(servicioCliente.generarNuevoId());
                } else {
                    pedido.setCliente(clienteExistente);
                    clientePedido = clienteExistente;
                }
            }
            servicioCliente.guardar(clientePedido);
        }
    }

    private void deducirStock(Pedido pedido) {
        boolean stockCambiado = false;

        for (ItemPedido item : pedido.getItems()) {
            int cantidadVendida = item.getCantidad();
            Platillo platillo = item.getPlatillo();

            if (platillo != null && platillo.getReceta() != null) {

                for (ItemReceta itemReceta : platillo.getReceta()) {

                    float cantidadRequeridaPorUnidad = itemReceta.getCantidadRequerida();
                    float totalARestar = cantidadRequeridaPorUnidad * cantidadVendida;
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
        if (stockCambiado) {
            servicioInventario.guardarStock();
        }
    }

    public List<Pedido> obtenerVentasPorFecha(Date fechaDia) {
        return pedidosMemoria.stream()
                .filter(p -> esMismoDia(p.getFechaHora(), fechaDia))
                .collect(Collectors.toList());
    }

    public float obtenerTotalDelDia(Date fechaDia) {
        return (float) obtenerVentasPorFecha(fechaDia).stream()
                .mapToDouble(Pedido::calcularTotal)
                .sum();
    }

    public List<Pedido> obtenerTodasLasVentas() {
        return new ArrayList<>(pedidosMemoria);
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    private void reemplazarOMeterMemoria(Pedido pedido) {
        for (int i = 0; i < pedidosMemoria.size(); i++) {
            if (pedidosMemoria.get(i).getId() == pedido.getId()) {
                pedidosMemoria.set(i, pedido);
                return;
            }
        }
        pedidosMemoria.add(pedido);
    }

    private int generarIdMemoria() {
        int max = 0;
        for (Pedido p : pedidosMemoria) {
            if (p.getId() > max) max = p.getId();
        }
        return max + 1;
    }

    private boolean esMismoDia(Date a, Date b) {
        if (a == null || b == null) return false;
        return truncarDia(a).getTime() == truncarDia(b).getTime();
    }

    private Date truncarDia(Date d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}