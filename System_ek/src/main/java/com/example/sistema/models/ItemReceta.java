package com.example.sistema.models;

/**
 * Representa un ingrediente específico con la cantidad requerida para una porción de un platillo.
 */
public class ItemReceta {

    private Ingrediente ingrediente;
    private float cantidadRequerida;

    public ItemReceta() {
    }

    public ItemReceta(Ingrediente ingrediente, float cantidadRequerida) {
        this.ingrediente = ingrediente;
        this.cantidadRequerida = cantidadRequerida;
    }

    // Getters y Setters
    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public float getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(float cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }
}