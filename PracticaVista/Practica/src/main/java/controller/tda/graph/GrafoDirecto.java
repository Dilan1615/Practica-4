package controller.tda.graph;

import controller.excepcion.ListEmptyException;
import controller.tda.list.LinkedList;

public class GrafoDirecto extends Grafo {
    private Integer num_vertices;
    private Integer num_aristas;
    private LinkedList<Adyacencia> listaAdyacencia[];

    public GrafoDirecto(Integer num_vertices) {
        this.num_vertices = num_vertices;
        this.num_aristas = 0;
        listaAdyacencia = new LinkedList[num_vertices +1];
        for (int i = 0; i <= num_vertices; i++) {
            listaAdyacencia[i] = new LinkedList<Adyacencia>();
        }
    }
    public Integer num_aristas() {
        return this.num_aristas;
    }

    public Integer num_vertices() {
        return this.num_vertices;
    }

    @Override
    public Boolean existe_arista(Integer v1, Integer v2) throws Exception {
        Boolean is = false;
        if (v1.intValue() <= num_vertices && v2.intValue() <= num_vertices) {
            LinkedList<Adyacencia> lista = listaAdyacencia[v1];
            if (!lista.isEmpty()) {
                Adyacencia[] matrix = lista.toArray();
                for (int i = 0; i < matrix.length; i++) {
                    Adyacencia aux = matrix[i];
                    if (aux.getDestino().intValue() == v2.intValue()) {
                        is = true;
                        break;
                    }
                }
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
        return is;
        
    }
    
    
    public Float peso_arista (Integer v1, Integer v2) throws Exception {
        Float weight = Float.NaN;
        if (v1.intValue() <= num_vertices && v2.intValue() <= num_vertices) {
            LinkedList<Adyacencia> lista = listaAdyacencia[v1];
            if (!lista.isEmpty()) {
                Adyacencia[] matrix = lista.toArray();
                for (int i = 0; i < matrix.length; i++) {
                    Adyacencia aux = matrix[i];
                    if (aux.getDestino().intValue() == v2.intValue()) {
                        weight = aux.getPeso();
                        break;
                    }
                }
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
        return weight;

    }

    public void insertar_arista (Integer v1, Integer v2, Float weight) throws Exception {
        if (v1.intValue() <= num_vertices && v2.intValue() <= num_vertices) {
            if (!existe_arista(v1, v2)) {
                num_aristas++;
                Adyacencia aux = new Adyacencia(v2, weight);
                aux.setPeso(weight);
                aux.setDestino(v2);
                listaAdyacencia[v1].add(aux);
                
            }
        } else {
            throw new  ListEmptyException("El vertice no existe");
        }
        
    }

    @Override
    public void insertar_arista(Integer v1, Integer v2) throws Exception {
        this.insertar_arista(v1, v2,Float.NaN);
    }

    public LinkedList<Adyacencia> adyacencias (Integer v1) {
        return listaAdyacencia[v1];
    }

    public LinkedList<Adyacencia>[] getListaAdyacencia() {
        return this.listaAdyacencia;
    }

   public Integer setNum_aristas (Integer num_aristas){
    return this.num_aristas = num_aristas;
   }


}
