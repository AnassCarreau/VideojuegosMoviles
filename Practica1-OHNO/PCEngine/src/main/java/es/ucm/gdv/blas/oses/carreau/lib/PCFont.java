package es.ucm.gdv.blas.oses.carreau.lib;

import java.awt.Font;

class PCFont implements es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font {
    protected Font _font;

    /**
     * Construye un objeto de tipo fuente especifico para la implementacion en PC
     *
     * @param font, tipo de fuente, especifico de PC, usando un objeto de la clase java.awt.Font
     */
    public PCFont(Font font) {
        this._font = font;
    }

    /**
     * Metodo para saber si la fuente esta en negrita o no
     *
     * @return boolean, true si la fuente esta en negrita, false si no
     */
    @Override
    public final boolean isBold() {
        return _font.isBold();
    }
}
