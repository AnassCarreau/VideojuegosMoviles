package es.ucm.gdv.blas.oses.carreau.lib;

public class Celda {

    private EstadoCelda _estadoActual;
    private boolean _modificable;
    private int _valorDefault;
    private int _vistasAct;
    private StructPista pista = null;

    /**
     * Constructora por defecto de la clase Celda que representa
     * una casilla del tablero
     */
    public Celda() {
        _estadoActual = EstadoCelda.Vacia;
        _modificable = true;
        _valorDefault = 0;
        _vistasAct = 0;
    }

    /**
     * Devuelve el estado de la celda: Azul, Roja o Vacia
     * @return EstadoCelda, estado actual
     */
    public EstadoCelda getEstado() {
        return _estadoActual;
    }

    /**
     * Setter del estado de la celda
     * @param newEstado, el nuevo estado de la celda
     */
    public void setEstado(EstadoCelda newEstado) {
        _estadoActual = newEstado;
    }

    /**
     * Metodo para comprobar si la celda es modificable o no
     * @return boolean, si la celda puede ser modificada por el usuario
     */
    public boolean isModifiable() {
        return _modificable;
    }

    /**
     * Setter para determinar si la celda es modificable o no
     * @param isMod, boolean.
     */
    public void setModificable(boolean isMod) {
        _modificable = isMod;
    }

    /**
     * Devuelve el numero de azules visibles por la celda actualmente (si la
     * celda es Azul)
     * @return, int, numero de azules adyacentes inmediatas que esta viendo la celda
     */
    public int getCurrentVisibles() {
        return _vistasAct;
    }

    /**
     * Metodo para devolver el numero de celdas azules adyacentes que deberíamos
     * estar viendo (Azules no modificables)
     * @return, int, numero de celdas Azules que deberiamos estar viendo desde la nuestra
     */
    public int getValorDefault() {
        return _valorDefault;
    }

    /**
     * Metodo para establecer el valor por defecto (Azules no modificables) de celdas azules
     * Adyacentes que deberia estar viendo esta celda
     * @param def
     */
    public void setValorDefault(int def) {
        _valorDefault = def;
    }

    /**
     * Metodo para establecer el valor de celdas azules adyacentes que actualmente esta
     * viendo esta celda
     * @param curr
     */
    public void setCurrentVisibles(int curr) {
        _vistasAct = curr;
    }

    /**
     * Metodo que devuelve, si hay, la pista asociada a una celda del tablero
     * @return StructPista, pista asociada a esta celda
     */
    public StructPista getPista() {
        return pista;
    }

    /**
     * Metodo para actualizar la pista asociada a esta celda en el tablero de juego
     * @param curr, pista asociada a esta casilla
     */
    public void setCurrentPista(StructPista curr) {
        pista = curr;
    }

    /**
     * Metodo para reestablecer la celda a sus valores por defecto
     */
    public void resetCelda() {
        _estadoActual = EstadoCelda.Vacia;
        _modificable = true;
        _valorDefault = 0;
        _vistasAct = 0;
        pista = null;
    }
}