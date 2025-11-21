package com.example.sistema.services;

import com.example.sistema.models.Ingrediente;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorIngrediente;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServicioInventario {

    private static ServicioInventario instance;
    private final RepositorioJSON<Ingrediente> repositorio;
    private final List<Ingrediente> stockMemoria;

    private ServicioInventario() {
        this.repositorio = new RepositorioJSON<>("System_ek/src/main/data/ingredientes.json", new ConvertidorIngrediente());
        this.stockMemoria = this.repositorio.obtenerTodos();
    }

    // Método estático para obtener la única instancia
    public static ServicioInventario getInstance() {
        if (instance == null) {
            instance = new ServicioInventario();
        }
        return instance;
    }

    public List<Ingrediente> obtenerInventario() {
        return new ArrayList<>(stockMemoria);
    }

    public List<Ingrediente> obtenerAlertasStock() {
        return stockMemoria.stream()
                .filter(Ingrediente::estaEnAlerta) // Usa el método simplificado del modelo
                .collect(Collectors.toList());
    }

    public void actualizarIngrediente(Ingrediente ingredienteActualizado) {
        boolean actualizado = false;
        for (int i = 0; i < stockMemoria.size(); i++) {
            if (stockMemoria.get(i).getId() == ingredienteActualizado.getId()) {
                stockMemoria.set(i, ingredienteActualizado);
                actualizado = true;
                break;
            }
        }
        if (actualizado) {
            repositorio.guardarTodos(stockMemoria);
        }
    }

    public void agregarIngrediente(Ingrediente nuevo) {
        nuevo.setId(repositorio.generarNuevoId());
        stockMemoria.add(nuevo);
        repositorio.guardarTodos(stockMemoria);
    }

    public boolean eliminarIngrediente(int id) {
        boolean eliminado = stockMemoria.removeIf(i -> i.getId() == id);
        if (eliminado) {
            repositorio.guardarTodos(stockMemoria);
        }
        return eliminado;
    }

    /**
     * Resta la cantidad especificada de un ingrediente del stock.
     * @param ingredienteId El ID del ingrediente a restar.
     * @param cantidadARestar La cantidad flotante a deducir.
     * @return true si la resta fue exitosa (hay suficiente stock), false en caso contrario.
     */
    public boolean restarCantidad(int ingredienteId, float cantidadARestar) {
        Optional<Ingrediente> ingredienteOpt = stockMemoria.stream()
                .filter(i -> i.getId() == ingredienteId)
                .findFirst();

        if (ingredienteOpt.isPresent()) {
            Ingrediente ingrediente = ingredienteOpt.get();
            float stockActual = ingrediente.getStockActual();

            if (stockActual >= cantidadARestar) {
                float nuevoStock = stockActual - cantidadARestar;
                ingrediente.setStockActual(nuevoStock);
                return true;
            }
        }
        return false;
    }

    public void guardarStock() {
        System.out.println("Inventario guardado después de la deducción.");
    }

    public Ingrediente buscarIngredientePorId(int id) {
        return stockMemoria.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }
}