package es.ucm.gdv.blas.oses.carreau.lib;

public class Celda{

    public Celda(){
        _estadoActual = EstadoCelda.Vacia;
        _modificable = true;
        _vistasDef = 0;
        _vistasAct = 0;
    }

    public Celda(EstadoCelda est, boolean mod, int vistasDef, int vistasAct){
        _estadoActual = est;
        _modificable = mod;
        _vistasDef = vistasDef;
        _vistasAct = vistasAct;
    }

    public EstadoCelda getEstado(){
        return _estadoActual;
    }

    public void setEstado(EstadoCelda newEstado){
        _estadoActual = newEstado;
    }

    public boolean isModifiable(){
        return _modificable;
    }

    public int getCurrentVisibles(){
        return _vistasAct;
    }

    public int getDefVisibles(){
        return _vistasDef;
    }

    public void setCurrentVisibles(int curr){
        _vistasAct = curr;
    }

    EstadoCelda _estadoActual;
    boolean _modificable;
    int _vistasDef;
    int _vistasAct;


}