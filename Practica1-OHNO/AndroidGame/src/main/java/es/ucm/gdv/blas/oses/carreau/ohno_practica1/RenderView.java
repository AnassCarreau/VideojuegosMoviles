package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

class RenderView extends View {
    public RenderView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // to be implemented
        canvas.drawRGB(255, 255, 255);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawLine(0, 0, canvas.getWidth()-1, canvas.getHeight()-1, paint);
        invalidate();
    }
}
