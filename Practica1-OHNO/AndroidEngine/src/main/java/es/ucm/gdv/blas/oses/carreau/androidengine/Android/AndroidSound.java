package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.media.SoundPool;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;

public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;
    /**
     * Constructora AndroidSound
     * @param soundPool  SoundPool, pool de sonidos
     * @param soundId   int , identificador del sonido
     */
    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }
    /**
     * Metodo que reproduce un sonido con determinado volumen
     * @param volume, float, volumen del juego
     */
    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);

    }
    /**
     * Metodo que libera la carga del sonido
     */
    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}