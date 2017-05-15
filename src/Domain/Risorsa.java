package Domain;

import org.jetbrains.annotations.NotNull;

/**
 * Created by pietro on 15/05/17.
 */
public class Risorsa {

    private final static short NUMRISORSE = 6;
    private short[] risorse = new short[NUMRISORSE];

    Risorsa(){}
    public Risorsa(int legno, int pietra, int servi, int puntiVittoria, int puntiMilitari, int puntiFede ){
        this.risorse[0]=(short) legno;
        this.risorse[1]= (short) pietra;
        this.risorse[2]= (short) servi;
        this.risorse[3]= (short) puntiVittoria;
        this.risorse[4]= (short) puntiMilitari;
        this.risorse[5]= (short) puntiFede;
    }
    public Risorsa(int risorsa, TipoRisorsa tipoRisorsa){
        setRisorse(tipoRisorsa, risorsa);
    }
    Risorsa(short[] array){
        if (array.length!=NUMRISORSE) throw new UnsupportedOperationException("Array di dimensioni inadeguate");
        risorse=array.clone();
    }



    public int getLegno(){return risorse[0];}
    public int getPietra(){return risorse[1];}
    public int getServi(){return risorse[2];}
    public int getPuntiVittoria(){return risorse[3];}
    public int getPuntiFede(){return risorse[4];}
    public int getPuntiMilitari(){return risorse[5];}

    short[] getArrayRisorse(){return risorse;}

    public void setRisorse(TipoRisorsa tipoRisorsa, int value){
        this.risorse[tipoRisorsa.getPosizione()]= (short) value;
    }

    public void add(Risorsa risorsa){this.setRisorse(add(this, risorsa));}
    public void sub(Risorsa sottrattore){this.setRisorse(sub(this, sottrattore));}
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

    private void setRisorse(Risorsa risorse){
        this.risorse=risorse.getArrayRisorse().clone();
    }

    public enum TipoRisorsa{
        LEGNO("Legno", 0), PIETRA("Pietra", 1), SERVI("Servi", 2), PVITTORIA("Punti Vittoria", 3), PFEDE("Punti Fede", 4), PMILITARI("Punti Militari", 5);

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
