package com.example.sistema.models;

import com.example.sistema.persistencia.Identificable;
import java.util.Date;
import java.util.List;

public class Pedido implements Identificable {

    private int id;              // NUEVO
    private Date fechaHora;
    private Cliente cliente;
    private List<ItemPedido> items;
    private float total;
    private boolean pagado;

    public Pedido() {}

    @Override
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) { this.items = items; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }

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
