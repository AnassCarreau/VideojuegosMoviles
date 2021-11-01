package es.ucm.gdv.blas.oses.carreau.lib.Game;
import java.awt.Color;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;

public class GameScreen implements Screen {
    private Engine engine;
    public GameScreen(Engine eng) {
        this.engine = eng;
    }

    @Override
    public void init() {
        Graphics g = engine.getGraphics();
        
    }

    @Override
    public void update(double deltaTime) {
        Graphics g = engine.getGraphics();
        List<TouchEvent> touchEvents = engine.getInput().getTouchEvents();
        //engine.getInput().getKeyEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, g.getLogWidth() / 3, g.getLogHeight() - 32, 96, 32)) {
                    //CLose
                    return;
                } else if (inBounds(event, g.getLogWidth() / 3, g.getLogHeight() - 32, 96, 32)) {
                    //history
                    return;
                } else if (inBounds(event, g.getLogWidth() / 3, g.getLogHeight() - 32, 96, 32)) {
                    //eye
                    return;
                }
            }

        }
    }
    @Override
    public void render() {
        Graphics g = engine.getGraphics();
        g.clear(Color.WHITE.getRGB());
        //  g.clear(0);
        //ohno texto g.drawText()
        //Jugar texto g.drawText()
        //Descripcion texto g.drawText()
        g.drawImage(Assets.close, 0, 0);
        g.drawImage(Assets.history, 0, 0);
        g.drawImage(Assets.eye, 0, 0);

    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }


}

