package com.example.sistema.models;

/**
 * Modelo temporal para representar un **Cliente** junto con su métrica de pedidos (frecuencia).
 * <p>
 * Esta clase está diseñada para ser utilizada solo en la capa de reportes/presentación
 * y hereda las propiedades base de la clase {@link Cliente}.
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-22
 */
public class ClienteFrecuente extends Cliente { // Hereda las propiedades de Cliente
    /**
     * La cantidad total de pedidos realizados por este cliente.
     */
    private final long cantidadPedidos;

    /**
     * Construye una instancia de {@code ClienteFrecuente} tomando un objeto
     * {@code Cliente} existente y añadiéndole la métrica de frecuencia de pedidos.
     *
     * @param cliente El objeto {@link Cliente} base cuyas propiedades serán copiadas.
     * @param cantidadPedidos El cálculo total de pedidos realizados por este cliente.
     */
    public ClienteFrecuente(Cliente cliente, long cantidadPedidos) {
        // Copiando propiedades del objeto Cliente base
        this.setId(cliente.getId());
        this.setNombre(cliente.getNombre());
        this.setTelefono(cliente.getTelefono());
        this.setPreferencias(cliente.getPreferencias());

        this.cantidadPedidos = cantidadPedidos;
    }

    /**
     * Obtiene la cantidad total de pedidos realizados por este cliente.
     *
     * @return La cantidad de pedidos (frecuencia).
     */
    public long getCantidadPedidos() {
        return cantidadPedidos;
    }

}