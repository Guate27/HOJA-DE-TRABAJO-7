import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para manejar la lectura de archivos CSV
 */
public class CSVHandler {
    
    /**
     * Lee un archivo CSV y lo convierte en una lista de productos
     * @param filePath Ruta del archivo CSV
     * @return Lista de productos
     * @throws IOException Si ocurre un error de lectura
     */
    public static List<Producto> cargarProductosDesdeCSV(String filePath) throws IOException {
        List<Producto> productos = new ArrayList<>();
        Path path = obtenerRutaValida(filePath);
        
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            
            // Índices de las columnas
            int skuIndex = -1;
            int priceRetailIndex = -1;
            int priceCurrentIndex = -1;
            int productNameIndex = -1;
            int categoryIndex = -1;
            
            while ((line = br.readLine()) != null) {
                // Utilizamos un regex para dividir por comas pero respetando los valores entre comillas
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Procesamos la primera línea para identificar los índices
                if (firstLine) {
                    // Mostrar las columnas encontradas para depuración
                    System.out.println("Columnas encontradas en el CSV:");
                    
                    for (int i = 0; i < values.length; i++) {
                        String column = values[i].replace("\"", "").trim();
                        System.out.println(i + ": " + column);
                        
                        // Buscar coincidencias exactas primero
                        if (column.equalsIgnoreCase("SKU")) {
                            skuIndex = i;
                        } else if (column.equalsIgnoreCase("Price_Retail")) {
                            priceRetailIndex = i;
                        } else if (column.equalsIgnoreCase("Price_Current")) {
                            priceCurrentIndex = i;
                        } else if (column.equalsIgnoreCase("Product_Name")) {
                            productNameIndex = i;
                        } else if (column.equalsIgnoreCase("Category")) {
                            categoryIndex = i;
                        }
                        
                        // Si no encontramos coincidencias exactas, buscar coincidencias parciales
                        if (skuIndex == -1 && column.toLowerCase().contains("sku")) {
                            skuIndex = i;
                        }
                        if (priceRetailIndex == -1 && (column.toLowerCase().contains("retail") || 
                             column.toLowerCase().contains("list price"))) {
                            priceRetailIndex = i;
                        }
                        if (priceCurrentIndex == -1 && (column.toLowerCase().contains("current") || 
                             column.toLowerCase().contains("sale price") || 
                             column.toLowerCase().contains("price"))) {
                            priceCurrentIndex = i;
                        }
                        if (productNameIndex == -1 && (column.toLowerCase().contains("product") && 
                             column.toLowerCase().contains("name"))) {
                            productNameIndex = i;
                        }
                        if (categoryIndex == -1 && column.toLowerCase().contains("category")) {
                            categoryIndex = i;
                        }
                    }
                    
                    firstLine = false;
                    
                    // Verificamos que tenemos todos los índices
                    boolean faltanColumnas = false;
                    StringBuilder columnasFaltantes = new StringBuilder("Columnas faltantes: ");
                    
                    if (skuIndex == -1) {
                        faltanColumnas = true;
                        columnasFaltantes.append("SKU, ");
                    }
                    if (priceRetailIndex == -1) {
                        faltanColumnas = true;
                        columnasFaltantes.append("Price_Retail, ");
                    }
                    if (priceCurrentIndex == -1) {
                        faltanColumnas = true;
                        columnasFaltantes.append("Price_Current, ");
                    }
                    if (productNameIndex == -1) {
                        faltanColumnas = true;
                        columnasFaltantes.append("Product_Name, ");
                    }
                    if (categoryIndex == -1) {
                        faltanColumnas = true;
                        columnasFaltantes.append("Category, ");
                    }
                    
                    if (faltanColumnas) {
                        throw new IOException(columnasFaltantes.toString());
                    }
                    
                    // Imprimir los índices encontrados para depuración
                    System.out.println("\nÍndices de columnas encontrados:");
                    System.out.println("SKU: " + skuIndex);
                    System.out.println("Price_Retail: " + priceRetailIndex);
                    System.out.println("Price_Current: " + priceCurrentIndex);
                    System.out.println("Product_Name: " + productNameIndex);
                    System.out.println("Category: " + categoryIndex);
                    
                    continue;
                }
                
                try {
                    // Verificamos que la línea tenga todos los campos necesarios
                    if (values.length <= Math.max(skuIndex, Math.max(priceRetailIndex, 
                                     Math.max(priceCurrentIndex, Math.max(productNameIndex, categoryIndex))))) {
                        System.out.println("Línea con formato incorrecto: " + line);
                        continue;
                    }
                    
                    // Extraemos y limpiamos los valores
                    String sku = limpiarCampo(values[skuIndex]);
                    
                    // Verificamos que el SKU no esté vacío
                    if (sku.isEmpty()) {
                        continue;
                    }
                    
                    double priceRetail = parseDouble(limpiarCampo(values[priceRetailIndex]), 0.0);
                    double priceCurrent = parseDouble(limpiarCampo(values[priceCurrentIndex]), 0.0);
                    String productName = limpiarCampo(values[productNameIndex]);
                    String category = limpiarCampo(values[categoryIndex]);
                    
                    // Creamos y agregamos el producto
                    Producto producto = new Producto(sku, priceRetail, priceCurrent, productName, category);
                    productos.add(producto);
                    
                } catch (Exception e) {
                    System.out.println("Error al procesar línea: " + e.getMessage());
                }
            }
        }
        
        System.out.println("Total de productos cargados: " + productos.size());
        return productos;
    }
    
    /**
     * Obtiene una ruta válida para el archivo a partir de la ruta proporcionada
     * @param filePath Ruta del archivo
     * @return Path del archivo
     * @throws IOException Si no se puede encontrar el archivo
     */
    private static Path obtenerRutaValida(String filePath) throws IOException {
        try {
            // Limpia comillas y espacios innecesarios
            filePath = filePath.trim().replaceAll("^\"|\"$", "");
            
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                return path;
            }
            
            // Si tiene doble extensión, intentamos corregirla
            String fileName = path.getFileName().toString();
            if (fileName.endsWith(".csv.csv")) {
                String newFileName = fileName.substring(0, fileName.length() - 4);
                Path newPath = path.resolveSibling(newFileName);
                if (Files.exists(newPath)) {
                    return newPath;
                }
            }
            
            // Intentamos con el nombre en el directorio actual
            Path currentDirPath = Paths.get(fileName);
            if (Files.exists(currentDirPath)) {
                return currentDirPath;
            }
            
            // Intentamos buscar en el directorio actual cualquier archivo CSV
            File dir = new File(".");
            File[] matchingFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
            if (matchingFiles != null && matchingFiles.length > 0) {
                System.out.println("Usando archivo encontrado en directorio actual: " + matchingFiles[0].getName());
                return matchingFiles[0].toPath();
            }
            
            // Si no se encontró ningún archivo, lanzar excepción
            throw new IOException("No se pudo encontrar el archivo: " + filePath);
            
        } catch (InvalidPathException e) {
            throw new IOException("Ruta de archivo inválida: " + e.getMessage());
        }
    }
    
    private static String limpiarCampo(String campo) {
        return campo.replaceAll("^\"|\"$", "").trim();
    }
    
    private static double parseDouble(String valor, double defaultValue) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}