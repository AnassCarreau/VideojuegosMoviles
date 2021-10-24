package es.ucm.gdv.blas.oses.carreau.lib;

import java.util.ArrayList;
import java.util.List;

public class Celda{

    public Celda(){
        _estadoActual = EstadoCelda.Vacia;
        _modificable = true;
        _valorDefault = 0;
        _vistasAct = 0;
        ady = new ArrayList<>();
    }

    public Celda(EstadoCelda est, boolean mod, int vistasDef, int vistasAct){
        _estadoActual = est;
        _modificable = mod;
        _valorDefault = vistasDef;
        _vistasAct = vistasAct;
        ady = new ArrayList<>();
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

    public void setModificable(boolean isMod){_modificable = isMod;}

    public int getCurrentVisibles(){
        return _vistasAct;
    }

    public int getValorDefault(){
        return _valorDefault;
    }
    public void setValorDefault(int def){ _valorDefault = def; }

    public void setCurrentVisibles(int curr){
        _vistasAct = curr;
    }

    public void addCeldaAdy(Celda c){ ady.add(c);}

    private EstadoCelda _estadoActual;
    private boolean _modificable;
    private int _valorDefault;
    private int _vistasAct;
    private List<Celda> ady;

}