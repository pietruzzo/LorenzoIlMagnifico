package graphic.Gui;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lorenzo.Applicazione;
import lorenzo.MainGame;

/**
 * Created by pietro on 28/05/17.
 */
public class ControllerLogin implements Controller {

    @FXML ChoiceBox communicationChoice;
    @FXML TextField usernameInput;
    @FXML RadioButton radioGui;
    @FXML RadioButton radioCli;
    @FXML ToggleGroup interfaccia;
    @FXML Label messaggioErrore;


    ObservableList<String> communicationChoiceBoxList = FXCollections.observableArrayList("RMI", "Socket");
    private MainGame mainGame;
    private Applicazione applicazione;

    /**
     * Metodo che inizializza alcuni elementi grafici del login_stage
     */
    @FXML private void initialize(){
        communicationChoice.setValue("RMI");
        communicationChoice.setItems(communicationChoiceBoxList);
    }

    @FXML
    private void tryLogin(){
        MainGame.TipoConnessione tipoConnessione;
        MainGame.TipoInterfaccia tipoInterfaccia;
        String nomeUtente= usernameInput.getText();
        String sceltaConnessione = communicationChoice.getSelectionModel().getSelectedItem().toString();
        boolean gui = radioGui.isSelected();


        if (sceltaConnessione.equals("RMI")) tipoConnessione= MainGame.TipoConnessione.RMI;
        else tipoConnessione= MainGame.TipoConnessione.Socket;

        if (gui) tipoInterfaccia = MainGame.TipoInterfaccia.GUI;
        else tipoInterfaccia = MainGame.TipoInterfaccia.CLI;

        if(!communicationChoice.isDisabled()) {
            System.out.println("communicationChoice: " + sceltaConnessione + "\n gui: " + gui);
            mainGame.SetConnessioneServer(tipoConnessione);
            communicationChoice.setDisable(true);
        }

        try {
            mainGame.Login(nomeUtente);
            mainGame.AvviaUI(applicazione, tipoInterfaccia);
            mainGame.VerificaInizioAutomatico();
        } catch (Exception e) {
            messaggioErrore.setText(e.getMessage());
        }

    }

    @Override
    public void setArgApplicationGui(Applicazione applicazione) {
        this.mainGame= applicazione.getMainGame();
        this.applicazione = applicazione;
    }
}