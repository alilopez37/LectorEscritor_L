package main.Model;


import java.util.Observable;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Lector extends Observable implements Runnable {
    private static Semaphore Mutex = new Semaphore(1);
    private Semaphore acceso_esc;
    private static int numeroLectores = 0;
    private BaseDatos baseDatos;
    private Random random;

    public Lector(Semaphore acceso_esc, BaseDatos baseDatos){
        this.baseDatos = baseDatos;
        this.acceso_esc = acceso_esc;
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
                acceso_esc.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Mutex.release();
        baseDatos.read();
        try {
            Thread.sleep(random.nextInt(2000) + 8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers(Thread.currentThread().getName());

        System.out.println("L:");
        try {
            Mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numeroLectores--;
        if (numeroLectores == 0)
            acceso_esc.release();
        Mutex.release();
    }
}
