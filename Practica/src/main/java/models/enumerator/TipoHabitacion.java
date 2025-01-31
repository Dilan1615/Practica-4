package models.enumerator;

public enum TipoHabitacion {
     SALA("SALA"),
    COCINA("COCINA"),
    BANIO("BANIO"),
    DORMITORIO("DORMITORIO"),
    COMEDOR("COMEDOR"),
    SALON("SALON"),
    ESTUDIO("ESTUDIO"),
    LAVANDERIA("LAVANDERIA"),
    PASILLO("PASILLO"),
    JARDIN("JARDIN"),
    BIBLIOTECA("BIBLIOTECA"),
    GARAJE("GARAJE"),
    ALACENA("ALACENA"),
    BODEGA("BODEGA"),
    TERRAZA("TERRAZA"),
    BALCON("BALCON");

    private String name;
    TipoHabitacion(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }
}
