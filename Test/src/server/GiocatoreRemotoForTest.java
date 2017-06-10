package server;

import Domain.DTO.UpdateGiocatoreDTO;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.TipoAzione;
import Exceptions.NetworkException;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Michele on 09/06/2017.
 */
public class GiocatoreRemotoForTest extends GiocatoreRemoto {

    public GiocatoreRemotoForTest()
    {
    }

    @Override
    protected void setPartita(Partita partita) {
        super.setPartita(partita);
    }

    @Override
    public Partita getPartita() {
        return super.getPartita();
    }

    @Override
    public void PartitaIniziata(Tabellone tabellone) throws NetworkException {
    }

    @Override
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws NetworkException {
    }

    @Override
    public void IniziaMossa(int idGiocatore) throws NetworkException {
    }

    @Override
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo) throws NetworkException {
    }

    @Override
    public void SceltaSostegnoChiesa() throws NetworkException {
    }

    @Override
    public void ComunicaAggiornaGiocatore(UpdateGiocatoreDTO update) throws NetworkException {
    }

    @Override
    public void ComunicaFinePartita(LinkedHashMap<Short, Integer> mappaRisultati) throws NetworkException {
    }

    @Override
    public void SceltaPrivilegioConsiglio(int numPergamene) throws NetworkException {
    }

    @Override
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse) throws NetworkException {
    }
}

