package server;

import Domain.Carta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 10/06/17.
 * Classe utilizzata per il debug, da rimuovere
 */
public class LoaderCarteTest {
    static List<Carta> mazzoCarte = new ArrayList();


    public static void main(String[] args) {
        caricaCarte();
    }
    public static void caricaCarte(){
        int i = 0;
        mazzoCarte=new LoaderCarte().getListaCarte();
        for(Carta c : mazzoCarte){
            i=i+1;
            System.out.println(i+" " +c.getTipoCarta()+" ");
        }
    }
}
