package es.ucm.gdv.blas.oses.carreau.androidengine.Android;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Pool;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

public class AndroidInput implements OnTouchListener, Input {
   // AccelerometerHandler accelHandler;
    boolean isTouched;
    int touchX;
    int touchY;
    //Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
       // accelHandler = new AccelerometerHandler(context);
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        //touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public boolean isTouchDown(int pointer) {
        synchronized(this) {
            if(pointer == 0)
                return isTouched;
            else
                return false;
        }
    }
    public int getTouchX(int pointer) {
        synchronized(this) {
            return touchX;
        }
    }
    public int getTouchY(int pointer) {
        synchronized(this) {
            return touchY;
        }
    }

    public List<TouchEvent> getTouchEvents() {
        synchronized(this) {
            int len = touchEvents.size();
            for( int i = 0; i < len; i++ )
                //touchEventPool.free(touchEvents.get(i));
                touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        synchronized(this) {
            //TouchEvent touchEvent = touchEventPool.newObject();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //touchEvent.type = TouchEvent.TOUCH_DOWN;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    //touchEvent.type = TouchEvent.TOUCH_UP;
                    isTouched = false;
                    break;
            }
            //touchEvent.x = touchX = (int)(event.getX() * scaleX);
            //touchEvent.y = touchY = (int)(event.getY() * scaleY);
            //touchEventsBuffer.add(touchEvent);
            return true;
        }
    }
}