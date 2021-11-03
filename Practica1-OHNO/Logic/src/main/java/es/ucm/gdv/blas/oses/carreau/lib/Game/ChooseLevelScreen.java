package es.ucm.gdv.blas.oses.carreau.lib.Game;

import java.awt.Color;

import es.ucm.gdv.blas.oses.carreau.lib.Assets;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Screen;

public class ChooseLevelScreen implements Screen {

    private Engine engine;

    public ChooseLevelScreen(Engine eng){
        this.engine = eng;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render() {
        Graphics g = engine.getGraphics();
        g.clear(Color.WHITE.getRGB());
        //Eleccion
        g.setColor(0x000000FF);
        g.drawText("Oh NO", Assets.molleregular,g.getLogWidth()/2,g.getLogHeight()/6);
        g.drawText("Elija el tamaño a jugar ",Assets.josefisans,g.getLogWidth()/2, g.getLogHeight()/2 - (int)Assets.josefisans.getFontSize()*2 );

        int radio = (g.getLogWidth()/5)/2;
        for (int i = 0 ;i< 6 ;i++)
        {
            if(i %2 == 0)g.setColor(0xFF0000FF);
            else g.setColor(0x0000FFFF);

            int x = g.getLogWidth()/5 + radio + ((i%3) * radio*2);
            int y = 2*g.getLogHeight()/5 + (i / 3) * (radio*2) + radio;
            g.fillCircle(x,y,radio );
            g.setColor(0xFFFFFFFF);
            g.drawText(  Integer.toString( i +4 ),Assets.josefisans,x,y + radio/2);
        }
        g.drawImage(Assets.close, g.getLogWidth()/2 - Assets.close.getWidth()/2, g.getLogHeight()  - Assets.close.getHeight(), Assets.close.getWidth(), Assets.close.getHeight());
    }
}
