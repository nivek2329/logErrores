public class EmptyInventoryException extends Exception {
    public EmptyInventoryException() {
        super("El inventario está vacío.");
    }
}