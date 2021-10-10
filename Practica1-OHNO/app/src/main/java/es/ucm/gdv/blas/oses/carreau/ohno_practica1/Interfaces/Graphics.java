package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces;

import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Font;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Image;

public interface Graphics {


    int width = 0;
    int height = 0;

    //canvas* canvas;
    Image newImage(String name);
    public Font newFont(String filename, int size, boolean isBold);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawImage(Image img, int x, int y, int srcX, int srcY,
                           int srcWidth, int srcHeight);

    //public void drawPixmap(Pixmap pixmap, int x, int y);

    public int getWidth();

    public int getHeight();





    //practica
    public void clear(int color);

    public void translate(float x, float y);

    public void scale(float x, float y);

    public int save();

    public void restore();

    void drawImage(Image image, int x, int y);

    public void setColor(int color);

    public void fillCircle(float cx, float cy, int r);

    public void drawText(String text, int x, int y);
    /*{
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);
            if (character == ' ') {
                x += 20;
                continue;
            }
            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }
            x += srcWidth;
        }*/
};
