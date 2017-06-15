package graphic.Gui;


import Domain.Risorsa;
import graphic.Gui.Items.CartaGraphic;
import graphic.Gui.Items.CasellaGraphic;
import graphic.Gui.Items.FamiliareGraphic;
import graphic.Gui.Items.GiocatoreGraphic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
            mainGame.AvviaUI(tipoInterfaccia);
            mainGame.VerificaInizioAutomatico();

        } catch (Exception e) {
            messaggioErrore.setText(e.getMessage());
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void setArgApplicationGui(MainGame mainGame) {
        this.mainGame= mainGame;
    }

    @Override
    public void cartaTabelloneToGiocatore(CartaGraphic carta, GiocatoreGraphic giocatoreGraphic) {

    }

    @Override
    public void riscossionePrivilegio(Risorsa risorse) {

    }

    @Override
    public void exitGame() {

    }

    @Override
    public void giocaAdesso() {

    }

    @Override
    public void selezionaFamiliare(FamiliareGraphic familiare, boolean piazzamentoPossibile) {

    }

    @Override
    public void casellaSelezionata(CasellaGraphic casella) {

    }
}