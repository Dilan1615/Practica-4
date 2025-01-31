package controller.tda.graph;

import controller.excepcion.ListEmptyException;

public class GrafoNoDirecto extends GrafoDirecto {
    public GrafoNoDirecto(Integer num_vertices){
        super(num_vertices);
    }

    public void insertar_arista(Integer v1, Integer v2, Float peso) throws Exception {
        if (v1.intValue() <= num_vertices() && v2.intValue() <= num_vertices()) {
            if (!existe_arista(v1, v2)) {

                setNum_aristas(num_aristas() + 1);
                Adyacencia aux = new Adyacencia(v2, peso);
                aux.setPeso(peso);
                aux.setDestino(v2);
                getListaAdyacencia()[v1].add(aux);

                Adyacencia aux2 = new Adyacencia(v1, peso);
                aux.setPeso(peso);
                aux.setDestino(v1);
                getListaAdyacencia()[v2].add(aux2);
            }
        } else {
            throw new ListEmptyException("El vertice no existe");
        }
    }
}
