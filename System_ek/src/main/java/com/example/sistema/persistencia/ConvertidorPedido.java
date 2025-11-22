package com.example.sistema.persistencia;

import com.example.sistema.models.ItemPedido;
import com.example.sistema.models.Pedido;
import com.example.sistema.models.Platillo;
import com.example.sistema.models.Cliente; // 🔑 NECESARIO
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConvertidorPedido implements ConvertidorJSON<Pedido> {

    @Override
    public JSONObject aJSON(Pedido pedido) {
        JSONObject json = new JSONObject();
        json.put("id", pedido.getId());
        json.put("nombre", pedido.getNombre());
        json.put("fechaHora", pedido.getFechaHora() != null ? pedido.getFechaHora().getTime() : null);
        json.put("total", pedido.getTotal());
        json.put("pagado", pedido.isPagado());

        // 🔑 SERIALIZACIÓN DE CLIENTE (Tu código actual, que es correcto)
        if (pedido.getCliente() != null) {
            JSONObject clienteJson = new JSONObject();
            clienteJson.put("clienteId", pedido.getCliente().getId());
            clienteJson.put("clienteNombre", pedido.getCliente().getNombre());
            // Si Cliente tiene otros datos, los serializas aquí.
            json.put("cliente", clienteJson);
        } else {
            json.put("cliente", null);
        }

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

    @Override
    public Pedido deJSON(JSONObject jsonObject) {
        Pedido p = new Pedido();
        p.setId(Math.toIntExact((Long) jsonObject.get("id")));
        p.setNombre((String) jsonObject.get("nombre"));
        Object ts = jsonObject.get("fechaHora");
        p.setFechaHora(ts != null ? new Date((Long) ts) : null);
        p.setTotal(((Number) jsonObject.get("total")).floatValue());
        p.setPagado((Boolean) jsonObject.get("pagado"));

        // 🔑 INICIO DE DESERIALIZACIÓN DE CLIENTE (LA PARTE QUE FALTABA)
        JSONObject clienteJson = (JSONObject) jsonObject.get("cliente");
        Cliente cliente = null;

        if (clienteJson != null) {
            cliente = new Cliente();
            Long idLong = (Long) clienteJson.get("clienteId");
            String nombre = (String) clienteJson.get("clienteNombre");

            // Reconstruir el objeto Cliente si los datos existen
            if (idLong != null && nombre != null) {
                cliente.setId(Math.toIntExact(idLong));
                cliente.setNombre(nombre);
                // Si Cliente tiene otros setters (ej. setTelefono), llámalos aquí.
            } else {
                cliente = null; // Si los datos están incompletos, dejarlo nulo.
            }
        }
        p.setCliente(cliente); // 🔑 ASIGNAR EL CLIENTE RECONSTRUIDO AL PEDIDO
        // 🔑 FIN DE DESERIALIZACIÓN DE CLIENTE


        List<ItemPedido> items = new ArrayList<>();
        JSONArray itemsArray = (JSONArray) jsonObject.get("items");
        if (itemsArray != null) {
            // ... (Tu lógica existente para Items) ...
            for (Object obj : itemsArray) {
                JSONObject it = (JSONObject) obj;
                ItemPedido item = new ItemPedido();
                Platillo platillo = new Platillo();
                platillo.setId(it.get("platilloId") != null ? Math.toIntExact((Long) it.get("platilloId")) : 0);
                platillo.setNombre((String) it.get("platilloNombre"));
                item.setPlatillo(platillo);
                item.setCantidad(Math.toIntExact((Long) it.get("cantidad")));
                item.setPrecioUnitario(((Number) it.get("precioUnitario")).floatValue());
                items.add(item);
            }
        }
        p.setItems(items);
        p.calcularTotal();
        return p;
    }
}