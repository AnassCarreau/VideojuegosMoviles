package es.ucm.gdv.blas.oses.carreau.lib;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import  es.ucm.gdv.blas.oses.carreau.lib.Celda;

public class Pistas {

    private List<Pair<TipoPista, Pair<Integer, Integer>>> listaPistas;

    public Pistas(){
        listaPistas = new ArrayList<>();
    }

    /**
     * Metodo que añade una pista cuando el jugador ha hecho algo mal y guarda la posicion de la que
     * tiene que dar la pista.
     */
    public void addPista(TipoPista l, int posX, int posY){
        listaPistas.add(new Pair(l, new Pair(posX, posY)));
    }

    public String getPistaTablero() {

        //Pair<TipoPista, Pair<Integer, Integer>> p = getRandomCasilla();
        for (int i = 0; i < listaPistas.size(); i++) {
            Pair<TipoPista, Pair<Integer, Integer>> p = listaPistas.get(i);
            TipoPista tP = p.getLeft();
            Pair<Integer, Integer> pos = p.getRight();
            switch (tP) {
                case ValueReached:
                    return "This number can see all its dots " + pos.getLeft() + " " + pos.getRight(); // 1
                case WouldExceed:
                    return "Looking further in one direction would exceed this number " + pos.getLeft() + " " + pos.getRight(); // 2
                case OneDirectionRequired:
                    return "One specific dot is included <br>in all solutions imaginable" + pos.getLeft() + " " + pos.getRight(); // 3
                case ErrorClosedTooLate:
                    return "This number sees a bit too much " + pos.getLeft() + " " + pos.getRight(); // 4
                case ErrorClosedTooEarly:
                    return "This number can't see enough" + pos.getLeft() + " " + pos.getRight(); // 5
                case MustBeWall:
                    return "This one should be easy... " + pos.getLeft() + " " + pos.getRight(); //6.1
                case LockedIn:
                    return "A blue dot should always see at least one other " + pos.getLeft() + " " + pos.getRight();//6.2
                case ImposibleVision:
                    return "Imposible to fill the vision of this tile" + pos.getLeft() + " " + pos.getRight(); // 10
            }
        }
        return "";
    }


    /*public  String getPistaTableroInAdecuado() {
        Pair<Celda, Pair<Integer, Integer>> p = getRandomCasilla();
        Celda c = p.getLeft();
        Pair<Integer, Integer> pos = p.getRight();
        if (c.getValorDefault() > c.getCurrentVisibles()) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "Sobrevision";
        }
        else if (c.getCurrentVisibles() < c.getValorDefault() /*&& cerrada*) {
            return "Celda " + pos.getLeft() +" "+ pos.getRight() + "infravision y cerrada";

        }
       
        return "" ;
    }*/

    private  Pair<TipoPista, Pair<Integer, Integer>> getRandomCasilla()
    {
        Random r= new Random();
        return listaPistas.get(r.nextInt(listaPistas.size()));
    }


}
