package es.ucm.gdv.blas.oses.carreau.lib.Engine;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

public interface Screen {
    public void init();

    public void update(double deltaTime);

    public void render();

    public void handleEvents() ;

    public int getScreenID();
}