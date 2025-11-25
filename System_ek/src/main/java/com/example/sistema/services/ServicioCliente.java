package com.example.sistema.services;

import com.example.sistema.models.Cliente;
import com.example.sistema.models.ClienteFrecuente;
import com.example.sistema.models.Pedido;
import com.example.sistema.persistencia.ConvertidorCliente;
import com.example.sistema.persistencia.RepositorioJSON;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Proporciona los **servicios de gestión y consulta** para los objetos {@link Cliente}.
 * <p>
 * Implementa el patrón **Singleton** para garantizar una única instancia de servicio
 * y gestiona la persistencia a través de {@link RepositorioJSON}.
 * Incluye lógica de negocio, como la identificación de clientes frecuentes.
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 2.0
 * @since 2025-11-22
 */
public class ServicioCliente {

    private static ServicioCliente instance;
    /** Repositorio utilizado para la persistencia de los objetos Cliente. */
    private final RepositorioJSON<Cliente> repositorio;
    /** Lista maestra de todos los clientes cargados al inicio del sistema. */
    private final List<Cliente> clientesMaestros;

    /**
     * Constructor privado: NO llama a ServicioVentas directamente.
     * Inicializa solo sus propias dependencias (el repositorio y la lista maestra).
     */
    private ServicioCliente() {
        this.repositorio = new RepositorioJSON<>("System_ek/src/main/data/clientes.json", new ConvertidorCliente());
        this.clientesMaestros = this.repositorio.obtenerTodos();
    }

    /**
     * Devuelve la única instancia de ServicioCliente (patrón Singleton).
     *
     * @return La única instancia activa de ServicioCliente.
     */
    public static ServicioCliente getInstance() {
        if (instance == null) {
            instance = new ServicioCliente();
        }
        return instance;
    }

    /**
     * Busca un cliente por su nombre, ignorando mayúsculas/minúsculas y espacios.
     *
     * @param nombre El nombre del cliente a buscar.
     * @return El objeto {@link Cliente} si se encuentra, o {@code null} en caso contrario.
     */
    public Cliente buscarClientePorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return null;

        return clientesMaestros.stream()
                .filter(c -> c.getNombre() != null && c.getNombre().trim().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene una lista de los clientes más frecuentes basándose en el historial de pedidos.
     * <p>
     * La lista incluye solo clientes que han realizado **más de 5 pedidos**
     * y está ordenada de forma descendente por la cantidad de pedidos.
     *
     * @return Una lista de objetos {@link ClienteFrecuente}.
     */
    public List<ClienteFrecuente> obtenerClientesFrecuentes() {
        // Se obtiene la instancia de ServicioVentas, asumiendo que ya fue inicializado
        ServicioVentas servicioVentas = ServicioVentas.getInstance();
        List<Pedido> todosLosPedidos = servicioVentas.getPedidosMaestros();

        // 1. Contar pedidos por cliente
        Map<Cliente, Long> conteoPorCliente = todosLosPedidos.stream()
                .filter(p -> p.getCliente() != null)
                .collect(Collectors.groupingBy(
                        Pedido::getCliente,
                        Collectors.counting()
                ));

        // 2. Filtrar, ordenar y mapear a ClienteFrecuente
        return conteoPorCliente.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(entry -> entry.getValue() > 5)
                .map(entry -> new ClienteFrecuente(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista completa y maestra de todos los clientes cargados en memoria.
     *
     * @return La lista de objetos {@link Cliente}.
     */
    public List<Cliente> obtenerTodos() {
        return clientesMaestros;
    }

    /**
     * Guarda un cliente nuevo o actualiza uno existente en la lista maestra y en el repositorio.
     * <p>
     * Si el ID del cliente es menor o igual a 0, se le asigna un nuevo ID.
     *
     * @param cliente El objeto {@link Cliente} a guardar o actualizar.
     */
    public void guardar(Cliente cliente) {
        if (cliente.getId() <= 0) {
            cliente.setId(generarNuevoId());
        }

        // Eliminar el cliente existente si lo hay (para actualización)
        clientesMaestros.removeIf(c -> c.getId() == cliente.getId());
        // Añadir la versión actualizada o nueva
        clientesMaestros.add(cliente);

        // Persistir en el repositorio
        repositorio.guardarTodos(clientesMaestros);
    }

    /**
     * Elimina un cliente de la lista maestra y del repositorio por su ID.
     *
     * @param id El ID del cliente a eliminar.
     * @return {@code true} si el cliente fue encontrado y eliminado; {@code false} en caso contrario.
     */
    public boolean eliminar(int id) {
        boolean eliminado = clientesMaestros.removeIf(c -> c.getId() == id);
        if (eliminado) {
            repositorio.guardarTodos(clientesMaestros);
        }
        return eliminado;
    }

    /**
     * Genera un nuevo ID único para un cliente.
     *
     * @return El nuevo ID entero.
     */
    public int generarNuevoId() {
        return repositorio.generarNuevoId();
    }

    /**
     * Busca un cliente en la lista maestra por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return El objeto {@link Cliente} si se encuentra, o {@code null} en caso contrario.
     */
    public Cliente buscarPorId(int id) {
        return clientesMaestros.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}