import java.util.HashMap;
import java.util.logging.*;
import java.io.IOException;

/**
 * @return clase que gestiona los productos de un inventario
 */
 
public class Inventario {

    private HashMap<Integer, Producto> productos = new HashMap<>();
 
    private static final Logger expectedLog = Logger.getLogger("expected_errors_log");

    private static final Logger inventarioLog = Logger.getLogger("inventario_log");
 
    static {

        try {

            FileHandler expectedHandler = new FileHandler("expected_errors_log.log", true);

            expectedHandler.setFormatter(new SimpleFormatter());

            expectedLog.addHandler(expectedHandler);

            expectedLog.setUseParentHandlers(false);
 
            FileHandler inventarioHandler = new FileHandler("inventario_log.log", true);

            inventarioHandler.setFormatter(new SimpleFormatter());

            inventarioLog.addHandler(inventarioHandler);

            inventarioLog.setUseParentHandlers(false);
 
        } catch (IOException e) {

            Logger.getLogger(Inventario.class.getName()).severe("Error al configurar logs: " + e.getMessage());

        }

    }

    /**
     * @param p producto que se va a agregar al inventario
     * @return no retorna ningún valor
     */

    public void agregarProducto(Producto p) {

        try {

            productos.put(p.getId(), p);

            inventarioLog.info("Producto agregado: " + p);

        } catch (Exception e) {

            inventarioLog.severe("Error inesperado al agregar producto: " + e.getMessage());

        }

    }

    /**
     * @param id identificador del producto a buscar
     * @return producto encontrado con el id especificado
     */

    public Producto buscarProducto(int id) throws ProductNotFoundException {

        try {

            if (!productos.containsKey(id)) {

                throw new ProductNotFoundException(id);

            }

            return productos.get(id);

        } catch (ProductNotFoundException e) {

            expectedLog.warning("Búsqueda fallida: " + e.getMessage());

            throw e;

        } catch (Exception e) {

            inventarioLog.severe("Error inesperado al buscar producto id=" + id + ": " + e.getMessage());

            throw e;

        }

    }

    /**
     * @param id identificador del producto a eliminar
     * @return no retorna ningún valor
     */

    public void eliminarProducto(int id) throws ProductNotFoundException, EmptyInventoryException {

        try {

            if (productos.isEmpty()) {

                throw new EmptyInventoryException();

            }

            if (!productos.containsKey(id)) {

                throw new ProductNotFoundException(id);

            }

            productos.remove(id);

            inventarioLog.info("Producto eliminado: id=" + id);

        } catch (EmptyInventoryException | ProductNotFoundException e) {

            expectedLog.warning("Eliminación fallida: " + e.getMessage());

            throw e;

        } catch (Exception e) {

            inventarioLog.severe("Error inesperado al eliminar producto id=" + id + ": " + e.getMessage());

            throw e;

        }

    }
}