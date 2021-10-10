package es.ucm.gdv.blas.oses.carreau.ohno_practica1.InputClasses;


import java.util.List;
import android.view.View.OnTouchListener;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<TouchEvent> getTouchEvents();
}
