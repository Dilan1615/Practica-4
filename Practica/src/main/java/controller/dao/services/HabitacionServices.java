package controller.dao.services;

import controller.dao.HabitacionDao;
import controller.tda.list.LinkedList;
import models.Habitacion;
import models.enumerator.TipoHabitacion;

public class HabitacionServices {
    private HabitacionDao obj;

    public HabitacionServices(){
        obj = new HabitacionDao();
    }

     // Método para guardar un habitacion
     public Boolean save() throws Exception {
        return obj.save();
    }

    // Método para actualizar un habitacion
    public Boolean update() throws Exception {
        return obj.update();
    }

    // Método para listar todos los habitaciones
    public LinkedList<Habitacion> listAll() {
        return obj.getListAll();
    }

    // Método para obtener un habitacion
    public Habitacion getHabitacion() {
        return obj.getHabitacion();
    }

    // Método para establecer un habitacion
    public void setHabticacion(Habitacion habitacion) {
        obj.setHabticacion(habitacion);
    }

    public TipoHabitacion getTipoHabitacion(String tipo) {
        return obj.getTipoHabitacion(tipo);
    }

    public TipoHabitacion[] getTipos() {
        return obj.getTipos();
    }

    // Método para obtener un habitacion por ID
    public Habitacion get(Integer id) throws Exception {
        return obj.get(id);
    }

    // Método para eliminar un habitacion
    public Boolean delete(Integer id) throws Exception {
        return obj.delete(id);
    }

    // Método para calcular el camino corto entre dos habitaciones usando el algoritmo seleccionado
    public String calcularCaminoCorto(int origen, int destino, int algoritmo) throws Exception {
        return obj.caminoCorto(origen, destino, algoritmo);
    }
}
