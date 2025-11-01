package com.example.sistema.models;

/**
 * Representa un ingrediente utilizado en la preparación de platillos,
 * gestionando su inventario (stock actual) y alertando sobre niveles mínimos.
 *
 * <p>Hereda las propiedades básicas de identificación y nombre de {@code ModelManagement}.</p>
 *
 * @author Raul Aguayo
 * @version 2.0
 * @since 2025-11-28
 */
public class Ingrediente extends ModelManagement {
    /**
     * La cantidad actual del ingrediente disponible en inventario.
     */
    private float stockActual;
    /**
     * El nivel mínimo de stock que debe mantenerse para evitar desabastecimiento.
     */
    private float stockMinino;
    /**
     * La unidad de medida utilizada para este ingrediente (ej. "kg", "litros", "piezas").
     */
    private String unidades;

    /**
     * Constructor por defecto (sin argumentos) de la clase Ingrediente.
     */
    public Ingrediente(){

    }

    /**
     * Constructor utilizado para inicializar un objeto Ingrediente con todos sus atributos.
     *
     * @param id Identificador único del ingrediente (heredado).
     * @param nombre Nombre del ingrediente (heredado).
     * @param stockActual Cantidad actual en inventario.
     * @param stockMinino Cantidad mínima requerida en inventario.
     * @param unidades La unidad de medida del stock.
     */
    public Ingrediente(int id, String nombre, float stockActual, float stockMinino, String unidades){
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinino = stockMinino;
        this. unidades = unidades;
    }

    /**
     * Actualiza el stock actual del ingrediente sumando la cantidad proporcionada.
     * Si el nuevo stock queda igual o por debajo del stock mínimo, lanza una excepción
     * para alertar, pero el stock se actualiza de todas formas.
     *
     * @param canditidad La cantidad a sumar o restar (si es negativa) al stock actual.
     * @throws IllegalArgumentException Si el stock resultante es igual o menor al stock mínimo.
     */
    public void actualizarStock(float canditidad){
        float nuevoStock = this.stockActual + canditidad;
        if(nuevoStock > stockMinino){
            this.stockActual = nuevoStock;
        }else{
            this.stockActual = nuevoStock;
            // Se actualiza el stock, pero se lanza la alerta.
            throw new IllegalArgumentException("Stock al minimo: "+ nombre);
        }
    }

    //getters y setters

    /**
     * Obtiene la cantidad actual del ingrediente disponible en inventario.
     * @return El stock actual.
     */
    public float getStockActual() {
        return stockActual;
    }

    /**
     * Establece una nueva cantidad para el stock actual del ingrediente.
     *
     * @param nuevoSock La nueva cantidad de stock.
     * @throws IllegalArgumentException si el stock proporcionado es negativo.
     */
    public void setStockActual(float nuevoSock) {
        if(nuevoSock >= 0 ){
            this.stockActual = nuevoSock;
        }else{
            throw new IllegalArgumentException("stock debe ser mayor o igual a 0");
        }
    }

    /**
     * Obtiene el stock mínimo requerido para este ingrediente.
     * @return El stock mínimo.
     */
    public float getStockMinino() {
        return stockMinino;
    }

    /**
     * Establece el stock mínimo requerido para el ingrediente.
     * @param stockMinino El nuevo stock mínimo.
     */
    public void setStockMinino(float stockMinino) {
        this.stockMinino = stockMinino;
    }

    /**
     * Obtiene la unidad de medida utilizada para el stock de este ingrediente.
     * @return La unidad de medida (ej. "kg").
     */
    public String getUnidades() {
        return unidades;
    }

    /**
     * Establece la unidad de medida para el stock del ingrediente.
     * @param unidades La nueva unidad de medida.
     */
    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
}