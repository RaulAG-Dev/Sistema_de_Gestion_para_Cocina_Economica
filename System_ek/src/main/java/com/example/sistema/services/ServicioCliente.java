package com.example.sistema.services;

import com.example.sistema.models.Cliente;
import com.example.sistema.persistencia.ConvertidorCliente;
import com.example.sistema.persistencia.RepositorioJSON;

import java.util.List;

public class ServicioCliente{

    private final RepositorioJSON<Cliente> repositorio;

    public ServicioCliente() {
        this.repositorio = new RepositorioJSON<>("System_ek/src/main/data/clientes.json", new ConvertidorCliente());
    }

    public List<Cliente> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public void guardar(Cliente cliente) {
        repositorio.guardar(cliente);
    }

    public boolean eliminar(int id) {
        return repositorio.eliminar(id);
    }

    public int generarNuevoId() {
        return repositorio.generarNuevoId();
    }

    public Cliente buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }
}
