/**
 * @return excepción que representa la condición de inventario vacío
 */
public class EmptyInventoryException extends Exception {
    /**
     * @return excepción con el mensaje indicando que el inventario está vacío
     */
    public EmptyInventoryException() {
        super("El inventario está vacío.");
    }
}