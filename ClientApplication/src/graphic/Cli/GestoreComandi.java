package graphic.Cli;

import Domain.ColoreDado;
import Domain.Risorsa;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michele on 15/06/2017.
 */
public class GestoreComandi {

    private final ControllerCli cli;
    private Map<String, Comando> comandi;

    public GestoreComandi(ControllerCli cli)
    {
        this.cli = cli;
        comandi = new HashMap<>();
        comandi.put("iniziaPartita", this::iniziaPartita);
    }

    /**
     * Imposta i comandi per la scelta del privilegio
     */
    public void setComandiDefault()
    {
        comandi = new HashMap<>();
        comandi.put("vediRisorse", this::vediRisorse);
        comandi.put("vediCarte", this::vediCarte);
        comandi.put("vediDadi", this::vediDadi);
    }

    /**
     * Imposta i comandi per la scelta del privilegio
     */
    public void addComandiDefault()
    {
        comandi.put("vediRisorse", this::vediRisorse);
        comandi.put("vediCarte", this::vediCarte);
        comandi.put("vediDadi", this::vediDadi);
    }

    /**
     * Imposta i comandi per la scelta del privilegio
     */
    public void setComandiRispostaPrivilegio()
    {
        comandi = new HashMap<>();
        comandi.put("privilegio", this::sceltaPrivilegio);
        this.addComandiDefault();
    }

    /**
     * Imposta i comandi per la scelta del privilegio
     */
    public void setComandiSostegnoChiesa()
    {
        comandi = new HashMap<>();
        comandi.put("sostegno", this::rispostaSostegnoChiesa);
        this.addComandiDefault();
    }

    /**
     * Imposta i comandi per il piazzamento del familiare
     */
    public void setComandiPiazzamentoFamiliare()
    {
        comandi = new HashMap<>();
        comandi.put("piazza", this::piazzaFamiliare);
        this.addComandiDefault();
    }

    /**
     * Imposta i comandi per l'azione bonus
     */
    public void setComandiAzioneBonus()
    {
        comandi = new HashMap<>();
        comandi.put("bonus", this::azioneBonusEffettuata);
        this.addComandiDefault();
    }

    /**
     * Tenta la comunicazione con il server per cominciare la partita
     */
    private void iniziaPartita(String[] params) throws ComandoNonValido {
        if(params.length == 0)
            cli.mainGame.IniziaPartita();
        else
            throw new ComandoNonValido("Numero di parametri errato");
    }

    /**
     * Comunica se sostenere o meno la chiesa
     * @param params
     * @throws ComandoNonValido
     */
    private void rispostaSostegnoChiesa(String[] params) throws ComandoNonValido {
        if(params.length == 1) {
            boolean risposta;

            if (params[0].toLowerCase().equals("si")) {
                risposta = true;
            }
            else {
                if (params[0].toLowerCase().equals("no")) {
                    risposta = false;
                }
                else throw new ComandoNonValido("La risposta deve essere si oppure no!");
            }

            cli.mainGame.RispostaSostegnoChiesa(risposta);

        }
        else
            throw new ComandoNonValido("Numero di parametri errato");
    }

    /**
     * Comunica la scelta del privilegio
     */
    private void sceltaPrivilegio(String[] params) throws ComandoNonValido {
        if(params.length == 1) {
            String privilegioScelto = params[0];
            Risorsa risorsa;
            switch (privilegioScelto) {
                case "1":
                    risorsa = new Risorsa(1, 1, 0, 0, 0, 0, 0);
                    break;
                case "2":
                    risorsa = new Risorsa(Risorsa.TipoRisorsa.SERVI, 2);
                    break;
                case "3":
                    risorsa = new Risorsa(Risorsa.TipoRisorsa.MONETE, 2);
                    break;
                case "4":
                    risorsa = new Risorsa(Risorsa.TipoRisorsa.PMILITARI, 2);
                    break;
                case "5":
                    risorsa = new Risorsa(Risorsa.TipoRisorsa.PFEDE, 1);
                    break;

                default:
                    throw new ComandoNonValido("La scelta deve essere compresa tra 1 e 5!");
            }
            cli.mainGame.RiscuotiPrivilegiDelConsiglio(risorsa);
        }
        else
            throw new ComandoNonValido("Numero di parametri errato");
    }

    /**
     * Comunica il piazzamento del familiare
     */
    private void piazzaFamiliare(String[] params) throws ComandoNonValido {
        if(params.length == 3) {
            ColoreDado colore = getColore(params[0]);
            int idSpazioAzione = getInt(params[1]);
            int servitoriAggiunti = getInt(params[2]);

            if(idSpazioAzione < 1 || idSpazioAzione > 25)
                throw new ComandoNonValido("Lo spazio azione deve essere compreso tra 1 e 25");

            cli.mainGame.PiazzaFamiliare(colore, idSpazioAzione, servitoriAggiunti);
        }
        else
            throw new ComandoNonValido("Numero di parametri errato");
    }

    /**
     * Manda al server la richiesta di validazione e effettuazione dell'azione bonus
     */
    public void azioneBonusEffettuata(String[] params) throws ComandoNonValido
    {
        if(params.length == 2) {
            int idSpazioAzione = getInt(params[0]);
            int servitoriAggiunti = getInt(params[1]);

            if(idSpazioAzione < 1 || idSpazioAzione > 25)
                throw new ComandoNonValido("Lo spazio azione deve essere compreso tra 1 e 25");

            //TODO andarsi a prendere valore e risorsa
            cli.mainGame.AzioneBonusEffettuata(idSpazioAzione, cli.valoreAzioneBonus, cli.risorsaAzioneBonus, servitoriAggiunti);
        }
        else
            throw new ComandoNonValido("Numero di parametri errato");
    }

    /**
     * Permette al giocatore di vedere le proprie risorse
     */
    public void vediRisorse(String[] params)
    {
        cli.vediRisorse();
    }

    /**
     * Permette al giocatore di vedere le proprie carte
     */
    public void vediCarte(String[] params)
    {
        cli.vediCartePlancia();
    }

    /**
     * Permette al giocatore di vedere le proprie carte
     */
    public void vediDadi(String[] params)
    {
        cli.vediDadi();
    }

    /**
     * @param input riga letta
     * @throws ComandoSconosciutoException se il comando non esiste
     */
    public void handle(String input) throws ComandoSconosciutoException {
        String[] parts = input.split(" ");
        if (parts.length > 0 && comandi.containsKey(parts[0])) {
            try {
                comandi.get(parts[0]).esegui(Arrays.copyOfRange(parts, 1, parts.length));
            } catch (ComandoNonValido e) {
                if(e.getMessage() != null && !e.getMessage().isEmpty())
                    cli.stampaMessaggio(e.getMessage());
                else
                    cli.stampaMessaggio("Comando non valido.");
            }
        } else {
            throw new ComandoSconosciutoException();
        }
    }

    /**
     * Ritorna il colore data la sua codifica testuale
     *  w = white
     *  o = orange
     *  b = black
     *  n = neutro
     * @param coloreTestuale codifica
     */
    public ColoreDado getColore(String coloreTestuale) throws ComandoNonValido {
        switch (coloreTestuale) {
            case "w":
                return ColoreDado.BIANCO;
            case "o":
                return ColoreDado.ARANCIO;
            case "b":
                return ColoreDado.NERO;
            case "n":
                return ColoreDado.NEUTRO;

            default:
                throw new ComandoNonValido("La scelta del familiare deve essere una tra 'w, o, b, n'!");
        }

    }

    /**
     * Ritorna l'intero associato a una stringa
     */
    public int getInt(String valoreTestuale) throws ComandoNonValido {
        try {
            return Integer.parseInt(valoreTestuale);
        }catch (Exception e){
            throw new ComandoNonValido("Campo numerico non valido");
        }
    }
}
