package part1;

/**
 * Clase que gestiona la creación, pausa y reanudación de hilos que buscan números primos.
 * Extiende la clase Thread para ejecutar su lógica en un hilo separado.
 */
public class Control extends Thread {

    private final static int NTHREADS = 3; // # de hilos que se crean
    private final static int MAXVALUE = 30000000; //valor max que se busca el # primo
    private final static int TMILISECONDS = 5000; //intervalo donde pausaran los hilos
    private final int NDATA = MAXVALUE / NTHREADS; //rango de # que cada hilo procesa
    private PrimeFinderThread pft[]; //arrglo que busca #s
    private final Object lock = new Object(); //objeto para poder sincronizar


    /**
     * Constructor privado de la clase Control.
     * Inicializa el arreglo de hilos PrimeFinderThread y asigna a cada hilo un rango de números.
     */
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, lock);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, lock);
    }

    /**
     * Método estático que crea y retorna una instancia de la clase Control.
     *
     * @return Una nueva instancia de Control.
     */
    public static Control newControl() {
        return new Control();
    }


    /**
     * Método principal que se ejecuta cuando se inicia el hilo de Control.
     * Gestiona la creación, pausa y reanudación de los hilos PrimeFinderThread.
     */
    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        while (true) {
            try {
                Thread.sleep(TMILISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock) {
                System.out.println("Pausando todos los hilos...");
                // pausa todos los hilos
                for (PrimeFinderThread thread : pft){
                    thread.pauseThread();
                }

                int totalPrimes = 0;
                // Mostrar primos encontrados
                for(PrimeFinderThread thread : pft){
                    totalPrimes += thread.getPrimes().size();
                }
                System.out.println("Numero de Primos encontrados por el momento: " + totalPrimes);

                // Verificar si todos los hilos han terminado
                boolean allThreadsFinished = true;
                for (PrimeFinderThread thread : pft) {
                    if (thread.isAlive()) {
                        allThreadsFinished = false;
                        break;
                    }
                }
                // Si todos los hilos han terminado, finaliza el programa.
                if (allThreadsFinished) {
                    System.out.println("Todos los hilos han terminado. Finalizando el programa.");
                    break;
                }

                // Esperar que el usuario presione ENTER
                System.out.println("Presione ENTER para continuar!");

                try{
                    System.in.read();
                }catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println("Reanudando todos los hilos...");

                // Reanudar todos los hilos
                for (PrimeFinderThread thread : pft) {
                    thread.resumeThread();
                }

            }
        }

    }

}
