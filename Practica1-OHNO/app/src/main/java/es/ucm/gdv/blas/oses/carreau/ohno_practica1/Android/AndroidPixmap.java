package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Android;
import android.graphics.Bitmap;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.PixmapFormat;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Pixmap;
public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }
    public int getWidth() {
        return bitmap.getWidth();
    }
    public int getHeight() {
        return bitmap.getHeight();
    }
    public PixmapFormat getFormat() {
        return format;
    }
    public void dispose() {
        bitmap.recycle();
    }
}