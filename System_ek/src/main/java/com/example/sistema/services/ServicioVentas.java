package com.example.sistema.services;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Pedido;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorPedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioVentas {

    private static ServicioVentas instance;
    // Se elimina pedidosMaestros, ya que pedidosMemoria cumple la misma función.
    private final RepositorioJSON<Pedido> repositorio;
    private final List<Pedido> pedidosMemoria;
    private Pedido pedidoActual;

    /**
     * Constructor privado para forzar el patrón Singleton.
     * Inicializa las dependencias (repositorio y datos) internamente.
     */
    private ServicioVentas() {
        // Inicializa el RepositorioJSON con los argumentos necesarios (nombre de archivo y convertidor)
        this.repositorio = new RepositorioJSON<Pedido>("System_ek/src/main/data/pedidos.json", new ConvertidorPedido());

        // Inicializa la lista maestra de pedidos cargando desde el repositorio (si existe)
        this.pedidosMemoria = this.repositorio != null ? this.repositorio.obtenerTodos() : new ArrayList<>();
    }

    // El constructor original con argumentos (que causaba errores) ha sido eliminado/reemplazado.

    /**
     * Devuelve la única instancia de ServicioVentas (patrón Singleton).
     */
    public static ServicioVentas getInstance() {
        if (instance == null) {
            instance = new ServicioVentas(); // Llama al constructor sin argumentos
        }
        return instance;
    }

    public List<Pedido> getPedidosMaestros() {
        // Usa la lista que contiene los datos de la aplicación
        return pedidosMemoria;
    }

    public Pedido crearPedido() {
        pedidoActual = new Pedido();
        pedidoActual.setFechaHora(new Date());
        pedidoActual.setItems(new ArrayList<>());
        pedidoActual.setPagado(false);
        pedidoActual.setTotal(0f);

        // Si el repositorio es null (lo cual no debería ocurrir con la nueva inicialización),
        // se usa el generador de IDs en memoria
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

    public void guardarPedido(Pedido pedido) {
        pedido.calcularTotal();
        pedido.generarNombreDesdeItems();
        reemplazarOMeterMemoria(pedido);
        if (repositorio != null) {
            repositorio.guardar(pedido);
            // Actualizar la lista en memoria si la persistencia fue exitosa
            pedidosMemoria.clear();
            pedidosMemoria.addAll(repositorio.obtenerTodos());
        }
        if (pedidoActual == pedido) pedidoActual = null;
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

    // ----------------- utilidades internas -----------------
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