package com.example.sistema.services;

import com.example.sistema.models.Platillo;
import com.example.sistema.persistencia.RepositorioJSON;
import com.example.sistema.persistencia.ConvertidorPlatillo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioMenu {
    private RepositorioJSON<Platillo> repoPlatillos;
    public ServicioMenu() {
        this.repoPlatillos = new RepositorioJSON<>("System_ek/src/main/data/platillos.json", new ConvertidorPlatillo());
    }


    public List<Platillo> obtenerPlatillosPorTipo(String tipoMenu) {
        if (tipoMenu == null || tipoMenu.isEmpty()) {
            return new ArrayList<>();
        }

        return repoPlatillos.obtenerTodos().stream()
                .filter(p -> p.getTipoMenu().equalsIgnoreCase(tipoMenu))
                .collect(Collectors.toList());
    }

    public void guardarPlatillo(Platillo platillo) {

        if (platillo.getId() == 0) {
            platillo.setId(repoPlatillos.generarNuevoId());
        }
        repoPlatillos.guardar(platillo);
    }

    public boolean eliminarPlatillo(int id) {
        return repoPlatillos.eliminar(id);
    }
}