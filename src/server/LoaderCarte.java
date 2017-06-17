package server;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.io.File.separator;

/**
 * Created by pietro on 09/06/17.
 */
public class LoaderCarte {
    private static final String FILECARTE = "/carte.csv";

    private List<Carta> listaCarte;

    public LoaderCarte() {

        listaCarte = new ArrayList<>();
        Scanner scan;
        List<String> righeFile= new ArrayList<>();
        try {
            URL url = getClass().getResource(FILECARTE);
            scan = new Scanner(new FileReader(url.getPath()));
            while (scan.hasNext())
                righeFile.add(scan.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("File carte non trovato");
        } catch (IOException e){
            System.out.println("Errore in lettura file carte");
        }
        righeFile = rimuoviSpaziCommenti(righeFile);
        formaCarte(righeFile);

    }

    public List<Carta> getListaCarte() {
        return listaCarte;
    }

    private List<String> rimuoviSpaziCommenti(List<String> listaStringhe) {

        List<String> stringheFiltrate = new ArrayList<>();

        for (String stringaCorrente : listaStringhe) {
            //rimuovo commenti
            String[] elementi = stringaCorrente.split("//");
            //rimuovo doppi \t\t
            stringaCorrente= rimuoviRicorTab(stringaCorrente);
            //rimuovo stringhe vuote
            if (!stringaCorrente.isEmpty()&&!stringaCorrente.equals("\t"))
                stringheFiltrate.add(stringaCorrente);
        }
        return stringheFiltrate;
    }

    private void formaCarte(List<String> stringhe) {
        int rigaCorrente = 0;
        List<String> cartaCorrente;
        String stringaCorrente;

        stringhe = rimuoviSpaziCommenti(stringhe);

        stringaCorrente = stringhe.get(0);
        while (rigaCorrente < stringhe.size()) {
            stringaCorrente = stringhe.get(rigaCorrente);
            cartaCorrente = new ArrayList<>();

            //region Filtro le righe della singola carta
            while (!stringaCorrente.equals("END") && rigaCorrente < stringhe.size()) {
                cartaCorrente.add(stringaCorrente);
                rigaCorrente = rigaCorrente + 1;
                stringaCorrente = stringhe.get(rigaCorrente);
            }
            //endregion

            //region genero la carta o stampo eventuali errori
            Carta carta;
            try {
                carta = stringToCard(cartaCorrente);
                listaCarte.add(carta);
            } catch (IllegalArgumentException e) {
                System.out.println("Carta non riconosciuta(" + e + "):");
                for (String s : cartaCorrente) {
                    System.out.println("\t" + s);
                }
            }
            //endregion

            //region ignoro riga END
            rigaCorrente = rigaCorrente + 1;
            //endregion
        }
    }

    private Carta stringToCard(List<String> infoCarta) throws IllegalArgumentException {
        String nome=null;
        TipoCarta tipo=null;
        Integer valore = 0;
        int periodo=0;
        List<Effetto> immediati = new ArrayList<>();
        List<Effetto> permanenti = new ArrayList<>();

        for (int i = 0; i < infoCarta.size(); i++) {
            String stringaCorrente = infoCarta.get(i);
            String[] rigaCorrente = stringaCorrente.replaceAll("\t\t", "\t").split("\t");

            switch (rigaCorrente[0]) {
                case "NOME":
                    nome = rigaCorrente[1];
                    if (nome.isEmpty()) throw new IllegalArgumentException("Nome Carta Vuoto");
                    break;
                case "TIPO":
                    tipo = TipoCarta.valueOf(rigaCorrente[1]);
                    for (TipoCarta t : TipoCarta.values()) {
                        if (t==null){
                            throw new IllegalArgumentException("TipoCarta non Riconosciuto");
                        }
                    }
                    break;
                case "PERIODO":
                    periodo = Integer.valueOf(rigaCorrente[1]);
                    if (periodo < 1 || periodo > 3) throw new IllegalArgumentException("Periodo Irregolare");
                    break;
                case "VALORE":
                    valore = Integer.valueOf(rigaCorrente[1]);
                    if (valore < 1 || valore > 7)
                        throw new IllegalArgumentException("Il valore dovrebbe essere tra 1 e 7");
                    break;
                case "COSTO":
                    Risorsa[] cost;
                    Risorsa[] gain;
                    Risorsa costo1 = stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 1, 8));
                    Risorsa gain1 = new Risorsa();
                    if (rigaCorrente.length > 8) {
                        gain1 = stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 8, 15));
                    }

                    if (infoCarta.get(i + 1).startsWith("\t")) {
                        String[] rigaSuccessiva = infoCarta.get(i + 1).split("\t");
                        Risorsa costo2 = stringToRisorsa(Arrays.copyOfRange(rigaSuccessiva, 1, 8));
                        Risorsa gain2;
                        cost = new Risorsa[2];
                        cost[0] = costo1;
                        cost[1] = costo2;
                        gain = new Risorsa[2];
                        gain[0] = gain1;
                        if (rigaSuccessiva.length > 8) {
                            gain2 = stringToRisorsa(Arrays.copyOfRange(rigaSuccessiva, 8, 15));
                        } else gain2=new Risorsa();
                        gain[1] = gain2;
                    } else {
                        cost = new Risorsa[1];
                        cost[0] = costo1;
                        gain = new Risorsa[1];
                        gain[0] = gain1;
                    }
                    immediati.add(new ScambiaRisorse(cost, gain, true));
                    break;
                case "AnnullaBonusSpazioAz":
                    List<Integer> caselleDaAnnullare = new ArrayList<>();
                    try{
                        for (int j = 2; j < rigaCorrente.length; j++) {
                            caselleDaAnnullare.add(Integer.valueOf(rigaCorrente[j]));
                        }

                        if (rigaCorrente[1].equals("true")) {
                            immediati.add(new AnnullaBonusSpazioAz(caselleDaAnnullare));

                        } else {
                            permanenti.add(new AnnullaBonusSpazioAz(caselleDaAnnullare));
                        }
                    } catch (Exception e){
                        throw new IllegalArgumentException("AnnullaBonusAzione non riconosciuta correttamente");
                    }
                    break;
                case "AumentaValoreAzione":
                    TipoAzione tipoAzione=null;
                    int incremento;
                    try{
                        tipoAzione=stringToTipoAzione(rigaCorrente[2]);
                        if(tipoAzione==null) throw new IllegalArgumentException("tipoAzioneNonRiconosciuta");
                        incremento= Integer.valueOf(rigaCorrente[3]);
                        if (rigaCorrente[1].equals("true")) {
                            immediati.add(new AumentaValoreAzione(tipoAzione, incremento));

                        } else {
                            permanenti.add(new AumentaValoreAzione(tipoAzione, incremento));
                        }
                    } catch (Exception e)
                    {throw new IllegalArgumentException("Errore in AumentaValoreAzione: "+e.getMessage());}
                    break;
                case "BonusRisorseXCarte":
                    TipoCarta carta = null;
                    Risorsa risorsa;
                    try{
                        for (TipoCarta c: TipoCarta.values()){
                            if (c.toString().equals(rigaCorrente[2])){
                                carta=c;
                                break;
                            }
                        }
                        if(carta==null) throw new IllegalArgumentException("tipoCartaNonRiconosciuta");
                        risorsa = stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 3, 10));
                        if (rigaCorrente[1].equals("true")) {
                            immediati.add(new BonusRisorseXCarte(carta, risorsa));
                        } else {
                            permanenti.add(new BonusRisorseXCarte(carta, risorsa));
                        }
                    } catch (Exception e)
                    {throw new IllegalArgumentException("Errore in BonusRisorseXCarte"+e.getMessage());}
                    break;
                case "DimezzaRisorseAllocate":
                    Risorsa.TipoRisorsa tipoRisorsa=null;
                    try{
                        for (Risorsa.TipoRisorsa r: Risorsa.TipoRisorsa.values()){
                            if (r.toString().equals(rigaCorrente[2])){
                                tipoRisorsa=r;
                                break;
                            }
                        }
                        if(tipoRisorsa==null) throw new IllegalArgumentException("tipoRisorsaNonRiconosciuta");
                        if (rigaCorrente[1].equals("true")) {
                            immediati.add(new DimezzaRisorsaAllocata(tipoRisorsa));
                        } else {
                            permanenti.add(new DimezzaRisorsaAllocata(tipoRisorsa));
                        }
                    } catch (Exception e)
                    {throw new IllegalArgumentException("Errore in BonusRisorseXCarte"+e.getMessage());}
                    break;
                case "DisabilitaCasella":
                    List<Integer> caselleDaDisabilitare = new ArrayList<>();
                    try{
                        for (int j = 2; j < rigaCorrente.length; j++) {
                            caselleDaDisabilitare.add(Integer.valueOf(rigaCorrente[j]));
                        }
                        Integer[] v= new Integer[caselleDaDisabilitare.size()];
                        caselleDaDisabilitare.toArray(v);
                        if (rigaCorrente[1].equals("true")) {
                            immediati.add(new DisabilitaCasella(v));

                        } else {
                            permanenti.add(new DisabilitaCasella(v));
                        }
                    } catch (Exception e){
                        throw new IllegalArgumentException("DisabilitaCaselle non riconosciuta correttamente");
                    }
                    break;
                case "DisabilitaTurni":
                    int turno;
                    try{
                        turno= Integer.valueOf(rigaCorrente[2]);
                        if (rigaCorrente[1].equals("true")) {
                            immediati.add(new DisabilitaTurni(turno));

                        } else {
                            permanenti.add(new DisabilitaTurni(turno));
                        }
                    } catch (Exception e){
                        throw new IllegalArgumentException("DisabilitaTurno non riconosciuta correttamente");
                    }
                    break;
                case "EndGame":
                    try{
                        if(rigaCorrente.length==3){
                            //Try Get TipoCarta
                            TipoCarta tipoEndGame=null;
                            try{tipoEndGame=TipoCarta.valueOf(rigaCorrente[2]);
                            } catch (Exception e){tipoEndGame=null;}
                            //Sono monete o tipoCarta o scalacarte impresa
                            if (rigaCorrente[2].equals("true")) new EffettoEndGame(true);
                            else if(tipoEndGame!=null) new EffettoEndGame(tipoEndGame);
                            else permanenti.add(new EffettoEndGame(Integer.valueOf(rigaCorrente[2])));
                        }else if (rigaCorrente.length==10){
                            //Scala risorse per tipoCarta
                            permanenti.add(new EffettoEndGame(stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 3, 10)), TipoCarta.valueOf(rigaCorrente[2])));
                        }
                        else if(rigaCorrente.length==9){
                            permanenti.add(new EffettoEndGame(stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 2, 9))));
                        }
                        else {
                            throw new RuntimeException();
                        }
                    } catch (Exception e){
                        throw new IllegalArgumentException("EndGame non riconosciuta correttamente: "+e.getMessage());
                    }
                    break;
                case "EffettuaAzioneSpecifica":
                    try{
                        tipoAzione=stringToTipoAzione(rigaCorrente[2]);
                        if(tipoAzione==null) throw new IllegalArgumentException("Il tipo azione non è riconosciuto");
                        if(rigaCorrente.length<5) {
                            immediati.add(new EffettuaAzioneSpecifica(tipoAzione, new Risorsa(), Integer.valueOf(rigaCorrente[3])));
                        } else{
                            immediati.add(new EffettuaAzioneSpecifica(tipoAzione, stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 4, 11)), Integer.valueOf(rigaCorrente[3])));
                        }

                    } catch (Exception e){
                        throw new IllegalArgumentException("EffettuaAzioneSpecifica non riconosciuta correttamente: "+e.getMessage());
                    }
                    break;
                case "MalusAttivazioneRisorse":
                    try{
                        permanenti.add(new MalusAttivazioneBonus(stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 2, 9))));
                    } catch (Exception e){
                        throw new IllegalArgumentException("MalusAttivazioneRisorse non riconosciuta correttamente");
                    }
                    break;
                case "Pergamena":
                    try{
                        if(rigaCorrente[1].equals("true")) immediati.add(new Pergamena(Integer.valueOf(rigaCorrente[2])));
                        else permanenti.add(new Pergamena(Integer.valueOf(rigaCorrente[2])));
                    } catch (Exception e){
                        throw new IllegalArgumentException("Pergamena non riconosciuta correttamente");
                    }
                    break;
                case "ScambiaRisorse":
                    try{
                        int j=i;
                        List<Risorsa> esborso = new ArrayList<>();
                        List<Risorsa> guadagno = new ArrayList<>();
                        do{
                            if(i==j) {
                                esborso.add(stringToRisorsa(Arrays.copyOfRange(infoCarta.get(j).split("\t"), 2, 9)));
                                guadagno.add(stringToRisorsa(Arrays.copyOfRange(infoCarta.get(j).split("\t"), 9, 16)));
                            }else{
                                esborso.add(stringToRisorsa(Arrays.copyOfRange(infoCarta.get(j).split("\t"), 1, 8)));
                                guadagno.add(stringToRisorsa(Arrays.copyOfRange(infoCarta.get(j).split("\t"), 8, 15)));
                            }
                        }while(infoCarta.get(j).startsWith("\t"));
                        Risorsa[] v1, v2;
                        v1= new Risorsa[esborso.size()];
                        v2= new Risorsa[guadagno.size()];
                        esborso.toArray(v1);
                        guadagno.toArray(v2);
                        if (rigaCorrente[1].equals("true")) immediati.add(new ScambiaRisorse(v1, v2, false));
                        else permanenti.add(new ScambiaRisorse(v1, v2, false));
                    } catch (Exception e){
                        throw new IllegalArgumentException("ScambiaRisorse non riconosciuta correttamente");
                    }
                    break;
                case "ScontoRisorseXCarte":
                    try{
                        TipoCarta card = TipoCarta.valueOf(rigaCorrente[2]);
                        if (rigaCorrente[1].equals("true")) immediati.add(new ScontoRisorsaCarte(card, stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 3, 10))));
                            else permanenti.add(new ScontoRisorsaCarte(card, stringToRisorsa(Arrays.copyOfRange(rigaCorrente, 3, 10))));
                    } catch (Exception e){
                        throw new IllegalArgumentException("ScontoRisorsePerCarte non riconosciuta correttamente"+ e.getMessage());
                    }
                    break;
                case "BonusRisorseXRisorse":
                    try{
                        Risorsa.TipoRisorsa tipoRisorsa1= Risorsa.TipoRisorsa.valueOf(rigaCorrente[2]);
                        Risorsa.TipoRisorsa tipoRisorsa2= Risorsa.TipoRisorsa.valueOf(rigaCorrente[3]);
                        double fattore = Double.parseDouble(rigaCorrente[4]);
                        immediati.add(new BonusRisorseXRisorse(tipoRisorsa1, tipoRisorsa2, fattore));
                    } catch (Exception e){
                        throw new IllegalArgumentException("BonusRisorseXRisorse non riconosciuto correttamente");
                    }
                    break;
                case "LISTA_EFFETTI":
                    break;

                default:
                   break;
            }
        }
        return creaCarta(nome, periodo, valore, immediati, permanenti, tipo);

    }

    private Risorsa stringToRisorsa(String[] stringhe) {
        if (stringhe.length != 7) throw new IllegalArgumentException("stringhe risorse di lunghezza errata");
        try {
            return new Risorsa(
                    Integer.valueOf(stringhe[0]),
                    Integer.valueOf(stringhe[1]),
                    Integer.valueOf(stringhe[2]),
                    Integer.valueOf(stringhe[3]),
                    Integer.valueOf(stringhe[4]),
                    Integer.valueOf(stringhe[5]),
                    Integer.valueOf(stringhe[6])
            );
        } catch (Exception e) {throw new IllegalArgumentException("stringhe risorse non corrette");}
    }

    private Carta creaCarta(String nome, int periodo, Integer valore,  List<Effetto> immediati, List<Effetto> permanenti, TipoCarta tipo){
        switch (tipo){
            case Impresa:
                return new CartaImpresa(nome, periodo, immediati, permanenti);
            case Edificio:
                return new CartaEdificio(nome, periodo, valore, immediati, permanenti);
            case Territorio:
                return new CartaTerritorio(nome, periodo, valore, immediati, permanenti);
            case Personaggio:
                return new CartaPersonaggio(nome, periodo, immediati, permanenti);
            case Scomunica:
                return new TesseraScomunica(nome, periodo, permanenti);
        }
        throw new IllegalArgumentException("Tipo carta non riconosciuto");
    }
    private String rimuoviRicorTab(String stringa){
        if(stringa.contains("\t\t")){
            stringa=stringa.replaceAll("\t\t", "\t");
            return rimuoviRicorTab(stringa);
        } else if(stringa.endsWith("\t")&& stringa.length()>1) return stringa.substring(0, stringa.length()-1);
        else return stringa;
    }


    private TipoAzione stringToTipoAzione (String s){

        if(s.toUpperCase().equals("TORRE")) return TipoAzione.TORRE;
        for (TipoAzione t : TipoAzione.values()){
            if (t.toString().contains(s.toUpperCase())) return t;
        }
        System.out.println("Tipo azione in input FALLITA: "+s);
        return null;
    }
}
