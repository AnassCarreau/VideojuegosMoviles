package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

public class AndroidInput implements View.OnTouchListener, Input {
    //TODO para que es este booleano??
    boolean isTouched;

    private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    private Engine engine;
    private AbstractGraphics graphics;

    /**
     * Contructora del motor de gestion de entrada para
     * la implementacion de Android
     *
     * @param engine, Motor de juego
     * @param view,   renderView
     */
    public AndroidInput(Engine engine, View view) {
        this.engine = engine;
        this.graphics = (AbstractGraphics) (engine.getGraphics());
        view.setOnTouchListener(this);
    }

    /**
     * Metodo que devuelve la lista de eventos sin procesar
     *
     * @return Lista de eventos del tipo TouchEvent que se han ido almacenando
     * desde la ultima vez que cogimos la lista
     */
    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            if (touchEventsBuffer.size() > 0) {
                List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
                touchEvents.addAll(touchEventsBuffer);
                touchEventsBuffer.clear();
                return touchEvents;
            }
            return touchEventsBuffer;
        }
    }

    /**
     * Metodo heredado de Viewer.OnTouchListener.
     * Cuando recibimos un toque se llama a este metodo y por
     * tanto añadimos un nuevo evento a nuestra lista
     *
     * @param v,     renderView, de quien estamos escuchando eventos
     * @param event, MotionEvent, evento de haber tocado la pantalla
     * @return
     */
    //TODO REVISAR EL SYNCHRONIZED PORQUE IGUAL SOLO HACE FALTA AL AÑADIR EL EVENTO
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            TouchEvent touchEvent = new TouchEvent();
            for (int i = 0; i < event.getPointerCount(); i++) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchEvent.type = TouchEvent.TOUCH_DOWN;
                        isTouched = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                        isTouched = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        touchEvent.type = TouchEvent.TOUCH_UP;
                        isTouched = false;
                        break;
                }
                touchEvent.pointer = event.getPointerId(i);
                int[] pos = graphics.physicalToLogical((int) event.getX(i), (int) event.getY(i));
                touchEvent.x = pos[0];
                touchEvent.y = pos[1];
                touchEventsBuffer.add(touchEvent);
                /*synchronized (this) {
                    touchEventsBuffer.add(touchEvent);
                }*/
            }
            return true;
        }
    }
}