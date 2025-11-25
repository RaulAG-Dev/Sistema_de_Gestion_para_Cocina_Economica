package com.example.sistema.persistencia;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Pedido;
import com.example.sistema.models.Platillo;
import com.example.sistema.models.Cliente; // NECESARIO
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase implementadora de la interfaz {@link ConvertidorJSON} que se encarga
 * de la conversión bidireccional entre el objeto de modelo **{@link Pedido}**
 * y su representación en formato {@code JSONObject}.
 *
 * <p>Esta clase maneja la estructura anidada de un Pedido, incluyendo la
 * serialización de los datos clave del Cliente y la lista de ItemPedido.</p>
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 2.0
 * @since 2025-11-22
 */
public class ConvertidorPedido implements ConvertidorJSON<Pedido> {

    /**
     * Convierte un objeto {@link Pedido} a un objeto {@code JSONObject}.
     *
     * <p>Serializa el ID, nombre, fecha y hora (como timestamp Long), total,
     * estado de pago, y los detalles básicos del Cliente y los ítems del pedido.</p>
     *
     * @param pedido El objeto Pedido que se desea serializar.
     * @return Un {@code JSONObject} que contiene la estructura completa del pedido.
     * @Override
     */
    @Override
    public JSONObject aJSON(Pedido pedido) {
        JSONObject json = new JSONObject();
        json.put("id", pedido.getId());
        json.put("nombre", pedido.getNombre());
        // Se almacena la fecha/hora como un timestamp (Long)
        json.put("fechaHora", pedido.getFechaHora() != null ? pedido.getFechaHora().getTime() : null);
        json.put("total", pedido.getTotal());
        json.put("pagado", pedido.isPagado());

        // Serialización del Cliente (solo ID y Nombre para evitar anidación profunda)
        if (pedido.getCliente() != null) {
            JSONObject clienteJson = new JSONObject();
            clienteJson.put("clienteId", pedido.getCliente().getId());
            clienteJson.put("clienteNombre", pedido.getCliente().getNombre());
            json.put("cliente", clienteJson);
        } else {
            json.put("cliente", null);
        }

        // Serialización de la Lista de Ítems
        JSONArray itemsArray = new JSONArray();
        if (pedido.getItems() != null) {
            for (ItemPedido item : pedido.getItems()) {
                JSONObject it = new JSONObject();
                it.put("platilloId", item.getPlatillo() != null ? item.getPlatillo().getId() : null);
                it.put("platilloNombre", item.getPlatillo() != null ? item.getPlatillo().getNombre() : null);
                it.put("cantidad", item.getCantidad());
                it.put("precioUnitario", item.getPrecioUnitario());
                itemsArray.add(it);
            }
        }
        json.put("items", itemsArray);
        return json;
    }

    /**
     * Convierte un objeto {@code JSONObject} a un objeto de modelo {@link Pedido}.
     *
     * <p>Reconstruye el objeto Pedido, inicializa el Cliente y los Platillos (con ID y Nombre)
     * y, finalmente, recalcula el total para asegurar la integridad de los datos.</p>
     *
     * @param jsonObject El {@code JSONObject} que contiene la información del pedido.
     * @return Una nueva instancia de {@link Pedido} inicializada con los datos del JSON.
     * @throws ClassCastException si alguna clave tiene un tipo de dato inesperado.
     * @Override
     */
    @Override
    public Pedido deJSON(JSONObject jsonObject) {
        Pedido p = new Pedido();
        // Atributos base
        p.setId(Math.toIntExact((Long) jsonObject.get("id")));
        p.setNombre((String) jsonObject.get("nombre"));

        // Conversión de timestamp a Date
        Object ts = jsonObject.get("fechaHora");
        p.setFechaHora(ts != null ? new Date((Long) ts) : null);

        // Conversión de total y pagado
        p.setTotal(((Number) jsonObject.get("total")).floatValue());
        p.setPagado((Boolean) jsonObject.get("pagado"));

        // Deserialización del Cliente
        JSONObject clienteJson = (JSONObject) jsonObject.get("cliente");
        Cliente cliente = null;

        if (clienteJson != null) {
            cliente = new Cliente();
            Long idLong = (Long) clienteJson.get("clienteId");
            String nombre = (String) clienteJson.get("clienteNombre");

            if (idLong != null && nombre != null) {
                cliente.setId(Math.toIntExact(idLong));
                cliente.setNombre(nombre);
                // Otros datos del cliente (teléfono, preferencias) se cargarían por separado si fueran necesarios.
            } else {
                cliente = null;
            }
        }
        p.setCliente(cliente);

        // Deserialización de la Lista de Ítems
        List<ItemPedido> items = new ArrayList<>();
        JSONArray itemsArray = (JSONArray) jsonObject.get("items");
        if (itemsArray != null) {
            for (Object obj : itemsArray) {
                JSONObject it = (JSONObject) obj;
                ItemPedido item = new ItemPedido();

                // Reconstrucción parcial del Platillo (solo ID y Nombre)
                Platillo platillo = new Platillo();
                platillo.setId(it.get("platilloId") != null ? Math.toIntExact((Long) it.get("platilloId")) : 0);
                platillo.setNombre((String) it.get("platilloNombre"));
                item.setPlatillo(platillo);

                // Atributos del ItemPedido
                item.setCantidad(Math.toIntExact((Long) it.get("cantidad")));
                item.setPrecioUnitario(((Number) it.get("precioUnitario")).floatValue());
                items.add(item);
            }
        }
        p.setItems(items);
        // Se recalcula el total para garantizar que el valor cargado sea correcto
        p.calcularTotal();
        return p;
    }
}