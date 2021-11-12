package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

public interface Screen {
    public void update(double deltaTime);

    public void render();

    public void handleEvents() ;

    public int getScreenID();
}