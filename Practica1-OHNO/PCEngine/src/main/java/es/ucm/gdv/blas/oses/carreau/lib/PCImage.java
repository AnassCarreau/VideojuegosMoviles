package es.ucm.gdv.blas.oses.carreau.lib;

public class PCImage implements es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image {
    public java.awt.Image _image;

    /**
     * Construye un objeto de tipo imagen especifico para la implementacion en PC
     *
     * @param img, imagen especifica de PC, usando un objeto de la clase java.awt.Image
     */
    public PCImage(java.awt.Image img) {
        _image = img;
    }

    /**
     * Metodo que devuelve el ancho de la imagen
     *
     * @return int, ancho de la imagen
     */
    @Override
    public int getWidth() {
        return _image.getWidth(null);
    }

    /**
     * Metodo que devuelve el alto de la imagen
     *
     * @return int, alto de la imagen
     */
    @Override
    public int getHeight() {
        return _image.getHeight(null);
    }


}