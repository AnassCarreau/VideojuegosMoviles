package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.awt.Color;
import java.util.List;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Assets;

public class MainMenuScreen implements Screen {

    private Engine engine;

    public MainMenuScreen(Engine engine) {
        this.engine = engine;
        init();
    }

    @Override
    public void init() {
        Graphics g = engine.getGraphics();
    }

    @Override
    public void update(double deltaTime) {
        Graphics g = engine.getGraphics();
        List<TouchEvent> touchEvents = engine.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            System.out.println("procesando eventos");
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                System.out.println("evento TOUCH_UP");
                if (inBounds(event, g.getLogWidth()/2, g.getLogHeight()/2, 200, 100)) {
                    engine.setScreen(new GameScreen(engine));
                    return;
                }
            }
        }
    }

    @Override
    public void render() {
        Graphics g = engine.getGraphics();;
        g.clear(0XFFFFFFFF);
        g.drawImage(Assets.q42, g.getLogWidth()/2  - Assets.q42.getWidth()/24, g.getLogHeight()/4 * 3 ,Assets.q42.getWidth()/14,Assets.q42.getHeight()/14);
        g.setColor(0x000000FF);
        g.drawText("Oh NO", Assets.molleregular,g.getLogWidth()/2,g.getLogHeight()/6);
        g.drawText("Jugar", Assets.josefisans,g.getLogWidth()/2, g.getLogHeight()/2);
        g.drawRect(g.getLogWidth()/2, g.getLogHeight()/2, 200, 100, 0x0000FFFF);
        g.setColor(0xCCCCCCFF);
        g.drawText("Un juego copiado a Q42",Assets.josefisans,g.getLogWidth()/2, g.getLogHeight()/3 * 2 );
        g.setColor(0xCCCCCCFF);
        g.drawText("Creado por Martin Kool",Assets.josefisans,g.getLogWidth()/2, g.getLogHeight()/9 * 7   );

    }



    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width && event.y > y && event.y < y + height;
    }
}

