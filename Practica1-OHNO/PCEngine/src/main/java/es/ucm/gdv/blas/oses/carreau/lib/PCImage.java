package es.ucm.gdv.blas.oses.carreau.lib;

public class PCImage implements es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image {
    public java.awt.Image _image;

    public PCImage(java.awt.Image img){
        _image = img;
    }

    @Override
    public int getHeight() {
        return _image.getHeight(null);
    }

    @Override
    public int getWidth(){
        return _image.getWidth(null);
    }
}