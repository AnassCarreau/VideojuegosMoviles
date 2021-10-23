package es.ucm.gdv.blas.oses.carreau.ohno_practica1;


import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;

public class MySuperAwesomeStartScreen extends Screen
{
    Image awesomePic;
    int x;
    public MySuperAwesomeStartScreen(Engine game) {
        super(game);
        awesomePic = game.getGraphics().newImage("q42.png");
        game.getStartScreen();
    }
    @Override
    public void update(float deltaTime) {
        x += 1;
        if (x > 100)
            x = 0;
    }
    @Override
    public void present(float deltaTime) { engine.getGraphics().clear(0);
    engine.getGraphics().
            drawImage(awesomePic, 50,50);
    }
    @Override
    public void pause() {
// nothing to do here
    }
    @Override
    public void resume() {
// nothing to do here
    }
    @Override
    public void dispose() {
      //  awesomePic.dispose();
    }
}