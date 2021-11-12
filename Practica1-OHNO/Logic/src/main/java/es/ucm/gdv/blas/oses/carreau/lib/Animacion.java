package es.ucm.gdv.blas.oses.carreau.lib;
public class Animacion {
    public double  actTime;
    public double  lstTime;
    public boolean vibrate;

    public Animacion(double actTime,
             double lstTime,
             boolean vibrate){
        this.actTime = actTime;
        this.lstTime= lstTime;
        this.vibrate= vibrate;
    }
}
