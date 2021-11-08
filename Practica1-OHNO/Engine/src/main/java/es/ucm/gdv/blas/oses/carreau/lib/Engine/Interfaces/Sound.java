package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

public interface Sound {
    void play(float volume) ;
    void dispose();
}