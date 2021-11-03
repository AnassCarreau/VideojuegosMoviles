package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;



public class AndroidGraphics extends AbstractGraphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer,int logWidth, int logHeight,int winWidth,int winHeight ) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.logWidth = logWidth;
        this.logHeight = logHeight;
        this.winWidth = winWidth;
        this.winHeight = winHeight;

    }

    @Override
    public void clear(int color) {
        int c=(color & 0xffffff00) >> 8 | (color& 0x000000ff)<<24;
        canvas.drawColor(c);
    }


    ////////////////////METODOS DE TRANSFORMACION//////////////////////////////
    @Override
    public void translate(float x, float y) {
        this.canvas.translate(x, y);
    }

    @Override
    public void scale(float w, float h) {
        this.canvas.scale(w, h);
    }


    @Override
    public int save() {
        return this.canvas.save();
    }

    @Override
    public void restore() {
        this.canvas.restore();
    }

    @Override
    public void setColor(int color) {
        int c=(color & 0xffffff00) >> 8 | (color& 0x000000ff)<<24;
        this.paint.setColor(c);
    }

    ////////////////////METODOS DE DIBUJADO//////////////////////////////
    @Override
    public void fillRealCircle(float cx, float cy, int r) {
        this.canvas.drawCircle(cx, cy, r, this.paint);
        paint.reset();

    }

    @Override
    public void drawRealText(String text, Font font, int x, int y,float tam) {
        Typeface aFont = ((AndroidFont) font)._font;

        if (font != null && aFont != null) {
            paint.setTypeface(aFont);
            paint.setTextSize(getScaleFactor() * tam);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, x, y, paint);
            paint.reset();

        }


/*
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setFont( ((PCFont) font)._font.deriveFont((float) (((PCFont) font)._font).getSize() * getScaleFactor()));

        int len = g.getFontMetrics().stringWidth(text) / 2;

        g.drawString(text, (int) x - len, y);
  */
    }

    @Override
    public void drawRealImage(Image image, int x, int y) {
        canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, null);
    }



    @Override
    public void drawRealImage(Image image, int x, int y, int w, int h) {
        srcRect.left = 0;
        srcRect.top = 0;
        srcRect.right = image.getWidth();
        srcRect.bottom = image.getHeight();
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + w;
        dstRect.bottom = y + h;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
    }


    @Override
    public void drawRealRect(int x, int y, int width, int height, int color) {
        setColor(color);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(x, y, x + width , y + width , paint);
        paint.reset();

    }

    ////////////////////METODOS DE CREACION//////////////////////////////
    @Override
    public Image newImage(String name) {
        Config config = null;
        Options options = new Options();
        options.inPreferredConfig = config;
        Bitmap bitmap = null;
        InputStream in = null;

        //Java 1.7
        try {
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

    @Override
    public Font newFont(String filename, float size, boolean isBold) {

        AndroidFont font = new AndroidFont(Typeface.createFromAsset(this.assets, filename), size, filename);
        return font;
    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }
}