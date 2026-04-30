/**
 * @return excepción que indica que un producto no fue encontrado
 */
public class ProductNotFoundException extends Exception {

    /**
     * @param id identificador del producto que no fue encontrado
     * @return excepción con el mensaje indicando que el producto no existe
     */
    public ProductNotFoundException(int id) {
        super("Producto con id " + id + " no encontrado.");
    }
}