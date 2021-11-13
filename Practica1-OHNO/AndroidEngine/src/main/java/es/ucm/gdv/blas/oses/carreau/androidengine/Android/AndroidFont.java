package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.graphics.Typeface;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;

public class AndroidFont implements Font {
    protected Typeface _font;
    protected float _size;
    protected String _fontName;

    /**
     * Construye un objeto de tipo fuente especifico para la implementacion en Android
     *
     * @param font, tipo de fuente, especifico de Android, usando un objeto de la clase Typeface
     */
    public AndroidFont(Typeface font, float size, String fontName) {
        this._font = font;
        this._size = size;
        this._fontName = fontName;
    }

    /**
     * Metodo para saber si la fuente esta en negrita o no
     *
     * @return boolean, true si la fuente esta en negrita, false si no
     */
    @Override
    public boolean isBold() {
        return _font.isBold();
    }


}