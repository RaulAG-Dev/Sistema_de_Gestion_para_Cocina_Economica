package com.example.sistema.persistencia;

import com.example.sistema.models.Usuario;
import org.json.simple.JSONObject;

public class ConvertidorUsuario implements ConvertidorJSON<Usuario> {
    @Override
    public JSONObject aJSON(Usuario usuario) {
        JSONObject json = new JSONObject();
        json.put("id", usuario.getId());
        json.put("nombre", usuario.getNombre());
        json.put("contrasena", usuario.getContrasena());
        json.put("rol",usuario.getRol());
        return json;
    }


    @Override
    public Usuario deJSON(JSONObject jsonobjct) {
        int id = Math.toIntExact(((long) jsonobjct.get("id")));
        String nombre = (String) jsonobjct.get("nombre");
        String contrasena = (String) jsonobjct.get("contrasena");
        String rol = (String) jsonobjct.get("rol");
        return new Usuario(id, nombre, contrasena, rol);
    }
}
