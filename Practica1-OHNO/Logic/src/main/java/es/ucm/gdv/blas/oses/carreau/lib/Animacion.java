package es.ucm.gdv.blas.oses.carreau.lib;
public class Animacion {
    public double  actTime;
    public double  lstTime;
    public boolean fade;

    public Animacion(double actTime,
             double lstTime,
             boolean fade){
        this.actTime = actTime;
        this.lstTime= lstTime;
        this.fade= fade;
    }
}
