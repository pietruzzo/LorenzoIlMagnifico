package graphic.Gui.Items;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.ScambiaRisorse;
import Domain.TesseraScomunica;
import Domain.TipoCarta;
import graphic.Gui.ControllerCallback;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.io.File.separator;

/**
 * Created by pietro on 02/06/17.
 */

public class MazzoGraphic {

   private final static String percorsoFileDescrizione = "file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator+"Carte"+separator;
   private final static int cartaW=90;
   private final static int cartaH=141;
   private final static int CARTAWINGRANDITA=350;
   private final static int CARTAHINGRANDITA=538;

   private final static int cartaScomW=65;
   private final static int cartaScomH=112;

   private List<CartaGraphic> carte;

   public MazzoGraphic(List<Carta> carte, List<TesseraScomunica>tessereScomunica, ControllerCallback callback){
      this.carte = new ArrayList<>();
      //per le carte sviluppo, carica anche l aversione ingrandita
      for (Carta c : carte){
          CartaGraphic carta = new CartaGraphic(c.getNome(), c.getTipoCarta(), getImage(c.getNome(), c.getTipoCarta(), cartaW, cartaH), getImage(c.getNome(), c.getTipoCarta(), CARTAWINGRANDITA, CARTAHINGRANDITA), callback);

          //region setta numero scelte effetti
          int numOpzioniCosto=0;
          int numOpzioniImmed=0;
          int numOpzioniPerma=0;
          for (Effetto e : c.getEffettoImmediato()){
              if(e instanceof ScambiaRisorse && ((ScambiaRisorse)e).getNumeroOpzioni()>1){
                  ScambiaRisorse scambio = (ScambiaRisorse) e;
                  if (scambio.isCosto()){
                      numOpzioniCosto=scambio.getNumeroOpzioni();
                  } else {
                      numOpzioniImmed=scambio.getNumeroOpzioni();
                  }
              }
          }
          for (Effetto e : c.getEffettoPermanente()){
              if(e instanceof ScambiaRisorse && ((ScambiaRisorse)e).getNumeroOpzioni()>1){
                  numOpzioniPerma=((ScambiaRisorse)e).getNumeroOpzioni();
              }
          }
          if(numOpzioniCosto>1 || numOpzioniImmed>1 ||numOpzioniPerma>1) carta.setNumeroMaxScelte(numOpzioniCosto, numOpzioniImmed, numOpzioniPerma);
          carta.generaSceltaImmediataeCosto();
          //endregion
          this.carte.add(carta);
      }

      for(TesseraScomunica t : tessereScomunica){
         this.carte.add(new CartaGraphic(t.getNome(), t.getTipoCarta(), getImage(t.getNome(), t.getTipoCarta(), cartaScomW, cartaScomH), getImage(t.getNome(), t.getTipoCarta(), CARTAWINGRANDITA, CARTAHINGRANDITA), callback));
      }
   }

   /**
    * @param nome
    * @return la carta corrispondente al nome richiesta
    * @throws NoSuchElementException se non trova la carta
    */
   public CartaGraphic getCarta(String nome) throws NoSuchElementException{
      for (CartaGraphic carta: this.carte
           ) {
         if (carta.getNome().equals(nome)) return carta;
      }
      throw new NoSuchElementException("Carta: "+nome+" non trovata");
   }

    public List<CartaGraphic> getCarte() {
        return carte;
    }

    /**
    * @param nome
    * @param tipo
    * @return da Risorse l'immagina della carta richiesta della dimensione data dal tipo di carta
    */
   private Image getImage(String nome, TipoCarta tipo, int dimX, int dimY){

      String percorsoCompleto;
      if(tipo != TipoCarta.Scomunica){
          percorsoCompleto = getClass().getResource("/Carte/carteSviluppo").getPath();
          percorsoCompleto = "file:" + percorsoCompleto + separator + nome + ".jpg";
      } else {
         percorsoCompleto = percorsoFileDescrizione+"carteScomunica"+separator+nome+".png";
      }
      return new Image(percorsoCompleto, dimX, dimY, false, true);
   }
}



