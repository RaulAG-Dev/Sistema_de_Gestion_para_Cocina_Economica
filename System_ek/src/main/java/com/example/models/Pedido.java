package com.example.models;

import java.util.Date;
import java.util.List;

public class Pedido {
    private Date fechaHora;
    private Cliente cliente;
    private List<ItemPedido> items;
    private float total;
    private boolean pagado;


    public Pedido(){}

    public Pedido(Date fechaHora, Cliente cliente, List<ItemPedido> items, float total, boolean pagado) {
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.items = items;
        this.total = total;
        this.pagado = pagado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

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
