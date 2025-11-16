package com.example.sistema.persistencia;

import com.example.sistema.models.Ingrediente;
import org.json.simple.JSONObject;

public class ConvertidorIngrediente implements ConvertidorJSON<Ingrediente> {

    @Override
    public JSONObject aJSON(Ingrediente ingrediente) {
        JSONObject obj = new JSONObject();
        obj.put("id", ingrediente.getId());
        obj.put("nombre", ingrediente.getNombre());
        obj.put("stockActual", ingrediente.getStockActual());
        obj.put("stockMinino", ingrediente.getStockMinimo());
        obj.put("unidades", ingrediente.getUnidades());
        return obj;
    }

    @Override
    public Ingrediente deJSON(JSONObject obj) {
        int id = ((Long) obj.get("id")).intValue();
        String nombre = (String) obj.get("nombre");
        float stockActual = ((Double) obj.get("stockActual")).floatValue();
        float stockMinino = ((Double) obj.get("stockMinino")).floatValue();
        String unidades = (String) obj.get("unidades");

        return new Ingrediente(id, nombre, stockActual, stockMinino, unidades);
    }
}

