package Domain;

import org.jetbrains.annotations.NotNull;

/**
 * Created by pietro on 15/05/17.
 * Lo sconto risorse deve essere scalato dal costo della carta (non complessivo)
 */
public class Risorsa {

    private final static short NUMRISORSE = 7;
    private short[] risorse = new short[NUMRISORSE];

    public Risorsa(){}
    public Risorsa(int legno, int pietra, int servi, int monete, int puntiVittoria, int puntiMilitari, int puntiFede ){
        this.risorse[0]=(short) legno;
        this.risorse[1]= (short) pietra;
        this.risorse[2]= (short) servi;
        this.risorse[3]= (short) monete;
        this.risorse[4]= (short) puntiVittoria;
        this.risorse[5]= (short) puntiMilitari;
        this.risorse[6]= (short) puntiFede;
    }
    public Risorsa(TipoRisorsa tipoRisorsa, int risorsa){
        setRisorse(tipoRisorsa, risorsa);
    }
    Risorsa(short[] array){
        if (array.length!=NUMRISORSE) throw new UnsupportedOperationException("Array di dimensioni inadeguate");
        risorse=array.clone();
    }


    public int getLegno(){return risorse[0];}
    public int getPietra(){return risorse[1];}
    public int getServi(){return risorse[2];}
    public int getMonete(){return risorse[3];}
    public int getPuntiVittoria(){return risorse[4];}
    public int getPuntiFede(){return risorse[5];}
    public int getPuntiMilitari(){return risorse[6];}

    short[] getArrayRisorse(){return risorse;}

    public void setRisorse(TipoRisorsa tipoRisorsa, int value){
        this.risorse[tipoRisorsa.getPosizione()]= (short) value;
    }
    //TODO: uniformare getRisorse con getLegno, getPietra...
    public int getRisorse(TipoRisorsa tipoRisorsa){
        return this.risorse[tipoRisorsa.getPosizione()];
    }

    @Deprecated
    public void add(Risorsa risorsa){this.setRisorse(add(this, risorsa));}
    @Deprecated
    public void sub(Risorsa sottrattore){this.setRisorse(sub(this, sottrattore));}
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
    public boolean isPositivo(){
        for (short r : risorse){
            if (r<0) return false;
        }
        return true;
    }
    public Risorsa multScalare(int scalare){
        short[] array = this.getArrayRisorse();
        short[] newArray = new short[NUMRISORSE];
        for (int i=0; i<NUMRISORSE; i++){
            newArray[i]=(short) (array[i]*scalare);
        }
        return new Risorsa(newArray);
    }
    public Risorsa clone(){ return new Risorsa(this.getArrayRisorse().clone());}
    public static Risorsa setNegToZero(Risorsa risorsa){
        short[] newRisorse = risorsa.getArrayRisorse().clone();
        for (short s: newRisorse) {
            if (s<0) s=0;
        }
        return new Risorsa(newRisorse);
    }

    @Deprecated
    private void setRisorse(Risorsa risorse){
        this.risorse=risorse.getArrayRisorse().clone();
    }

    public enum TipoRisorsa{
        LEGNO("Legno", 0), PIETRA("Pietra", 1), SERVI("Servi", 2), MONETE("Monete", 3), PVITTORIA("Punti Vittoria", 4), PFEDE("Punti Fede", 5), PMILITARI("Punti Militari", 6);

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
}
