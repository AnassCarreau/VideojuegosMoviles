package es.ucm.gdv.blas.oses.carreau.lib;

public class Celda {

    private EstadoCelda _estadoActual;
    private boolean _modificable;
    private int _valorDefault;
    private int _vistasAct;
    private StructPista pista = null;

    public Celda() {
        _estadoActual = EstadoCelda.Vacia;
        _modificable = true;
        _valorDefault = 0;
        _vistasAct = 0;
    }

    public EstadoCelda getEstado() {
        return _estadoActual;
    }

    public void setEstado(EstadoCelda newEstado) {
        _estadoActual = newEstado;
    }

    public boolean isModifiable() {
        return _modificable;
    }

    public void setModificable(boolean isMod) {
        _modificable = isMod;
    }

    public int getCurrentVisibles() {
        return _vistasAct;
    }

    public int getValorDefault() {
        return _valorDefault;
    }

    public void setValorDefault(int def) {
        _valorDefault = def;
    }

    public void setCurrentVisibles(int curr) {
        _vistasAct = curr;
    }

    public StructPista getPista() {
        return pista;
    }

    public void setCurrentPista(StructPista curr) {
        pista = curr;
    }

    public void resetCelda() {
        _estadoActual = EstadoCelda.Vacia;
        _modificable = true;
        _valorDefault = 0;
        _vistasAct = 0;
    }
}