package com.example.models;

public class Usuario extends ModelManagement {
    private String contrasena;
    private String rol;

    public Usuario(){}

    public Usuario(int id,String nombre, String contrasena, String rol){
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }


    public boolean validarContrasena(String contrasena){
        if(contrasena.equals(this.contrasena)){
            return true;
        }else{
            return false;
        }
    }


    //getters y setters

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


}
