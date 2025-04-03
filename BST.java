/**
 * Implementación de un árbol binario de búsqueda genérico
 * @param <E> Tipo de elementos que contendrá el árbol
 */
public class BST<E extends Comparable<E>> {
    // Clase interna para los nodos del árbol
    private class BSTNode {
        E data;
        BSTNode left;
        BSTNode right;
        
        public BSTNode(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }
    
    private BSTNode root;
    private int size;
    
    /**
     * Constructor por defecto
     */
    public BST() {
        root = null;
        size = 0;
    }
    
    /**
     * Inserta un elemento en el árbol
     * @param element Elemento a insertar
     */
    public void insert(E element) {
        if (element == null) {
            throw new IllegalArgumentException("No se puede insertar un elemento nulo");
        }
        root = insertRecursive(root, element);
    }
    
    private BSTNode insertRecursive(BSTNode current, E element) {
        // Si llegamos a un nodo nulo, creamos un nuevo nodo
        if (current == null) {
            size++;
            return new BSTNode(element);
        }
        
        // Comparamos el elemento a insertar con el elemento del nodo actual
        int compareResult = element.compareTo(current.data);
        
        if (compareResult < 0) {
            // Si es menor, insertamos en el subárbol izquierdo
            current.left = insertRecursive(current.left, element);
        } else if (compareResult > 0) {
            // Si es mayor, insertamos en el subárbol derecho
            current.right = insertRecursive(current.right, element);
        } else {
            // Si es igual, reemplazamos los datos (o ignoramos la inserción)
            current.data = element; // Actualizamos los datos
        }
        
        return current;
    }
    
    /**
     * Busca un elemento en el árbol
     * @param element Elemento a buscar
     * @return El elemento encontrado o null si no existe
     */
    public E search(E element) {
        if (element == null) {
            throw new IllegalArgumentException("No se puede buscar un elemento nulo");
        }
        BSTNode result = searchRecursive(root, element);
        return (result == null) ? null : result.data;
    }
    
    private BSTNode searchRecursive(BSTNode current, E element) {
        // Si el nodo es nulo o encontramos el elemento, retornamos el nodo actual
        if (current == null || element.compareTo(current.data) == 0) {
            return current;
        }
        
        // Decidimos si buscar en el subárbol izquierdo o derecho
        if (element.compareTo(current.data) < 0) {
            return searchRecursive(current.left, element);
        } else {
            return searchRecursive(current.right, element);
        }
    }
    
    /**
     * Realiza un recorrido in-order del árbol (orden ascendente)
     * @param action Acción a realizar con cada elemento
     */
    public void inOrderTraversal(java.util.function.Consumer<E> action) {
        if (action == null) {
            throw new IllegalArgumentException("La acción no puede ser nula");
        }
        inOrderTraversal(root, action);
    }
    
    private void inOrderTraversal(BSTNode node, java.util.function.Consumer<E> action) {
        if (node != null) {
            inOrderTraversal(node.left, action);
            action.accept(node.data);
            inOrderTraversal(node.right, action);
        }
    }
    
    /**
     * Realiza un recorrido in-order inverso del árbol (orden descendente)
     * @param action Acción a realizar con cada elemento
     */
    public void reverseInOrderTraversal(java.util.function.Consumer<E> action) {
        if (action == null) {
            throw new IllegalArgumentException("La acción no puede ser nula");
        }
        reverseInOrderTraversal(root, action);
    }
    
    private void reverseInOrderTraversal(BSTNode node, java.util.function.Consumer<E> action) {
        if (node != null) {
            reverseInOrderTraversal(node.right, action);
            action.accept(node.data);
            reverseInOrderTraversal(node.left, action);
        }
    }
    
    /**
     * Devuelve el número de elementos en el árbol
     * @return Número de elementos
     */
    public int size() {
        return size;
    }
    
    /**
     * Verifica si el árbol está vacío
     * @return true si está vacío, false en caso contrario
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Elimina todos los elementos del árbol
     */
    public void clear() {
        root = null;
        size = 0;
    }
}