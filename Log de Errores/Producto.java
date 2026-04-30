/**
 * @return clase que representa un producto del inventario
 */
public class Producto {
    private int id;
    private String nombre;
    private int cantidad;
 
    /**
     * @param id identificador del producto
     * @param nombre nombre del producto
     * @param cantidad cantidad disponible del producto
     * @return objeto producto inicializado
     */

    public Producto(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    /**
     * @return identificador del producto
     */

    public int getId() { 
        return id; 
    }
    
    /**
     * @return identificador del producto
     */

    public String getNombre() { 
        return nombre; 
    }
    
    /**
     * @return cantidad disponible del producto
     */

    public int getCantidad() { 
        return cantidad; 
    }

    /**
     * @return representación en texto del producto
     */

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', cantidad=" + cantidad + "}";
    }
}