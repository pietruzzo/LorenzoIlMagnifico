package graphic.Gui.Items;

import graphic.Gui.Controller;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lorenzo.Applicazione;

import java.io.IOException;

/**
 * Created by pietro on 15/06/17.
 */
public class OpzioniMossaAlternative{

    private SubScene scena;
    private Button conferma;
    private TextField servi;
    private AnchorPane areaCarta;
    private Pane pannelloContenitore;
    private Button annulla;

    public OpzioniMossaAlternative(Controller callBack, CasellaGraphic casella, FamiliareGraphic familiare, Applicazione applicazione) throws IOException {


        Parent parent;
        if(casella instanceof CasellaConCartaGraphic) {
            parent = applicazione.getFXML("opzioni_mossa_carta.fxml");
            scena = new SubScene(parent, 700, 600);
        }
        else {
            parent= applicazione.getFXML("opzioni_mossa.fxml");
            scena = new SubScene(parent, 350, 250);
        }

        conferma = (Button) parent.lookup("#conferma");
        servi = (TextField) parent.lookup("#numeroServi");
        annulla = (Button) parent.lookup("#annulla");


        if(casella instanceof CasellaConCartaGraphic){
            areaCarta= (AnchorPane) parent.lookup("#spazioCarta");
            Group cartaIm = ((CasellaConCartaGraphic) casella).getCartaAssociata().getIngrandimento();
            cartaIm.setLayoutX(0);
            cartaIm.setLayoutY(0);
            cartaIm.setTranslateX(0);
            cartaIm.setTranslateY(30);
            cartaIm.setVisible(true);
            areaCarta.getChildren().add(cartaIm);
        }

        conferma.setOnMouseClicked(mouseEvent -> {
            try {
                int numeroServi = Integer.parseInt(servi.getText());
                callBack.mandaMossaAlServer(familiare, casella, numeroServi);
                pannelloContenitore.getChildren().remove(scena);
            } catch (Exception e){
                System.out.println("Bad input, retry");
            }
        });

        annulla.setOnMouseClicked(mouseEvent -> pannelloContenitore.getChildren().remove(scena));
    }

    public void setSubScene(Pane pannello) {
        this.pannelloContenitore=pannello;
        scena.setLayoutX(pannello.getWidth() / 2 - scena.getWidth() / 2);
        scena.setLayoutY(pannello.getHeight() / 2 - scena.getHeight() / 2);
        pannello.getChildren().add(scena);
    }
}
