package socket;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Domain.TipoAzione;
import lorenzo.MainGame;
import server.socket.ProtocolEvents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Portatile on 17/05/2017.
 */
public class SocketClientProtocol {

    //region Proprieta
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    private final HashMap<Object, ResponseHandler> listaEventHandler;

    private final MainGame mainGame;
    //endregion

    /**
     * Costruttore
     */
    public SocketClientProtocol(ObjectInputStream inputStream, ObjectOutputStream outputStream, MainGame mainGame)
    {
        this.mainGame = mainGame;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.listaEventHandler = new HashMap<>();
        this.PopolaListaEventHandler();
    }

    /**
     * Specifica tutti i possibili eventi ricevibili dal server e ad ognuno associa un metodo per gestirlo
     */
    private void PopolaListaEventHandler()
    {
        this.listaEventHandler.put(ProtocolEvents.TIRATA_ECCEZIONE, this::HandleException);
        this.listaEventHandler.put(ProtocolEvents.PARTITA_INIZIATA, this::PartitaIniziata);
        this.listaEventHandler.put(ProtocolEvents.INIZIO_TURNO, this::IniziaTurno);
        this.listaEventHandler.put(ProtocolEvents.INIZIO_MOSSA, this::IniziaMossa);
        this.listaEventHandler.put(ProtocolEvents.GIOCATORI_SCOMUNICATI, this::ComunicaScomunica);
        this.listaEventHandler.put(ProtocolEvents.SOSTEGNO_CHIESA, this::SceltaSostegnoChiesa);
        this.listaEventHandler.put(ProtocolEvents.AGGIORNA_GIOCATORE, this::AggiornaGiocatore);
        this.listaEventHandler.put(ProtocolEvents.SCEGLI_PRIVILEGIO, this::SceltaPrivilegioConsiglio);
        this.listaEventHandler.put(ProtocolEvents.AZIONE_BONUS, this::EffettuaAzioneBonus);
        this.listaEventHandler.put(ProtocolEvents.FINE_PARTITA, this::FinePartita);
    }

    //region Handler Eventi del server

    /**
     * Gestisce l'arrivo di una eccezione, mostra a video il messaggio
     */
    private void HandleException()
    {
        try{
            String message = (String) inputStream.readObject();
            System.out.println(String.format("E' arrivato l'errore '%s'", message));
            mainGame.MostraEccezione(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'inizio di una partita
     */
    private void PartitaIniziata()
    {
        try {
            Tabellone tabellone = (Tabellone)this.inputStream.readObject();
            mainGame.PartitaIniziata(tabellone);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di inizio turno
     */
    private void IniziaTurno()
    {
        try {
            int[] ordineGiocatori = (int[]) this.inputStream.readObject();
            int[] esitoDadi = (int[]) this.inputStream.readObject();
            HashMap<Integer, String> mappaCarte = (HashMap<Integer, String>) this.inputStream.readObject();

            mainGame.IniziaTurno(ordineGiocatori, esitoDadi, mappaCarte);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gestisce l'evento di inizio mossa
     */
    private void IniziaMossa()
    {
        try {
            int idGiocatore = (int) this.inputStream.readObject();
            mainGame.IniziaMossa(idGiocatore);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gestisce l'evento di scomunica di giocatori
     */
    private void ComunicaScomunica()
    {
        try {
            int[] idGiocatoriScomunicati = (int[]) this.inputStream.readObject();
            int periodo = (int) this.inputStream.readObject();
            mainGame.ComunicaScomunica(idGiocatoriScomunicati, periodo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di scelta di sostegno alla chiesa
     */
    public void SceltaSostegnoChiesa()
    {
        mainGame.SceltaSostegnoChiesa();
    }


    /**
     * Gestisce l'evento di aggiornamento di un giocatore
     */
    private void AggiornaGiocatore()
    {
        try {
            UpdateGiocatoreDTO update = (UpdateGiocatoreDTO) this.inputStream.readObject();
            mainGame.AggiornaGiocatore(update);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di fine Partita
     */
    private void SceltaPrivilegioConsiglio()
    {
        try {
            int numPergamene = (int)this.inputStream.readObject();
            mainGame.SceltaPrivilegioConsiglio(numPergamene);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di fine Partita
     */
    private void EffettuaAzioneBonus()
    {
        try {
            TipoAzione tipoAzione = (TipoAzione) this.inputStream.readObject();
            int valoreAzione = (int)this.inputStream.readObject();
            Risorsa bonusRisorse = (Risorsa) this.inputStream.readObject();
            mainGame.EffettuaAzioneBonus(tipoAzione, valoreAzione, bonusRisorse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di fine Partita
     */
    private void FinePartita()
    {
        try {
            LinkedHashMap<Short, Integer> mappaRisultati= (LinkedHashMap<Short, Integer>) this.inputStream.readObject();
            mainGame.FinePartita(mappaRisultati);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //endregion

    //region Messaggi dal client al server
    /**
     * Comunica al server le informazioni per aggiungere il GiocatoreGraphic alla partita
     */
    public void Login(String nome) throws Exception {
        int codiceRisposta = ProtocolEvents.OK;
        try {
            outputStream.writeObject(ProtocolEvents.LOGIN);
            outputStream.writeObject(nome);

            outputStream.flush();
            codiceRisposta = (int)inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Se era già presente un utente con l'username specificato
        if(codiceRisposta == ProtocolEvents.USER_ESISTENTE)
            throw new Exception("Esiste già un utente con questo username");
    }

    /**
     * La partita inizia automaticamente se è stato raggiunto il numero massimo di giocatori
     */
    public void VerificaInizioAutomatico()
    {
        try {
            outputStream.writeObject(ProtocolEvents.INIZIO_AUTOMATICO);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comunica al server di iniziare la partita
     */
    public void IniziaPartita()
    {
        try {
            outputStream.writeObject(ProtocolEvents.INIZIA_PARTITA);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comunica al server la risposta al sostegno della chiesa
     * @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    public void RispostaSostegnoChiesa(Boolean risposta)
    {
        try {
            outputStream.writeObject(ProtocolEvents.RISPOSTA_SOSTEGNO_CHIESA);
            outputStream.writeObject(risposta);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comunica al server l'intenzione di piazzare un familiare nello spazio azione associato
     * @param piazzaFamiliareDTO parametri relativi al piazzamento del familiare
     */
    public void PiazzaFamiliare(PiazzaFamiliareDTO piazzaFamiliareDTO)   {
        try {
            outputStream.writeObject(ProtocolEvents.PIAZZA_FAMILIARE);
            outputStream.writeObject(piazzaFamiliareDTO);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Comunica al server l'intenzione di effettuare un'azione bonus
     * @param azioneBonusDTO parametri relativi all'azione bonus
     */
    public void AzioneBonusEffettuata(AzioneBonusDTO azioneBonusDTO)   {
        try {
            outputStream.writeObject(ProtocolEvents.AZIONE_BONUS_EFFETTUATA);
            outputStream.writeObject(azioneBonusDTO);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manda al server la scelta del privilegio del consiglio
     * @param risorsa risorse da aggiungere al giocatore
     */
    public void RiscuotiPrivilegiDelConsiglio(Risorsa risorsa){
        try {
            outputStream.writeObject(ProtocolEvents.RISCUOTI_PRIVILEGIO);
            outputStream.writeObject(risorsa);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    /**
     * Dato uno specifico evento fa partire il metodo destinato a gestirlo
     * @param object evento lanciato lato server
     */
    public void HandleResponse(Object object)
    {
        ResponseHandler handler = listaEventHandler.get(object);
        if(handler != null)
            handler.Handle();
    }

    @FunctionalInterface
    private interface ResponseHandler{
        //Tutte le volte che il server scrive un messaggio al client
        //viene invocato questo metodo, a seconda del tipo di richiesta fatta
        //verrà implementato un handler diverso, seguenda la listaEventHandler
        void Handle();
    }
}
