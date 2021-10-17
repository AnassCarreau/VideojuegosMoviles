package es.ucm.gdv.blas.oses.carreau.androidengine.Android;
import android.graphics.Bitmap;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;

public class AndroidImage implements Image{
    Bitmap bitmap;
    //PixmapFormat format;

    public AndroidImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        //this.format = format;
    }
    public int getWidth() {
        return bitmap.getWidth();
    }
    public int getHeight() {
        return bitmap.getHeight();
    }
    public void dispose() {
        bitmap.recycle();
    }
}