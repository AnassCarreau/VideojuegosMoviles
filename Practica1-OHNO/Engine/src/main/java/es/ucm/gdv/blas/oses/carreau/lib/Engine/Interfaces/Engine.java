package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Engine {
    public Input getInput();
    public Audio getAudio();
    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();
}