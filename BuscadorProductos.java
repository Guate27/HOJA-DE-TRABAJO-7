import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Clase principal del programa que gestiona la búsqueda de productos
 */
public class BuscadorProductos {
    private final BST<Producto> productosTree;
    
    /**
     * Constructor por defecto
     */
    public BuscadorProductos() {
        productosTree = new BST<>();
    }
    
    /**
     * Carga productos desde un archivo CSV
     * @param filePath Ruta del archivo CSV
     * @throws IOException Si ocurre un error de lectura
     */
    public void cargarProductos(String filePath) throws IOException {
        List<Producto> productos = CSVHandler.cargarProductosDesdeCSV(filePath);
        
        // Limpiar el árbol antes de cargar nuevos productos
        productosTree.clear();
        
        // Insertar productos en el árbol
        for (Producto producto : productos) {
            productosTree.insert(producto);
        }
        
        System.out.println("Se han cargado " + productosTree.size() + " productos en el árbol.");
    }
    
    /**
     * Busca un producto por su SKU
     * @param sku SKU del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    public Producto buscarProductoPorSKU(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return null;
        }
        return productosTree.search(new Producto(sku.trim()));
    }
    
    /**
     * Lista todos los productos en orden ascendente por SKU
     * @return Lista de productos ordenados
     */
    public List<Producto> listarProductosAscendente() {
        List<Producto> productos = new ArrayList<>();
        productosTree.inOrderTraversal(productos::add);
        return productos;
    }
    
    /**
     * Lista todos los productos en orden descendente por SKU
     * @return Lista de productos ordenados
     */
    public List<Producto> listarProductosDescendente() {
        List<Producto> productos = new ArrayList<>();
        productosTree.reverseInOrderTraversal(productos::add);
        return productos;
    }
    
    /**
     * Método principal que ejecuta el programa
     */
    public static void main(String[] args) {
        try {
            // Configurar look and feel para diálogos
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignorar errores del look and feel
        }
        
        Scanner scanner = new Scanner(System.in);
        BuscadorProductos buscador = new BuscadorProductos();
        
        System.out.println("===== BUSCADOR DE PRODUCTOS =====");
        System.out.println("Programa para buscar y listar productos del retail");
        
        // Obtener la ruta del archivo CSV
        String filePath = obtenerRutaArchivo(scanner);
        
        try {
            // Cargar productos desde el archivo
            System.out.println("Cargando productos...");
            buscador.cargarProductos(filePath);
            
            // Menú principal
            mostrarMenuPrincipal(scanner, buscador);
            
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
            System.out.println("Verifica que el archivo existe y tiene el formato correcto.");
        } finally {
            scanner.close();
        }
        
        System.out.println("¡Gracias por usar el Buscador de Productos!");
    }
    
    /**
     * Solicita al usuario la ruta del archivo CSV
     * @param scanner Scanner para leer entrada del usuario
     * @return Ruta del archivo seleccionado
     */
    private static String obtenerRutaArchivo(Scanner scanner) {
        String filePath = null;
        
        // Intentar primero con el selector de archivos gráfico
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo CSV de productos");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
            
            int result = fileChooser.showOpenDialog(null);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();
            }
        } catch (Exception e) {
            // Si falla el selector gráfico, no hacer nada y continuar con entrada por consola
        }
        
        // Si no se seleccionó un archivo con el selector gráfico, pedirlo por consola
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("El selector de archivos no está disponible o no se seleccionó ningún archivo.");
            System.out.print("Ingrese la ruta del archivo CSV de productos: ");
            filePath = scanner.nextLine();
        }
        
        return filePath;
    }
    
    /**
     * Muestra el menú principal del programa
     * @param scanner Scanner para leer entrada del usuario
     * @param buscador Instancia del buscador de productos
     */
    private static void mostrarMenuPrincipal(Scanner scanner, BuscadorProductos buscador) {
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Buscar producto por SKU");
            System.out.println("2. Listar productos (orden ascendente por SKU)");
            System.out.println("3. Listar productos (orden descendente por SKU)");
            System.out.println("4. Cargar otro archivo CSV");
            System.out.println("5. Salir");
            
            System.out.print("\nIngrese una opción: ");
            
            // Manejar posibles errores de entrada
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    buscarProducto(scanner, buscador);
                    break;
                case 2:
                    listarProductos(scanner, buscador, true);
                    break;
                case 3:
                    listarProductos(scanner, buscador, false);
                    break;
                case 4:
                    cargarNuevoArchivo(scanner, buscador);
                    break;
                case 5:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
    
    /**
     * Busca un producto por SKU
     * @param scanner Scanner para leer entrada del usuario
     * @param buscador Instancia del buscador de productos
     */
    private static void buscarProducto(Scanner scanner, BuscadorProductos buscador) {
        System.out.print("\nIngrese el SKU del producto: ");
        String sku = scanner.nextLine().trim();
        
        Producto producto = buscador.buscarProductoPorSKU(sku);
        
        if (producto != null) {
            System.out.println("\n===== PRODUCTO ENCONTRADO =====");
            System.out.println(producto);
            
            // Mostrar información adicional de mejor precio
            System.out.println("\nInformación de precios:");
            System.out.printf("- Precio Retail: $%.2f\n", producto.getPriceRetail());
            System.out.printf("- Precio Actual: $%.2f\n", producto.getPriceCurrent());
            
            double ahorro = producto.getPriceRetail() - producto.getPriceCurrent();
            if (ahorro > 0) {
                double porcentajeAhorro = (ahorro / producto.getPriceRetail()) * 100;
                System.out.printf("- Ahorro: $%.2f (%.2f%%)\n", ahorro, porcentajeAhorro);
            }
        } else {
            System.out.println("\nNo se encontró ningún producto con el SKU: " + sku);
        }
    }
    
    /**
     * Lista los productos en orden ascendente o descendente
     * @param scanner Scanner para leer entrada del usuario
     * @param buscador Instancia del buscador de productos
     * @param ascendente true para orden ascendente, false para descendente
     */
    private static void listarProductos(Scanner scanner, BuscadorProductos buscador, boolean ascendente) {
        List<Producto> productos = ascendente ? 
                buscador.listarProductosAscendente() : 
                buscador.listarProductosDescendente();
        
        if (productos.isEmpty()) {
            System.out.println("\nNo hay productos cargados.");
            return;
        }
        
        System.out.println("\n===== PRODUCTOS (ORDEN " + 
                (ascendente ? "ASCENDENTE" : "DESCENDENTE") + ") =====");
        System.out.println("Total de productos: " + productos.size());
        
        int itemsPorPagina = 10;
        int totalPaginas = (int) Math.ceil((double) productos.size() / itemsPorPagina);
        int paginaActual = 1;
        
        while (paginaActual <= totalPaginas) {
            System.out.println("\nPágina " + paginaActual + " de " + totalPaginas);
            
            int inicio = (paginaActual - 1) * itemsPorPagina;
            int fin = Math.min(inicio + itemsPorPagina, productos.size());
            
            for (int i = inicio; i < fin; i++) {
                System.out.println((i + 1) + ". " + productos.get(i));
            }
            
            if (paginaActual < totalPaginas) {
                System.out.print("\n[N]ext para siguiente página, [Q]uit para volver al menú: ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                
                if (respuesta.equals("q") || respuesta.equals("quit")) {
                    break;
                } else {
                    paginaActual++;
                }
            } else {
                System.out.print("\nPresione ENTER para volver al menú...");
                scanner.nextLine();
                break;
            }
        }
    }
    
    /**
     * Carga un nuevo archivo CSV
     * @param scanner Scanner para leer entrada del usuario
     * @param buscador Instancia del buscador de productos
     */
    private static void cargarNuevoArchivo(Scanner scanner, BuscadorProductos buscador) {
        String filePath = obtenerRutaArchivo(scanner);
        
        try {
            System.out.println("Cargando productos desde nuevo archivo...");
            buscador.cargarProductos(filePath);
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
    }
}