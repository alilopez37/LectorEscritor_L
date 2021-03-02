package main.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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


public class rootController implements Observer {
    private Bandera bandera = new Bandera();
    Timeline timer;
    ArrayList<Circle> arrayLector = new ArrayList<>();
    ArrayList<Circle> arrayEscritor = new ArrayList<>();

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnDetener;

    @FXML
    void btnDetenerOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void iniciarOnMouseClicked(MouseEvent event) {

        BaseDatos baseDatos = new BaseDatos();
        Semaphore semEscritor = new Semaphore(1);
        Random random = new Random(System.currentTimeMillis());

        timer = new Timeline(
            new KeyFrame(Duration.millis(50L),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            switch(random.nextInt(2)){
                                case 0:
                                    Lector lector = new Lector(semEscritor,baseDatos);
                                    lector.addObserver(rootController.this);
                                    new Thread(lector).start();
                                    break;
                                case 1:
                                    Escritor escritor = new Escritor(semEscritor,baseDatos);
                                    escritor.addObserver(rootController.this);
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

    }
}