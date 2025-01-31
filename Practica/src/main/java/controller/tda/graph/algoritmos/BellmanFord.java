package controller.tda.graph.algoritmos;

import java.util.HashMap;
import java.util.Map;
import controller.tda.list.LinkedList;
import controller.excepcion.ListEmptyException;
import controller.tda.graph.Adyacencia;
import controller.tda.graph.Grafo;
import controller.tda.graph.GrafoEtiquetadoNoDirecto;
import controller.tda.graph.GrafoEtiquetadoDirecto;
import java.util.Arrays;

public class BellmanFord {
    private GrafoEtiquetadoNoDirecto<String> grafo;
    private int origen;
    private float[] distancias;
    private int[] predecesores;

    public BellmanFord(GrafoEtiquetadoNoDirecto<String> grafo, int origen, int destino) {
        this.grafo = grafo;
        this.origen = origen;
        int n = grafo.num_vertices();
        this.distancias = new float[n + 1];
        this.predecesores = new int[n + 1];
    }

    public String caminoCorto(int destino) throws Exception {
        int n = grafo.num_vertices();

        // Inicialización
        Arrays.fill(distancias, Float.MAX_VALUE);
        distancias[origen] = 0;
        Arrays.fill(predecesores, -1);

        // Relajación de las aristas (n-1 veces)
        for (int i = 1; i < n; i++) {
            for (int u = 1; u <= n; u++) {
                LinkedList<Adyacencia> adyacencias = grafo.adyacencias(u);
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia adyacencia = adyacencias.get(j);
                    int v = adyacencia.getDestino();
                    float peso = adyacencia.getPeso();
                    if (distancias[u] != Float.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                        distancias[v] = distancias[u] + peso;
                        predecesores[v] = u;
                    }
                }
            }
        }

        // Verificación de ciclos negativos
        for (int u = 1; u <= n; u++) {
            LinkedList<Adyacencia> adyacencias = grafo.adyacencias(u);
            for (int j = 0; j < adyacencias.getSize(); j++) {
                Adyacencia adyacencia = adyacencias.get(j);
                int v = adyacencia.getDestino();
                float peso = adyacencia.getPeso();
                if (distancias[u] != Float.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                    return "El grafo tiene un ciclo negativo";
                }
            }
        }
        return reconstruirCamino(origen, destino);
    }

    private String reconstruirCamino(int origen, int destino) {
        if (distancias[destino] == Float.MAX_VALUE) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = destino;

        while (actual != -1) {
            String nombre = grafo.getLabelL(actual);
            camino.insert(0, nombre + "(" + actual + ")");
            
            if (predecesores[actual] != -1) {
                camino.insert(0, " <- ");  // Solo agregar " <- " si no es el origen
            }

            System.out.println("Nodo actual: " + actual + " (predecesor: " + predecesores[actual] + ")");
            actual = predecesores[actual];
        }
        return camino.toString();
    }

}
