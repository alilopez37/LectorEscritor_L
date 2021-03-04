package main.Model;

import java.util.ArrayList;
import java.util.Random;

public class BaseDatos {
    private ArrayList<String> buffer;
    private Random random;

    public BaseDatos(){
        buffer = new ArrayList<>();
        buffer.add("flag");
        random = new Random(System.currentTimeMillis());
    }

    public void insert() {
        //Simulación de tiempo de escritura
        try {
            Thread.sleep(random.nextInt(3000) + 1000);
        } catch (InterruptedException e) { }
        buffer.add(Thread.currentThread().getName());
    }

    public String read() {
        //Simulación de tiempo de lectura
        try {
            Thread.sleep(random.nextInt(1000) + 500);
        } catch (InterruptedException e) { }
       return buffer.get(random.nextInt(buffer.size()));
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}
