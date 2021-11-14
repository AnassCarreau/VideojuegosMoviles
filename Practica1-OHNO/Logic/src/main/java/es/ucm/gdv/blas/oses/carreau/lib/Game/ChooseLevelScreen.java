package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Vector;

public class ChooseLevelScreen implements Screen {

    private final List<Vector> celdas;
    private static final int RADIO_SIZE = 40;
    private static final int CIRCLES_PER_ROW  = 3;
    private static final int ROWS = 2;
    private static final int MAX_LEVELS = 6;

    public ChooseLevelScreen() {
        celdas=new ArrayList<>();
        for (int i = 0; i < MAX_LEVELS; i++) {
            int x = 400  / 5 + RADIO_SIZE + ((i % CIRCLES_PER_ROW) * RADIO_SIZE * 2);
            int y = 2 * 600 / 5 + (i / CIRCLES_PER_ROW) * (RADIO_SIZE * 2) + RADIO_SIZE;
            celdas.add(new Vector(x, y));
        }
    }

    @Override
    public void update(double deltaTime) {
    }


    @Override
    public void render(Graphics g) {

        //Eleccion
        g.setColor(0x000000FF);
        g.drawText("Oh NO", Assets.molleregular, g.getLogWidth() / 2, g.getLogHeight() / 6, 60);
        g.drawText("Elija el tamaño a jugar ", Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 2 - 50 * 2, 30);

        int radio = (g.getLogWidth() / 5) / 2;
        for (int i = 0; i < celdas.size(); i++) {
            if (i % ROWS == 0) g.setColor(0xFF3D53FF);
            else g.setColor(0x00BFFFFF);

            g.fillCircle(celdas.get(i).x, celdas.get(i).y, radio);
            g.setColor(0xFFFFFFFF);
            g.drawText(Integer.toString(i + 4), Assets.josefisans, celdas.get(i).x, celdas.get(i).y + radio * 2 / 4, radio * 4.0f / 3.0f);
        }
        g.drawImage(Assets.close, g.getLogWidth() / 2 - Assets.close.getWidth() / 2, g.getLogHeight() - Assets.close.getHeight() * 2, Assets.close.getWidth(), Assets.close.getHeight());
    }

    @Override
    public void handleEvents(Engine engine) {
        Graphics g = engine.getGraphics();
        List<Input.TouchEvent> touchEvents = engine.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {

                int radio = (g.getLogWidth() / 5) / 2;

                for (int j = 0; j < celdas.size(); j++) {
                    int x = celdas.get(j).x;
                    int y = celdas.get(j).y;
                    if (inBoundsCircle(event, x, y, radio)) {
                        Assets.click.play(1);
                        engine.setScreen(new GameScreen( j + 4, true));
                    }
                }

                if(inBounds(event,g.getLogWidth() / 2 - Assets.close.getWidth() / 2, g.getLogHeight() - Assets.close.getHeight()*2, Assets.close.getWidth(), Assets.close.getHeight() ))
                {
                    engine.setScreen(new MainMenuScreen());
                }
            }
        }
    }

    @Override
    public int getScreenID() {
        return 2;
    }


    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width && event.y > y && event.y < y + height;
    }


    private boolean inBoundsCircle(Input.TouchEvent event, int cx, int cy, int radius) {
        int rx = event.x - cx;
        int ry = event.y - cy;

        return (float) Math.sqrt(Math.pow(ry, 2) + Math.pow(rx, 2)) <= radius;
    }
}
