package lorenzo;

import graphic.Gui.Controller;
import graphic.Gui.ControllerCampoGioco;
import graphic.Gui.ControllerLogin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by casa on 26/05/17.
 */
public class Applicazione extends Application {

    private final String fxmlPath = System.getProperty("user.dir") + File.separator +"ClientApplication" + File.separator+ "src" +File.separator + "graphic" +File.separator +"Gui" +File.separator+ "fxml"+File.separator;
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
        //Platform.runLater(() -> mainGame= new MainGame(applicazioneCorrente));
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
                login= getFXML("login_scene.fxml", new ControllerLogin());
                finestra.setTitle("Login");
                finestra.setScene(new Scene(login));
            } catch (IOException e) {
                System.err.println("login_scene.fxml non trovato, prova a controllare il PATH");
                stopGUI();
            }

    }

    /**
     * Avvia il campo da gioco
     * @return controller del Campo di gioco
     */
    public ControllerCampoGioco startGame() throws NullPointerException{

        ControllerCampoGioco controllerCampoGioco = null;
        Scene gioco;
        Parent campo = null;
        try{
            campo= getFXML("campo_gioco_scene.fxml", controllerCampoGioco);
            gioco = new Scene(campo);
            finestra.setTitle("Lorenzo il Magnifico");
            finestra.setScene(gioco);
            finestra.setFullScreen(true);
        } catch (IOException e){
            System.err.println("campo_gioco_scene.fxml non trovato, prova a controllare il PATH");
            stopGUI();
        }
        return controllerCampoGioco;
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
    public Parent getFXML(String nomeFile, Controller controller) throws IOException{
        Parent parent;
        FXMLLoader fxmlLoader = new FXMLLoader();
        parent = fxmlLoader.load(new FileInputStream(fxmlPath + nomeFile));

        Object controllerFromFXML = fxmlLoader.getController();

        if (controllerFromFXML instanceof Controller){
            controller = (Controller) controllerFromFXML;
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
}
