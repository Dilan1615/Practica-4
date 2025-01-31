package controller.tda.graph;

public class Adyacencia {
    private Integer destino;
    private Float peso;// se coloca float ya que no necesitamos con exactitud

    public Adyacencia(Integer destino, Float peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Integer getDestino() {
        return this.destino;
    }

    public void setDestino(Integer destino) {
        this.destino = destino;
    }

    public Float getPeso() {
        return this.peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public String ToString() {
        return "Adyacencia {" +
                "Destino: " + destino +
                "\n Peso: " + peso + "}";
    }
}