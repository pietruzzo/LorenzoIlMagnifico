package Domain;

import Domain.DTO.UpdateGiocatoreDTO;
import Domain.Effetti.GestoreEffettiGiocatore;
import Exceptions.NetworkException;

import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Giocatore implements Serializable {

    //region Proprietà
    private short IdGiocatore;
    protected String Nome;
    protected ColoreGiocatore Colore;
    protected List<CartaTerritorio> CarteTerritorio;
    protected List<CartaEdificio> CarteEdificio;
    protected List<CartaPersonaggio> CartePersonaggio;
    protected List<CartaImpresa> CarteImpresa;
    protected List<Familiare> Familiari;
    protected List<TesseraScomunica> CarteScomunica;
    protected transient GestoreEffettiGiocatore gestoreEffettiGiocatore;
    protected Risorsa Risorse;
    private int ordineTurno;

    private Boolean rapportoVaticanoEffettuato;
    private Boolean azioneBonusDaEffettuare;
    private int privilegiDaScegliere;
    //endregion

    //region Getters
    public short getIdGiocatore() {
        return IdGiocatore;
    }

    public int getOrdineTurno() { return ordineTurno; }

    public Risorsa getRisorse() {
        return Risorse;
    }

    public List<Carta> getListaCarte(){
        List<Carta> listaCarte=new ArrayList<Carta>();
        listaCarte.addAll(CarteTerritorio);
        listaCarte.addAll(CarteEdificio);
        listaCarte.addAll(CartePersonaggio);
        listaCarte.addAll(CarteImpresa);
        listaCarte.addAll(CarteScomunica);
        return  listaCarte;
    }

    public Boolean getRapportoVaticanoEffettuato() {
        return rapportoVaticanoEffettuato;
    }

    public Familiare getFamiliareByColor(ColoreDado coloreDado)
    {
        return this.Familiari.stream().filter(f -> f.ColoreDado == coloreDado).findFirst().orElse(null);
    }

    public List<CartaImpresa> getCarteImpresa() {
        return CarteImpresa;
    }

    public Boolean getAzioneBonusDaEffettuare() {
        return azioneBonusDaEffettuare;
    }

    public int getPrivilegiDaScegliere() {
        return privilegiDaScegliere;
    }

    /**
     * @return colore del giocatore
     */
    public ColoreGiocatore getColore() {
        return Colore;
    }

    /**
     * @return nome del giocatore
     */
    public String getNome(){
        return Nome;
    }
    //endregion

    //region Setters
    public void setOrdineTurno(int ordineTurno) {
        this.ordineTurno = ordineTurno;
    }

    public void setRapportoVaticanoEffettuato(Boolean rapportoVaticanoEffettuato) {
        this.rapportoVaticanoEffettuato = rapportoVaticanoEffettuato;
    }

    public void setAzioneBonusDaEffettuare(Boolean azioneBonusEffettuata) {
        this.azioneBonusDaEffettuare = azioneBonusEffettuata;
    }

    /**
     * Aumenta di uno i privilegi da scegliere
     */
    public void incrementaPrivilegiDaScegliere()
    {
        this.privilegiDaScegliere++;
    }

    /**
     * Diminuisce di uno i privilegi da scegliere
     */
    public void decrementaPrivilegiDaScegliere()
    {
        this.privilegiDaScegliere--;
    }
    //endregion

    /**
     * Costruttore
     */
    protected Giocatore( )
    {
        this.CarteTerritorio = new ArrayList<>();
        this.CarteEdificio = new ArrayList<>();
        this.CartePersonaggio = new ArrayList<>();
        this.CarteImpresa = new ArrayList<>();
        this.CarteScomunica = new ArrayList<>();
        gestoreEffettiGiocatore= new GestoreEffettiGiocatore(this);
        //region Inizializzazione Familiari
        //Inizializzazione Familiari
        this.Familiari = new ArrayList<>();
        ColoreDado coloreFamiliare = null;

        for(int i = 0; i <= 3; i++)
        {
            switch (i)
            {
                case 0:
                    coloreFamiliare = ColoreDado.NERO;
                    break;

                case 1:
                    coloreFamiliare = ColoreDado.BIANCO;
                    break;

                case 2:
                    coloreFamiliare = ColoreDado.ARANCIO;
                    break;

                case 3:
                    coloreFamiliare= ColoreDado.NEUTRO;
                    break;
            }
            this.Familiari.add(new Familiare(this, coloreFamiliare));
        }
        //endregion
    }

    /**
     *  Setta le proprietà al login del giocatore
     */
    public void SettaProprietaIniziali(short idGiocatore, String nome, ColoreGiocatore colore, int monete)
    {
        this.IdGiocatore = idGiocatore;
        this.ordineTurno = idGiocatore;
        this.Nome = nome;
        this.Colore = colore;
        this.rapportoVaticanoEffettuato = false;
        this.azioneBonusDaEffettuare = false;

        this.Risorse = new Risorsa(2, 2, 3, monete, 0, 0, 0 );
    }

    /**
     * Incrementa le risorse ottenute da un familiare su uno spazio azione o da un privilegio del consiglio
     */
    public void OttieniBonusRisorse(Risorsa risorseSpazioAzione)
    {
        this.Risorse.add(risorseSpazioAzione);
    }

    /**
     * Paga le risorse necessarie per prendere una carta
     */
    public void PagaRisorse(Risorsa costoRisorse)
    {
        this.Risorse.sub(costoRisorse);
    }

    /**
     * Aggiorna il valore delle azioni dei singoli familiari
     * @param esitoDadi valore dei dadi tirati all'inizio del turno
     */
    public void SettaValoreFamiliare(int[] esitoDadi)
    {
        for (Familiare fam : this.Familiari) {
            switch(fam.ColoreDado)
            {
                case NERO:
                    fam.setValore(esitoDadi[0]);
                    break;

                case BIANCO:
                    fam.setValore(esitoDadi[1]);
                    break;

                case ARANCIO:
                    fam.setValore(esitoDadi[2]);
                    break;

                case NEUTRO: //Il familiare neutro non ha valore
                    fam.setValore(0);
                    break;
            }
        }
    }

    /**
     * Spende tutti i suoi punti fede
     * ottiene un certo numero di punti vittoria in base ai punti fede spesi
     */
    public UpdateGiocatoreDTO SostieniLaChiesa(int bonusPuntiVittoria)
    {
        this.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, this.Risorse.getPuntiVittoria() + bonusPuntiVittoria );
        this.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 0);
        this.setRapportoVaticanoEffettuato(true);

        return new UpdateGiocatoreDTO(this.IdGiocatore, this.Risorse, null, null );
    }

    /**
     * Calcola la somma dei Punti Vittoria indicati sulle carte impresa sulla propria plancia giocatore.
     * In generale applica tutti gli effetti delle carte che si attivano alla fine della partita
     * Oltre alle carte impresa dunque, considera anche gli effetti delle tessere scomunica
     */
    public void updatePuntiVittoriaByEffettiCarte()
    {
        this.gestoreEffettiGiocatore.endGame(this.Risorse);
    }

    /**
     * Ritorna 1 Punto Vittoria ogni 5 risorse, calcolando tutte le risorse (legno, pietra, servitori, monete) insieme.
     */
    public int getPuntiVittoriaByRisorse()
    {
        int totaleRisorse = this.Risorse.getLegno() + this.Risorse.getPietra() + this.Risorse.getServi() + this.Risorse.getMonete();
        return totaleRisorse / 5;
    }

    /**
     * Metodo chiamato dal giocatore remoto per segnalare le pergamene da scegliere
     * @param numPergamene numero di pergamene da scegliere
     */
    public void SceltaPrivilegioConsiglio(int numPergamene) throws NetworkException {
    }

    /**
     * Metodo chiamato dal giocatore remoto per segnalare il tipo di azione bonus da svolgere
     * @param tipoAzioneBonus tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse) throws NetworkException {

    }
}

