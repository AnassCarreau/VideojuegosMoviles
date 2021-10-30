package es.ucm.gdv.blas.oses.carreau.lib.Engine;

public interface Screen {
    public void init();

    public void update(float deltaTime);

    public void render();

    //TO DO: REVISAR YA QUE ES SOLO NECESARIO EN ANDROID
    /*public void pause();

    public void resume();
    public void dispose();*/
}