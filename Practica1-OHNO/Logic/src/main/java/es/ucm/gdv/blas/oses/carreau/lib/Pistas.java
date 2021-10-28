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
        String s = new String();
        //Pair<TipoPista, Pair<Integer, Integer>> p = getRandomCasilla();
        for (int i = 0; i < listaPistas.size(); i++) {
            Pair<TipoPista, Pair<Integer, Integer>> p = listaPistas.get(i);
            TipoPista tP = p.getLeft();
            Pair<Integer, Integer> pos = p.getRight();

            switch (tP) {
                case ValueReached:
                    s+= "This number can see all its dots " + pos.getLeft() + " " + pos.getRight()+ "\n"; // 1
                    break;
                case WouldExceed:
                    s+= "Looking further in one direction would exceed this number " + pos.getLeft() + " " + pos.getRight()+ "\n"; // 2
                    break;
                case OneDirectionRequired:
                    s+= "One specific dot is included in all solutions imaginable " + pos.getLeft() + " " + pos.getRight()+ "\n"; // 3
                    break;
                case ErrorClosedTooLate:
                    s+= "This number sees a bit too much " + pos.getLeft() + " " + pos.getRight()+ "\n"; // 4
                    break;
                case ErrorClosedTooEarly:
                    s+= "This number can't see enough" + pos.getLeft() + " " + pos.getRight()+ "\n"; // 5
                    break;
                case MustBeWall:
                    s+= "This one should be easy... " + pos.getLeft() + " " + pos.getRight()+ "\n"; //6.1
                    break;
                case LockedIn:
                    s+= "A blue dot should always see at least one other " + pos.getLeft() + " " + pos.getRight()+ "\n";//6.2
                    break;
                case ImposibleVision:
                    s+= "Imposible to fill the vision of this tile " + pos.getLeft() + " " + pos.getRight() + "\n"; // 10
                    break;
            }
        }
        return s;
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
