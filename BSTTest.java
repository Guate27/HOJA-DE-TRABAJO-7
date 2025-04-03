import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el BST
 */
public class BSTTest {
    
    private BST<Integer> bstInteger;
    private BST<String> bstString;
    private BST<Producto> bstProducto;
    
    @Before
    public void setUp() {
        // Inicializar árboles para pruebas
        bstInteger = new BST<>();
        bstString = new BST<>();
        bstProducto = new BST<>();
    }
    
    @Test
    public void testInsertAndSizeInteger() {
        assertEquals(0, bstInteger.size());
        
        bstInteger.insert(10);
        assertEquals(1, bstInteger.size());
        
        bstInteger.insert(5);
        assertEquals(2, bstInteger.size());
        
        bstInteger.insert(15);
        assertEquals(3, bstInteger.size());
        
        // Insertar un elemento duplicado no debería aumentar el tamaño
        bstInteger.insert(10);
        assertEquals(3, bstInteger.size());
    }
    
    @Test
    public void testSearchInteger() {
        assertNull(bstInteger.search(10)); // Árbol vacío
        
        bstInteger.insert(10);
        bstInteger.insert(5);
        bstInteger.insert(15);
        bstInteger.insert(3);
        bstInteger.insert(7);
        
        assertEquals(Integer.valueOf(10), bstInteger.search(10));
        assertEquals(Integer.valueOf(5), bstInteger.search(5));
        assertEquals(Integer.valueOf(15), bstInteger.search(15));
        assertEquals(Integer.valueOf(3), bstInteger.search(3));
        assertEquals(Integer.valueOf(7), bstInteger.search(7));
        
        assertNull(bstInteger.search(20)); // Elemento no existente
    }
    
    @Test
    public void testInsertAndSizeString() {
        assertEquals(0, bstString.size());
        
        bstString.insert("banana");
        assertEquals(1, bstString.size());
        
        bstString.insert("apple");
        assertEquals(2, bstString.size());
        
        bstString.insert("cherry");
        assertEquals(3, bstString.size());
        
        // Insertar un elemento duplicado no debería aumentar el tamaño
        bstString.insert("banana");
        assertEquals(3, bstString.size());
    }
    
    @Test
    public void testSearchString() {
        assertNull(bstString.search("banana")); // Árbol vacío
        
        bstString.insert("banana");
        bstString.insert("apple");
        bstString.insert("cherry");
        
        assertEquals("banana", bstString.search("banana"));
        assertEquals("apple", bstString.search("apple"));
        assertEquals("cherry", bstString.search("cherry"));
        
        assertNull(bstString.search("orange")); // Elemento no existente
    }
    
    @Test
    public void testInsertAndSizeProducto() {
        assertEquals(0, bstProducto.size());
        
        Producto p1 = new Producto("SKU001", 100.0, 90.0, "Producto 1", "Categoría 1");
        Producto p2 = new Producto("SKU002", 200.0, 180.0, "Producto 2", "Categoría 2");
        Producto p3 = new Producto("SKU003", 300.0, 270.0, "Producto 3", "Categoría 1");
        
        bstProducto.insert(p1);
        assertEquals(1, bstProducto.size());
        
        bstProducto.insert(p2);
        assertEquals(2, bstProducto.size());
        
        bstProducto.insert(p3);
        assertEquals(3, bstProducto.size());
        
        // Insertar un producto con SKU duplicado no debería aumentar el tamaño
        Producto p1Duplicado = new Producto("SKU001", 150.0, 130.0, "Producto 1 Nuevo", "Categoría X");
        bstProducto.insert(p1Duplicado);
        assertEquals(3, bstProducto.size());
    }
    
    @Test
    public void testSearchProducto() {
        Producto p1 = new Producto("SKU001", 100.0, 90.0, "Producto 1", "Categoría 1");
        Producto p2 = new Producto("SKU002", 200.0, 180.0, "Producto 2", "Categoría 2");
        Producto p3 = new Producto("SKU003", 300.0, 270.0, "Producto 3", "Categoría 1");
        
        assertNull(bstProducto.search(p1)); // Árbol vacío
        
        bstProducto.insert(p1);
        bstProducto.insert(p2);
        bstProducto.insert(p3);
        
        // Búsqueda por SKU (creando un nuevo producto con el mismo SKU)
        Producto searchP1 = new Producto("SKU001");
        Producto foundP1 = bstProducto.search(searchP1);
        assertNotNull(foundP1);
        assertEquals("SKU001", foundP1.getSku());
        assertEquals(100.0, foundP1.getPriceRetail(), 0.001);
        assertEquals(90.0, foundP1.getPriceCurrent(), 0.001);
        assertEquals("Producto 1", foundP1.getProductName());
        assertEquals("Categoría 1", foundP1.getCategory());
        
        // Búsqueda de un producto no existente
        Producto searchP4 = new Producto("SKU004");
        assertNull(bstProducto.search(searchP4));
    }
    
    @Test
    public void testClear() {
        bstInteger.insert(10);
        bstInteger.insert(5);
        bstInteger.insert(15);
        assertEquals(3, bstInteger.size());
        
        bstInteger.clear();
        assertEquals(0, bstInteger.size());
        assertNull(bstInteger.search(10));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInsertNull() {
        bstInteger.insert(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSearchNull() {
        bstInteger.search(null);
    }
}