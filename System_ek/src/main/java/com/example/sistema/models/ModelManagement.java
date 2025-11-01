package com.example.sistema.models;

import com.example.sistema.persistencia.Identificable;

/**
 * Clase base para la gestión de modelos en el sistema.
 * Implementa la interfaz {@code Identificable} para garantizar que todos
 * los modelos contengan un identificador único (ID) y un nombre.
 *
 * <p>Esta clase provee la estructura fundamental para las clases de dominio.</p>
 *
 * @author Raul Aguayo
 * @version 2.0
 * @since 2025-11-28
 */
public class ModelManagement implements Identificable {
    /**
     * Identificador único del modelo.
     */
    protected int id;
    /**
     * Nombre descriptivo del modelo o entidad.
     */
    protected String nombre;

    /**
     * Obtiene el identificador único del modelo.
     *
     * @return El ID del modelo.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del modelo.
     *
     * @param id El nuevo ID del modelo.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre descriptivo del modelo.
     *
     * @return El nombre del modelo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre descriptivo del modelo.
     *
     * @param nombre El nuevo nombre del modelo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}