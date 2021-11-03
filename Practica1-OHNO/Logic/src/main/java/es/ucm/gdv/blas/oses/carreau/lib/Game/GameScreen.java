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


        //Eleccion
     /*   g.drawText("Oh NO", Assets.molleregular,g.getLogWidth()/2,g.getLogHeight()/6);
        g.drawText("Elija el tamaño a jugar ",Assets.josefisans,g.getLogWidth()/2, g.getLogHeight()/2 - (int)Assets.josefisans.getFontSize()*2 );

        int radio =120;
        for (int i = 0 ;i< 6 ;i++)
        {
            g.fillCircle(g.getLogWidth()/5 *2 +  (i % 3) * (radio + 20) - radio,g.getLogHeight()/3 *2 + (i / 3) * (radio + 20) - radio ,radio  );
            g.drawText(  Integer.toString( i +4 ),Assets.josefisans,g.getLogWidth()/5 *2  +  (i % 3) * (radio + 20) - radio,g.getLogHeight()/3 *2 + (i / 3) * (radio + 20) - radio + (int)Assets.josefisans.getFontSize()/2 );
        }
        g.drawImage(Assets.close, g.getLogWidth()/2, g.getLogHeight()  - Assets.close.getHeight() );
*/

        //Juego empezado

        int size=4;
        g.drawText(Integer.toString(size) + "x" +Integer.toString(size)  ,Assets.josefisans,g.getLogWidth()/2, g.getLogHeight()/4 - (int)Assets.josefisans.getFontSize()*2 );

        int radio =100;
       // int div = g.getLogWidth()/radio*2;
       for (int i = 0 ;i< size ;i++)
        {
            for (int j=0;j<size;j++) {
                g.fillCircle(g.getLogWidth() /( size) + i * (radio + 20) - radio, g.getLogHeight() / 3 + j * (radio + 20) - radio, radio);
               // g.drawText(Integer.toString(i + 4), Assets.josefisans, g.getLogWidth() / 5 * 2 + (i % 3) * (radio + 20) - radio, g.getLogHeight() / 3 * 2 + (i / 3) * (radio + 20) - radio + (int) Assets.josefisans.getFontSize() / 2);

            }
        }
        g.drawImage(Assets.close, g.getLogWidth()/5- Assets.close.getWidth(), g.getLogHeight()  - Assets.close.getHeight() );
        g.drawImage(Assets.history, g.getLogWidth()/5*3 - Assets.history.getWidth(), g.getLogHeight()  - Assets.history.getHeight() );
        g.drawImage(Assets.eye, g.getLogWidth() - Assets.eye.getWidth(), g.getLogHeight()  - Assets.eye.getHeight() );


    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }


}

