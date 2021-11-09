package es.ucm.gdv.blas.oses.carreau.lib;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import  es.ucm.gdv.blas.oses.carreau.lib.Celda;

public class Pistas {

    //Lista de pista cuya clase StructPista contiene el tipo de pista, su posicion y direccion si
    //aplica
    private List<StructPista> listaPistas;

    public Pistas(){
        listaPistas = new ArrayList<>();
    }

    /**
     * Metodo que añade una pista cuando el jugador ha hecho algo mal y guarda la posicion de la que
     * tiene que dar la pista.
     */
    public void addPista(StructPista pista){
        listaPistas.add(pista);
    }

    public boolean isEmpty(){
        return listaPistas.isEmpty();
    }

    public String getPistaTablero() {
        String s = new String();
        StructPista p = getRandomCasilla();

        if(p == null) return s;

        TipoPista tP = p.getTipoPista();
        Pair<Integer, Integer> pos = p.getPosPista();

        switch (tP) {
            case ValueReached:
                s += "This number can see all #its dots " + pos.getLeft() + " " + pos.getRight() + "\n"; // 1
                break;
            case WouldExceed:
                s += "Looking further in one direction #would exceed this number " + pos.getLeft() + " " + pos.getRight() + "\n"; // 2
                break;
            case OneDirectionRequired:
                s += "One specific dot is included #in all solutions imaginable " + pos.getLeft() + " " + pos.getRight() + "\n"; // 3
                break;
            case ErrorClosedTooLate:
                s += "This number sees #a bit too much " + pos.getLeft() + " " + pos.getRight() + "\n"; // 4
                break;
            case ErrorClosedTooEarly:
                s += "This number can't see #enough" + pos.getLeft() + " " + pos.getRight() + "\n"; // 5
                break;
            case MustBeWall:
                s += "This one should #be easy... " + pos.getLeft() + " " + pos.getRight() + "\n"; //6.1
                break;
            case LockedIn:
                s += "A blue dot should always #see at least one other " + pos.getLeft() + " " + pos.getRight() + "\n";//6.2
                break;
            case ImposibleVision:
                s += "Imposible to fill the vision #of this tile " + pos.getLeft() + " " + pos.getRight() + "\n"; // 10
                break;
        }

        return s;
    }

    private StructPista getRandomCasilla()
    {
        Random r= new Random();
        if(listaPistas.size() > 0) return listaPistas.get(r.nextInt(listaPistas.size()));
        else return null;
    }

    public StructPista getFirstPista(){
        if(listaPistas.isEmpty()) return null;
        else return listaPistas.get(0);
    }
}
