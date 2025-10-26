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

    public void actualizarStock(float canditidad){
        float nuevoStock = this.stockActual + canditidad;
        if(nuevoStock > stockMinino){
            this.stockActual = nuevoStock;
        }else{
            this.stockActual = nuevoStock;
            throw new IllegalArgumentException("Stock al minimo: "+ nombre);
        }

    }

    //getters y setters

    public float getStockActual() {
        return stockActual;
    }

    public void setStockActual(float nuevoSock) {
        if(nuevoSock >= 0 ){
            this.stockActual = nuevoSock;
        }else{
            throw new IllegalArgumentException("stock debe ser mayor o igual a 0");
        }
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
