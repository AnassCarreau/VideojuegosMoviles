package es.ucm.gdv.blas.oses.carreau.lib;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;


public class PCSound implements Sound {

    private javax.sound.sampled.Clip clip;
    public PCSound(javax.sound.sampled.Clip clip){
        this.clip = clip;
    }
    @Override
    public void play(float volume)  {

        ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f*  (float)Math.log10(volume));
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void dispose() {
        clip.stop();
        clip.flush();
        //No se si el close hace falta o cierra todoel archivo
        clip.close();
    }
}
