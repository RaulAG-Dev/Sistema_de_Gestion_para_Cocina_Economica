package com.example.sistema.models;

import java.util.Date;
import java.util.List;
/**
 * Clase que representa un pedido realizado por un cliente.
 * Incluye información como fecha, cliente, lista de ítems,
 * total a pagar y si ha sido pagado o no.
 * @autor Michelle Chuc
 * @version 2.0
 */

public class Pedido {
    private Date fechaHora;
    private Cliente cliente;
    private List<ItemPedido> items;
    private float total;
    private boolean pagado;

    /**
     * Constructor vacío.
     */
    public Pedido(){}

    /**
     * Crea un nuevo pedido con los datos proporcionados.
     *
     * @param fechaHora Fecha y hora del pedido
     * @param cliente Cliente que realiza el pedido
     * @param items Lista de productos incluidos en el pedido
     * @param total Monto total a pagar
     * @param pagado Bandera que indica si el pedido fue pagado
     */
    public Pedido(Date fechaHora, Cliente cliente, List<ItemPedido> items, float total, boolean pagado) {
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.items = items;
        this.total = total;
        this.pagado = pagado;
    }

    /** @return Fecha y hora del pedido */
    public Date getFechaHora() {
        return fechaHora;
    }
    /** @param fechaHora Nueva fecha y hora del pedido */
    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
    /** @return Cliente asociado al pedido */
    public Cliente getCliente() {
        return cliente;
    }
    /** @param cliente Nuevo cliente del pedido */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    /** @return Lista de ítems del pedido */
    public List<ItemPedido> getItems() {
        return items;
    }
    /** @param items Nueva lista de ítems del pedido */
    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }
    /** @return Total acumulado del pedido */
    public float getTotal() {
        return total;
    }
    /** @param total Nuevo total del pedido */
    public void setTotal(float total) {
        this.total = total;
    }
    /** @return true si el pedido ha sido pagado, false en caso contrario */
    public boolean isPagado() {
        return pagado;
    }
    /** @param pagado Define si el pedido ha sido pagado */
    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    /**
     * Calcula el total en base al subtotal de cada ítem del pedido.
     * @return Total del pedido
     */
    public float calcularTotal() {
        float suma = 0;
        if (items != null) {
            for (ItemPedido item : items) {
                suma += item.calcularSubtotal();
            }
        }
        this.total = suma;
        return suma;
    }


}
