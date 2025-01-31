package com.pratica.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controller.dao.services.HabitacionServices;
import controller.tda.graph.GrafoEtiquetadoNoDirecto;
import controller.tda.list.LinkedList;
import models.Habitacion;
import controller.dao.HabitacionDao;

@Path("/habitaciones")
public class HabitacionApi {

    @Path("/lista")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHabitaciones() {
        HashMap<String, Object> map = new HashMap<>();
        HabitacionServices hs = new HabitacionServices();
        map.put("msg", "Lista de habitaciones");
        map.put("data", hs.listAll().toArray());
        if (hs.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }
        return Response.ok(map).build();
    }

    @Path("/guardar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionServices hs = new HabitacionServices();
            hs.getHabitacion().setTipo(hs.getTipoHabitacion(map.get("Tipo").toString()));
            hs.getHabitacion().setX(Double.parseDouble(map.get("Eje X").toString()));
            hs.getHabitacion().setY(Double.parseDouble(map.get("Eje Y").toString()));
            hs.save();

            res.put("msg", "Ok");
            res.put("data", "habitacion guardada exitosamente");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

        }
    }

    @Path("/lista/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHabitacion(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        HabitacionServices hs = new HabitacionServices();
        try {
            hs.setHabticacion(hs.get(id));
        } catch (Exception e) {

        }
        map.put("msg", "Habitaicon");
        map.put("data", hs.getHabitacion());
        if (hs.getHabitacion().getId() == null) {
            map.put("data", "no exiten datos");
            return Response.status(Response.Status.NOT_FOUND).entity(map).build();
        }
        return Response.ok(map).build();
    }

    @Path("/actualizar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionServices hs = new HabitacionServices();
            hs.setHabticacion(hs.get(Integer.parseInt(map.get("id").toString())));
            hs.getHabitacion().setTipo(hs.getTipoHabitacion(map.get(("Tipo")).toString()));
            hs.getHabitacion().setX(Double.parseDouble(map.get(("Eje X")).toString()));
            hs.getHabitacion().setY(Double.parseDouble(map.get(("Eje Y")).toString()));
            hs.update();
            res.put("msg", "Ok");
            res.put("data", "Habitacion actualizado exitosamente");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/eliminar/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionServices hs = new HabitacionServices();
            hs.setHabticacion(hs.get(id));
            hs.delete(id);
            res.put("msg", "Ok");
            res.put("data", "Eliminado exitoso");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/caminoCorto/{origen}/{destino}/{algoritmo}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularCaminoCorto(@PathParam("origen") int origen,
            @PathParam("destino") int destino,
            @PathParam("algoritmo") int algoritmo) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionDao hd = new HabitacionDao();
            JsonArray grafo = hd.obtenerPeso();
            String resultado = hd.caminoCorto(origen, destino, algoritmo);

            res.put("msg", "Camino corto calculado");
            res.put("data", resultado);

            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/unionAleatoria")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response unionesgrafos() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionDao hd = new HabitacionDao();
            // Obtener el grafo antes de la modificación
            GrafoEtiquetadoNoDirecto<String> grafo = hd.obtenerGrafo();
            //System.out.println(grafo.toString());
            // Guardar cambios en el grafo
            hd.guardarGrafo();
            res.put("msg", "Grafo actualizado exitosamente");
            res.put("data", grafo.toString());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/mapadegrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletegrafoData() {
        try {
            HabitacionDao hd = new HabitacionDao();
            JsonObject grafo = hd.dataGrafo();               
            return Response.ok(grafo.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {            
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/mostrarGrafos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getgrafo() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionDao hd = new HabitacionDao();
            JsonArray grafo = hd.obtenerPeso();
            res.put("msg", "Grafo obtenido exitosamente");
            return Response.ok(grafo.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/listaGrafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response grafoVerAdmin() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            HabitacionDao hd = new HabitacionDao();
            LinkedList<Habitacion> listaHabitaciones = hd.getListAll();
            hd.crearGrafo();
            hd.guardarGrafo();
            res.put("msg", "Grafo generado exitosamente");
            res.put("lista", listaHabitaciones.toArray());
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    @Path("/bfs/{origen}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response bfs(@PathParam("origen") int origen) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        HabitacionDao hd = new HabitacionDao();
        JsonArray grafo = hd.obtenerPeso();
        String respuesta = hd.bfs(origen);


        try {
        res.put("respuesta", respuesta);
        return Response.ok(res).build();          
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
}
