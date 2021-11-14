package es.ucm.gdv.blas.oses.carreau.lib;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pistas {

    //Lista de pista cuya clase StructPista contiene el tipo de pista, su posicion y direccion si
    //aplica
    private final List<StructPista> listaPistas;

    /**
     *  Constructora de la clase pista, inicializa la lista de pistas
     */
    public Pistas() {
        listaPistas = new ArrayList<>();
    }

    /**
     * Metodo para añadir una pista de una celda en concreto a nuestra lista de pistas
     * @param pista, StructPista, pista a añadir a la lista
     */
    public void addPista(StructPista pista) {
        listaPistas.add(pista);
    }

    /**
     * Metodo para comprobar si la lista de pistas esta vacia
     * @return boolean, true si esta vacia, false en caso contrario
     */
    public boolean isEmpty() {
        return listaPistas.isEmpty();
    }

    /**
     * Metodo para obtener el mensaje de una pista de la lista de pistas y mostrarlo por
     * pantalla
     * @return String con el texto a mostar y un vector con las coordenadas de la celda en el tablero
     */
    public Pair<String, Vector> getPistaTablero() {
        String s = "";
        StructPista p = getRandomCasilla();

        if (p == null) return null;

        TipoPista tP = p.getTipoPista();
        Vector pos = p.getPosPista();

        switch (tP) {
            case ValueReached:
                s += "This number can see all #its dots " + "\n"; // 1
                break;
            case WouldExceed:
                s += "Looking further in one direction #would exceed this number " + "\n"; // 2
                break;
            case OneDirectionRequired:
                s += "One specific dot is included #in all solutions imaginable " + "\n"; // 3
                break;
            case ErrorClosedTooLate:
                s += "This number sees #a bit too much " + "\n"; // 4
                break;
            case ErrorClosedTooEarly:
                s += "This number can't see #enough" + "\n"; // 5
                break;
            case MustBeWall:
                s += "This one should #be easy... " + "\n"; //6.1
                break;
            case LockedIn:
                s += "A blue dot should always #see at least one other " + "\n";//6.2
                break;
            case ImposibleVision:
                s += "Imposible to fill the vision #of this tile " + "\n"; // 10
                break;
        }

        return new Pair<>(s, pos);
    }

    /**
     * Devuelve una pista aleatoria desde la lista de pistas
     * @return pista de la lista de pistas
     */
    private StructPista getRandomCasilla() {
        Random r = new Random();
        if (listaPistas.size() > 0) return listaPistas.get(r.nextInt(listaPistas.size()));
        else return null;
    }

    /**
     * Metodo que devuelve la primera pista de la lista de pistas sin sacarla de la misma
     * @return
     */
    public StructPista getFirstPista() {
        if (listaPistas.isEmpty()) return null;
        else return listaPistas.get(0);
    }

    /**
     * Metodo para devolver la lista de pistas
     * @return List<StructPista>, la lista con las pistas
     */
    public List<StructPista> getListaPistas() {
        return listaPistas;
    }
}
