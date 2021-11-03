package es.ucm.gdv.blas.oses.carreau.androidengine.Android;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Pool;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

public class AndroidInput implements View.OnTouchListener, Input {
    boolean isTouched;
    //Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    Engine engine;
    AbstractGraphics graphics;

    public AndroidInput(Engine engine,  View view) {

        this.engine=engine;
        this.graphics = (AbstractGraphics) (engine.getGraphics());
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        //touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);

    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized(this) {
            if(touchEventsBuffer.size() > 0){
                List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
                /*int len = touchEvents.size();
                for( int i = 0; i < len; i++ )
                    //touchEventPool.free(touchEvents.get(i));*/
                //touchEvents.clear();
                touchEvents.addAll(touchEventsBuffer);
                touchEventsBuffer.clear();
                return touchEvents;
            }
            return touchEventsBuffer;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        synchronized(this) {
            TouchEvent touchEvent = new TouchEvent();
            for (int i = 0; i < event.getPointerCount(); i++){
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
                int[] pos = graphics.physicalToLogical((int)event.getX(i), (int)event.getY(i));
                touchEvent.x = pos[0];
                touchEvent.y = pos[1];
                synchronized (this) {
                    touchEventsBuffer.add(touchEvent);
                }
            }
            //touchEvent.x = touchX = (int)(event.getX() * scaleX);
            //touchEvent.y = touchY = (int)(event.getY() * scaleY);
            //touchEventsBuffer.add(touchEvent);
            return true;
        }
    }
}