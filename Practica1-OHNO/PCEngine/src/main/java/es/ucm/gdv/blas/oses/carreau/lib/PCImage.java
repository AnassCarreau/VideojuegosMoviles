package es.ucm.gdv.blasosescarreau.pcengine;

public class PCImage implements es.ucm.gdv.blasosescarreau.engine.Image {
    java.awt.Image _image;

    public Image(java.awt.Image img){
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