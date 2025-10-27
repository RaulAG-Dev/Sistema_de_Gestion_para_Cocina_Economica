package com.example.sistema.services;

import com.example.sistema.models.Usuario;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorUsuario;

public class ServicioUsuarios {
    private RepositorioJSON<Usuario> repoUsuarios;

    public ServicioUsuarios() {
        repoUsuarios = new RepositorioJSON<>("System_ek/src/main/data/usuarios.json" , new ConvertidorUsuario());
        System.out.println("Usuarios cargados: " + repoUsuarios.obtenerTodos().size());
    }



    public Usuario autenticar(String nombre, String pass) throws Exception {
        for (Usuario u : repoUsuarios.obtenerTodos()) {
            if (u.getNombre().equals(nombre)) {
                if (u.validarContrasena(pass)) {
                    return u;
                } else {
                    throw new Exception("Contraseña incorrecta");
                }
            }
        }
        throw new Exception("Usuario no encontrado");
    }
}
