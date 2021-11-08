package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;
public interface Audio {
    Music newMusic(String filename);

    Sound newSound(String filename);
    //context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
}
