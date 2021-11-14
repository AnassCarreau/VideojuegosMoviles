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
        synchronized (this) {
            if (touchEventsBuffer.size() > 0) {
                List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();

                touchEvents.addAll(touchEventsBuffer);
                touchEventsBuffer.clear();
                Arrays.fill(indexProcessed, Boolean.FALSE);
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
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        touchEvent.type = TouchEvent.TOUCH_DOWN;
                        touchEvent.pointer = event.getPointerId(event.getActionIndex());
                        if(!indexProcessed[touchEvent.pointer]) {
                            indexProcessed[touchEvent.pointer] = true;
                            int[] pos = graphics.physicalToLogical((int) event.getX(event.getActionIndex()), (int) event.getY(event.getActionIndex()));
                            touchEvent.x = pos[0];
                            touchEvent.y = pos[1];
                            touchEventsBuffer.add(touchEvent);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = event.getPointerId(event.getActionIndex());
                        if(!indexProcessed[touchEvent.pointer]) {
                            indexProcessed[touchEvent.pointer] = true;
                            int[] pos = graphics.physicalToLogical((int) event.getX(event.getActionIndex()), (int) event.getY(event.getActionIndex()));
                            touchEvent.x = pos[0];
                            touchEvent.y = pos[1];
                            touchEventsBuffer.add(touchEvent);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP: {
                        touchEvent.type = TouchEvent.TOUCH_UP;
                        touchEvent.pointer = event.getPointerId(event.getActionIndex());
                        if(!indexProcessed[touchEvent.pointer]) {
                            indexProcessed[touchEvent.pointer] = true;
                            int[] pos = graphics.physicalToLogical((int) event.getX(event.getActionIndex()), (int) event.getY(event.getActionIndex()));
                            touchEvent.x = pos[0];
                            touchEvent.y = pos[1];
                            touchEventsBuffer.add(touchEvent);
                        }
                        break;
                    }
                }

            }
            return true;
        }
    }
}