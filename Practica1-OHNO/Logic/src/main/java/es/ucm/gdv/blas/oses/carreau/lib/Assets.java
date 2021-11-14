package es.ucm.gdv.blas.oses.carreau.lib;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;

/**
 * Clase con los recursos graficos, tipograficos y sonoros que vamos a tener
 * en nuestro juego. Puesto que solo vamos a querer que haya una unica instancia de estos
 * mismos pero la emplearemos para pintar/escribir multiples veces son estaticos
 */
public class Assets {
    public static Image q42;
    public static Image lock;
    public static Image history;
    public static Image eye;
    public static Image close;
    public static Font josefisans;
    public static Font molleregular;
    public static Sound click;
    public static Sound ganar;

}
