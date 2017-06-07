package graphic.Gui.Items;

import Domain.TipoCarta;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.io.File.separator;

/**
 * Created by pietro on 02/06/17.
 */

public class CarteGioco{

   private final static String percorsoFileDescrizione = "file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator+"Carte"+separator;
   private final static int cartaW=90;
   private final static int cartaH=141;
   private final static int CARTAWINGRANDITA=350;
   private final static int CARTAHINGRANDITA=538;

   private final static int cartaScomW=30;
   private final static int cartaScomH=600;

   private List<CartaGraphic> carte = new ArrayList<>();

   public CarteGioco(){
      //TODO carica le carte da file, aggiungile alla lista
      //per le carte sviluppo, carica anche l aversione ingrandita
   }

   /**
    * @param nome
    * @return la carta corrispondente al nome richiesta
    * @throws NoSuchElementException se non trova la carta
    */
   public CartaGraphic getCarta(String nome) throws NoSuchElementException{
      for (CartaGraphic carta: carte
           ) {
         if (carta.getNome().equals(nome)) return carta;
      }
      throw new NoSuchElementException("Carta: "+nome+" non trovata");
   }


   /**
    * @param nome
    * @param tipo
    * @return da Risorse l'immagina della carta richiesta della dimensione data dal tipo di carta
    */
   private Image getImage(String nome, TipoCarta tipo, int dimX, int dimY){

      String percorsoCompleto;
      if(tipo != TipoCarta.Scomunica){
         percorsoCompleto = "file:"+percorsoFileDescrizione+"carteSviluppo"+separator+nome+".jpg";
      } else {
         percorsoCompleto = "file:"+percorsoFileDescrizione+"carteScomunica"+separator+nome+".png";
      }
      return new Image(percorsoCompleto, dimX, dimY, false, true);
   }
}



