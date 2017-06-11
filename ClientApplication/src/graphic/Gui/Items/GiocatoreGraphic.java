package graphic.Gui.Items;

import Domain.ColoreDado;
import Domain.ColoreGiocatore;
import Domain.Giocatore;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


import static java.io.File.separator;

/**
 * Created by pietro on 02/06/17.
 */

public class GiocatoreGraphic {

    private Giocatore giocatore;
    private FamiliareGraphic[] familiari;

    public GiocatoreGraphic(Giocatore giocatore){
        this.giocatore=giocatore;
        familiari = new FamiliareGraphic[4];

        //Genera i familiari
        familiari[0]=new FamiliareGraphic(giocatore.getColore(), ColoreDado.NERO);
        familiari[1]=new FamiliareGraphic(giocatore.getColore(), ColoreDado.BIANCO);
        familiari[2]=new FamiliareGraphic(giocatore.getColore(), ColoreDado.ARANCIO);
        familiari[3]=new FamiliareGraphic(giocatore.getColore(), ColoreDado.NEUTRO);
    }


    public ColoreGiocatore getColoreGiocatore() {
        return giocatore.getColore();
    }

    public FamiliareGraphic[] getFamiliari() {
        return familiari;
    }

    /**
     * @param coloreDado
     * @return familiare corrispondente al colore, tra i familiari le giocatore
     * @signal NullPointerException familiare non presente
     */
    public FamiliareGraphic getFamiliare(ColoreDado coloreDado){
        for (FamiliareGraphic f : familiari){
            if(f.getColore()==coloreDado) return f;
        }
        throw new NullPointerException("Familiare non trovato");
    }

    public String getNome(){ return giocatore.getNome();}

    public int getIdGiocatore(){ return giocatore.getIdGiocatore();}

}


