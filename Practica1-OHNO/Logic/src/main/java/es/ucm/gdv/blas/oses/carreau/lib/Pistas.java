package es.ucm.gdv.blas.oses.carreau.lib;


import java.util.List;
import java.util.Random;

import  es.ucm.gdv.blas.oses.carreau.lib.Celda;
public class Pistas {

    private List< Pair <Celda,Pair<Integer, Integer>>> casillas;

    public  String getPistaTableroAdecuado() {
        Pair<Celda, Pair<Integer, Integer>> p = getRandomCasilla();
        Celda c = p.getLeft();
        Pair<Integer, Integer> pos = p.getRight();
        if (c.getValorDefault() == c.getCurrentVisibles()) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "Satisfecha .Cierra los extremos";
        }
        else if (c.getCurrentVisibles() + 1 == c.getValorDefault() ) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "casi superada .Pon pared";

        }
        /*else if (c.getCurrentVisibles()  == c.getValorDefault()  ) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "casi superada .Pon pared";

        }*/
        return "" ;
    }


    public  String getPistaTableroInAdecuado() {
        Pair<Celda, Pair<Integer, Integer>> p = getRandomCasilla();
        Celda c = p.getLeft();
        Pair<Integer, Integer> pos = p.getRight();
        if (c.getValorDefault() > c.getCurrentVisibles()) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "Sobrevision";
        }
        else if (c.getCurrentVisibles() < c.getValorDefault() /*&& cerrada*/) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "infravision y cerrada";

        }
       
        return "" ;
    }



    private  Pair <Celda,Pair<Integer, Integer>> getRandomCasillaAdecuada(List< Pair <Celda,Pair<Integer, Integer>>> casillas)
    {
        Random r= new Random();
        return casillas.get(r.nextInt(casillas.size()));
    }
    private  Pair <Celda,Pair<Integer, Integer>> getRandomCasillaInAdecuada(List< Pair <Celda,Pair<Integer, Integer>>> casillas)
    {
        Random r= new Random();
        return casillas.get(r.nextInt(casillas.size()));
    }


    private  Pair <Celda,Pair<Integer, Integer>> getRandomCasilla()
    {
        Random r= new Random();
        return casillas.get(r.nextInt(casillas.size()));
    }


}
