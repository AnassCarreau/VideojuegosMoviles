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
import  es.ucm.gdv.blas.oses.carreau.androidengine.Android.AndroidFont;

public class AndroidGraphics extends AbstractGraphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    ////////////////////METODOS DE TRANSFORMACION//////////////////////////////
    /*@Override
    public void translate(int x, int y) {
        this.canvas.translate(x, y);
    }

    @Override
    public void scale(int w, int h) {
        this.canvas.scale(w, h);
    }
     */

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
        this.paint.setColor(color);
    }

    ////////////////////METODOS DE DIBUJADO//////////////////////////////
    @Override
    public void fillRealCircle(float cx, float cy, int r) {
        this.canvas.drawCircle(cx, cy, r, this.paint);
    }

    @Override
    public void drawRealText(String text, Font font, int x, int y) {
        Typeface aFont = ((AndroidFont) font)._font;
        if (font != null && aFont != null) {
            paint.setTypeface(aFont);
            canvas.drawText(text, x, y, paint);
        }
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
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
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