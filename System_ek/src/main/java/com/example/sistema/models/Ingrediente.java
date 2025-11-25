package com.example.sistema.models;

/**
 * Representa un **ingrediente** utilizado en la preparación de platillos.
 * <p>
 * Esta clase gestiona su inventario (stock actual) y alerta cuando los niveles de
 * stock caen por debajo de un mínimo especificado. Hereda propiedades base (como ID y nombre)
 * de la clase {@link ModelManagement}.
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 2.0
 * @since 2025-11-16
 */
public class Ingrediente extends ModelManagement { //Hereda las propiedades de ModelManagement
    /**
     * La cantidad actual del ingrediente en el inventario.
     */
    private float stockActual;
    /**
     * La cantidad mínima requerida de stock para evitar el estado de alerta.
     */
    private float stockMinimo;
    /**
     * La unidad de medida para el ingrediente (ej. "kg", "litros", "unidades").
     */
    private String unidades;

    /**
     * Constructor **vacío** por defecto.
     */
    public Ingrediente(){

    }

    /**
     * Constructor utilizado para inicializar un objeto Ingrediente con todos sus atributos.
     *
     * @param id Identificador único del ingrediente (heredado).
     * @param nombre Nombre del ingrediente (heredado).
     * @param stockActual La cantidad de stock actual.
     * @param stockMinimo La cantidad mínima de stock requerida para alerta.
     * @param unidades La unidad de medida (ej. "kg").
     */
    public Ingrediente(int id, String nombre, float stockActual, float stockMinimo, String unidades){
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this. unidades = unidades;
    }

    /**
     * Actualiza el stock actual del ingrediente sumando o restando la cantidad proporcionada.
     *
     * @param cantidad La cantidad a sumar o restar (si es negativa) al stock actual.
     * @throws IllegalArgumentException Si el stock resultante es negativo.
     */
    public void actualizarStock(float cantidad){
        float nuevoStock = this.stockActual + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock resultante no puede ser negativo para: " + nombre);
        }
        this.stockActual = nuevoStock;
    }

    //getters y setters

    /**
     * Obtiene la cantidad de stock actual del ingrediente.
     *
     * @return La cantidad de stock actual.
     */
    public float getStockActual() {
        return stockActual;
    }

    /**
     * Establece una nueva cantidad para el stock actual del ingrediente.
     *
     * @param nuevoSock La nueva cantidad de stock a establecer.
     * @throws IllegalArgumentException Si el stock proporcionado es menor a 0.
     */
    public void setStockActual(float nuevoSock) {
        if(nuevoSock >= 0 ){
            this.stockActual = nuevoSock;
        }else{
            throw new IllegalArgumentException("stock debe ser mayor o igual a 0");
        }
    }

    /**
     * Obtiene la cantidad mínima de stock requerida para alerta.
     *
     * @return La cantidad de stock mínima.
     */
    public float getStockMinimo() {
        return stockMinimo;
    }


    /**
     * Establece la cantidad mínima de stock requerida para alerta.
     *
     * @param stockMinimo La nueva cantidad de stock mínima.
     */
    public void setStockMinimo(float stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /**
     * Obtiene la unidad de medida para el ingrediente.
     *
     * @return La unidad de medida (ej. "kg").
     */
    public String getUnidades() {
        return unidades;
    }

    /**
     * Establece la unidad de medida para el ingrediente.
     *
     * @param unidades La nueva unidad de medida.
     */
    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    /**
     * Verifica si el ingrediente está en nivel de alerta.
     * @return true si el stock actual es igual o menor al stock mínimo.
     */
    public boolean estaEnAlerta() {
        return this.stockActual <= this.stockMinimo;
    }
}