package com.example.sistema.persistencia;

import com.example.sistema.models.Platillo;
import com.example.sistema.models.ItemReceta;
import com.example.sistema.models.Ingrediente;
import com.example.sistema.services.ServicioInventario; // Necesario para buscar el Ingrediente durante la carga

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ConvertidorPlatillo implements ConvertidorJSON<Platillo> {
    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();

    @Override
    public JSONObject aJSON(Platillo platillo) {
        JSONObject json = new JSONObject();
        json.put("id", platillo.getId());
        json.put("nombre", platillo.getNombre());
        json.put("descripcion", platillo.getDescripcion());
        json.put("precio", platillo.getPrecio());
        json.put("disponible", platillo.isDisponible());
        json.put("tipoMenu", platillo.getTipoMenu());

        JSONArray recetaArray = new JSONArray();
        if (platillo.getReceta() != null) {
            for (ItemReceta item : platillo.getReceta()) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("ingredienteId", item.getIngrediente() != null ? item.getIngrediente().getId() : -1);
                itemJson.put("cantidadRequerida", item.getCantidadRequerida());
                recetaArray.add(itemJson);
            }
        }
        json.put("receta", recetaArray);

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

        List<ItemReceta> receta = new ArrayList<>();
        JSONArray recetaArray = (JSONArray) jsonObject.get("receta");

        if (recetaArray != null) {
            for (Object obj : recetaArray) {
                JSONObject itemJson = (JSONObject) obj;

                int ingredienteId = Math.toIntExact((Long) itemJson.get("ingredienteId"));
                float cantidad = ((Number) itemJson.get("cantidadRequerida")).floatValue();

                Ingrediente ingrediente = servicioInventario.buscarIngredientePorId(ingredienteId);

                if (ingrediente != null) {
                    ItemReceta itemReceta = new ItemReceta(ingrediente, cantidad);
                    receta.add(itemReceta);
                } else {
                    System.err.println("ADVERTENCIA: Ingrediente ID " + ingredienteId + " no encontrado durante la carga de Platillo ID " + id);
                }
            }
        }
        return new Platillo(id, nombre, descripcion, precio, disponible, receta, tipoMenu);
    }
}