package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

public interface Sound {
    /**
     * Metodo que reproduce un sonido con determinado volumen
     * @param volume, float, volumen del juego
     */
    void play(float volume);
    /**
     * Metodo que libera el sonido
     */
    void dispose();
}