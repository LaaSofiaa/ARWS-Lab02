package part1;

/**
 * Clase principal que contiene el método main, punto de entrada del programa.
 */
public class Main {

    /**
     * Método principal que inicia la ejecución del programa.
     */
    public static void main(String[] args) {

        Control control = Control.newControl();

        control.start();// Inicia el hilo de Control

    }

}
