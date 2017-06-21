package graphic.Gui.Items;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lorenzo.Applicazione;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

/**
 * Created by pietro on 18/06/17.
 */
public class ClassificaGraphic {

    Label[] classificaLabel;
    private ImageView sfondo;
    /**
     * Construisci e mostra al centro del pannello la classifica finale
     * @param pannello pannello sul quale viene mostrato il risultato
     * @param risultati mappa con GiocatoreGraphic e pVittoria in ordine di classifica
     */
    public ClassificaGraphic(LinkedHashMap<GiocatoreGraphic, Integer> risultati, Pane pannello){

        AnchorPane parent = null;
        classificaLabel = new Label[4];
        //Carica l'FXML
        try {
            FXMLLoader fxmlLoader= new FXMLLoader();
            fxmlLoader.setLocation(Paths.get(Applicazione.fxmlPath + "classifica_finale.fxml").toUri().toURL());
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Recupero variabili
        classificaLabel[0] = (Label) parent.lookup("#primo");
        classificaLabel[1] = (Label) parent.lookup("#secondo");
        classificaLabel[2] = (Label) parent.lookup("#terzo");
        classificaLabel[3] = (Label) parent.lookup("#quarto");
        sfondo = (ImageView) parent.lookup("#immagineSfondo");



        //Popolazione classifica
        GiocatoreGraphic[] giocatori = new GiocatoreGraphic[risultati.keySet().size()];
        int[] pVittoria = new int[risultati.keySet().size()];
        int i = 0;
        for (GiocatoreGraphic giocatore : risultati.keySet()) {
            giocatori[i] = giocatore;
            pVittoria[i] = risultati.get(giocatore);
            i= i+1;
        }
        //Sort
        for (i = 0; i < giocatori.length-1; i++) {
            for (int j = i+1; j < giocatori.length; j++) {
                if(pVittoria[i]< pVittoria[j]){
                    int temp = pVittoria[i];
                    GiocatoreGraphic tempG = giocatori[i];
                    pVittoria[i]=pVittoria[j];
                    giocatori[i]=giocatori[j];
                    pVittoria[j]=temp;
                    giocatori[j]=tempG;
                }
            }
        }
        //Printa
        for (i=0; i < giocatori.length; i++) {
            classificaLabel[i].setText((i+1)+ " - "+giocatori[i].getNome()+" - "+pVittoria[i]);
            classificaLabel[i].setTextFill(giocatori[i].getColoreGiocatore().getColore());
        }

        //Trasla il pannello
        parent.setLayoutX((pannello.getWidth()-parent.getPrefWidth()) /2);
        parent.setLayoutY((pannello.getHeight()-parent.getPrefHeight()) /2);

        //Setta sfondo
        sfondo.setImage(new Image("/classificaFinale.png"));

        //Mostra
        pannello.getChildren().add(parent);


    }
}
