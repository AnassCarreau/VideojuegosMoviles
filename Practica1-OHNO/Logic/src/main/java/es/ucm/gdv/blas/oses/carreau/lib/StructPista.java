package es.ucm.gdv.blas.oses.carreau.lib;

public class StructPista {
    private TipoPista tipoPista;
    private Pair<Integer, Integer> posPista;
    private Pair<Integer, Integer> dirPista;

    public StructPista(TipoPista tp, Pair<Integer, Integer> pos, Pair<Integer, Integer> dir){
        this.tipoPista = tp;
        this.posPista = pos;
        this.dirPista = dir;
    }

    public TipoPista getTipoPista(){
        return tipoPista;
    }

    public Pair<Integer, Integer> getPosPista(){
        return posPista;
    }

    public Pair<Integer, Integer> getDirPista(){
        return dirPista;
    }
}
