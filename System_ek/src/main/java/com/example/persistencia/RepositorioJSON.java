package com.example.persistencia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioJSON<T extends Identificable> {

    private String archivo;
    private List<T> datos;
    private JSONParser parser;
    private ConvertidorJSON<T> convertidor;

    public RepositorioJSON(String archivo, ConvertidorJSON<T> convertidor) {
        this.archivo = archivo;
        this.convertidor = convertidor;
        this.datos = new ArrayList<>();
        this.parser = new JSONParser();
        this.cargarDatos();
    }

    private void cargarDatos(){
        try {
            File file = new File(archivo);
            if (!file.exists()) {
                file.createNewFile();
                datos = new ArrayList<>();
                return;
            }

            if (file.length() == 0) {
                datos = new ArrayList<>();
                return;
            }

            Reader reader = new FileReader(archivo);
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            reader.close();

            datos = new ArrayList<>();
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                T objeto = convertidor.deJSON(jsonObj);
                datos.add(objeto);
            }

        } catch (IOException | ParseException e) {
            datos = new ArrayList<>();
        }
    }



    private void guardarDatos() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (T objeto : datos) {
                JSONObject jsonObj = convertidor.aJSON(objeto);
                jsonArray.add(jsonObj);
            }

            Writer writer = new FileWriter(archivo);
            writer.write(jsonArray.toJSONString());
            writer.close();

        } catch (IOException e) {
            datos = new ArrayList<>();
        }
    }

    public T buscarPorId(int id) {
        for (T objeto : datos) {
            if (objeto.getId() == id) {
                return objeto;
            }
        }
        return null;
    }


    public void guardar(T objeto) {
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).getId() == objeto.getId()) {
                datos.set(i, objeto);
                guardarDatos();
                return;
            }
        }
        datos.add(objeto);
        guardarDatos();
    }

    public boolean eliminar(int id) {
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).getId() == id) {
                T eliminado = datos.remove(i);
                guardarDatos();
                return true;
            }
        }
        return false;
    }


    public List<T> obtenerTodos() {
        if (datos == null || datos.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(datos);
    }

}
