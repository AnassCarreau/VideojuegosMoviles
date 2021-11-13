package es.ucm.gdv.blas.oses.carreau.lib;

public class StructPista {
    private TipoPista tipoPista;
    private Vector posPista;
    private Vector dirPista;

    public StructPista(TipoPista tp, Vector pos, Vector dir) {
        this.tipoPista = tp;
        this.posPista = pos;
        this.dirPista = dir;
    }

    public TipoPista getTipoPista() {
        return tipoPista;
    }

    public Vector getPosPista() {
        return posPista;
    }

    public Vector getDirPista() {
        return dirPista;
    }
}
