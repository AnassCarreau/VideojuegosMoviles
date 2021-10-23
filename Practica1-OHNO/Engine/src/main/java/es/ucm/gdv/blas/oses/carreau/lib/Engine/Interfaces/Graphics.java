package es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;

public interface Graphics {
    public Image newImage(String name);

    //TO DO: mirar la putisima font :D
    public Font newFont(String filename, int size, boolean isBold);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawImage(Image img, int x, int y, int srcX, int srcY,
                           int srcWidth, int srcHeight);

    //seria para coger el tamaño del canvas¿?
    public int getWidth();

    public int getHeight();

    //practica
    public void clear(int color);

    public void translate(float x, float y);

    public void scale(float x, float y);

    public int save();

    public void restore();

    public void drawImage(Image image, int x, int y);

    public void setColor(int color);

    public void fillCircle(float cx, float cy, int r);

    public void drawText(String text, int x, int y);
};
