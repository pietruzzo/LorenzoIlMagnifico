package graphic.Gui.Items;

import Domain.TipoCarta;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.io.File.separator;

/**
 * Created by pietro on 02/06/17.
 */

public class CarteGioco{

   private final static String percorsoFileDescrizione = System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"Carte"+separator;
   private final static int cartaW=50;
   private final static int cartaH=100;
   private final static int cartaScomW=30;
   private final static int cartaScomH=600;

   private List<CartaGraphic> carte = new ArrayList<>();

   public CarteGioco(){
      //TODO carica le carte da file, aggiungile alla lista
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
   private Image getImage(String nome, TipoCarta tipo){
      if(tipo != TipoCarta.Scomunica){
         return new Image(nome, cartaW, cartaH, false, true);
      }
      return  new Image(nome, cartaScomW, cartaScomH, false, true);


   }
}


 class CartaGraphic extends ImageView {

    private TipoCarta tipoCarta;
    private String nome;

    public CartaGraphic (String nome, TipoCarta tipoCarta, Image immagineCarta){
       super(immagineCarta);
       this.tipoCarta= tipoCarta;
       this.nome=nome;
    }

    public TipoCarta getTipoCarta() {
       return tipoCarta;
    }

    public String getNome() {
       return nome;
    }
 }
