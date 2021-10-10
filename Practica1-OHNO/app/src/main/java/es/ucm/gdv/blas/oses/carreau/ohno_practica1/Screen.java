package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Engine;

public abstract class Screen {
    protected final Engine engine;

    public Screen(Engine engine) {
        this.engine = engine;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

   // public abstract void pause();

  //  public abstract void resume();
    //public abstract void dispose();
}