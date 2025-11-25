package com.example.sistema.models;

/**
 * Representa un **cliente** en el sistema, extendiendo la funcionalidad base
 * de {@code ModelManagement}.
 * <p>
 * Esta clase incluye detalles específicos como el número telefónico y las
 * preferencias de consumo, y hereda propiedades base (como ID y nombre)
 * de la clase {@link ModelManagement}.
 *
 * @author Michelle Chuc
 * @version 2.0
 * @since 2025-10-28
 */
public class Cliente extends ModelManagement {
    /**
     * El número telefónico del cliente.
     */
    private String telefono;
    /**
     * Las preferencias de consumo del cliente (ej. restricciones dietéticas, artículos favoritos).
     */
    private String preferencias;

    /**
     * Constructor **vacío** por defecto.
     */
    public Cliente(){}

    /**
     * Crea un cliente con todos sus datos.
     *
     * @param id Identificador único del cliente
     * @param nombre Nombre del cliente
     * @param telefono Número telefónico del cliente
     * @param preferencias Preferencias del cliente
     */
    public Cliente(int id, String nombre, String telefono, String preferencias) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.preferencias = preferencias;
    }
    /**
     * Compara este objeto cliente con el objeto especificado para verificar igualdad.
     * Dos clientes se consideran iguales si sus **IDs son los mismos**.
     *
     * @param o El objeto con el que se va a comparar.
     * @return {@code true} si el objeto dado es un Cliente y su ID coincide con el ID de este cliente; {@code false} en caso contrario.
     * @Override
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    /**
     * Devuelve un **valor de código hash** para el cliente.
     * El código hash se basa únicamente en el ID del cliente.
     *
     * @return Un valor de código hash para este objeto.
     * @Override
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Obtiene el número telefónico del cliente.
     *
     * @return El número telefónico del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece un nuevo número telefónico para el cliente.
     *
     * @param telefono El nuevo número de teléfono a establecer.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene las preferencias de consumo del cliente.
     *
     * @return Las preferencias del cliente.
     */
    public String getPreferencias() {
        return preferencias;
    }

    /**
     * Establece nuevas preferencias de consumo para el cliente.
     *
     * @param preferencias Las nuevas preferencias del cliente a establecer.
     */
    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    /**
     * Proporciona una **representación textual** del cliente.
     * El formato incluye el nombre, teléfono y preferencias.
     *
     * @return Una cadena de texto que representa el objeto cliente.
     * @Override
     */
    @Override
    public String toString(){
        return nombre + " (" + telefono + ") - Preferencias: " + preferencias;
    }


}
