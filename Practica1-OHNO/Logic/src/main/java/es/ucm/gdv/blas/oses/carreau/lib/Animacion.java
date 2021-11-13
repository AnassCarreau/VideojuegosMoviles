package es.ucm.gdv.blas.oses.carreau.lib;

public class Animacion {
    public final double actTime;
    public final double lstTime;
    public final boolean vibrate;

    public Animacion(double actTime,
                     double lstTime,
                     boolean vibrate) {
        this.actTime = actTime;
        this.lstTime = lstTime;
        this.vibrate = vibrate;
    }
}
