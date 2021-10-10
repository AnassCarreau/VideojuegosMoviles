package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game;


import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Engine;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.PixmapFormat;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Assets;

public class LoadingScreen extends Screen {
    public LoadingScreen(Engine engine) {
        super(engine);
    }

    public void update(float deltaTime) {
        Graphics g = engine.getGraphics();
        Assets.q42 = g.newImage("q42.png");
        Assets.lock = g.newImage("lock.png");
        Assets.history = g.newImage("history.png");
        Assets.eye = g.newImage("eye.png");
        Assets.close = g.newImage("close.png");
        engine.setScreen(new MainMenuScreen(engine));
    }

    public void present(float deltaTime) {
    }
}