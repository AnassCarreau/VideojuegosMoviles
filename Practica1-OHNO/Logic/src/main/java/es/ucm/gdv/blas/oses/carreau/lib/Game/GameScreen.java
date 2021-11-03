package es.ucm.gdv.blas.oses.carreau.lib.Game;
import java.awt.Color;
import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Celda;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input.TouchEvent;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;
import es.ucm.gdv.blas.oses.carreau.lib.Tablero;

public class GameScreen implements Screen {
    private Engine engine;
    private Tablero board;
    private int boardDimensions;

    public GameScreen(Engine eng, int tableroSize) {
        this.engine = eng;
        this.board = new Tablero(tableroSize);
        this.boardDimensions = tableroSize;
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

        //Juego empezado

        g.setColor(0x000000FF);
        g.drawText(Integer.toString(boardDimensions) + "x" + Integer.toString(boardDimensions), Assets.josefisans, g.getLogWidth() / 2, g.getLogHeight() / 4 - (int) Assets.josefisans.getFontSize() * 2);

        int circleSize = g.getLogWidth()/ boardDimensions;

        int initialX = (int)(g.getLogWidth()/2 - circleSize * ((float)boardDimensions/2));
        for (int i = 0; i < boardDimensions; i++) {
            for (int j = 0; j < boardDimensions; j++) {
                Celda c = board.getCelda(i,j);
                boolean hasNumber = false;
                switch (c.getEstado()){
                    case Azul: {
                        g.setColor(0x0000FFFF);
                        hasNumber = true;
                        break;
                    }
                    case Rojo:{
                        g.setColor(0xFF0000FF);
                        break;
                    }
                    case Vacia:{
                        g.setColor(0xD3D3D3FF);
                        break;
                    }
                }

                int x = initialX + circleSize/2 + i * circleSize;
                int y = (g.getLogHeight()/6) + circleSize/2 + (j * circleSize);

                g.fillCircle(x, y, circleSize/2);

                //Si es de las azules fijas, pintamos sus numeros correspondientes
                if(hasNumber && !c.isModifiable()){
                    g.setColor(0xFFFFFFFF);
                    g.drawText(Integer.toString(c.getValorDefault()),Assets.josefisans,x,y + circleSize/4);
                }
            }
        }

        g.drawImage(Assets.close, g.getLogWidth() / 5 - Assets.close.getWidth(), g.getLogHeight() - Assets.close.getHeight(), Assets.close.getWidth()/2, Assets.close.getHeight()/2);
        g.drawImage(Assets.history, g.getLogWidth() / 5 * 3 - Assets.history.getWidth(), g.getLogHeight() - Assets.history.getHeight(), Assets.history.getWidth()/2, Assets.history.getHeight()/2);
        g.drawImage(Assets.eye, g.getLogWidth() - Assets.eye.getWidth(), g.getLogHeight() - Assets.eye.getHeight(), Assets.eye.getWidth()/2, Assets.eye.getHeight()/2);


    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }


}

