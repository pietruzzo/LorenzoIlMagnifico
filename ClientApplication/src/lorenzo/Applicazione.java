package lorenzo;

import graphic.Gui.ControllerCallBack;
import graphic.Gui.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by casa on 26/05/17.
 */
public class Applicazione extends Application {

    public static final String fxmlPath = System.getProperty("user.dir") + File.separator +"ClientApplication" + File.separator+ "src" +File.separator + "graphic" +File.separator +"Gui" +File.separator+ "fxml"+File.separator;
    /**
     * Finestra dell'interfaccia grafica
     */
    private Stage finestra;

    /**
     * Logica applicativa
     */
    MainGame mainGame;


    @Override
    public void start(Stage stage) throws Exception {
        Applicazione applicazioneCorrente = this;
        mainGame = new MainGame(applicazioneCorrente);
        finestra=stage;
        stage.show();
        startLogin();
    }


    /**
     * Avvia il login
     */
    public void startLogin() {

            try {
                Parent login = null;
                login= getFXML("login_scene.fxml");
                finestra.setTitle("Login");
                finestra.getIcons().add(new Image("/LorenzoIcon.png"));
                finestra.setScene(new Scene(login));
                finestra.setResizable(false);
                finestra.sizeToScene();
            } catch (IOException e) {
                System.err.println("login_scene.fxml non trovato, prova a controllare il PATH");
                stopGUI();
            }

    }

    /**
     * Avvia il campo da gioco
     * @return controller del Campo di gioco
     */
    public GameController startGame(){

        GameController gameController = null;
        Scene gioco;
        Parent campo = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            campo = fxmlLoader.load(new FileInputStream(fxmlPath + "campo_gioco_scene.fxml"));

            Object controllerFromFXML = fxmlLoader.getController();
            gameController = (GameController) controllerFromFXML;
            gameController.setArgApplicationGui(this.mainGame);
            gioco = new Scene(campo);
            finestra.setTitle("Lorenzo il Magnifico");
            finestra.setScene(gioco);
            finestra.setFullScreen(true);
            finestra.setOnCloseRequest(windowEvent -> {
                chiudiApplicazione();
            });
        } catch (IOException e){
            System.err.println("campo_gioco_scene.fxml non trovato, prova a controllare il PATH");
            stopGUI();
        }
        return gameController;
    }

    /**
     * Termina esecuzione della shell grafica
     */
    public void stopGUI(){
        finestra.close();
        finestra=null;
    }


    /**
     * crea la Scene dal file fxml indicato e passa l'applicationGUI al controller
     * @param nomeFile nome del file fxml
     * @return la scena generata dal file
     * @throws IOException
     */
    public Parent getFXML(String nomeFile) throws IOException{
        ControllerCallBack controller;
        Parent parent;
        FXMLLoader fxmlLoader = new FXMLLoader();
        parent = fxmlLoader.load(new FileInputStream(fxmlPath + nomeFile));

        Object controllerFromFXML = fxmlLoader.getController();

        if (controllerFromFXML instanceof ControllerCallBack){
            controller = (ControllerCallBack) controllerFromFXML;
            controller.setArgApplicationGui(this.mainGame);

        }
        return parent;
    }

    public MainGame getMainGame(){
        return mainGame;
    }

    public Stage getFinestra(){
        return finestra;
    }

    public void chiudiApplicazione(){
        mainGame.NotificaChiusuraClient();
        Platform.exit();
        System.exit(0);
    }
}
