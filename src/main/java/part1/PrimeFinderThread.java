package part1;


import java.util.LinkedList;
import java.util.List;

/**
 * Clase que extiende Thread para buscar números primos en un rango específico.
 * Implementa la lógica para pausar y reanudar la ejecución del hilo.
 */
public class PrimeFinderThread extends Thread{

    int a,b; // rango de # en los que el hilo buscara primos
    private List<Integer> primes;// lista que almacena # encontrados
    private final Object lock; // objeto para sincronizar la pausa y reanudacion del hilo
    private boolean paused = false; // indica si el hilo esta en pausa


    /**
     * Constructor de la clase PrimeFinderThread.
     * @param a    Inicio del rango de números.
     * @param b    Fin del rango de números.
     * @param lock Objeto utilizado para sincronizar la pausa y reanudación.
     */
    public PrimeFinderThread(int a, int b, Object lock) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
        this.lock = lock;
    }

    /**
     * Método principal que se ejecuta cuando se inicia el hilo.
     * Busca números primos en el rango [a, b) y gestiona la pausa y reanudación.
     */
    @Override
    public void run(){
        for (int i= a;i < b;i++){
            synchronized (lock){
                while(paused){
                    try{
                        lock.wait();//hilo en espera
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
            }
            // si el # es primo, lo agrega a la lista y lo imprime.
            if (isPrime(i)){
                primes.add(i);
                System.out.println(i);
            }
        }
    }

    /**
     * Verifica si un número es primo.
     *
     * @param n Número a verificar.
     * @return true si el número es primo, false en caso contrario.
     */
    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n%2 != 0;
            for(int i = 3;ans && i*i <= n; i+=2 ) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    /**
     * Retorna la lista de números primos encontrados.
     * @return Lista de números primos.
     */
    public List<Integer> getPrimes() {
        return primes;
    }


    /**
     * Pausa la ejecución del hilo.
     */
    public void pauseThread(){
        synchronized (lock){
           paused = true;
        }
    }

    /**
     * Reanuda la ejecución del hilo.
     */
    public void resumeThread(){
        synchronized (lock){
            paused = false;
            lock.notifyAll(); // notifica que salga de wait a los hilos
        }
    }

}
