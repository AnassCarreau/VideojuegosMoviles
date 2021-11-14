package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

public class AndroidInput implements View.OnTouchListener, Input {

    private final List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    private final AbstractGraphics graphics;
    private final int MAX_ID_PERMITTED = 10;

    //Array auxiliar para no añadir dos veces eventos con el mismo identificador en
    //la lista de eventos
    private boolean [] indexProcessed = new boolean [MAX_ID_PERMITTED];


    /**
     * Contructora del motor de gestion de entrada para
     * la implementacion de Android
     *
     * @param engine, Motor de juego
     * @param view,   renderView
     */
    public AndroidInput(Engine engine, View view) {
        this.graphics = (AbstractGraphics) (engine.getGraphics());
        view.setOnTouchListener(this);
        Arrays.fill(indexProcessed, Boolean.FALSE);
    }

    /**
     * Metodo que devuelve la lista de eventos sin procesar
     *
     * @return Lista de eventos del tipo TouchEvent que se han ido almacenando
     * desde la ultima vez que cogimos la lista
     */
    @Override
    public List<TouchEvent> getTouchEvents() {
        List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();

        synchronized (touchEventsBuffer) {
            if (touchEventsBuffer.size() > 0) {
                touchEvents.addAll(touchEventsBuffer);
                touchEventsBuffer.clear();
                Arrays.fill(indexProcessed, Boolean.FALSE);
            }
        }
        return touchEvents;

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
    public boolean onTouch(View v, MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    addEvent(TouchEvent.TOUCH_DOWN, event);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    addEvent(TouchEvent.TOUCH_DRAGGED, event);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {
                    addEvent(TouchEvent.TOUCH_UP, event);
                    break;
                }
            }
        }

        return true;
    }

    /**
     * Metodo para añadir un evento del tipo eventType a nuestra lista de eventos
     * Necesario que este sincronizado cuando añadimos el evento a la lista para
     * asegurarnos que no hay otra hebra modificandola
     * @param eventType, int, tipo de evento
     * @param event, MotionEvent, informacion del evento
     */
    private void addEvent(int eventType, MotionEvent event) {
        TouchEvent touchEvent = new TouchEvent();

        touchEvent.pointer = event.getPointerId(event.getActionIndex());
        if(!indexProcessed[touchEvent.pointer]) {
            touchEvent.type = eventType;
            int[] pos = graphics.physicalToLogical((int) event.getX(event.getActionIndex()), (int) event.getY(event.getActionIndex()));
            touchEvent.x = pos[0];
            touchEvent.y = pos[1];

            synchronized (touchEventsBuffer) {
                indexProcessed[touchEvent.pointer] = true;
                touchEventsBuffer.add(touchEvent);
            }
        }
    }
}