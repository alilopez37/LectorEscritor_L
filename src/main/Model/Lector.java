package main.Model;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Lector extends Observable implements Runnable {
    private static Semaphore Mutex = new Semaphore(1);
    private Semaphore semEscritor;
    private static int numeroLectores = 0;
    private final int READING = 0;
    private final int OUT = 1;
    private BaseDatos baseDatos;
    private Random random;

    public Lector(Semaphore semEscritor, BaseDatos baseDatos){
        this.baseDatos = baseDatos;
        this.semEscritor = semEscritor;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {

        try {
            Mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numeroLectores++;
        if (numeroLectores == 1) {
            try {
                semEscritor.acquire();
            } catch (InterruptedException e) { }
        }
        Mutex.release();
        //Notificación al controller que el lector ha llegado al Buffer
        setChanged();
        notifyObservers(String.valueOf(READING));
        baseDatos.read();
        //Notificación del salida del Buffer
        setChanged();
        notifyObservers(String.valueOf(OUT));
        try {
            Mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numeroLectores--;
        if (numeroLectores == 0)
            semEscritor.release();
        Mutex.release();
    }
}
