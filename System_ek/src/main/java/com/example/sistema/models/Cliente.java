package com.example.sistema.models;

/**
 * Esta clase representa a un cliente del sistema.
 * incluyendo su número telefónico y preferencias de consumo.
 * Hereda atributos de la clase ModelManagement.
 * @author Michelle Chuc
 * @version 2.0
 */
public class Cliente extends ModelManagement {
    private String telefono;
    private String preferencias;

    /**
     * Constructor vacío.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /** @return Teléfono del cliente*/
    public String getTelefono() {
        return telefono;
    }

    /**@param telefono Nuevo número de telefono*/
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return Preferencias del cliente*/
    public String getPreferencias() {
        return preferencias;
    }

    /** @param preferencias Nuevas preferencias del cliente*/
    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    /**
     * @return Representación textual del cliente
     * */
    @Override
    public String toString(){
        return nombre + " (" + telefono + ") - Preferencias: " + preferencias;
    }


}
