package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.awt.Color;
import java.util.List;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Game.GameScreen;

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
                if (inBounds(event, g.getWidth()/2, g.getHeight()/2, 200, 100)) {
                    engine.setScreen(new GameScreen(engine));
                    return;
                }
            }
        }
    }

    @Override
    public void render() {
        Graphics g = engine.getGraphics();
        g.clear(Color.WHITE.getRGB());
        g.drawImage(Assets.q42, g.getWidth()/2  - Assets.q42.getWidth()/24, g.getHeight()/4 * 3 ,Assets.q42.getWidth()/12,Assets.q42.getHeight()/9);
        g.drawText("Oh NO", Assets.molleregular,g.getWidth()/2,g.getHeight()/6);
        g.drawText("Jugar", Assets.josefisans,g.getWidth()/2, g.getHeight()/2);
        g.drawRect(g.getWidth()/2, g.getHeight()/2, 200, 100, Color.BLUE.getRGB());

        g.setColor(Color.GRAY.getRGB());
        g.drawText("Un juego copiado a Q42",Assets.josefisans,g.getWidth()/2, g.getHeight()/3 * 2 );
        g.drawText("Creado por Martin Kool",Assets.josefisans,g.getWidth()/2, g.getHeight()/7 * 5);

    }



    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width && event.y > y && event.y < y + height;
    }
}

