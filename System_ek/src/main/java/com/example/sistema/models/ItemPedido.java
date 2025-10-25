package com.example.sistema.models;

public class ItemPedido {
    private Platillo platillo;
    private int cantidad;
    private float precioUnitario;

    public ItemPedido(){}

    public ItemPedido(Platillo platillo, int cantidad, float precioUnitario) {
        this.platillo = platillo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Platillo getPlatillo() {
        return platillo;
    }

    public void setPlatillo(Platillo platillo) {
        this.platillo = platillo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public float calcularSubtotal(){
        return precioUnitario * cantidad;
    }

    @Override
    public String toString() {
        return platillo.getNombre() + " x" + cantidad + " $" + calcularSubtotal();
    }

}
