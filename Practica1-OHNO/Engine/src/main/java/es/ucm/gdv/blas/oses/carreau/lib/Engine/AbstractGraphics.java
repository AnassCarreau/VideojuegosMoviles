package es.ucm.gdv.blas.oses.carreau.lib.Engine;


import org.w3c.dom.css.Rect;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;

public abstract class AbstractGraphics implements Graphics {

    //Dimensiones lógicas
    protected int logWidth;
    protected int logHeight;

    //Dimensiones de ventana fisicas
    protected float winWidth;
    protected float winHeight;

    //Métodos getter para las dimensiones lógicas y físicas
    @Override
    public final int getLogWidth(){
        return logWidth;
    }
    @Override
    public final int getLogHeight(){
        return logHeight;
    }
    @Override
    public final int getWindowWidth(){
        return (int)winWidth;
    }
    @Override
    public final int getWindowHeight(){
        return (int)winHeight;
    }


    //Método que nos devuelve el factor de escalado a aplicar
    protected float getScaleFactor(){
        float factorWidth = winWidth / logWidth;
        float factorHeight = winHeight / logHeight;

        if(factorWidth < factorHeight) return factorWidth;
        else return factorHeight;
    }

   /** Método que nos devuelve un array de tamaño 4 con la posicion en x, y real, al igual que el tamaño
     * @param x
     * @param y
   */
   public int[] physicalToLogical(int x, int y)
    {
        float factor = getScaleFactor();
        int newx1=(int)(logWidth/2-((winWidth/factor)/2));
        int newy1=(int)(logHeight/2-((winHeight/factor)/2));
        return new int[]{(int)(newx1 + x/ factor), (int)( newy1 + y / factor)};
    }

    protected int[] translateBorder(int x, int y, float scale_factor) {
        int newX1 = (int)(winWidth/2 - ((logWidth * scale_factor) / 2));
        int newY1 = (int)(winHeight/2 - ((logHeight * scale_factor) / 2));

        int xDest = (int)(newX1 + (x * scale_factor));
        int yDest = (int)(newY1 + (y * scale_factor));

        return new int[]{xDest, yDest};
    }

    public final void prepareFrame(){
        float scaleFactor = getScaleFactor();
        int[] traslateBorder = translateBorder(0,0,scaleFactor);

        translate(traslateBorder[0],traslateBorder[1]);
        scale(scaleFactor, scaleFactor);

    }

}
