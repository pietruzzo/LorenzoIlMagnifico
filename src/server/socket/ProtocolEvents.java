package server.socket;

/**
 * Created by Portatile on 20/05/2017.
 */
public class ProtocolEvents {

    //Eventi lanciati dal client verso il server
    public static final String LOGIN = "login";
    public static final String INIZIA_PARTITA = "inziaPartita";

    //Eventi lanciati dal server verso il client
    public static final String TIRATA_ECCEZIONE = "tirataEccezione";
    public static final String PARTITA_INIZIATA = "partitaIniziata";
    public static final String INIZIO_TURNO = "inizioTurno";

    //Codici delle risposte date dal server al client (HTTP style)
    public static final int OK = 200;
    public static final int USER_ESISTENTE = 401;

}
