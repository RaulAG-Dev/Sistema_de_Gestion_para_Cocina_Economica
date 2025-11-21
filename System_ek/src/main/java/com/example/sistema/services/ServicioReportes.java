package com.example.sistema.services;

import com.example.sistema.models.Pedido;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestiona la lógica de filtrado de pedidos usando el API de tiempo moderno (java.time)
 * para generar reportes, pero se convierte a java.util.Date para integrarse con ServicioVentas.
 */
public class ServicioReportes {

    // Se conecta directamente al Singleton de ServicioVentas para obtener datos
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    /**
     * Método central: Filtra pedidos entre dos fechas.
     * Recibe LocalDateTime (moderno) para un cálculo preciso.
     */
    public List<Pedido> obtenerVentas(LocalDateTime inicio, LocalDateTime fin) {
        // Convertir LocalDateTime a Date (antiguo) para usar en el filtro
        Date fechaInicio = Date.from(inicio.atZone(ZoneId.systemDefault()).toInstant());
        Date fechaFin = Date.from(fin.atZone(ZoneId.systemDefault()).toInstant());

        // Obtener la lista completa para filtrar
        List<Pedido> todosLosPedidos = servicioVentas.getPedidosMaestros();

        return todosLosPedidos.stream()
                .filter(p -> {
                    Date fechaPedido = p.getFechaHora();
                    if (fechaPedido == null) return false;

                    // Comprobación usando el método antiguo isAfter/isBefore de Date
                    // La conversión garantiza que los rangos sean correctos.
                    return fechaPedido.after(fechaInicio) && fechaPedido.before(fechaFin);
                })
                .collect(Collectors.toList());
    }

    /**
     * Genera reportes de ventas del día actual (00:00:00 hasta ahora).
     */
    public List<Pedido> obtenerReporteHoy() {
        LocalDateTime inicioDelDia = LocalDate.now().atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();
        return obtenerVentas(inicioDelDia, ahora);
    }

    /**
     * Genera reporte Semanal (Lunes de esta semana hasta ahora).
     */
    public List<Pedido> obtenerReporteSemanal() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDateTime inicio = inicioSemana.atStartOfDay();

        return obtenerVentas(inicio, LocalDateTime.now());
    }

    /**
     * Genera reporte Mensual (Día 1 del mes actual hasta ahora).
     */
    public List<Pedido> obtenerReporteMensual() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDateTime inicio = inicioMes.atStartOfDay();

        return obtenerVentas(inicio, LocalDateTime.now());
    }
}