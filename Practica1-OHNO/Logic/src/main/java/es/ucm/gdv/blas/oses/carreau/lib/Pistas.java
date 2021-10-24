package es.ucm.gdv.blas.oses.carreau.lib;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import  es.ucm.gdv.blas.oses.carreau.lib.Celda;

public class Pistas {
    public enum tipoPista{
        VisionCompleta, SobreVision, PonerAzul, FaltaVision, Encerrada, VaciaIncomunicada, ErrorUsuario
    }

    private List<Pair<tipoPista, Pair<Integer, Integer>>> listaPistas;
    private List< Pair <Celda,Pair<Integer, Integer>>> casillas;

    public Pistas(){
        listaPistas = new ArrayList<>();
        casillas = new ArrayList<>();
    }

    /**
     * Metodo que añade una pista cuando el jugador ha hecho algo mal y guarda la posicion de la que
     * tiene que dar la pista.
     */
    public void addPista(tipoPista l, int posX, int posY){
        listaPistas.add(new Pair(l, new Pair(posX, posY)));
    }

    public String getPistaTablero() {
        Pair<tipoPista, Pair<Integer, Integer>> p = getRandomCasilla();
        tipoPista tP = p.getLeft();
        Pair<Integer, Integer> pos = p.getRight();
        switch (tP){
            case VisionCompleta:
                return "La vision de la casilla " + pos.getLeft() + " " + pos.getRight() + " esta completa, cierrala!";
            case FaltaVision:
                return "La vision de la casilla " + pos.getLeft() + " " + pos.getRight() + " es insuficiente";
            case SobreVision:
                return "La vision de la casilla " + pos.getLeft() + " " + pos.getRight() + " es demasiado alta.";
            case Encerrada:
                return "La casilla " + pos.getLeft() + " " + pos.getRight() + " esta encerrada, ¿que color tienes que poner?";
            case VaciaIncomunicada:
                return "La casilla " + pos.getLeft() + " " + pos.getRight() + " esta vacia y no ve a ninguna azul por lo que es pared";
            case ErrorUsuario:
                return "La casilla " + pos.getLeft() + " " + pos.getRight() + " no debería ser de este color";
        }
        return "" ;
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

    private  Pair<tipoPista, Pair<Integer, Integer>> getRandomCasilla()
    {
        Random r= new Random();
        return listaPistas.get(r.nextInt(listaPistas.size()));
    }


}
