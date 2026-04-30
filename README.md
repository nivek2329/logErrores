# Sistema de Inventario con Log de Errores

Ejercicio práctico de implementación de un inventario de productos en Java, con manejo de excepciones checked y registro de errores en archivos de log separados según su naturaleza.

---

## Tabla de contenido

1. [Descripción general](#descripción-general)
2. [Clases del dominio](#clases-del-dominio)
3. [Colección utilizada y justificación](#colección-utilizada-y-justificación)
4. [Excepciones](#excepciones)
5. [Sistema de logging](#sistema-de-logging)
6. [Métodos implementados](#métodos-implementados)

---

## Descripción general

El sistema gestiona un inventario de productos mediante tres operaciones básicas: agregar, buscar y eliminar. El requisito principal del ejercicio, más allá del CRUD, es la **separación del registro de errores en dos archivos de log**:

- `expected_errors_log.log` : errores de negocio esperados (excepciones checked del dominio).
- `inventario_log.log` : errores inesperados del programador (fallos no anticipados) y operaciones exitosas.

Esta separación permite que el equipo de soporte y el equipo de desarrollo consulten logs distintos sin mezclar información.

---

## Clases del dominio

### `Producto`

Entidad principal del sistema. Representa un ítem del inventario con tres atributos inmutables (se asignan en el constructor y no tienen setters), lo que garantiza que un producto no cambie su identidad una vez creado.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `int` | Identificador único. Es la clave del `HashMap` en `Inventario`. |
| `nombre` | `String` | Nombre descriptivo del producto. |
| `cantidad` | `int` | Unidades disponibles en inventario. |

El método `toString()` produce una representación legible que se incluye directamente en los mensajes de log.

---

### `Inventario`

Clase central del sistema. Contiene el contenedor de productos y los dos loggers, y expone los tres métodos de negocio.

**Atributos:**

| Atributo | Tipo | Rol |
|----------|------|-----|
| `productos` | `HashMap<Integer, Producto>` | Almacén principal de productos. |
| `expectedLog` | `Logger` | Escribe en `expected_errors_log.log`. |
| `inventarioLog` | `Logger` | Escribe en `inventario_log.log`. |

La configuración de los `FileHandler` se hace en un bloque `static` que se ejecuta una sola vez al cargar la clase, garantizando que los archivos de log estén listos antes de cualquier operación.

---

### `Main`

Clase de prueba que construye un `Inventario`, ejecuta los cuatro escenarios principales y captura las excepciones para mostrar su comportamiento:

1. Buscar un producto que **sí existe** : éxito.
2. Buscar un producto que **no existe** : `ProductNotFoundException`.
3. Eliminar un producto que **existe** : éxito.
4. Eliminar de un inventario **vacío** : `EmptyInventoryException`.

---

## Colección utilizada y justificación

| Colección | Clase | Tipo | Justificación |
|-----------|-------|------|---------------|
| `productos` | `Inventario` | `HashMap<Integer, Producto>` | Las operaciones `buscarProducto` y `eliminarProducto` acceden por `id` (clave entera). `HashMap` ofrece `containsKey` y `get`, lo que es óptimo para un inventario que puede crecer. No se requiere orden entre productos, por lo que no hay motivo para usar un `TreeMap`. La clave es el `id` del producto, que es único por diseño. |

---

## Excepciones

El sistema usa excepciones **checked** para los errores de negocio, lo que obliga al llamador a manejarlos explícitamente con `try-catch` o declararlos en `throws`.

```
Exception  (Java estándar)
├── ProductNotFoundException
└── EmptyInventoryException
```

### `ProductNotFoundException`

Se lanza en `buscarProducto` y en `eliminarProducto` cuando el `id` solicitado no existe en el `HashMap`.

```java
public ProductNotFoundException(int id) {
    super("Producto con id " + id + " no encontrado.");
}
```

- **Cuándo ocurre:** `buscarProducto(99)` con id inexistente, o `eliminarProducto(5)` con id no registrado.
- **Dónde se registra:** `expected_errors_log.log` (es un error esperado de negocio).

---

### `EmptyInventoryException`

Se lanza en `eliminarProducto` cuando el inventario está completamente vacío, antes de verificar el id. Tiene prioridad sobre `ProductNotFoundException` porque no tiene sentido buscar un id si no hay nada.

```java
public EmptyInventoryException() {
    super("El inventario está vacío.");
}
```

- **Cuándo ocurre:** llamar a `eliminarProducto` sobre un `Inventario` recién creado.
- **Dónde se registra:** `expected_errors_log.log`.

---

## Sistema de logging

La decisión de diseño más importante del ejercicio es la **separación en dos canales de log**:

### `expected_errors_log.log`
Registra las excepciones de negocio que **se anticipan** en el flujo normal del sistema: buscar un id que no existe, intentar eliminar de un inventario vacío. Son situaciones que el código ya contempla y maneja.

```java
expectedLog.warning("Búsqueda fallida: " + e.getMessage());
```

### `inventario_log.log`
Registra dos tipos de eventos:
- **Operaciones exitosas** (nivel `INFO`): producto agregado, producto eliminado.
- **Errores inesperados** (nivel `SEVERE`): cualquier excepción no contemplada (`catch (Exception e)`), que indica un fallo del programador o del entorno.

```java
inventarioLog.info("Producto agregado: " + p);
inventarioLog.severe("Error inesperado al agregar producto: " + e.getMessage());
```

### Configuración técnica

Ambos loggers se configuran con `FileHandler` en modo `append` (`true`) para no borrar el historial al reiniciar el programa, y con `setUseParentHandlers(false)` para evitar que los mensajes también aparezcan en la consola del sistema.

---

## Métodos implementados

### `agregarProducto(Producto p)`

Inserta el producto en el `HashMap` usando su `id` como clave. Registra la operación exitosa en `inventario_log`. Captura cualquier excepción inesperada con un `catch (Exception e)` defensivo.

---

### `buscarProducto(int id) throws ProductNotFoundException`

1. Verifica con `containsKey(id)`.
2. Si no existe, lanza `ProductNotFoundException`, la registra en `expected_errors_log` y la relanza.
3. Si existe, retorna el producto con `get(id)` .

---

### `eliminarProducto(int id) throws ProductNotFoundException, EmptyInventoryException`

1. Verifica `productos.isEmpty()` si es vacío, lanza `EmptyInventoryException`.
2. Verifica `containsKey(id)` si no existe, lanza `ProductNotFoundException`.
3. Si pasa ambas validaciones, elimina con `remove(id)` y registra el éxito.
4. Ambas excepciones de negocio se registran en `expected_errors_log` antes de relanzarse.

El orden de las validaciones es importante: primero inventario vacío, luego id inexistente.

---


Al terminar la ejecución se generan (o actualizan) dos archivos en el directorio de trabajo:

| Archivo | Contenido |
|---------|-----------|
| `expected_errors_log.log` | Advertencias de búsquedas y eliminaciones fallidas. |
| `inventario_log.log` | Confirmaciones de operaciones exitosas y errores graves inesperados. |
