package es.ucm.gdv.blas.oses.carreau.lib;


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

public class PCInput implements Input, MouseListener, MouseMotionListener {

    protected final Engine engine;

    private final List<TouchEvent> events;

    /**
     * Contructora del motor de gestion de entrada para
     * la implementacion de PC
     *
     * @param engine, Motor de juego
     */
    public PCInput(Engine engine) {
        events = new ArrayList<>();
        this.engine = engine;
    }

    /**
     * Metodo que devuelve la lista de eventos sin procesar
     *
     * @return Lista de eventos del tipo TouchEvent que se han ido almacenando
     * desde la ultima vez que cogimos la lista
     */
    @Override
    public final List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            if (events.size() > 0) {
                List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
                touchEvents.addAll(events);
                events.clear();
                return touchEvents;
            }
            return events;
        }
    }

    /**
     * Metodo heredado de la clase abstracta MouseListener
     * Se invoca cuando hay un click de raton. Cuando esto ocurre añadimos
     * un evento a nuestra lista de eventos
     *
     * @param mouseEvent, MouseEvent, evento de raton
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID();
        int[] aux = ((AbstractGraphics) engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x = aux[0];
        touchEvent.y = aux[1];
        synchronized (this) {
            events.add(touchEvent);
        }
    }

    /**
     * Metodo heredado de la clase abstracta MouseListener
     * Se invoca cuando hay pulsacion de raton. Cuando esto ocurre añadimos
     * un evento a nuestra lista de eventos
     *
     * @param mouseEvent, MouseEvent, evento de raton
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID();
        //touchEvent.pointer = 0;
        int[] aux = ((AbstractGraphics) engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x = aux[0];
        touchEvent.y = aux[1];
        synchronized (this) {
            events.add(touchEvent);
        }
    }

    /**
     * Metodo heredado de la clase abstracta MouseListener
     * Se invoca cuando se levanta la pulsacion de una tecla de raton.
     * Cuando esto ocurre añadimos el evento correspondiente (TOUCH_UP)
     * a nuestra lista de eventos
     *
     * @param mouseEvent, MouseEvent, evento de raton
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_UP;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID(); //TO DO: REVISAR SI ES CON GETID O SI ES 0
        //touchEvent.pointer = 0;
        int[] aux = ((AbstractGraphics) engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x = aux[0];
        touchEvent.y = aux[1];
        synchronized (this) {
            events.add(touchEvent);
        }
    }

    /**
     * Metodo heredado de MouseListener
     * No necesaria  su implementacion
     *
     * @param mouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    /**
     * Metodo heredado de MouseListener
     * No necesaria  su implementacion
     *
     * @param mouseEvent
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    /**
     * Metodo heredado de MouseMotionListener
     * Se invoca cuando se mueve el raton mientras mantienes clicado.
     * Cuando esto ocurre añadimos el evento correspondiente (TOUCH_DRAGGED)
     * a nuestra lista de eventos
     *
     * @param mouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID(); //TO DO: REVISAR SI ES CON GETID O SI ES 0
        //touchEvent.pointer = 0
        int[] aux = ((AbstractGraphics) engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x = aux[0];
        touchEvent.y = aux[1];
        synchronized (this) {
            events.add(touchEvent);
        }
    }

    /**
     * Metodo heredado de MouseMotionListener
     * No es necesaria su implementacion
     *
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
