package com.example.models;

import java.time.LocalDateTime;
import java.util.Date;

public class CorteCaja extends ModelManagement {
    private Date fecha;
    private float montoInicial;
    private float montoFinal;
    private float totalVentas;
    private Usuario usuario;

    public CorteCaja(){

    }

    public CorteCaja(int id, Date fecha, Usuario usuario, float montoInicial, float montoFinal, float totalVentas) {
        this.id = id;
        this.fecha = fecha;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.totalVentas = totalVentas;
    }


    public float calcularDiferencia(){
        float diferncia = montoFinal -(montoInicial+totalVentas);
        return diferncia;
    }


    //getters y setters


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(float montoInicial) {
        this.montoInicial = montoInicial;
    }

    public float getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(float montoFinal) {
        this.montoFinal = montoFinal;
    }

    public float getVentas() {
        return totalVentas;
    }

    public void setVentas(float ventas) {
        this.totalVentas = ventas;
    }



}
