package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Music {
    public void play();
    public void stop();
    public void pause();
    void setLooping(boolean looping);
    void setVolume(float volume);
    public boolean isPlaying();
    public boolean isStopped();
    public boolean isLooping();
    public void dispose();
}
