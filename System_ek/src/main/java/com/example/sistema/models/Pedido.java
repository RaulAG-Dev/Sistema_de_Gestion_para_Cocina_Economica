package com.example.sistema.models;

import com.example.sistema.persistencia.Identificable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa un **pedido** realizado por un cliente en el sistema.
 * <p>
 * Gestiona la lista de ítems solicitados, el cliente asociado,
 * la fecha y hora, el estado de pago, y calcula el monto total.
 * Implementa la interfaz {@link Identificable} para la gestión de persistencia.
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 3.0
 * @since 2025-11-16
 */
public class Pedido implements Identificable {

    /**
     * Identificador único del pedido.
     */
    private int id;
    /**
     * Fecha y hora en que se creó el pedido.
     */
    private Date fechaHora;
    /**
     * Nombre descriptivo del pedido, generado a partir de sus ítems.
     */
    private String nombre;
    /**
     * El cliente que realizó el pedido.
     */
    private Cliente cliente;
    /**
     * Lista de los ítems (platillos y cantidades) que componen el pedido.
     */
    private List<ItemPedido> items;
    /**
     * El costo total calculado del pedido.
     */
    private float total;
    /**
     * Indica si el pedido ha sido pagado (true) o no (false).
     */
    private boolean pagado;

    /**
     * Constructor vacío por defecto.
     */
    public Pedido() {}

    /**
     * Obtiene el identificador único del pedido.
     *
     * @return El ID del pedido.
     * @Override
     */
    @Override
    public int getId() { return id; }

    /**
     * Establece el identificador único del pedido.
     *
     * @param id El nuevo ID del pedido.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Obtiene la fecha y hora en que se realizó el pedido.
     *
     * @return El objeto {@link Date} con la fecha y hora.
     */
    public Date getFechaHora() { return fechaHora; }

    /**
     * Establece la fecha y hora del pedido.
     *
     * @param fechaHora La nueva fecha y hora.
     */
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    /**
     * Obtiene el nombre descriptivo del pedido.
     *
     * @return El nombre del pedido.
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre descriptivo del pedido.
     *
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el cliente asociado a este pedido.
     *
     * @return El objeto {@link Cliente}.
     */
    public Cliente getCliente() { return cliente; }

    /**
     * Establece el cliente que realizó el pedido.
     *
     * @param cliente El objeto {@link Cliente} asociado.
     */
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    /**
     * Obtiene la lista de ítems de este pedido.
     *
     * @return La lista de objetos {@link ItemPedido}.
     */
    public List<ItemPedido> getItems() { return items; }
    /**
     * Establece la lista de ítems que componen el pedido.
     *
     * @param items La nueva lista de ítems.
     */
    public void setItems(List<ItemPedido> items) { this.items = items; }

    /**
     * Obtiene el costo total calculado del pedido.
     *
     * @return El monto total.
     */
    public float getTotal() { return total; }
    /**
     * Establece el costo total del pedido.
     * (Nota: Idealmente se calcula con {@link #calcularTotal()}).
     *
     * @param total El nuevo monto total.
     */
    public void setTotal(float total) { this.total = total; }

    /**
     * Verifica si el pedido ha sido pagado.
     *
     * @return {@code true} si está pagado; {@code false} en caso contrario.
     */
    public boolean isPagado() { return pagado; }
    /**
     * Establece el estado de pago del pedido.
     *
     * @param pagado El estado de pago.
     */
    public void setPagado(boolean pagado) { this.pagado = pagado; }

    /**
     * Genera un nombre descriptivo para el pedido basado en la lista de ítems.
     * Si no hay ítems, el nombre será "Pedido vacío".
     * El formato es: Platillo 1 xCant, Platillo 2 xCant.
     */
    public void generarNombreDesdeItems() {
        if (items == null || items.isEmpty()) {
            this.nombre = "Pedido vacío";
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (ItemPedido item : items) {
            sb.append(item.getPlatillo().getNombre())
                    .append(" x").append(item.getCantidad())
                    .append(", ");
        }
        // Elimina la última coma y espacio
        this.nombre = sb.toString().replaceAll(", $", "");
    }

    /**
     * Calcula el monto total del pedido sumando los subtotales de todos sus ítems.
     * El resultado se almacena en el campo {@code total}.
     *
     * @return El monto total recalculado.
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
