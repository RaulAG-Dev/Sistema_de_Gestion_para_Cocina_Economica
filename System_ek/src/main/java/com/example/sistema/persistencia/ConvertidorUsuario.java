package com.example.sistema.persistencia;

import com.example.sistema.models.Usuario;
import org.json.simple.JSONObject;

/**
 * Clase implementadora de la interfaz {@link ConvertidorJSON}
 * que se encarga de la conversión bidireccional entre el objeto de modelo
 * **{@link Usuario}** y su representación en formato {@code JSONObject} (JSON simple).
 * <p>
 * Facilita la persistencia y carga de los datos de los usuarios del sistema.
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-10-27
 */
public class ConvertidorUsuario implements ConvertidorJSON<Usuario> {

    /**
     * Convierte un objeto {@link Usuario} a un objeto {@code JSONObject}.
     *
     * <p>Serializa el ID, nombre, la contraseña y el rol del usuario.</p>
     *
     * @param usuario El objeto Usuario que se desea serializar.
     * @return Un {@code JSONObject} que contiene los atributos clave del usuario.
     * @Override
     */
    @Override
    public JSONObject aJSON(Usuario usuario) {
        JSONObject json = new JSONObject();
        json.put("id", usuario.getId());
        json.put("nombre", usuario.getNombre());
        json.put("contrasena", usuario.getContrasena());
        json.put("rol",usuario.getRol());
        return json;
    }

    /**
     * Convierte un objeto {@code JSONObject} a un objeto de modelo {@link Usuario}.
     *
     * <p>Deserializa los atributos y reconstruye una instancia de {@link Usuario}.</p>
     *
     * @param jsonobjct El {@code JSONObject} que contiene la información del usuario.
     * @return Una nueva instancia de {@link Usuario} inicializada con los datos del JSON.
     * @throws ClassCastException si alguna clave tiene un tipo de dato inesperado.
     * @Override
     */
    @Override
    public Usuario deJSON(JSONObject jsonobjct) {
        // Se asume que el ID viene como Long y se convierte a int.
        int id = Math.toIntExact(((long) jsonobjct.get("id")));
        String nombre = (String) jsonobjct.get("nombre");
        String contrasena = (String) jsonobjct.get("contrasena");
        String rol = (String) jsonobjct.get("rol");
        // Se utiliza el constructor completo de Usuario
        return new Usuario(id, nombre, contrasena, rol);
    }
}
