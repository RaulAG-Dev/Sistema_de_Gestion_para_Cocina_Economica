package com.example.sistema.persistencia;

import com.example.sistema.models.Cliente;
import org.json.simple.JSONObject;

public class ConvertidorCliente implements ConvertidorJSON<Cliente> {

    @Override
    public JSONObject aJSON(Cliente cliente) {
        JSONObject obj = new JSONObject();
        obj.put("id", cliente.getId());
        obj.put("nombre", cliente.getNombre());
        obj.put("telefono", cliente.getTelefono());
        obj.put("preferencias", cliente.getPreferencias());
        return obj;
    }

    @Override
    public Cliente deJSON(JSONObject obj) {
        int id = Math.toIntExact((Long) obj.get("id"));
        String nombre = (String) obj.get("nombre");
        String telefono = (String) obj.get("telefono");
        String preferencias = (String) obj.get("preferencias");
        return new Cliente(id, nombre, telefono, preferencias);
    }
}
