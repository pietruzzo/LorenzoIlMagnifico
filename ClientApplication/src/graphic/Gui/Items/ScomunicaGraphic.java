package graphic.Gui.Items;

import graphic.Gui.ControllerCallback;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lorenzo.Applicazione;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by pietro on 19/06/17.
 */
public class ScomunicaGraphic {

    private AnchorPane parent;
    private ControllerCallback controller;

    public ScomunicaGraphic(Pane pannelloPrincipale, ControllerCallback controller){

        this.controller = controller;
        parent = null;

        //load fxml
        try {
            FXMLLoader fxmlLoader= new FXMLLoader();
            fxmlLoader.setLocation(Paths.get(Applicazione.fxmlPath + "scelta_scomunica.fxml").toUri().toURL());
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get oggetti dalla scena
        Button yesButton = (Button) parent.lookup("#siLabel");
        Button noButton = (Button) parent.lookup("#noLabel");
        ImageView sfondo = (ImageView) parent.lookup("#immagine");

        //Set Sfondo
        sfondo.setImage(new Image("/pergamenaScomunica.png"));

        //set Effetti
        yesButton.setOnMouseClicked(mouseEvent -> {
            controller.rispondiScomunica(true);
            pannelloPrincipale.getChildren().remove(parent);
        });
        noButton.setOnMouseClicked(mouseEvent -> {
            controller.rispondiScomunica(false);
            pannelloPrincipale.getChildren().remove(parent);
        });

        //Aggiungi al pannelloPrincipale
        parent.setLayoutX((pannelloPrincipale.getWidth()-parent.getPrefWidth())/2);
        parent.setLayoutY((pannelloPrincipale.getHeight()-parent.getPrefHeight())/2);
        pannelloPrincipale.getChildren().add(parent);
    }
}
