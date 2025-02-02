package part1;

/**
 * pausa y reanudación de los hilos
 */
public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private final Object lock = new Object(); //objeto para poder sincronizar

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

    public static Control newControl() {
        return new Control();
    }

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
                // Detener todos los hilos
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

                if (allThreadsFinished) {
                    System.out.println("Todos los hilos han terminado. Finalizando el programa.");
                    break; // Salir del bucle principal
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
