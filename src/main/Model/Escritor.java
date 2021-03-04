package main.Model;

import java.util.Observable;
import java.util.concurrent.Semaphore;

public class Escritor extends Observable implements Runnable {
    private Semaphore semEscritor;
    private BaseDatos baseDatos;
    private final int WRITING = 0;
    private final int OUT = 1;

    public Escritor(Semaphore semEscritor, BaseDatos baseDatos){
        this.semEscritor = semEscritor;
        this.baseDatos = baseDatos;
    }

    @Override
    public void run() {
        try {
            semEscritor.acquire();
            //Notificación al controller de que ha llegado a escribir
            setChanged();
            notifyObservers(WRITING);
            baseDatos.insert();
            //Notificación al controller de que ha salido del buffer
            setChanged();
            notifyObservers(OUT);
        } catch (InterruptedException e) { }
        semEscritor.release();
    }
}
