public class Producto {
    private int id;
    private String nombre;
    private int cantidad;
 
    public Producto(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }
 
    public int getId() { 
        return id; 
    }
    public String getNombre() { 
        return nombre; 
    }
    public int getCantidad() { 
        return cantidad; 
    }
 
    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', cantidad=" + cantidad + "}";
    }
}