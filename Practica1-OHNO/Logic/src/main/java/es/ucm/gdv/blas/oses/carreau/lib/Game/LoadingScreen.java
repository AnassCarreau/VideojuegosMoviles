package es.ucm.gdv.blas.oses.carreau.lib.Game;


import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Game.MainMenuScreen;

public class LoadingScreen extends Screen {
    public LoadingScreen(Engine engine) {
        super(engine);
    }
    @Override
    public void update(float deltaTime) {
        Graphics g = engine.getGraphics();
        Assets.q42 = g.newImage("q42.png");
        Assets.lock = g.newImage("lock.png");
        Assets.history = g.newImage("history.png");
        Assets.eye = g.newImage("eye.png");
        Assets.close = g.newImage("close.png");
        engine.setScreen(new MainMenuScreen(engine));
    }
    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}