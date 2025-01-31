package controller.excepcion;

public class OverFowException extends Exception {
    public  OverFowException (){

    }

    public  OverFowException (String msg){
        super(msg);
    }
}
