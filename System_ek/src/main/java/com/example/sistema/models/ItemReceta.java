package com.example.sistema.models;

/**
 * Representa un **ingrediente específico** con la **cantidad requerida**
 * para la preparación de una porción de un platillo (componente de una receta).
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-21
 */
public class ItemReceta {
    /**
     * El objeto Ingrediente necesario para esta parte de la receta.
     */
    private Ingrediente ingrediente;
    /**
     * La cantidad del ingrediente requerida para una porción del platillo.
     * La unidad de medida está definida en el objeto Ingrediente.
     */
    private float cantidadRequerida;

    /**
     * Constructor **vacío** por defecto.
     */
    public ItemReceta() {
    }

    /**
     * Crea un ítem de receta con el ingrediente y la cantidad requerida.
     *
     * @param ingrediente El objeto {@link Ingrediente} que se requiere.
     * @param cantidadRequerida La cantidad necesaria de ese ingrediente.
     */
    public ItemReceta(Ingrediente ingrediente, float cantidadRequerida) {
        this.ingrediente = ingrediente;
        this.cantidadRequerida = cantidadRequerida;
    }

    // Getters y Setters

    /**
     * Obtiene el objeto Ingrediente asociado a este ítem de la receta.
     *
     * @return El {@link Ingrediente} requerido.
     */
    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    /**
     * Establece el objeto Ingrediente asociado a este ítem.
     *
     * @param ingrediente El nuevo {@link Ingrediente} a establecer.
     */
    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    /**
     * Obtiene la cantidad del ingrediente requerida para la porción del platillo.
     *
     * @return La cantidad requerida.
     */
    public float getCantidadRequerida() {
        return cantidadRequerida;
    }

    /**
     * Establece la cantidad del ingrediente requerida.
     *
     * @param cantidadRequerida La nueva cantidad a establecer.
     */
    public void setCantidadRequerida(float cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }
}