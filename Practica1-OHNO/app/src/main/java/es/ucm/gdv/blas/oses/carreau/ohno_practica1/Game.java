package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

public interface Game {
    public Input getInput();

    //public FileIO getFileIO();
    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
}