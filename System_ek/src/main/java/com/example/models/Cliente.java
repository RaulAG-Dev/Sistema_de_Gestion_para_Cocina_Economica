package com.example.models;

public class Cliente extends ModelManagement {

    private String telefono;
    private String preferencias;

    public Cliente(){}

    public Cliente(int id, String nombre, String telefono, String preferencias) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.preferencias = preferencias;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    @Override
    public String toString(){
        return nombre + " (" + telefono + ") - Preferencias: " + preferencias;
    }


}
