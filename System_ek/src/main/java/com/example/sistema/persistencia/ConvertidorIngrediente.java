package com.example.sistema.persistencia;

import com.example.sistema.models.Ingrediente;
import org.json.simple.JSONObject;

/**
 * Clase implementadora de la interfaz {@link ConvertidorJSON}
 * que se encarga de la conversión bidireccional entre el objeto de modelo
 * {@link Ingrediente} y su representación en formato {@code JSONObject} (JSON simple).
 * <p>
 * Facilita la persistencia y carga de los datos del inventario de ingredientes.
 *
 * @author (Añade el Nombre del Autor Aquí)
 * @version 1.0
 * @since 2025-11-16
 */
public class ConvertidorIngrediente implements ConvertidorJSON<Ingrediente> {

    /**
     * Convierte un objeto {@link Ingrediente} a un objeto {@code JSONObject}.
     *
     * @param ingrediente El objeto Ingrediente que se desea serializar.
     * @return Un {@code JSONObject} que contiene los atributos del ingrediente: ID, nombre, stock actual, stock mínimo y unidades.
     * @Override
     */
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

    /**
     * Convierte un objeto {@code JSONObject} a un objeto de modelo {@link Ingrediente}.
     *
     * @param obj El {@code JSONObject} que contiene la información del ingrediente.
     * @return Una nueva instancia de {@link Ingrediente} inicializada con los datos del JSON.
     * @throws ClassCastException si alguna clave no tiene el tipo de dato esperado (ej. ID no es Long, stocks no son Double).
     * @Override
     */
    @Override
    public Ingrediente deJSON(JSONObject obj) {
        // Se asume que 'id' viene como Long y se convierte a int.
        int id = ((Long) obj.get("id")).intValue();
        String nombre = (String) obj.get("nombre");
        // Se asume que los valores float/double vienen como Double y se convierten a float.
        float stockActual = ((Double) obj.get("stockActual")).floatValue();
        float stockMinino = ((Double) obj.get("stockMinino")).floatValue();
        String unidades = (String) obj.get("unidades");
        // Se utiliza el constructor completo de Ingrediente
        return new Ingrediente(id, nombre, stockActual, stockMinino, unidades);
    }
}

