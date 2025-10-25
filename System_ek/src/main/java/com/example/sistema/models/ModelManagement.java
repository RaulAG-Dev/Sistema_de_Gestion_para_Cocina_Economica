package com.example.sistema.models;

import com.example.sistema.persistencia.Identificable;

public class ModelManagement implements Identificable {
    int id;
    String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
