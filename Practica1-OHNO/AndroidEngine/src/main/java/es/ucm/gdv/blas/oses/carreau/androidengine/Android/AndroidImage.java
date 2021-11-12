package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.graphics.Bitmap;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;

public class AndroidImage implements Image{
    Bitmap bitmap;

    /**
     * Construye un objeto de tipo imagen especifico para la implementacion en Android
     * @param bitmap, imagen especifica de Android
     */
    public AndroidImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Metodo que devuelve el ancho de la imagen
     * @return int, ancho de la imagen
     */
    public int getWidth() {
        return bitmap.getWidth();
    }

    /**
     * Metodo que devuelve el alto de la imagen
     * @return int, alto de la imagen
     */
    public int getHeight() {
        return bitmap.getHeight();
    }
}