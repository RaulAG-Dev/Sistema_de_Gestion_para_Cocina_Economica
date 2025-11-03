package com.example.sistema.models;

/**
 * Clase que representa un ítem dentro de un pedido,
 * incluyendo el platillo, la cantidad solicitada
 * y el precio unitario.
 * @author Michelle Chuc
 * @version 2.0
 */
public class ItemPedido {
    private Platillo platillo;
    private int cantidad;
    private float precioUnitario;

    /**
     * Constructor vacío.
     */
    public ItemPedido(){}

    /**
     * Crea un ítem de pedido con datos completos.
     *
     * @param platillo Platillo que se incluye en el pedido
     * @param cantidad Cantidad del platillo
     * @param precioUnitario Precio por unidad del platillo
     */
    public ItemPedido(Platillo platillo, int cantidad, float precioUnitario) {
        this.platillo = platillo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /** @return Platillo del item*/
    public Platillo getPlatillo() {
        return platillo;
    }
    /** @param platillo Nuevo platillo del ítem */
    public void setPlatillo(Platillo platillo) {
        this.platillo = platillo;
    }
    /** @return Cantidad del ítem */
    public int getCantidad() {
        return cantidad;
    }
    /** @param cantidad Nueva cantidad del ítem */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    /** @return Precio unitario del platillo */
    public float getPrecioUnitario() {
        return precioUnitario;
    }
    /** @param precioUnitario Nuevo precio unitario del platillo */
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
     * @return Representación textual del ítem del pedido
     */
    @Override
    public String toString() {
        return platillo.getNombre() + " x" + cantidad + " $" + calcularSubtotal();
    }

}
