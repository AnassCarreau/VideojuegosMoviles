package es.ucm.gdv.blas.oses.carreau.lib.Engine;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;

//TO DO: mirar donde va esta wea
public abstract class Screen {
    protected final Engine engine;

    public Screen(Engine engine) {
        this.engine = engine;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();
    public abstract void dispose();
}