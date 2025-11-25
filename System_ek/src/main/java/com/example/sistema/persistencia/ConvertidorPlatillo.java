package com.example.sistema.persistencia;

import com.example.sistema.models.Platillo;
import com.example.sistema.models.ItemReceta;
import com.example.sistema.models.Ingrediente;
import com.example.sistema.services.ServicioInventario; // Necesario para buscar el Ingrediente durante la carga

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase implementadora de la interfaz {@link ConvertidorJSON} que se encarga
 * de la conversión bidireccional entre el objeto de modelo **{@link Platillo}**
 * y su representación en formato {@code JSONObject} (JSON simple).
 *
 * <p>Maneja la serialización de la receta del platillo, almacenando solo el ID
 * del ingrediente y la cantidad requerida. Para la deserialización, utiliza
 * {@link ServicioInventario} para reconstruir las referencias completas de los ingredientes.</p>
 *
 * @author Raul Aguayo
 * @version 2.0
 * @since 2025-11-22
 */
public class ConvertidorPlatillo implements ConvertidorJSON<Platillo> {

    /**
     * Instancia del servicio de inventario utilizada para buscar objetos Ingrediente
     * durante la deserialización (carga del archivo JSON).
     */
    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();

    /**
     * Convierte un objeto {@link Platillo} a un objeto {@code JSONObject}.
     *
     * <p>Serializa los atributos base del platillo y la lista {@code receta},
     * almacenando de cada {@link ItemReceta} solo el ID del ingrediente
     * y la cantidad requerida para evitar la serialización completa del Ingrediente.</p>
     *
     * @param platillo El objeto Platillo que se desea serializar.
     * @return Un {@code JSONObject} que contiene la estructura del platillo.
     * @Override
     */
    @Override
    public JSONObject aJSON(Platillo platillo) {
        JSONObject json = new JSONObject();
        json.put("id", platillo.getId());
        json.put("nombre", platillo.getNombre());
        json.put("descripcion", platillo.getDescripcion());
        json.put("precio", platillo.getPrecio());
        json.put("disponible", platillo.isDisponible());
        json.put("tipoMenu", platillo.getTipoMenu());

        // Serialización de la Receta
        JSONArray recetaArray = new JSONArray();
        if (platillo.getReceta() != null) {
            for (ItemReceta item : platillo.getReceta()) {
                JSONObject itemJson = new JSONObject();
                // Solo se almacena el ID del ingrediente y la cantidad
                itemJson.put("ingredienteId", item.getIngrediente() != null ? item.getIngrediente().getId() : -1);
                itemJson.put("cantidadRequerida", item.getCantidadRequerida());
                recetaArray.add(itemJson);
            }
        }
        json.put("receta", recetaArray);

        return json;
    }

    /**
     * Convierte un objeto {@code JSONObject} a un objeto de modelo {@link Platillo}.
     *
     * <p>Deserializa los atributos base y reconstruye la lista {@code receta}.
     * Para cada ítem de la receta, busca el {@link Ingrediente} completo
     * utilizando su ID y el {@link ServicioInventario}.</p>
     *
     * @param jsonObject El {@code JSONObject} que contiene la información del platillo.
     * @return Una nueva instancia de {@link Platillo} inicializada con los datos.
     * @throws ClassCastException si alguna clave tiene un tipo de dato inesperado.
     * @Override
     */
    @Override
    public Platillo deJSON(JSONObject jsonObject) {
        int id = Math.toIntExact((Long) jsonObject.get("id"));
        String nombre = (String) jsonObject.get("nombre");
        String descripcion = (String) jsonObject.get("descripcion");
        // Se asume que 'precio' viene como Number (Double o Long)
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

                //Buscar la referencia completa del Ingrediente
                if (ingrediente != null) {
                    ItemReceta itemReceta = new ItemReceta(ingrediente, cantidad);
                    receta.add(itemReceta);
                } else {
                    // Advertencia si la referencia de ingrediente está rota
                    System.err.println("ADVERTENCIA: Ingrediente ID " + ingredienteId + " no encontrado durante la carga de Platillo ID " + id);
                }
            }
        }
        return new Platillo(id, nombre, descripcion, precio, disponible, receta, tipoMenu);
    }
}