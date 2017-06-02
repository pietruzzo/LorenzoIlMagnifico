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

    //Eventi lanciati dal server verso il client
    public static final String TIRATA_ECCEZIONE = "tirataEccezione";
    public static final String PARTITA_INIZIATA = "partitaIniziata";
    public static final String INIZIO_TURNO = "inizioTurno";
    public static final String INIZIO_MOSSA = "inizioMossa";
    public static final String GIOCATORI_SCOMUNICATI = "giocatoriScomunicati";
    public static final String SOSTEGNO_CHIESA = "sostegnoChiesa";

    //Codici delle risposte date dal server al client (HTTP style)
    public static final int OK = 200;
    public static final int USER_ESISTENTE = 401;

}
