package es.ucm.gdv.blas.oses.carreau.lib.Game;


import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Game.MainMenuScreen;

public class LoadingScreen implements Screen {
    private Engine engine;
    public LoadingScreen(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void init() {
        Graphics g = engine.getGraphics();
        Assets.q42 = g.newImage("q42.png");
        Assets.lock = g.newImage("lock.png");
        Assets.history = g.newImage("history.png");
        Assets.eye = g.newImage("eye.png");
        Assets.close = g.newImage("close.png");
        Assets.josefisans = g.newFont("JosefinSans-Bold.ttf",48,true);
        Assets.molleregular = g.newFont("Molle-Regular.ttf",72,true);
        engine.setScreen(new MainMenuScreen(engine));
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render() {

    }

    @Override
    public void handleEvents() {

    }

}