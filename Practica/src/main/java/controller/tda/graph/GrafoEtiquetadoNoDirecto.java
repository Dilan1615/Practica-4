package controller.tda.graph;

public class GrafoEtiquetadoNoDirecto<E> extends GrafoEtiquetadoDirecto<E> {
    public GrafoEtiquetadoNoDirecto (Integer num_vertices, Class<E> clazz){
        super(num_vertices, clazz);
    }

    public void insertar_aristaL (E v1, E v2, Float peso) throws Exception{
        if (isLabelsGraph()) {
            insertar_arista(getVerticeL(v1),getVerticeL(v2), peso);
            insertar_arista(getVerticeL(v2),getVerticeL(v1), peso);
        }else{
            throw new Exception("Grafo no etiquetado");
        }
    }
    public Float getWeigth2(Integer v1, Integer v2) throws Exception {
        return peso_arista(v1, v2);
    }
}
