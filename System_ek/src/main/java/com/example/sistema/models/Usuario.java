package com.example.sistema.models;

/**
 * Representa a un usuario dentro del sistema, extendiendo la funcionalidad base
 * de {@code ModelManagement} para incluir detalles específicos de autenticación y permisos.
 *
 * <p>Maneja la contraseña y el rol del usuario, y provee un método para validar credenciales.</p>
 *
 * @author Raul Aguayo
 * @version 2.0
 * @since 2025-11-28
 */
public class Usuario extends ModelManagement {
    /**
     * La contraseña del usuario. (Nota: En un sistema real, esta debería estar hasheada).
     */
    private String contrasena;
    /**
     * El rol o nivel de permisos asignado al usuario (ej. "Administrador", "Cajero", "Mesero").
     */
    private String rol;

    /**
     * Constructor por defecto (sin argumentos) de la clase Usuario.
     */
    public Usuario(){}

    /**
     * Constructor utilizado para inicializar un objeto Usuario con todos sus atributos.
     *
     * @param id Identificador único del usuario (heredado).
     * @param nombre Nombre de usuario (heredado).
     * @param contrasena La contraseña del usuario.
     * @param rol El rol o nivel de acceso asignado al usuario.
     */
    public Usuario(int id,String nombre, String contrasena, String rol){
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }


    /**
     * Valida si la contraseña proporcionada coincide con la contraseña registrada para este usuario.
     *
     * <p>Utiliza una comparación de cadenas simple.</p>
     *
     * @param contrasena La cadena de contraseña a verificar.
     * @return {@code true} si las contraseñas coinciden; {@code false} en caso contrario.
     */
    public boolean validarContrasena(String contrasena){
        if(contrasena.equals(this.contrasena)){
            return true;
        }else{
            return false;
        }
    }


    //getters y setters

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña almacenada.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contrasena La nueva contraseña.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el rol o nivel de permisos del usuario.
     * @return El rol asignado.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol o nivel de permisos del usuario.
     * @param rol El nuevo rol.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }


}