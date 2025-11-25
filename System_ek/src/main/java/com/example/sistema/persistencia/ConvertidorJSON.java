package com.example.sistema.persistencia;

import org.json.simple.JSONObject;

/**
 * Define el **contrato (interfaz)** para la conversión bidireccional
 * entre un objeto de modelo específico (representado por el tipo genérico T)
 * y su representación en el formato {@code JSONObject} (JSON simple).
 * <p>
 * Cualquier clase que implemente esta interfaz debe proveer la lógica
 * para serializar (a JSON) y deserializar (de JSON) un tipo de objeto T.
 *
 * @param <T> El tipo de objeto de modelo que la clase implementadora convertirá (ej. Cliente, Platillo, Usuario).
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-10-25
 */
public interface ConvertidorJSON<T> {

    /**
     * Convierte un objeto de modelo al formato {@code JSONObject} para su persistencia.
     *
     * @param objeto El objeto de modelo (T) que se desea serializar.
     * @return Un {@code JSONObject} que contiene los atributos del objeto.
     */
    JSONObject aJSON(T objeto);

    /**
     * Convierte un objeto {@code JSONObject} a una instancia del objeto de modelo.
     *
     * @param jsonobjct El {@code JSONObject} que contiene la información serializada.
     * @return Una nueva instancia del objeto de modelo (T) inicializada con los datos del JSON.
     */
    T deJSON(JSONObject jsonobjct);
}
