package com.example.sistema.models;

import java.util.Date;

/**
 * Representa el registro de un corte de caja, incluyendo los montos de inicio,
 * fin y el total de ventas realizadas en el periodo.
 *
 * <p>Esta clase extiende de {@code ModelManagement} (asumiendo que maneja el ID y otros metadatos).</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 27-10-25
 */
public class CorteCaja extends ModelManagement {
    /**
     * Fecha y hora en la que se realizó el corte de caja.
     */
    private Date fecha;
    /**
     * Cantidad de dinero con la que inicia la caja al momento del corte.
     */
    private float montoInicial;
    /**
     * Cantidad de dinero final registrada en la caja al momento del corte.
     */
    private float montoFinal;
    /**
     * Suma total de las ventas registradas durante el turno o periodo del corte.
     */
    private float totalVentas;
    /**
     * El usuario que realizó o es responsable del corte de caja.
     */
    private Usuario usuario;

    /**
     * Constructor por defecto de la clase CorteCaja.
     */
    public CorteCaja(){

    }

    /**
     * Constructor con todos los parámetros para inicializar un objeto CorteCaja.
     *
     * @param id Identificador único del corte de caja (heredado de ModelManagement).
     * @param fecha Fecha y hora en la que se realizó el corte.
     * @param usuario El objeto Usuario responsable del corte.
     * @param montoInicial Cantidad inicial de la caja.
     * @param montoFinal Cantidad final registrada en la caja.
     * @param totalVentas El total de las ventas registradas.
     */
    public CorteCaja(int id, Date fecha, Usuario usuario, float montoInicial, float montoFinal, float totalVentas) {
        this.id = id;
        this.fecha = fecha;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.totalVentas = totalVentas;
        this.usuario = usuario; // Asegúrate de inicializar el usuario si lo pasas en el constructor
    }


    /**
     * Calcula la diferencia (faltante o sobrante) entre el monto final registrado
     * y la suma esperada (monto inicial + total de ventas).
     *
     * @return Un valor flotante que representa la diferencia.
     * Un valor positivo indica sobrante; un valor negativo indica faltante.
     */
    public float calcularDiferencia(){
        float diferncia = montoFinal -(montoInicial+totalVentas);
        return diferncia;
    }


    //getters y setters

    /**
     * Obtiene la fecha y hora en la que se realizó el corte de caja.
     * @return La fecha del corte.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora del corte de caja.
     * @param fecha La nueva fecha del corte.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el monto inicial con el que empezó la caja.
     * @return El monto inicial.
     */
    public float getMontoInicial() {
        return montoInicial;
    }

    /**
     * Establece el monto inicial de la caja.
     * @param montoInicial El nuevo monto inicial.
     */
    public void setMontoInicial(float montoInicial) {
        this.montoInicial = montoInicial;
    }

    /**
     * Obtiene el monto final registrado en la caja.
     * @return El monto final.
     */
    public float getMontoFinal() {
        return montoFinal;
    }

    /**
     * Establece el monto final registrado en la caja.
     * @param montoFinal El nuevo monto final.
     */
    public void setMontoFinal(float montoFinal) {
        this.montoFinal = montoFinal;
    }

    /**
     * Obtiene el total de las ventas registradas durante el periodo del corte.
     * @return El total de ventas.
     */
    public float getVentas() {
        return totalVentas;
    }

    /**
     * Establece el total de las ventas registradas.
     * @param ventas El nuevo total de ventas.
     */
    public void setVentas(float ventas) {
        this.totalVentas = ventas;
    }

    /**
     * Obtiene el usuario responsable del corte de caja.
     * @return El objeto Usuario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario responsable del corte de caja.
     * @param usuario El objeto Usuario.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
