package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game;
import java.util.List;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;

public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);
    }

    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, g.getWidth() / 3, g.getHeight() - 32, 96, 32)) {
                    game.setScreen(new GameScreen(game));
                    return;
                }
            }
        }
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
      //  g.clear(0);
        //  g.drawPixmap(Assets.mainMenu, 64, 220);
        //ohno texto g.drawText()
        //Jugar texto g.drawText()
        //Descripcion texto g.drawText()
        g.drawPixmap(Assets.q42, 0, 0);

    }
}

