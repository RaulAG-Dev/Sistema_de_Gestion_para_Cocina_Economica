package com.example.sistema.persistencia;

import com.example.sistema.models.Platillo;
import org.json.simple.JSONObject;
import java.util.Collections;

public class ConvertidorPlatillo implements ConvertidorJSON<Platillo> {

    @Override
    public JSONObject aJSON(Platillo platillo) {
        JSONObject json = new JSONObject();
        json.put("id", platillo.getId());
        json.put("nombre", platillo.getNombre());
        json.put("descripcion", platillo.getDescripcion());
        json.put("precio", platillo.getPrecio());
        json.put("disponible", platillo.isDisponible());

        json.put("tipoMenu", platillo.getTipoMenu());
        return json;
    }

    @Override
    public Platillo deJSON(JSONObject jsonObject) {
        int id = Math.toIntExact((Long) jsonObject.get("id"));
        String nombre = (String) jsonObject.get("nombre");
        String descripcion = (String) jsonObject.get("descripcion");
        float precio = ((Number) jsonObject.get("precio")).floatValue();
        boolean disponible = (Boolean) jsonObject.get("disponible");
        String tipoMenu = (String) jsonObject.get("tipoMenu");

        return new Platillo(id, nombre, descripcion, precio, disponible, Collections.emptyList(), tipoMenu);
    }
}