package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

public interface Engine {
    public Input getInput();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();
}