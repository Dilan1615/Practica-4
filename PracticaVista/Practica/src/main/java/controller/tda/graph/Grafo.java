package controller.tda.graph;

import controller.dao.HabitacionDao;
import controller.excepcion.ListEmptyException;
import controller.tda.list.LinkedList;
import models.Habitacion;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Grafo {
    private static String archivo = "data/";
    private Map<Integer, Habitacion> verticeModelo = new HashMap<>();

    public abstract Integer num_vertices();

    public abstract Integer num_aristas();

    public abstract Boolean existe_arista(Integer v1, Integer v2) throws Exception;

    public abstract Float peso_arista(Integer v1, Integer v2) throws Exception;

    public abstract void insertar_arista(Integer v1, Integer v2) throws Exception;

    public abstract void insertar_arista(Integer v1, Integer V2, Float peso) throws Exception;

    public abstract LinkedList<Adyacencia> adyacencias(Integer v1);

    public Integer getVertice(Integer v) throws Exception {
        return v;
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        try {
            for (int i = 1; i < this.num_vertices(); i++) {
                grafo.append("Vertice: ").append(i).append("\n");
                LinkedList<Adyacencia> lista = this.adyacencias(i);
                if (!lista.isEmpty()) {
                    Adyacencia[] ady = lista.toArray();
                    for (Adyacencia adyacencia : ady) {
                        grafo.append("Adyacencia: V").append(adyacencia.getDestino())
                                .append(" Peso: ").append(adyacencia.getPeso()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo.toString();
    }

    public void saveGrafoDirigido(String fileName) throws Exception {
        JsonArray grafoArray = new JsonArray();
        for (int i = 1; i <= this.num_vertices(); i++) {
            JsonObject verticesObject = new JsonObject();
            verticesObject.addProperty("labelId", this.getVertice(i));

            JsonArray destionArray = new JsonArray();
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia ady1 = adyacencias.get(j);
                    JsonObject destiJsonObject = new JsonObject();
                    destiJsonObject.addProperty("from", this.getVertice(i));
                    destiJsonObject.addProperty("to", ady1.getDestino());
                    destionArray.add(destiJsonObject);
                }
            }
            verticesObject.add("destinations", destionArray);
            grafoArray.add(verticesObject);
        }
        Gson gson = new Gson();
        String json = gson.toJson(grafoArray);

        File directory = new File(archivo);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(archivo + fileName)) {
            fileWriter.write(json);
        }
    }

    public void cargaModelosDao() throws ListEmptyException {
        HabitacionDao habitacionDao = new HabitacionDao();
        LinkedList<Habitacion> habitacionList = habitacionDao.getListAll();

        for (int i = 0; i < habitacionList.getSize(); i++) {
            Habitacion habitacion = habitacionList.get(i);
            verticeModelo.put(habitacion.getId(), habitacion);
        }
    }

    public void cargarGrafo(String fileName) throws Exception {
        try (FileReader fileReader = new FileReader(archivo + fileName)) {
            Gson gson = new Gson();
            JsonArray grafoArray = gson.fromJson(fileReader, JsonArray.class);

            for (JsonElement jsonElement : grafoArray) {
                JsonObject vertiObject = jsonElement.getAsJsonObject();

                Integer labelID = vertiObject.get("labelId").getAsInt();

                Habitacion modelo = verticeModelo.get(labelID);

                if (modelo == null) {
                    continue;
                }
                this.addVerticeModelo(labelID, modelo);
                JsonArray destiJsonArray = vertiObject.getAsJsonArray("destinations");

                for (JsonElement destinoElement : destiJsonArray) {
                    JsonObject destiJsonObject = destinoElement.getAsJsonObject();

                    Integer from = destiJsonObject.get("from").getAsInt();
                    Integer to = destiJsonObject.get("to").getAsInt();

                    Habitacion modelFrom = verticeModelo.get(from);
                    Habitacion modelTo = verticeModelo.get(to);
                    if (modelFrom == null || modelTo == null) {

                    } else {
                        Float peso = (float) calcularDistancia(modelFrom, modelTo);

                        this.insertar_arista(from, to, peso);
                        System.out.println("Arista añadida de " + from + " a " + to + " con peso calculado: " + peso);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Método para borrar todas las adyacencias de todos los vértices
    public void clearEdges() {
        for (int i = 1; i <= this.num_vertices(); i++) {
            this.adyacencias(i).reset(); // Limpiar la lista de adyacencias para cada vértice
        }
    }

    public void grafoAristanRandom(String filename) throws Exception {
        // Primero, cargamos el grafo desde el archivo JSON
        cargarGrafo(filename);

        // Ahora obtenemos los habitaciones desde el DAO para asociarlos a los vértices
        cargaModelosDao();

        clearEdges(); // Función para borrar las adyacencias

        // Para cada vértice, agregamos al menos 3 conexiones aleatorias
        Random random = new Random();
        for (int i = 1; i <= this.num_vertices(); i++) {
            LinkedList<Adyacencia> existingEdges = this.adyacencias(i);
            int connectionsCount = existingEdges.getSize();

            // Aseguramos que cada vértice tenga al menos 3 conexiones
            while (connectionsCount < 3) {
                // Generamos un vértice aleatorio diferente al actual
                int randomVertex = random.nextInt(this.num_vertices()) + 1;
                while (randomVertex == i || existe_arista(i, randomVertex)) {
                    randomVertex = random.nextInt(this.num_vertices()) + 1;
                }

                // Obtenemos los modelos de los habitaciones correspondientes a los vértices
                Habitacion modelFrom = verticeModelo.get(i);
                Habitacion modelTo = verticeModelo.get(randomVertex);

                // Calculamos un peso aleatorio (o puedes usar el método calcularDistancia si ya
                // tienes coordenadas)
                float weight = (float) calcularDistancia(modelFrom, modelTo); // Peso aleatorio entre 0 y 100
                insertar_arista(i, randomVertex, weight); // Agregar la arista
                connectionsCount++;
            }
        }

        // Después de añadir las conexiones aleatorias, guardamos el grafo
        saveGrafoDirigido(filename);
    }

    // Método para agregar un vértice con su modelo asociado
    public void addVerticeModelo(Integer vertexId, Habitacion model) {
        verticeModelo.put(vertexId, model); // Asociar el vértice con su modelo
    }

    public boolean existsFile(String filename) {
        File file = new File(archivo + filename);
        return file.exists();
    }

    public static double calcularDistancia(Habitacion habitacion1, Habitacion habitacion2) {
        double distanciaX = habitacion2.getX() - habitacion1.getX();
        double distanciaY = habitacion2.getY() - habitacion1.getY();
        double distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
        return Math.round(distancia * 100.0) / 100.0;
    }

    public JsonArray obtainWeights() throws Exception {
        JsonArray result = new JsonArray();

        // Iterar sobre todos los vértices del grafo
        for (int i = 1; i <= this.num_vertices(); i++) {
            JsonObject verticeInfo = new JsonObject();
            Habitacion modelo = verticeModelo.get(i);
            if (modelo != null) {
                verticeInfo.addProperty("nombre", modelo.getTipo().getName());
            }
            verticeInfo.addProperty("labelId", this.getVertice(i)); // ID del vértice actual

            JsonArray destinations = new JsonArray(); // Lista de conexiones para el vértice
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);

            for (int j = 0; j < adyacencias.getSize(); j++) { // Corregido el rango del bucle
                Adyacencia adj = adyacencias.get(j);

                JsonObject destinationInfo = new JsonObject();
                destinationInfo.addProperty("from", this.getVertice(i)); // Desde el vértice actual
                destinationInfo.addProperty("to", adj.getDestino()); // Al destino
                destinationInfo.addProperty("weight", adj.getPeso()); // Peso de la arista
                destinations.add(destinationInfo);

            }

            verticeInfo.add("destinations", destinations); // Agregar las conexiones al vértice
            result.add(verticeInfo); // Agregar la información del vértice al resultado
        }

        return result;
    }

    public void guardarGrafo() {
        try {
            // Asigna el nombre del archivo como "grafo.json"
            String filename = "grafo.json"; // El nombre que deseas para el archivo
            saveGrafoDirigido(filename); // Llamas a tu método con el nombre del archivo
        } catch (Exception e) {
            e.printStackTrace(); // En caso de error, imprime la traza del error
        }
    }

    public JsonObject getVisGraphData() throws Exception {
        JsonObject visGraph = new JsonObject();

        // Arrays para los nodos y las aristas
        JsonArray nodes = new JsonArray();
        JsonArray edges = new JsonArray();

        // Iteramos sobre los vértices
        for (int i = 1; i <= this.num_vertices(); i++) {
            JsonObject node = new JsonObject();
            Habitacion model = verticeModelo.get(i);
            if (model != null) {
                node.addProperty("name", model.getTipo().name()); // Nombre del vértice
            }
            node.addProperty("id", i); // ID del nodo
            node.addProperty("label", "V" + i); // Etiqueta del nodo (puedes personalizarlo)

            // Opcional: Agregar un color o más propiedades si lo deseas
            node.addProperty("color", "#ff0000"); // Un color de ejemplo, puedes personalizarlo
            nodes.add(node);

            // Obtener las adyacencias de este vértice
            LinkedList<Adyacencia> adyacencias = this.adyacencias(i);
            if (!adyacencias.isEmpty()) {
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adyacencia adj = adyacencias.get(j);
                    JsonObject edge = new JsonObject();
                    edge.addProperty("from", i); // Nodo origen
                    edge.addProperty("to", adj.getDestino()); // Nodo destino
                    edge.addProperty("weight", adj.getPeso());

                    // Peso de la arista

                    // Opcional: Puedes agregar más propiedades a la arista si lo deseas
                    edge.addProperty("color", "#7CFC00"); // Color de la arista (personalizable)
                    edges.add(edge);
                }
            }
        }

        // Añadir nodos y aristas al objeto principal
        visGraph.add("nodes", nodes);
        visGraph.add("edges", edges);

        return visGraph;
    }

}
