package es.ucm.gdv.blas.oses.carreau.lib.Engine;

public interface Screen {
    public void init();

    public void update(double deltaTime);

    public void render();

    public void handleEvents();

    public int getScreenID();
}