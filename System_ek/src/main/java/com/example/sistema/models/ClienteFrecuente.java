package com.example.sistema.models;

/**
 * Modelo temporal para representar un Cliente junto con su métrica de pedidos (frecuencia).
 * Utilizado solo en la capa de reportes/presentación.
 */
public class ClienteFrecuente extends Cliente { // Hereda las propiedades de Cliente

    private final long cantidadPedidos;
    public ClienteFrecuente(Cliente cliente, long cantidadPedidos) {
        this.setId(cliente.getId());
        this.setNombre(cliente.getNombre());
        this.setTelefono(cliente.getTelefono());
        this.setPreferencias(cliente.getPreferencias());

        this.cantidadPedidos = cantidadPedidos;
    }

    public long getCantidadPedidos() {
        return cantidadPedidos;
    }

}