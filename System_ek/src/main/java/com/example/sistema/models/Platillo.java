package com.example.sistema.models;

import java.util.List;

/**
 * Representa un plato o ítem del menú en un sistema de restaurante,
 * incluyendo detalles como precio, disponibilidad e ingredientes necesarios.
 *
 * <p>Esta clase hereda propiedades de gestión de modelos a través de {@code ModelManagement}.</p>
 *
 * @author Raul Aguayo
 * @version 2.0
 * @since 2025-11-28
 */
public class Platillo extends ModelManagement {
    /**
     * Descripción detallada del platillo, incluyendo sus características principales.
     */
    private String descripcion;
    /**
     * El precio de venta del platillo.
     */
    private float precio;
    /**
     * Indica si el platillo está disponible para ser ordenado (true) o no (false).
     */
    private boolean disponible;
    /**
     * Lista de objetos Ingrediente que componen este platillo.
     */
    private List<Ingrediente> ingredientes;
    /**
     * El tipo de menú al que pertenece el platillo.
     */
    private String tipoMenu;

    /**
     * Constructor por defecto (sin argumentos) de la clase Platillo.
     */
    public Platillo(){}

    /**
     * Constructor utilizado para inicializar un objeto Platillo con todos sus atributos.
     *
     * @param id Identificador único del platillo (heredado de ModelManagement).
     * @param nombre El nombre del platillo (asumiendo que es una propiedad heredada o implícita).
     * @param descripcion Descripción detallada del platillo.
     * @param precio El precio de venta del platillo.
     * @param disponible El estado de disponibilidad actual del platillo.
     * @param ingredientes La lista de ingredientes requeridos.
     */
    public Platillo(int id, String nombre, String descripcion, float precio, boolean disponible, List<Ingrediente> ingredientes, String tipoMenu){
        this.id = id;
        this. nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
        this.ingredientes = ingredientes;
        this.tipoMenu = tipoMenu;

    }

    //getters y setters

    /**
     * Obtiene el tipo de menu al que pertenece platillo.
     * @return el tipo de menú del platillo.
     */
    public String getTipoMenu() {
        return tipoMenu;
    }

    /**
     * Establece el tipo de menú al que pertenecerá el platillo.
     * @param tipoMenu el nueva tipo de menu.
     */
    public void setTipoMenu(String tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    /**
     * Obtiene la descripción detallada del platillo.
     * @return La descripción del platillo.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada del platillo.
     * @param descripcion La nueva descripción.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio de venta del platillo.
     * @return El precio del platillo.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de venta del platillo.
     *
     * @param precio El nuevo precio del platillo.
     * @throws IllegalArgumentException si el precio es menor a cero.
     */
    public void setPrecio(float precio) {
        if(precio < 0){
            throw new IllegalArgumentException("Debe ser mayor a cero");
        }

        this.precio = precio;
    }

    /**
     * Verifica si el platillo está disponible para la venta.
     * @return {@code true} si está disponible, {@code false} en caso contrario.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Establece la disponibilidad del platillo.
     * @param disponible El nuevo estado de disponibilidad.
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Obtiene la lista de ingredientes que componen el platillo.
     * @return La lista de objetos Ingrediente.
     */
    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    /**
     * Establece la lista de ingredientes para el platillo.
     * @param ingredientes La nueva lista de ingredientes.
     */
    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }


    /**
     * Retorna una representación en cadena del objeto Platillo.
     * @return Una cadena que incluye el ID, nombre, descripción, precio e ingredientes.
     */
    @Override
    public String toString(){
        return id + nombre + descripcion + precio + ingredientes;
    }



}
