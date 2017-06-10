package Domain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by pietro on 15/05/17.
 * Lo sconto risorse deve essere scalato dal costo della carta (non complessivo)
 */
public class Risorsa  implements Serializable {

    private final static short NUMRISORSE = 7;
    private short[] risorse = new short[NUMRISORSE];

    /**
     * Costruttore base
     */
    public Risorsa(){}

    /**
     * Costruttore parametrizzato
     * @param legno numero di legni iniziali
     * @param pietra numero di pietre iniziali
     * @param servi numero di servitori iniziali
     * @param monete numero di monete iniziali
     * @param puntiVittoria numero di punti vittoria iniziali
     * @param puntiMilitari numero di punti militari iniziali
     * @param puntiFede numero di punti fede iniziali
     */
    public Risorsa(int legno, int pietra, int servi, int monete, int puntiVittoria, int puntiMilitari, int puntiFede ){
        this.risorse[0]=(short) legno;
        this.risorse[1]= (short) pietra;
        this.risorse[2]= (short) servi;
        this.risorse[3]= (short) monete;
        this.risorse[4]= (short) puntiVittoria;
        this.risorse[5]= (short) puntiMilitari;
        this.risorse[6]= (short) puntiFede;
    }

    /**
     * Costruttore per tipo di risorsa
     * @param tipoRisorsa tipo di risorsa da creare
     * @param risorsa quantitativo di risorsa da creare
     */
    public Risorsa(TipoRisorsa tipoRisorsa, int risorsa){
        risorse[tipoRisorsa.getPosizione()]=(short) risorsa;
    }

    /**
     * Costruttore della risorsa dato l'array
     * @param array array delle risorse
     */
    Risorsa(short[] array){
        if (array.length!=NUMRISORSE) throw new UnsupportedOperationException("Array di dimensioni inadeguate");
        risorse=array.clone();
    }

    //region Getters
    public int getLegno(){return risorse[0];}
    public int getPietra(){return risorse[1];}
    public int getServi(){return risorse[2];}
    public int getMonete(){return risorse[3];}
    public int getPuntiVittoria(){return risorse[4];}
    public int getPuntiMilitari(){return risorse[5];}
    public int getPuntiFede(){return risorse[6];}
    short[] getArrayRisorse(){return risorse;}

    public int getRisorse(TipoRisorsa tipoRisorsa){
        return this.risorse[tipoRisorsa.getPosizione()];
    }
    //endregion

    /**
     *
     * @param tipoRisorsa
     * @param value
     * @return ritorna copia dell'oggetto corrente con TipoRisorsa settata a value
     */
    public Risorsa setRisorse(TipoRisorsa tipoRisorsa, int value){
        /*
        short[] risorsaCorrente = this.getArrayRisorse();
        short[] nuovaRisorsa = risorsaCorrente.clone();
        nuovaRisorsa[tipoRisorsa.getPosizione()]=(short)value;
        return new Risorsa(nuovaRisorsa);
        */

        this.getArrayRisorse()[tipoRisorsa.getPosizione()] = (short)value;
        return this;
    }

    /**
     * aggiungi a this
     * @param risorsa da sommare a this
     */
    @NotNull
    public void add (Risorsa risorsa){
        if(risorsa==null) throw new NullPointerException("Risorsa NULL");

        short[] array = risorsa.getArrayRisorse();
        for (int i=0; i<array.length; i++){
            this.risorse[i]= (short) (this.risorse[i]+array[i]);
        }
    }

    /**
     * sottrai a this
     * @param risorsa da sottrarre a this
     */
    @NotNull
    public void sub (Risorsa risorsa){
        if(risorsa==null) throw new NullPointerException("Risorsa NULL");

        short[] array = risorsa.getArrayRisorse();
        for (int i=0; i<array.length; i++){
            this.risorse[i]= (short) (this.risorse[i]-array[i]);
        }
    }

    /**
     * Ritorna la somma delle risorse passate
     * @param risorsa1 prima risorsa da sommare
     * @param risorsa2 seconda risorsa da sommare
     */
    @NotNull
    public static Risorsa add (Risorsa risorsa1, Risorsa risorsa2){
        short[] array1 = risorsa1.getArrayRisorse();
        short[] array2 = risorsa2.getArrayRisorse();
        short[] result = new short[array1.length];
        for (int i=0; i<array1.length; i++){
            result[i]=(short) (array1[i]+array2[i]);
        }
        return new Risorsa(result);
    }

    /**
     * Ritorna la differenza delle risorse passate
     * @param minuendo risorsa minuendo
     * @param sottrattore risorsa sottraendo
     */
    @NotNull
    public static Risorsa sub(Risorsa minuendo, Risorsa sottrattore){
        short[]array1=minuendo.getArrayRisorse();
        short[]array2=sottrattore.getArrayRisorse();
        short[] result = new short[array1.length];
        for (int i=0; i<array1.length; i++){
            result[i]=(short) (array1[i]-array2[i]);
        }
        return new Risorsa(result);
    }

    /**
     * Torna true se tutte le risorse sono positive
     */
    public boolean isPositivo(){
        for (short r : risorse){
            if (r<0) return false;
        }
        return true;
    }


    /**
     * Moltiplica tutte le risorse per uno scalare e RESTITUISCE il risultato
     * @param scalare scalare che moltiplica le risorse
     */
    public Risorsa multScalare(int scalare){
        short[] array = this.getArrayRisorse();
        short[] newArray = new short[NUMRISORSE];
        for (int i=0; i < NUMRISORSE; i++){
            newArray[i]= (short)(array[i]*scalare);
        }
        return new Risorsa(newArray);
    }

    /**
     * Clona una risorsa
     * @return la risorsa passata come parametro clonata
     */
    public Risorsa clone(){ return new Risorsa(this.getArrayRisorse().clone());}

    /**
     * Setta tutte le risorse negative pari a zero
     * @param risorsa risorsa da controllare
     */
    public static Risorsa setNegToZero(Risorsa risorsa){
        short[] newRisorse = risorsa.getArrayRisorse().clone();
        for (int i = 0; i < newRisorse.length; i++)
        {
            if(newRisorse[i] < 0)
                newRisorse[i] = 0;
        }
        return new Risorsa(newRisorse);
    }

    /**
     * Inidica i tipi di risorsa possibili
     */
    public enum TipoRisorsa{
        LEGNO("Legno", 0), PIETRA("Pietra", 1), SERVI("Servi", 2), MONETE("Monete", 3), PVITTORIA("Punti Vittoria", 4), PFEDE("Punti Fede", 6), PMILITARI("Punti Militari", 5);

        private final String tipoRisorsa;
        private final short posizione;

        TipoRisorsa(String tipoRisorsa, int posizione) {
            this.tipoRisorsa = tipoRisorsa;
            this.posizione=(short) posizione;
        }

        public String toString() {
            return tipoRisorsa;
        }

        int getPosizione(){ return posizione;}
    }

    public boolean isEmpty(){
        for (short i : this.risorse)
            if(i!=0) return false;
        return true;
    }
}
