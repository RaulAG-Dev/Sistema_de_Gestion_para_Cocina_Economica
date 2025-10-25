# Historias de Usuario
### Gestión de menú
#### HU-01:
Como administrador o encargado de cocina, quiero poder registrar, editar y eliminar platos del menú con su descripción y precio, para mantener actualizada la oferta de comidas disponibles para los clientes.
#### Criterios de aceptación:
- El sistema permite agregar un nuevo plato con nombre, descripción y precio.
- Se puede modificar o eliminar un plato existente.
- El sistema valida que los precios sean numéricos positivos.

### Registro de pedidos
#### HU-02:
Como empleado de caja, quiero registrar los pedidos de los clientes, incluyendo las órdenes solicitadas y la cantidad, para llevar un control de los pedidos realizados.
#### Criterios de aceptación:
- Se pueden seleccionar los platos y las cantidades deseadas.
- El sistema permite modificar o eliminar un pedido antes de confirmarlo.
- El pedido queda registrado con fecha y hora.

#### HU-03:
Como empleado de caja, quiero que el sistema calcule automáticamente el precio total del pedido, para agilizar la atención al cliente y evitar errores manuales.
#### Criterios de aceptación:
- El sistema calcula el precio total en función de los platos seleccionados y sus cantidades.
- El total se actualiza automáticamente si se agregan o eliminan platos.
- El total se muestra antes de confirmar el pedido.

### Gestión de ingredientes y stock
#### HU-04:
Como encargado de cocina, quiero registrar los ingredientes disponibles y su cantidad en inventario, para llevar control de los insumos necesarios para preparar los platos.
#### Criterios de aceptación:
- Se puede agregar, editar y eliminar ingredientes.
- El sistema valida que las cantidades sean numéricas positivas.

#### HU-05:
Como encargado de cocina, quiero poder consultar el stock actual de cada ingrediente, para conocer en todo momento la cantidad disponible de insumos.
#### Criterios de aceptación:
- El sistema muestra una lista de ingredientes con su cantidad actual.
- La información se actualiza automáticamente al registrar compras o consumos.

#### HU-06:
Como encargado de cocina, quiero recibir alertas cuando el stock de un ingrediente sea bajo, para reabastecer a tiempo y evitar faltantes durante la preparación.
#### Criterios de aceptación:
- El sistema muestra una notificación o alerta visual cuando el stock ≤ nivel mínimo.
- La alerta desaparece cuando se actualiza el inventario.

### Registro de ventas y gestión de pagos
#### HU-07:
Como empleado de caja, quiero que el sistema mantenga un historial automático de todas las ventas realizadas, incluyendo fecha, platos vendidos y precio total, para tener un registro completo de las operaciones.
#### Criterios de aceptación:
- Cada venta incluye fecha, hora, platos vendidos y total.
- El registro se genera automáticamente al completar una venta.

#### HU-08:
Como empleado de caja, quiero poder consultar el registro de ventas en cualquier momento, para revisar operaciones pasadas y verificar información de ventas.
#### Criterios de aceptación:
- El sistema permite buscar ventas por fecha, cliente o plato.
- Los resultados se muestran de forma clara y ordenada.

#### HU-11:
Como empleado de caja, quiero generar recibos de pago después de registrar y cobrar un pedido, para entregárselos al cliente como comprobante de su compra.
#### Criterios de aceptación:
- El recibo contiene el nombre de la cocina, fecha, platos, precios y total.
- El recibo puede imprimirse o guardarse en formato digital.

### Gestión de clientes
#### HU-09:
Como empleado o administrador, quiero registrar información de contacto y preferencias de los clientes, para ofrecer un servicio más personalizado y conservar sus datos.
#### Criterios de aceptación:
- Se pueden registrar nombre, teléfono y preferencias del cliente.
- El sistema valida que los campos obligatorios estén completos.

#### HU-10:
Como empleado o administrador, quiero qué el sistema creee automaticamente un registro de clientes habituales, para tener un historial actualizado de los clientes frecuentes.
#### Criterios de aceptación:
- Se pueden agregar, editar o eliminar registros de clientes.
- El sistema permite visualizar la lista de clientes habituales.

### Generación de reportes
#### HU-12:
Como administrador, quiero generar reportes diarios, semanales y mensuales de ventas y ganancias, para analizar el rendimiento económico de la cocina.
#### Criterios de aceptación:
- Los reportes incluyen fecha, cantidad de ventas y ganancia total.


#### HU-13:
Como administrador, quiero obtener un informe de los platos más vendidos, para identificar cuáles son los más populares y ajustar el menú en consecuencia.
#### Criterios de aceptación:
- El sistema ordena los platos por frecuencia de venta.
- El reporte muestra la cantidad vendida y porcentaje de participación.
