package com.example.sistema.models;

/**
 * Representa un ingrediente utilizado en la preparación de platillos,
 * gestionando su inventario (stock actual) y alertando sobre niveles mínimos.
 */
public class Ingrediente extends ModelManagement {
    private float stockActual;
    private float stockMinimo;
    private String unidades;

    public Ingrediente(){

    }

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

    public float getStockActual() {
        return stockActual;
    }

    /**
     * Establece una nueva cantidad para el stock actual del ingrediente.
     */
    public void setStockActual(float nuevoSock) {
        if(nuevoSock >= 0 ){
            this.stockActual = nuevoSock;
        }else{
            throw new IllegalArgumentException("stock debe ser mayor o igual a 0");
        }
    }

    public float getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(float stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getUnidades() {
        return unidades;
    }

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