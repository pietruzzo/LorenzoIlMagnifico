package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.EndGame;
import Domain.Risorsa;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 * Esempio di classe totalmente generica che viene eseguita a fine gioco
 */
public class EffettoEndGame implements EndGame {
    @Override
    public void azioneTerminale(Risorsa risorseGiocatore, List<Carta> listaCarte) {

    }
}
