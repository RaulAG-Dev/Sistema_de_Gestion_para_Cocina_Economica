package com.example.sistema.services;

import com.example.sistema.models.Ingrediente;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorIngrediente;

import java.util.List;
import java.util.stream.Collectors;

public class ServicioInventario {

    private final RepositorioJSON<Ingrediente> repositorio;

    public ServicioInventario() {
        this.repositorio = new RepositorioJSON<>("ingredientes.json", new ConvertidorIngrediente());
    }

    public List<Ingrediente> obtenerInventario() {
        return repositorio.obtenerTodos();
    }

    public List<Ingrediente> obtenerAlertasStock() {
        return repositorio.obtenerTodos().stream()
                .filter(i -> i.getStockActual() <= i.getStockMinimo())
                .collect(Collectors.toList());
    }

    public void actualizarIngrediente(Ingrediente ingredienteActualizado) {
        repositorio.guardar(ingredienteActualizado);
    }

    public void agregarIngrediente(Ingrediente nuevo) {
        nuevo.setId(repositorio.generarNuevoId());
        repositorio.guardar(nuevo);
    }

    public boolean eliminarIngrediente(int id) {
        return repositorio.eliminar(id);
    }
}
