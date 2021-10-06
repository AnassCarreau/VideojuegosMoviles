package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Android;
import java.util.List;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Input;
public class AndroidInput implements Input {
   // AccelerometerHandler accelHandler;
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;
    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
       // accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);
        touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }
   /* public float getAccelX() {
        return accelHandler.getAccelX();
    }
    public float getAccelY() {
        return accelHandler.getAccelY();
    }
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }*/
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}