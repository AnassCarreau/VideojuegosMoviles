package es.ucm.gdv.blas.oses.carreau.lib;

import java.io.File;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Audio;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PCAudio implements Audio {

    /**
     * Metodo que carga un sonido
     *
     * @param filename, String, nombre del archivo
     */
    @Override
    public Sound newSound(String filename) {
        PCSound s = null;

        try {
            AudioInputStream au = javax.sound.sampled.AudioSystem.getAudioInputStream(new File("assets/" + filename));
            Clip clip = AudioSystem.getClip();
            clip.open(au);
            s = new PCSound(clip);


        } catch (Exception e) {
            System.err.println(e);
        }
        return s;
    }

}
