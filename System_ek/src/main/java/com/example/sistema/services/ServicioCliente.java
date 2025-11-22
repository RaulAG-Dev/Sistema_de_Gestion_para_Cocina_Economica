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

public class ServicioCliente {

    private static ServicioCliente instance;
    private final RepositorioJSON<Cliente> repositorio;
    private final List<Cliente> clientesMaestros;

    /**
     * Constructor privado: NO llama a ServicioVentas.
     * Inicializa solo sus propias dependencias.
     */
    private ServicioCliente() {
        this.repositorio = new RepositorioJSON<>("System_ek/src/main/data/clientes.json", new ConvertidorCliente());
        this.clientesMaestros = this.repositorio.obtenerTodos();
    }

    /**
     * Devuelve la única instancia de ServicioCliente (patrón Singleton).
     */
    public static ServicioCliente getInstance() {
        if (instance == null) {
            instance = new ServicioCliente();
        }
        return instance;
    }

    /**
     * Sincroniza la lista maestra de clientes con todos los clientes encontrados
     * en el historial de Pedidos. Debe ser llamado UNA VEZ al inicio del programa
     * después de que ServicioVentas se haya inicializado.

    public void sincronizarClientesDesdePedidos() {
        ServicioVentas servicioVentas = ServicioVentas.getInstance();

        List<Pedido> todosLosPedidos = servicioVentas.getPedidosMaestros();

        for (Pedido pedido : todosLosPedidos) {
            Cliente clientePedido = pedido.getCliente();

            if (clientePedido != null) {
                Cliente clienteExistente = buscarPorId(clientePedido.getId());
                if (clienteExistente == null) {
                    guardar(clientePedido);
                }
            }
        }
    }*/

    /**
     * Busca un cliente por su nombre, ignorando mayúsculas/minúsculas.
     */
    public Cliente buscarClientePorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return null;

        return clientesMaestros.stream()
                .filter(c -> c.getNombre() != null && c.getNombre().trim().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<ClienteFrecuente> obtenerClientesFrecuentes() {
        ServicioVentas servicioVentas = ServicioVentas.getInstance();
        List<Pedido> todosLosPedidos = servicioVentas.getPedidosMaestros();

        Map<Cliente, Long> conteoPorCliente = todosLosPedidos.stream()
                .filter(p -> p.getCliente() != null)
                .collect(Collectors.groupingBy(
                        Pedido::getCliente,
                        Collectors.counting()
                ));

        return conteoPorCliente.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(entry -> entry.getValue() > 5)
                .map(entry -> new ClienteFrecuente(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    public List<Cliente> obtenerTodos() {
        return clientesMaestros;
    }

    public void guardar(Cliente cliente) {
        if (cliente.getId() <= 0) {
            cliente.setId(generarNuevoId());
        }

        clientesMaestros.removeIf(c -> c.getId() == cliente.getId());
        clientesMaestros.add(cliente);

        repositorio.guardarTodos(clientesMaestros);
    }

    public boolean eliminar(int id) {
        boolean eliminado = clientesMaestros.removeIf(c -> c.getId() == id);
        if (eliminado) {
            repositorio.guardarTodos(clientesMaestros);
        }
        return eliminado;
    }

    public int generarNuevoId() {
        return repositorio.generarNuevoId();
    }

    public Cliente buscarPorId(int id) {
        return clientesMaestros.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}