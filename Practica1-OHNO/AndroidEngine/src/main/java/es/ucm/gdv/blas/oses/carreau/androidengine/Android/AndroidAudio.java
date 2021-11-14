package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Audio;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;

public class AndroidAudio implements Audio {
    private final AssetManager assets;
    private final SoundPool soundPool;

    /**
     * Constructora de AndroidAudio
     *
     * @param activity, Activity, actividad
     */
    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Metodo que carga un nuevo sonido
     *
     * @param filename, String, nombre del fichero
     */
    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}