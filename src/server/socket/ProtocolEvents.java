package server.socket;

/**
 * Created by Portatile on 20/05/2017.
 */
public class ProtocolEvents {

    //Eventi lanciati dal client verso il server
    public static final String LOGIN = "login";
    public static final String INIZIO_AUTOMATICO = "inizioAutomatico";
    public static final String INIZIA_PARTITA = "inziaPartita";
    public static final String RISPOSTA_SOSTEGNO_CHIESA = "rispostaSostegnoChiesa";
    public static final String PIAZZA_FAMILIARE = "piazzaFamiliare";
    public static final String AZIONE_BONUS_EFFETTUATA = "azioneBonusEffettuata";
    public static final String RISCUOTI_PRIVILEGIO = "riscuotiPrivilegio";
    public static final String SCELTA_EFFETTI = "sceltaEffetti";
    public static final String CHIUSURA_CLIENT = "chiusuraClient";

    //Eventi lanciati dal server verso il client
    public static final String TIRATA_ECCEZIONE = "tirataEccezione";
    public static final String PARTITA_INIZIATA = "partitaIniziata";
    public static final String INIZIO_TURNO = "inizioTurno";
    public static final String INIZIO_MOSSA = "inizioMossa";
    public static final String GIOCATORI_SCOMUNICATI = "giocatoriScomunicati";
    public static final String SOSTEGNO_CHIESA = "sostegnoChiesa";
    public static final String AGGIORNA_GIOCATORE = "aggiornaGiocatore";
    public static final String SCEGLI_PRIVILEGIO = "scegliPrivilegio";
    public static final String AZIONE_BONUS = "azioneBonus";
    public static final String FINE_PARTITA = "finePartita";
    public static final String GIOCATORE_DISCONNESSO = "giocatoreDisconnesso";

    //Codici delle risposte date dal server al client (HTTP style)
    public static final int OK = 200;
    public static final int USER_ESISTENTE = 401;

}
