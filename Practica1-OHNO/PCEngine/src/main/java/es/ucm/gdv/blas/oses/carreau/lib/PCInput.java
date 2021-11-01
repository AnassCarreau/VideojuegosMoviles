package es.ucm.gdv.blas.oses.carreau.lib;


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
//import es.ucm.gdv.blas.oses.carreau.lib.MouseInput;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

public class PCInput implements Input, MouseListener, MouseMotionListener{

    protected Engine engine;

    private List<TouchEvent> events;
    //private MouseInput mouseInput;

    public PCInput(Engine engine){
        //this.mouseInput = new MouseInput();
        events = new ArrayList<>();
        this.engine=engine;
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized(this) {
            if(events.size() > 0){
                List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
                /*int len = touchEvents.size();
                for( int i = 0; i < len; i++ )
                    //touchEventPool.free(touchEvents.get(i));*/
                //touchEvents.clear();
                touchEvents.addAll(events);
                events.clear();
                return touchEvents;
            }
            return events;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("he clickado papa?");
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID(); //TO DO: REVISAR SI ES CON GETID O SI ES 0
        //touchEvent.pointer = 0;
        int[]aux=((AbstractGraphics)engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x=aux[0];
        touchEvent.y=aux[1];
        synchronized (this){
            events.add(touchEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        System.out.println("he presionado papa?");
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID(); //TO DO: REVISAR SI ES CON GETID O SI ES 0
        //touchEvent.pointer = 0;
        int[]aux=((AbstractGraphics)engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x=aux[0];
        touchEvent.y=aux[1];
        synchronized (this){
            events.add(touchEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        System.out.println("he soltado papa?");
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_UP;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID(); //TO DO: REVISAR SI ES CON GETID O SI ES 0
        //touchEvent.pointer = 0;
        int[]aux=((AbstractGraphics)engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x=aux[0];
        touchEvent.y=aux[1];
        synchronized (this){
            events.add(touchEvent);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
        touchEvent.x = mouseEvent.getX();
        touchEvent.y = mouseEvent.getY();
        touchEvent.pointer = mouseEvent.getID(); //TO DO: REVISAR SI ES CON GETID O SI ES 0
        //touchEvent.pointer = 0
        int[]aux=((AbstractGraphics)engine.getGraphics()).physicalToLogical(touchEvent.x, touchEvent.y);
        touchEvent.x=aux[0];
        touchEvent.y=aux[1];
        synchronized (this){
            events.add(touchEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
