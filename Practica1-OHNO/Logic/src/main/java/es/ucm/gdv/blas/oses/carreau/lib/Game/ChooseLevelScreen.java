package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Pair;

public class ChooseLevelScreen implements Screen {

    private final List<Pair<Integer, Integer>> celdas;

    public ChooseLevelScreen() {
        //TODO los 400 y 600 estan a pelo
        celdas=new ArrayList<>();
        int radio = (400 / 5) / 2;
        for (int i = 0; i < 6; i++) {
            int x = 400  / 5 + radio + ((i % 3) * radio * 2);
            int y = 2 * 600 / 5 + (i / 3) * (radio * 2) + radio;
            celdas.add(new Pair<Integer, Integer>(x, y));
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
            if (i % 2 == 0) g.setColor(0xFF3D53FF);
            else g.setColor(0x00BFFFFF);

            g.fillCircle(celdas.get(i).getLeft(), celdas.get(i).getRight(), radio);
            g.setColor(0xFFFFFFFF);
            g.drawText(Integer.toString(i + 4), Assets.josefisans, celdas.get(i).getLeft(), celdas.get(i).getRight() + radio * 2 / 4, radio * 4.0f / 3.0f);
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
            if (event.type == Input.TouchEvent.TOUCH_UP) {

                if(inBounds(event,g.getLogWidth() / 2 - Assets.close.getWidth() / 2, g.getLogHeight() - Assets.close.getHeight()*2, Assets.close.getWidth(), Assets.close.getHeight() ))
                {
                    engine.setScreen(new MainMenuScreen());
                }
                int radio = (g.getLogWidth() / 5) / 2;

                for (int j = 0; j < celdas.size(); j++) {
                    int x = celdas.get(j).getLeft();
                    int y = celdas.get(j).getRight();
                    if (inBoundsCircle(event, x, y, radio)) {
                        Assets.click.play(1);
                        engine.setScreen(new GameScreen( j + 4, true));
                    }
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
