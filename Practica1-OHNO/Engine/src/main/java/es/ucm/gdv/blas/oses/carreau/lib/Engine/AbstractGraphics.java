package es.ucm.gdv.blas.oses.carreau.lib.Engine;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Graphics;

public abstract class AbstractGraphics implements Graphics {

    //Dimensiones lógicas
    protected int logWidth;
    protected int logHeight;

    //Dimensiones de ventana fisicas
    protected float winWidth;
    protected float winHeight;

    /**
     * Metodo que devuelve el ancho de las dimensiones logicas del juego
     *
     * @return int, ancho logico del juego
     */
    @Override
    public final int getLogWidth() {
        return logWidth;
    }

    /**
     * Metodo que devuelve el alto de las dimensiones logicas del juego
     *
     * @return int,  alto logico del juego
     */
    @Override
    public final int getLogHeight() {
        return logHeight;
    }

    /**
     * Metodo que devuelve el ancho de la ventana en la que se ejecuta el juego
     *
     * @return int, ancho de la ventana de juego
     */
    @Override
    public final int getWindowWidth() {
        return (int) winWidth;
    }

    /**
     * Metodo que devuelve el alto de la ventana en la que se ejecuta el juego
     *
     * @return int, alto de la ventana de juego
     */
    @Override
    public final int getWindowHeight() {
        return (int) winHeight;
    }


    /**
     * Metodo que devuelve el factor de escalado a aplicar teniendo en cuenta las dimensiones
     * de la ventana y las dimensiones logicas por defecto del juego
     *
     * @return factor de escalado
     */
    protected final float getScaleFactor() {
        float factorWidth = winWidth / logWidth;
        float factorHeight = winHeight / logHeight;

        if (factorWidth < factorHeight) return factorWidth;
        else return factorHeight;
    }

    /**
     * Metodo que dadas unas coordenadas de ventana, las transforma en coordenadas logicas del juego
     *
     * @param x, coordenada x, en el espacio de la ventana, que queremos transformar
     * @param y, coordenada y, en el espacio de la ventana, que queremos transformar
     * @return
     */
    public final int[] physicalToLogical(int x, int y) {
        float factor = getScaleFactor();
        int newx1 = (int) (logWidth / 2 - ((winWidth / factor) / 2));
        int newy1 = (int) (logHeight / 2 - ((winHeight / factor) / 2));
        return new int[]{(int) (newx1 + x / factor), (int) (newy1 + y / factor)};
    }

    /**
     * Metodo para calcular cual seria el nuevo 0,0 si queremos tener el
     * juego centrado y manteniendo la proporcion de las dimensiones logicas
     * que hemos establecido por defecto
     *
     * @param x,            coordenada x, en el espacio de coordenadas logicas, de la que queremos calcular su
     *                      posicion en coordenadas fisicas
     * @param y             coordenada y, en el espacio de coordenadas logicas, de la que queremos calcular su
     *                      posicion en coordenadas fisicas
     * @param scale_factor, factor de escalado
     * @return int[], coordenadas x e y convertidas al espacio fisico de la ventana
     */
    protected final int[] translateBorder(int x, int y, float scale_factor) {
        int newX1 = (int) (winWidth / 2 - ((logWidth * scale_factor) / 2));
        int newY1 = (int) (winHeight / 2 - ((logHeight * scale_factor) / 2));

        int xDest = (int) (newX1 + (x * scale_factor));
        int yDest = (int) (newY1 + (y * scale_factor));

        return new int[]{xDest, yDest};
    }

    /**
     * Metodo que, llamando a los metodos translate y scale redefinidos
     * en una clase que herede de AbstractGraphics, centra el juego y lo escala
     * ajustandolo a las dimensiones de ventana manteniendo la relacion de aspecto
     * establecida en las coordenadas logicas.
     */
    public final void prepareFrame() {
        float scaleFactor = getScaleFactor();
        int[] traslateBorder = translateBorder(0, 0, scaleFactor);

        translate(traslateBorder[0], traslateBorder[1]);
        scale(scaleFactor, scaleFactor);

    }

}
