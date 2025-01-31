package controller.tda.graph.algoritmos;

import controller.tda.graph.GrafoEtiquetadoNoDirecto;
import java.util.*;

public class BFS {
    private GrafoEtiquetadoNoDirecto<String> grafo;
    private int origen;

    public BFS(GrafoEtiquetadoNoDirecto<String> grafo, int origen) {
        this.grafo = grafo;
        this.origen = origen;
    }

    public String recorrerGrafo() throws Exception {
        int n = grafo.num_vertices();
        boolean[] visitados = new boolean[n + 1];  //nodo visitado
        Arrays.fill(visitados, false);
        
        Queue<Integer> cola = new LinkedList<>();
        cola.add(origen);
        visitados[origen] = true;   
        List<Integer> recorrido = new ArrayList<>();
        
        // BFS
        while (!cola.isEmpty()) {
            int nodoActual = cola.poll();
            recorrido.add(nodoActual);

            // Aquí obtenemos los vecinos del nodo actual
            List<Integer> vecinos = obtenerVecinos(nodoActual);
            for (int vecino : vecinos) {
                if (!visitados[vecino]) {
                    visitados[vecino] = true;
                    cola.add(vecino);
                }
            }
        }

        // Retornamos el recorrido en formato de lista
        return "Recorrido BFS: " + recorrido.toString();
    }

    private List<Integer> obtenerVecinos(int nodo) throws Exception {
        List<Integer> vecinos = new ArrayList<>();
        
        int n = grafo.num_vertices();
        for (int i = 1; i <= n; i++) {
            try {
                // Comprobamos si hay una arista entre 'nodo' y 'i'
                Float peso = grafo.getWeigth2(nodo, i);
                if (peso != null && peso > 0) {
                    vecinos.add(i); 
                }
            } catch (Exception e) {                
            }
        }

        return vecinos;
    }
}
