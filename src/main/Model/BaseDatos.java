package main.Model;

import java.util.ArrayList;
import java.util.Random;

public class BaseDatos {
    private ArrayList<String> buffer;
    private Random random;

    public BaseDatos(){
        buffer = new ArrayList<>();
        random = new Random(System.currentTimeMillis());
    }

    public void insert(){
        buffer.add(Thread.currentThread().getName());
    }

    public String read(){
       return buffer.get(random.nextInt(buffer.size()));
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}
