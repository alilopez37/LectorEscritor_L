package main.Model;

import java.util.Observable;
import java.util.concurrent.Semaphore;

public class Escritor extends Observable implements Runnable {
    private Semaphore acceso_esc;
    private BaseDatos baseDatos;

    public Escritor(Semaphore acceso_esc, BaseDatos baseDatos){
        this.acceso_esc = acceso_esc;
        this.baseDatos = baseDatos;
    }

    @Override
    public void run() {
        try {
            acceso_esc.acquire();
            setChanged();
            notifyObservers(Thread.currentThread().getName());
            baseDatos.insert();
            System.out.println(baseDatos.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        acceso_esc.release();
    }
}
