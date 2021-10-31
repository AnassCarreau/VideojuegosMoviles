package es.ucm.gdv.blas.oses.carreau.androidengine.Android;
import android.graphics.Typeface;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;

public class AndroidFont implements Font {
    protected Typeface _font;
    protected float _size;
    protected String _fontName;

    public AndroidFont(Typeface font, float size, String fontName) {
        this._font = font;
        this._size = size;
        this._fontName = fontName;
    }

    @Override
    public String getFontName() {
        return this._fontName;
    }

    @Override
    public float getFontSize() {
        return this._size;
    }

    @Override
    public boolean isBold() {
        return _font.isBold();
    }


}