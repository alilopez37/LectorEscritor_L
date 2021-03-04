package main.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.Model.Bandera;
import main.Model.BaseDatos;
import main.Model.Escritor;
import main.Model.Lector;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class rootController implements Observer {
    private Bandera bandera = new Bandera();
    private AtomicInteger contadorLector = new AtomicInteger(0);
    private AtomicInteger contadorEscritor = new AtomicInteger(0);
    private AtomicInteger contadorBD = new AtomicInteger(0);
    Timeline timer;
    ArrayList<Circle> arrayLector = new ArrayList<>();
    ArrayList<Circle> arrayEscritor = new ArrayList<>();
    ArrayList<Circle> arrayBD =  new ArrayList<>();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnDetener;

    @FXML
    void btnDetenerOnMouseClicked(MouseEvent event) {
        timer.stop();
    }

    @FXML
    void iniciarOnMouseClicked(MouseEvent event) {

        BaseDatos baseDatos = new BaseDatos();
        Semaphore semEscritor = new Semaphore(1);
        Random random = new Random(System.currentTimeMillis());

        timer = new Timeline(
            new KeyFrame(Duration.millis(random.nextInt(400)+100),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        switch(random.nextInt(2)){
                            case 0:
                                Lector lector = new Lector(semEscritor,baseDatos);
                                lector.addObserver(rootController.this);
                                renderLector();
                                new Thread(lector).start();
                                break;
                            case 1:
                                Escritor escritor = new Escritor(semEscritor,baseDatos);
                                escritor.addObserver(rootController.this);
                                renderEscritor();
                                new Thread(escritor).start();
                        }
                    }
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof Lector){ //Verificación de que es un hilo Lector
            System.out.println("Lector");
            String flag = String.valueOf(arg);
            if (flag.equals("0")){ //Si llegó 0, significa que debo mover un Lector a la BD
                Circle circle = arrayLector.remove(arrayLector.size()-1);
                contadorLector.decrementAndGet();
                arrayBD.add(circle);
                contadorBD.incrementAndGet();
                Platform.runLater( ()-> {
                    circle.setCenterX(430 + ((contadorBD.get() % 4)*30));
                    circle.setCenterY(185 + ((contadorBD.get() / 4)*30));
                });

            } else { //Si llegó 1, significa que remover  un Lector de la BD
                Circle circle = arrayBD.remove(arrayBD.size()-1);
                Platform.runLater(()-> anchorPane.getChildren().remove(circle));
                contadorBD.decrementAndGet();
            }

        } else { //Verificación de que es un hilo Escritor
            String flag = String.valueOf(arg);
            if (flag.equals("0")){ //Si llegó 0, significa que debo mover un Lector a la BD

                Circle circle = arrayEscritor.remove(arrayEscritor.size()-1);
                contadorEscritor.decrementAndGet();
                arrayBD.add(circle);
                contadorBD.incrementAndGet();
                Platform.runLater( ()-> {
                    circle.setCenterX(430 + ((contadorBD.get() % 4)*30));
                    circle.setCenterY(185 + ((contadorBD.get() / 4)*30));
                });
            } else { //Si llegó 1, significa que remover  un Lector de la BD
                Circle circle = arrayBD.remove(arrayBD.size()-1);
                Platform.runLater(()-> anchorPane.getChildren().remove(circle));
                contadorBD.decrementAndGet();
            }
        }
    }

    private void renderLector(){
        Circle circle;
        circle = new Circle(30 + ((contadorLector.get() % 5)*30),90 + ((contadorLector.get() / 5)*30),10, Color.GREEN);
        anchorPane.getChildren().add(circle);
        arrayLector.add(circle);
        contadorLector.incrementAndGet();
    }
    private void renderEscritor(){
        Circle circle;
        circle = new Circle(190 + ((contadorEscritor.get() % 5)*30),90 + ((contadorEscritor.get() / 5)*30),10, Color.RED);
        anchorPane.getChildren().add(circle);
        arrayEscritor.add(circle);
        contadorEscritor.incrementAndGet();
    }
}