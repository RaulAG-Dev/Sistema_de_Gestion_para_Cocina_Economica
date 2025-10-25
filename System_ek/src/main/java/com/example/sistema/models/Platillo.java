package com.example.sistema.models;

import java.util.List;

public class Platillo extends ModelManagement{
    private String descripcion;
    private float precio;
    private boolean disponible;
    private List<Ingrediente> ingredientes;

    public Platillo(){}

    public Platillo(int id, String nombre, String descripcion, float precio, boolean disponible, List<Ingrediente> ingredientes){
        this.id = id;
        this. nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
        this.ingredientes = ingredientes;
    }

    //getters y setters

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        if(precio < 0){
            throw new IllegalArgumentException("Debe ser mayor a cero");
        }

        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }


    @Override
    public String toString(){
        return id + nombre + descripcion + precio + ingredientes;
    }



}
