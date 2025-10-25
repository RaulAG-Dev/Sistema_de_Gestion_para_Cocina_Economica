package com.example.models;

public class Ingrediente extends ModelManagement {
    private float stockActual;
    private float stockMinino;
    private String unidades;

    public Ingrediente(){

    }

    public Ingrediente(int id, String nombre, float stockActual, float stockMinino, String unidades){
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinino = stockMinino;
        this. unidades = unidades;
    }

    public void actualizarStock(){

    }

    //getters y setters

    public float getStockActual() {
        return stockActual;
    }

    public void setStockActual(float stockActual) {
        this.stockActual = stockActual;
    }

    public float getStockMinino() {
        return stockMinino;
    }

    public void setStockMinino(float stockMinino) {
        this.stockMinino = stockMinino;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }


}
