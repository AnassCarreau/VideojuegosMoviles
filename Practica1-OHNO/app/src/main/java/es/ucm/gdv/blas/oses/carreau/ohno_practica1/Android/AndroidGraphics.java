package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Android;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Font;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Image;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Input;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets ,Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    ////////////////////METODOS DE TRANSFORMACION//////////////////////////////
    public void translate(float x, float y) {
        this.canvas.translate(x, y);
    }

    public void scale(float x, float y) {
        this.canvas.scale(x, y);
    }

    public int save() {
        return this.canvas.save();
    }

    public void restore() {
        this.canvas.restore();
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    ////////////////////METODOS DE DIBUJADO//////////////////////////////
    public void fillCircle(float cx, float cy, int r) {
        this.canvas.drawCircle(cx, cy, r, this.paint);
    }

    public void drawImage(Image image, int x, int y) {
        canvas.drawBitmap(((AndroidImage)image).bitmap, x, y, null);
    }

    public void drawImage(Image img, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidImage) img).bitmap, srcRect, dstRect, null);

    }

    public void drawText(String text, int x, int y) {

    }

    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }

    ////////////////////METODOS DE CREACION//////////////////////////////
    public Image newImage(String name) {
        Config config = null;
        Options options = new Options();
        options.inPreferredConfig = config;
        Bitmap bitmap = null;
        InputStream in = null;

        //Java 1.7
        try{
            in = assets.open(name);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + name + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + name + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        return new AndroidImage(bitmap);
    }

    //TO DO
    public Font newFont(String filename, int size, boolean isBold) {

        return null;
    }

    ////////////////////METODOS GETTER//////////////////////////////
    public int getWidth() {
        return frameBuffer.getWidth();
    }
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}