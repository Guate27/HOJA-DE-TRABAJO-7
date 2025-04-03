/**
 * Clase que representa un producto del retail
 */
public class Producto implements Comparable<Producto> {
    // Atributos del producto
    private final String sku;
    private final double priceRetail;
    private final double priceCurrent;
    private final String productName;
    private final String category;
    
    /**
     * Constructor con todos los atributos
     */
    public Producto(String sku, double priceRetail, double priceCurrent, String productName, String category) {
        // Validación de entrada
        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("El SKU no puede ser nulo o vacío");
        }
        if (productName == null) {
            productName = ""; // Convertimos a cadena vacía si es nulo
        }
        if (category == null) {
            category = ""; // Convertimos a cadena vacía si es nulo
        }
        
        this.sku = sku.trim();
        this.priceRetail = priceRetail;
        this.priceCurrent = priceCurrent;
        this.productName = productName.trim();
        this.category = category.trim();
    }
    
    /**
     * Constructor para búsquedas (solo con SKU)
     */
    public Producto(String sku) {
        this(sku, 0.0, 0.0, "", "");
    }
    
    // Getters
    public String getSku() {
        return sku;
    }
    
    public double getPriceRetail() {
        return priceRetail;
    }
    
    public double getPriceCurrent() {
        return priceCurrent;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    /**
     * Compara productos por su SKU
     */
    @Override
    public int compareTo(Producto other) {
        if (other == null) {
            return 1; // Cualquier objeto es mayor que null
        }
        return this.sku.compareTo(other.sku);
    }
    
    /**
     * Verifica si dos productos son iguales basándose en su SKU
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Producto producto = (Producto) obj;
        return sku.equals(producto.sku);
    }
    
    /**
     * Genera un hash code basado en el SKU
     */
    @Override
    public int hashCode() {
        return sku.hashCode();
    }
    
    /**
     * Devuelve una representación en texto del producto
     */
    @Override
    public String toString() {
        return String.format("SKU: %s | Nombre: %s | Categoría: %s | Precio Retail: $%.2f | Precio Actual: $%.2f",
                sku, productName, category, priceRetail, priceCurrent);
    }
}