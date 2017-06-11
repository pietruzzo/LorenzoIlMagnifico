package server.socket;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Domain.TipoAzione;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class SocketServerProtocol {

    //region Proprieta
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final GiocatoreSocket giocatore;

    private final HashMap<Object, RequestHandler> listaEventHandler;
    private static final Object WRITE_TO_CLIENT_MUTEX = new Object();
    //endregion

    /**
     * Costruttore del protocollo di comunicazione del socket server
     */
    public SocketServerProtocol(ObjectInputStream input, ObjectOutputStream output, GiocatoreSocket giocatore)
    {
        this.inputStream = input;
        this.outputStream = output;
        this.giocatore = giocatore;
        this.listaEventHandler = new HashMap<>();
        this.PopolaListaEventHandler();
    }
    /**
     * Specifica tutti i possibili eventi ricevibili dal client e ad ognuno associa un metodo per gestirlo
     */
    private void PopolaListaEventHandler()
    {
        this.listaEventHandler.put(ProtocolEvents.LOGIN, this::Login);
        this.listaEventHandler.put(ProtocolEvents.INIZIA_PARTITA, this::IniziaPartita);
        this.listaEventHandler.put(ProtocolEvents.INIZIO_AUTOMATICO, this::VerificaInizioPartita);
        this.listaEventHandler.put(ProtocolEvents.RISPOSTA_SOSTEGNO_CHIESA, this::RispostaSostegnoChiesa);
        this.listaEventHandler.put(ProtocolEvents.PIAZZA_FAMILIARE, this::PiazzaFamiliare);
        this.listaEventHandler.put(ProtocolEvents.AZIONE_BONUS_EFFETTUATA, this::AzioneBonusEffettuata);
        this.listaEventHandler.put(ProtocolEvents.RISCUOTI_PRIVILEGIO, this::RiscuotiPrivilegiDelConsiglio);
        this.listaEventHandler.put(ProtocolEvents.SCELTA_EFFETTI, this::SettaSceltaEffetti);
    }

    //region Handler eventi
    private void Login()
    {
        try {
            String nomeUtente = (String) this.inputStream.readObject();
            this.LoginGiocatore(nomeUtente);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Effettua il login del giocatore e comunica l'esito dell'operazione al client
     */
    private void LoginGiocatore(String nome) throws IOException {
        int codiceRisposta;

        try {
            this.giocatore.Login(nome);
            codiceRisposta = ProtocolEvents.OK;
        } catch (Exception e) {
            codiceRisposta = ProtocolEvents.USER_ESISTENTE;
        }

        outputStream.writeObject(codiceRisposta);
        outputStream.flush();
    }

    /**
     * Se è stato raggiunto il limite massimo di giocatori la partita inizia automaticamente
     */
    private void VerificaInizioPartita()
    {
        this.giocatore.VerificaInizioPartita();
    }

    /**
     * Inizia la partita
     */
    private void IniziaPartita()
    {
        this.giocatore.IniziaPartita();
    }

    /**
     * Gestisce la risposta del client alla domanda sul sostegno della chiesa
     */
    private void RispostaSostegnoChiesa()
    {
        try {
            Boolean risposta = (Boolean)this.inputStream.readObject();
            this.giocatore.RispostaSostegnoChiesa(risposta);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento relativo al tentato piazzamento di un familiare da parte di un client
     */
    private void PiazzaFamiliare()
    {
        try {
            PiazzaFamiliareDTO piazzaFamiliareDTO = (PiazzaFamiliareDTO)this.inputStream.readObject();
            this.giocatore.PiazzaFamiliare(piazzaFamiliareDTO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gestisce l'evento relativo alla tentata azione bonus da parte di un client
     */
    private void AzioneBonusEffettuata()
    {
        try {
            AzioneBonusDTO azioneBonusDTO = (AzioneBonusDTO)this.inputStream.readObject();
            this.giocatore.AzioneBonusEffettuata(azioneBonusDTO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento relativo alla scelta del privilegio del consiglio
     */
    private void RiscuotiPrivilegiDelConsiglio()
    {
        try {
            Risorsa risorsa = (Risorsa) this.inputStream.readObject();
            this.giocatore.RiscuotiPrivilegiDelConsiglio(risorsa);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di scelta dell'effetto di default da attivare per le carte con scambia risorse
     */
    public void SettaSceltaEffetti()
    {
        try {
            String nomeCarta = (String) this.inputStream.readObject();
            Integer sceltaEffetto = (Integer) this.inputStream.readObject();
            this.giocatore.SettaSceltaEffetti(nomeCarta, sceltaEffetto);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //endregion


    //region Messaggi dal Server al Client
    /**
     * Comunica l'eccezione al client
     */
    public void ComunicaEccezione(String exceptionMessage)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try{
                outputStream.writeObject(ProtocolEvents.TIRATA_ECCEZIONE);
                outputStream.writeObject(exceptionMessage);
                outputStream.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica ai client l'avvenuto inizio della partita
     */
    public void PartitaIniziata(Tabellone tabellone)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.PARTITA_INIZIATA);
                this.outputStream.writeObject(tabellone);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Comunica ai client l'inizio di un nuovo turno
     */
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.INIZIO_TURNO);
                this.outputStream.writeObject(ordineGiocatori);
                this.outputStream.writeObject(esitoDadi);
                this.outputStream.writeObject(mappaCarte);

                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica attraverso i socket al client l'inzio di una nuova mossa
     * @param idGiocatore id del giocatore che deve effettuare la mossa
     */
    public void IniziaMossa(int idGiocatore)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.INIZIO_MOSSA);
                this.outputStream.writeObject(idGiocatore);

                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica ai client la scomunica di giocatori
     * @param idGiocatoriScomunicati array degli id dei giocatori scomunicati
     * @param periodo periodo nel quale avviene la scomunica
     */
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.GIOCATORI_SCOMUNICATI);
                this.outputStream.writeObject(idGiocatoriScomunicati);
                this.outputStream.writeObject(periodo);

                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica al client che deve scegliere se sostenere o meno la chiesa
     */
    public void SceltaSostegnoChiesa()
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.SOSTEGNO_CHIESA);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifica a tutti i client l'aggiornamento di un giocatore
     * @param update nuove caratteristiche del giocatore
     */
    public void AggiornaGiocatore(UpdateGiocatoreDTO update) {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.AGGIORNA_GIOCATORE);
                this.outputStream.writeObject(update);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica al client il numero di pergamene da scegliere
     * @param numPergamene numero di pergamene da scegliere
     */
    public void SceltaPrivilegioConsiglio(int numPergamene) {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.SCEGLI_PRIVILEGIO);
                this.outputStream.writeObject(numPergamene);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Comunica al client che può effettuare un'azione bonus
     * @param tipoAzioneBonus tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse){
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.AZIONE_BONUS);
                this.outputStream.writeObject(tipoAzioneBonus);
                this.outputStream.writeObject(valoreAzione);
                this.outputStream.writeObject(bonusRisorse);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica la fine della partita ai client
     * @param mappaRisultati mappa ordinata avente l'id del giocatore come chiave e i suoi punti vittoria come valore
     */
    public void ComunicaFinePartita(HashMap<Short, Integer> mappaRisultati)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.FINE_PARTITA);
                this.outputStream.writeObject(mappaRisultati);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion


    /**
     * Dato uno specifico evento fa partire il metodo destinato a gestirlo
     * @param object evento lanciato lato client
     */
    public void HandleRequest(Object object)
    {
        RequestHandler handler = listaEventHandler.get(object);
        if(handler != null)
            handler.Handle();
    }

    @FunctionalInterface
    private interface RequestHandler{
        //Tutte le volte che il server scrive un messaggio al client
        //viene invocato questo metodo, a seconda del tipo di richiesta fatta
        //verrà implementato un handler diverso, seguenda la listaEventHandler
        void Handle();
    }
}
