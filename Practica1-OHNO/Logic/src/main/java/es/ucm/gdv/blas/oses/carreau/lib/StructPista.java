package es.ucm.gdv.blas.oses.carreau.lib;

/**
 * Clase auxiliar que nos sirve para saber en que celda se ejecuta una pista
 * esa pista de que tipo en concreto es y si esa pista debe aplicarse en una direccion concreta
 */
public class StructPista {
    private final TipoPista tipoPista;
    private final Vector posPista;
    private final Vector dirPista;

    /**
     * Constructora de la clase StructPista
     * @param tp, enum TipoPista que representa el tipo de pista
     * @param pos, Vector, posicion en el tablero de la celda que da dicha pista
     * @param dir, Vector, direccion, si la hay, en la que es necesaria aplicar la pista
     */
    public StructPista(TipoPista tp, Vector pos, Vector dir) {
        this.tipoPista = tp;
        this.posPista = pos;
        this.dirPista = dir;
    }

    /**
     * Getter del tipo de pista
     * @return TipoPista, el tipo de pista
     */
    public TipoPista getTipoPista() {
        return tipoPista;
    }

    /**
     * Getter de la Posicion de la Celda en el tablero en la que se aplica la pista
     * @return
     */
    public Vector getPosPista() {
        return posPista;
    }

    /**
     * Getter de la direccion, si la hay, hacia la que hay que aplicar la pista
     * @return
     */
    public Vector getDirPista() {
        return dirPista;
    }
}
