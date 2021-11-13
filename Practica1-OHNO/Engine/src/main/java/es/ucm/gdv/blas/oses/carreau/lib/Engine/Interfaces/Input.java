package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

import java.util.List;

public interface Input {
    /**
     * Metodo que devuelve la lista de eventos sin procesar
     *
     * @return Lista de eventos del tipo TouchEvent que se han ido almacenando
     * desde la ultima vez que cogimos la lista
     */
    List<TouchEvent> getTouchEvents();

    /**
     * Clase estatica que representa un evento de toque en nuestros juegos
     * Contiene informacion sobre el tipo de evento, type
     * Las coordenadas en el espacio fisico en donde ha ocurrido una pulsacion, x & y
     * Identificador del puntero que ha realizado dicho toque, pointer
     */
    static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public int type;
        public int x, y;
        public int pointer;
    }
}