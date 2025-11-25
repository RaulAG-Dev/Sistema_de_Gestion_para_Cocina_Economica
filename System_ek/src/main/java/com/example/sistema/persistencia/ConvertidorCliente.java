package com.example.sistema.persistencia;

import com.example.sistema.models.Cliente;
import org.json.simple.JSONObject;

/**
 * Clase implementadora de la interfaz {@link ConvertidorJSON}
 * que se encarga de la conversión bidireccional entre el objeto de modelo
 * {@link Cliente} y su representación en formato {@code JSONObject} (JSON simple).
 * <p>
 * Facilita la persistencia y carga de los datos del cliente.
 *
 * @author Michelle Chuc
 * @version 1.0
 * @since 2025-11-21
 */
public class ConvertidorCliente implements ConvertidorJSON<Cliente> {

    /**
     * Convierte un objeto {@link Cliente} a un objeto {@code JSONObject}.
     *
     * @param cliente El objeto Cliente que se desea serializar.
     * @return Un {@code JSONObject} que contiene los atributos del cliente: ID, nombre, teléfono y preferencias.
     * @Override
     */
    @Override
    public JSONObject aJSON(Cliente cliente) {
        JSONObject obj = new JSONObject();
        obj.put("id", cliente.getId());
        obj.put("nombre", cliente.getNombre());
        obj.put("telefono", cliente.getTelefono());
        obj.put("preferencias", cliente.getPreferencias());
        return obj;
    }

    /**
     * Convierte un objeto {@code JSONObject} a un objeto de modelo {@link Cliente}.
     *
     * @param obj El {@code JSONObject} que contiene la información del cliente.
     * @return Una nueva instancia de {@link Cliente} inicializada con los datos del JSON.
     * @throws ClassCastException si alguna clave no tiene el tipo de dato esperado (ej. ID no es Long).
     * @Override
     */
    @Override
    public Cliente deJSON(JSONObject obj) {
        // Se asume que el ID viene como Long y se convierte a int.
        int id = Math.toIntExact((Long) obj.get("id"));
        String nombre = (String) obj.get("nombre");
        String telefono = (String) obj.get("telefono");
        String preferencias = (String) obj.get("preferencias");
        // Se utiliza el constructor completo de Cliente
        return new Cliente(id, nombre, telefono, preferencias);
    }
}
