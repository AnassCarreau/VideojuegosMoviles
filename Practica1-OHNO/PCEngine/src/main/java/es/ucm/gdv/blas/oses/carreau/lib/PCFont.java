package es.ucm.gdv.blas.oses.carreau.lib;
import java.awt.Font;

class PCFont implements es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font {
    protected Font _font;


    public PCFont(Font font, int size) {
        this._font = font;
    }

    @Override
    public String getFontName() {
        return _font.getName();
    }

    @Override
    public int getFontSize() {
        return _font.getSize();
    }

    @Override
    public boolean isBold() {
        return _font.isBold();
    }
}
