import java.util.logging.Logger;
 
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());
 
    public static void main(String[] args) {
        Inventario inv = new Inventario();
 
 
        inv.agregarProducto(new Producto(1, "Manzana", 50));
        inv.agregarProducto(new Producto(2, "Pera", 30));
 
        try {
            Producto p = inv.buscarProducto(1);
            log.info("Producto encontrado: " + p);
        } catch (ProductNotFoundException e) {
            log.warning(e.getMessage());
        }
 
        try {
            inv.buscarProducto(99);
        } catch (ProductNotFoundException e) {
            log.warning(e.getMessage());
        }
 
        try {
            inv.eliminarProducto(2);
            log.info("Producto id=2 eliminado correctamente.");
        } catch (ProductNotFoundException | EmptyInventoryException e) {
            log.warning(e.getMessage());
        }
 
        Inventario vacio = new Inventario();
        try {
            vacio.eliminarProducto(1);
        } catch (EmptyInventoryException e) {
            log.warning(e.getMessage());
        } catch (ProductNotFoundException e) {
            log.warning(e.getMessage());
        }
    }
}