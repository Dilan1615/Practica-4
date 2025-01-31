package controller.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controller.dao.implement.AdapterDao;
import controller.tda.graph.GrafoEtiquetadoNoDirecto;
import controller.tda.list.LinkedList;
import controller.tda.graph.algoritmos.Floyd;
import controller.tda.graph.algoritmos.BFS;
import controller.tda.graph.algoritmos.BellmanFord;
import models.Habitacion;
import models.enumerator.TipoHabitacion;

public class HabitacionDao extends AdapterDao<Habitacion> {
    private Habitacion habitacion;
    private LinkedList<Habitacion> listAll;
    private GrafoEtiquetadoNoDirecto<String> grafo;
    private LinkedList<String> verticeName;
    private String nombre = "HabitacionGrafo.json";

    public GrafoEtiquetadoNoDirecto<String> crearGrafo() {
        if (verticeName == null) {
            verticeName = new LinkedList<>();
        }

        LinkedList<Habitacion> list = this.getListAll();
        if (!list.isEmpty()) {
            if (grafo == null) {
                grafo = new GrafoEtiquetadoNoDirecto<>(list.getSize(), String.class);
            }

            Habitacion[] habitacion = list.toArray();
            for (int i = 0; i < habitacion.length; i++) { // Convierte el tipo a String

                // Usa el valor del Enum directamente en lugar de valueOf, si ya lo tienes
                this.grafo.labelsVertices(i + 1, habitacion[i].getTipo().name());
                verticeName.add(habitacion[i].getTipo().name());
            }
            this.grafo.dibujarGrafo();
        }
        return this.grafo;

    }

    public void guardarGrafo() throws Exception {
        this.grafo.guardarGrafoDirigido(nombre);
    }

    public JsonArray obtenerPeso() throws Exception {
        if (grafo == null) {
            crearGrafo();
        }

        if (grafo.existsFile(nombre)) {
            grafo.cargaModelosDao();
            grafo.cargarGrafo(nombre);

            JsonArray graphData = grafo.obtainWeights();            
            return graphData;
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
    }

    public GrafoEtiquetadoNoDirecto<String> obtenerGrafo() throws Exception {
        if (grafo == null) {
            crearGrafo();
        }
        if (grafo.existsFile(nombre)) {
            grafo.cargaModelosDao();
            grafo.grafoAristanRandom(nombre);
            System.out.println("Modelo asociado al grafo: " + nombre);
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }

        guardarGrafo();
        return grafo;
    }

    public String bfs(int origen) throws Exception {
        if (grafo == null) {
            throw new Exception("El grafo no existe");
        }
        BFS bfsAlgoritmo = new BFS(grafo, origen);       
        String recorrido = bfsAlgoritmo.recorrerGrafo();
        return recorrido;
    }

    public String caminoCorto(int origen, int destino, int algoritmo) throws Exception {
        if (grafo == null) {
            throw new Exception("El grafo no existe");
        }

        if (origen < 0 || destino < 0) {
            throw new IllegalArgumentException("Los nodos origen y destino deben ser válidos y no negativos");
        }

        if (algoritmo != 1 && algoritmo != 2) {
            throw new IllegalArgumentException("Algoritmo no válido. Use 1 para Floyd o 2 para Bellman-Ford.");
        }

        System.out.println("Calculando camino corto desde " + origen + " hasta " + destino);

        switch (algoritmo) {
            case 1: // Algoritmo de Floyd
                return new Floyd(grafo, origen, destino).caminoCorto();

            case 2: // Algoritmo de Bellman-Ford
                return new BellmanFord(grafo, origen, destino).caminoCorto(algoritmo);

            default:
                throw new UnsupportedOperationException("Algoritmo no soportado.");
        }
    }

    public JsonObject dataGrafo() throws Exception {
        if (grafo == null) {
            crearGrafo();
        }

        if (grafo.existsFile(nombre)) {
            grafo.cargaModelosDao();
            grafo.cargarGrafo(nombre);
            
            JsonObject graphData = grafo.getVisGraphData(); 
            return graphData; 
        } else {
            throw new Exception("El archivo de grafo no existe.");
        }
    }

    public HabitacionDao() {
        super(Habitacion.class);
    }

    public Habitacion getHabitacion() {
        if (habitacion == null) {
            habitacion = new Habitacion();
        }
        return this.habitacion;
    }

    public void setHabticacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public TipoHabitacion getTipoHabitacion(String tipo) {
        return TipoHabitacion.valueOf(tipo);
    }

    public TipoHabitacion[] getTipos() {
        return TipoHabitacion.values();
    }

    public LinkedList<Habitacion> getListAll() {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        return this.listAll;
    }

    // Guardar un nuevo habitacion
    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        getHabitacion().setId(id);
        persist(getHabitacion());
        return true;
    }

    // Actualizar habitacion
    public Boolean update() throws Exception {
        this.merge(getHabitacion(), getHabitacion().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean delete(Integer id) throws Exception {
        for (int i = 0; i < getListAll().getSize(); i++) {
            Habitacion hab = getListAll().get(i);
            if (hab.getId().equals(id)) {
                getListAll().delete(i);
                return true;
            }

        }
        throw new Exception("Habitacion no encontrado con ID: " + id);
    }
}
