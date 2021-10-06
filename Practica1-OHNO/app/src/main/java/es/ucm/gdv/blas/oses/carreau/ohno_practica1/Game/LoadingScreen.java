package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game;


import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import MainMenuScreen;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.PixmapFormat;
public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.q42 = g.newPixmap("q42.png", PixmapFormat.ARGB4444);
        Assets.lock = g.newPixmap("lock.png", PixmapFormat.ARGB4444);
        Assets.history = g.newPixmap("history.png", PixmapFormat.ARGB4444);
        Assets.eye = g.newPixmap("eye.png", PixmapFormat.ARGB4444);
        Assets.close = g.newPixmap("close.png", PixmapFormat.ARGB4444);
        game.setScreen(new MainMenuScreen(game));
    }

    public void present(float deltaTime) {
    }
}