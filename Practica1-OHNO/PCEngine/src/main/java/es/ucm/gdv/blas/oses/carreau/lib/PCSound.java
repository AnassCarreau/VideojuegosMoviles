package es.ucm.gdv.blas.oses.carreau.lib;

import javax.sound.sampled.FloatControl;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;


public class PCSound implements Sound {

    private final javax.sound.sampled.Clip clip;

    /**
     * Constructora PcSound
     *
     * @param clip, Clip, clip cargado
     */
    public PCSound(javax.sound.sampled.Clip clip) {
        this.clip = clip;
    }

    /**
     * Metodo que reproduce un sonido con determinado volumen
     *
     * @param volume, float, volumen del juego
     */
    @Override
    public void play(float volume) {
        ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f * (float) Math.log10(volume));
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Metodo que libera la carga del sonido
     */
    @Override
    public void dispose() {
        clip.stop();
        clip.flush();
        clip.close();
    }
}
