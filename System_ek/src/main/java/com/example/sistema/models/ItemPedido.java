package com.example.sistema.models;

/**
 * Clase que representa un **ítem dentro de un pedido**.
 * <p>
 * Este ítem incluye el platillo específico, la cantidad solicitada
 * y el precio unitario en el momento en que se realizó el pedido.
 *
 * @author Michelle Chuc
 * @version 2.0
 * @since 25-11-01
 */
public class ItemPedido {
    /**
     * El objeto Platillo incluido en este ítem del pedido.
     */
    private Platillo platillo;
    /**
     * La cantidad del platillo solicitada.
     */
    private int cantidad;
    /**
     * El precio por unidad del platillo en el momento en que se creó el ítem.
     */
    private float precioUnitario;

    /**
     * Constructor **vacío** por defecto.
     */
    public ItemPedido(){}

    /**
     * Crea un ítem de pedido con datos completos.
     *
     * @param platillo El platillo que se incluye en el ítem del pedido.
     * @param cantidad La cantidad del platillo.
     * @param precioUnitario El precio por unidad del platillo.
     */
    public ItemPedido(Platillo platillo, int cantidad, float precioUnitario) {
        this.platillo = platillo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene el platillo asociado a este ítem.
     *
     * @return El objeto Platillo.
     */
    public Platillo getPlatillo() {
        return platillo;
    }

    /**
     * Establece un nuevo platillo para este ítem.
     *
     * @param platillo El nuevo objeto Platillo.
     */
    public void setPlatillo(Platillo platillo) {
        this.platillo = platillo;
    }

    /**
     * Obtiene la cantidad del platillo en este ítem.
     *
     * @return La cantidad del ítem.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece una nueva cantidad para este ítem.
     *
     * @param cantidad La nueva cantidad del ítem.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del platillo.
     *
     * @return El precio unitario del platillo.
     */
    public float getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece un nuevo precio unitario para el platillo en este ítem.
     *
     * @param precioUnitario El nuevo precio unitario.
     */
    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Calcula el subtotal del ítem con base en cantidad y precio unitario.
     *
     * @return Subtotal del ítem
     */
    public float calcularSubtotal(){
        return precioUnitario * cantidad;
    }

    /**
     * Proporciona una **representación textual** del ítem del pedido.
     * El formato es "Nombre del Platillo xCantidad $Subtotal".
     *
     * @return Una cadena de texto que representa el ítem del pedido.
     * @Override
     */
    @Override
    public String toString() {
        return platillo.getNombre() + " x" + cantidad + " $" + calcularSubtotal();
    }

}
