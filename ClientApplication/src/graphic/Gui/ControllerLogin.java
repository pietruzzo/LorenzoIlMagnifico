package graphic.Gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Created by pietro on 28/05/17.
 */
public class ControllerLogin{

    @FXML ChoiceBox communicationChoice;
    @FXML TextField usernameInput;
    @FXML RadioButton radioGui;
    @FXML RadioButton radioCli;
    @FXML ToggleGroup interfaccia;
    @FXML Label messaggioErrore;


    ObservableList<String> communicationChoiceBoxList = FXCollections.observableArrayList("RMI", "Socket");


    /**
     * Metodo che inizializza alcuni elementi grafici del login_stage
     */
    @FXML private void initialize(){
        communicationChoice.setValue("RMI");
        communicationChoice.setItems(communicationChoiceBoxList);
    }

    @FXML
    private void tryLogin(){
        String nomeUtente= usernameInput.getText();
        String sceltaConnessione = (String) communicationChoice.getSelectionModel().getSelectedItem();

        if(!nomeUtente.isEmpty()){
            //TODO setup del game
        }
        else messaggioErrore.setText("inserire username");
    }
}