package com.example.sistema.persistencia;

/**
 * Define el **contrato (interfaz)** para cualquier objeto dentro del sistema
 * que deba ser identificado de forma única por un número entero.
 * <p>
 * Esta interfaz es utilizada comúnmente por clases de modelos (como Cliente, Pedido, Platillo)
 * que requieren un identificador para su gestión en la persistencia o bases de datos.
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-10-25
 */
public interface Identificable {

    /**
     * Obtiene el identificador único (ID) del objeto.
     *
     * @return El ID del objeto, representado por un número entero.
     */
    int getId();
}
