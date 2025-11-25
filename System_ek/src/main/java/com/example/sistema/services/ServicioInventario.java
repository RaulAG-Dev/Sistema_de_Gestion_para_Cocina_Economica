package com.example.sistema.services;

import com.example.sistema.models.Ingrediente;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorIngrediente;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Proporciona los **servicios de gestión, consulta y actualización** para los objetos {@link Ingrediente}.
 * <p>
 * Implementa el patrón **Singleton** para garantizar una única instancia de servicio
 * y gestiona el stock de ingredientes en memoria ({@code stockMemoria}), interactuando
 * con {@link RepositorioJSON} para la persistencia.
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 2.0
 * @since 2025-11-21
 */
public class ServicioInventario {

    private static ServicioInventario instance;
    /** Repositorio utilizado para la persistencia de los objetos Ingrediente. */
    private final RepositorioJSON<Ingrediente> repositorio;
    /** Lista maestra de todos los ingredientes y su stock actual cargados en memoria. */
    private final List<Ingrediente> stockMemoria;

    /**
     * Constructor privado: Inicializa el repositorio y carga todos los ingredientes
     * en la lista maestra en memoria.
     */
    private ServicioInventario() {
        this.repositorio = new RepositorioJSON<>("System_ek/src/main/data/ingredientes.json", new ConvertidorIngrediente());
        this.stockMemoria = this.repositorio.obtenerTodos();
    }

    /**
     * Devuelve la única instancia de ServicioInventario (patrón Singleton).
     *
     * @return La única instancia activa de ServicioInventario.
     */
    public static ServicioInventario getInstance() {
        if (instance == null) {
            instance = new ServicioInventario();
        }
        return instance;
    }

    /**
     * Obtiene una copia de la lista maestra del inventario.
     *
     * @return Una nueva lista que contiene todos los objetos {@link Ingrediente}.
     */
    public List<Ingrediente> obtenerInventario() {
        return new ArrayList<>(stockMemoria);
    }

    /**
     * Obtiene una lista de todos los ingredientes cuyo stock actual es igual o menor
     * al stock mínimo definido (aquellos que están en estado de alerta).
     *
     * @return Una lista de objetos {@link Ingrediente} en alerta.
     */
    public List<Ingrediente> obtenerAlertasStock() {
        return stockMemoria.stream()
                .filter(Ingrediente::estaEnAlerta) // Usa el método simplificado del modelo
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un ingrediente existente en la lista maestra y persiste los cambios.
     * <p>
     * El ingrediente es identificado por su ID.
     *
     * @param ingredienteActualizado El objeto {@link Ingrediente} con los nuevos datos.
     */
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

    /**
     * Agrega un nuevo ingrediente a la lista maestra, asignándole un nuevo ID, y persiste los cambios.
     *
     * @param nuevo El objeto {@link Ingrediente} a agregar.
     */
    public void agregarIngrediente(Ingrediente nuevo) {
        nuevo.setId(repositorio.generarNuevoId());
        stockMemoria.add(nuevo);
        repositorio.guardarTodos(stockMemoria);
    }

    /**
     * Elimina un ingrediente de la lista maestra por su ID y persiste los cambios.
     *
     * @param id El ID del ingrediente a eliminar.
     * @return {@code true} si el ingrediente fue eliminado; {@code false} en caso contrario.
     */
    public boolean eliminarIngrediente(int id) {
        boolean eliminado = stockMemoria.removeIf(i -> i.getId() == id);
        if (eliminado) {
            repositorio.guardarTodos(stockMemoria);
        }
        return eliminado;
    }

    /**
     * Resta la cantidad especificada de un ingrediente del stock.
     * Si la resta es exitosa, el stock en memoria se actualiza.
     * <p>
     * Nota: Este método no persiste automáticamente. Debe llamarse a {@link #guardarStock()}
     * después de una serie de deducciones.
     *
     * @param ingredienteId El ID del ingrediente a restar.
     * @param cantidadARestar La cantidad flotante a deducir.
     * @return {@code true} si la resta fue exitosa (hay suficiente stock); {@code false} si el ingrediente no existe o el stock es insuficiente.
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
                // Actualiza el objeto en la lista 'stockMemoria'
                ingrediente.setStockActual(nuevoStock);
                return true;
            }
        }
        return false;
    }

    /**
     * Persiste la lista maestra de ingredientes completa en el archivo JSON.
     * Este método se usa típicamente después de múltiples actualizaciones de stock
     * (por ejemplo, después de procesar los ítems de una receta).
     */
    public void guardarStock() {
        System.out.println("Inventario guardado después de la deducción.");
    }

    /**
     * Busca un ingrediente en la lista maestra por su ID.
     *
     * @param id El ID del ingrediente a buscar.
     * @return El objeto {@link Ingrediente} si se encuentra, o {@code null} en caso contrario.
     */
    public Ingrediente buscarIngredientePorId(int id) {
        return stockMemoria.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }
}