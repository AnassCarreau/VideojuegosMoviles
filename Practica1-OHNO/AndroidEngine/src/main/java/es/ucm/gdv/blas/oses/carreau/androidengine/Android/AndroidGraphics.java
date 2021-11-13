package es.ucm.gdv.blas.oses.carreau.androidengine.Android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.AbstractGraphics;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Font;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Image;


public class AndroidGraphics extends AbstractGraphics {
    final AssetManager assets;
    Canvas canvas;
    final Paint paint;
    final Rect srcRect = new Rect();
    final Rect dstRect = new Rect();
    final float dpi;

    /**
     * Constructora del motor grafico para la implementacion especifica de Android
     *
     * @param activity,    mainActivity
     * @param frameBuffer, Bitmap,framebuffer
     * @param logWidth,    ancho logica del juego
     * @param logHeight,   alto logico del juego
     */
    public AndroidGraphics(AppCompatActivity activity, Bitmap frameBuffer, int logWidth, int logHeight) {
        this.assets = activity.getAssets();
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.logWidth = logWidth;
        this.logHeight = logHeight;

        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        this.winWidth = p.x;
        this.winHeight = p.y;
        this.dpi = activity.getBaseContext().getResources().getDisplayMetrics().density;

    }

    /**
     * Metodo que limpia la pantalla con el color que le pasas como parametro
     *
     * @param color, int, color en hexadecimal con el que quieres limpiar la ventana (RGBA)
     */
    @Override
    public void clear(int color) {
        int c = (color & 0xffffff00) >> 8 | (color & 0x000000ff) << 24;
        canvas.drawColor(c);
    }

    /**
     * Metodo para trasladar el punto desde el cual se realizan las operaciones de pintado
     *
     * @param x, int, coordenada en x que representa el nuevo origen de dibujado
     * @param y, int, coordenada en y que representa el nuevo origen de dibujado
     */
    @Override
    public void translate(float x, float y) {
        this.canvas.translate(x, y);
    }

    /**
     * Metodo para actualizar el factor de escala del dibujado
     *
     * @param w, factor de escalado a lo ancho
     * @param h, factor de escalado a lo alto
     */
    @Override
    public void scale(float w, float h) {
        this.canvas.scale(w, h);
    }

    /**
     * Metodo para reestablecer los valores por defecto del contexto grafico
     */
    @Override
    public void restore() {
        this.canvas.restore();
    }

    /**
     * Metodo para settear como color por defecto para las operaciones de
     * pintado aquel que se pasa por parametros
     *
     * @param color, int, color, en hexadecimal, que queremos utilizar
     */
    @Override
    public void setColor(int color) {
        int c = (color & 0xffffff00) >> 8 | (color & 0x000000ff) << 24;
        this.paint.setColor(c);
    }

    /**
     * Metodo para dibujar un circulo completamente relleno
     *
     * @param cx, int, coordenada x, en coordenadas logicas, del centro del circulo
     * @param cy, int, coordenada y, en coordenadas logicas, del centro del circulo
     * @param r,  int, radio, en dimensiones logicas, que tiene el circulo
     */
    @Override
    public void fillCircle(float cx, float cy, int r) {
        this.canvas.drawCircle(cx, cy, r, this.paint);
    }

    /**
     * Metodo para dibujar una circunferencia (circulo no relleno)
     *
     * @param cx, int, coordenada x, en coordenadas logicas, del centro del circulo
     * @param cy, int, coordenada y, en coordenadas logicas, del centro del circulo
     * @param r,  int, radio, en dimensiones logicas, que tiene el circulo
     */
    @Override
    public void drawCircle(float cx, float cy, int r) {
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(2);
        this.canvas.drawCircle(cx, cy, r, this.paint);
        paint.setStyle(Style.FILL);

    }

    /**
     * Metodo para dibujar un texto centrado en las coordenadas x y
     *
     * @param text, String, texto que queremos dibujar
     * @param font, Font, fuente que vamos a usar para renderizar el texto
     * @param x,    int, coordenada x, en el espacio de coordenadas lógicas en la que queremos
     *              que este centrado el texto
     * @param y,    int, coordenada y, en el espacio de coordenadas lógicas en la que queremos
     *              que este centrado el texto
     * @param tam,  float, tamaño que queremos emplear al pintar la fuente
     */
    @Override
    public void drawText(String text, Font font, int x, int y, float tam) {
        Typeface aFont = ((AndroidFont) font)._font;

        if (aFont != null) {
            paint.setTypeface(aFont);
            paint.setTextSize((getScaleFactor() * tam) / dpi);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, x, y, paint);
            paint.reset();
        }
    }

    /**
     * Metodo para pintar una imagen en una posicion logica (x,y) con un tamaño logico (w,h)
     *
     * @param image, Image, imagen que queremos dibujar
     * @param x,     int, coordenada x, en dimensiones logicas, en la que queremos pintar
     * @param y,     int, coordenada y, en dimensiones logicas, en la que queremos pintar
     * @param w,     int, ancho, en dimensiones logicas, que queremos que tenga nuestra imagen
     * @param h,     int, alto, en dimensiones logicas, que queremos que tenga nuestra imagen
     */
    @Override
    public void drawImage(Image image, int x, int y, int w, int h) {
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

    /**
     * Metodo para dibujar un rectangulo/cuadrado sin rellenar
     *
     * @param x,      int, en el espacio de coordenadas logicas, coordenada x de la esquina superior
     *                izquierda del rectangulo
     * @param y,      int, en el espacio de coordenadas logicas, coordenada y de la esquina superior
     *                izquierda del rectangulo
     * @param width,  int, ancho, en coordenadas logicas, del rectangulo
     * @param height, int, alto, en coordenadas logicas, del rectangulo
     * @param color,  int, color en hexadecimal de la linea con la que se pinta el rectangulo
     */
    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        setColor(color);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(x, y, x + width, y + width, paint);
        paint.reset();
    }

    /**
     * Metodo que crea una nueva imagen a partir de una ruta dada
     *
     * @param name, ruta de la imagen a cargar
     * @return Image, objeto que representa una imagen en el juego, en su
     * implementacion especifica
     */
    @Override
    public Image newImage(String name) {
        Bitmap bitmap = null;
        InputStream in = null;

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
                    System.err.println(e.getCause().toString());
                }
            }
        }

        return new AndroidImage(bitmap);
    }

    /**
     * Metodo que crea una nueva fuente
     *
     * @param filename, String, ruta del archivo con la fuente a cargar
     * @param size,     int, tamaño de la fuente que queremos que tenga por defecto
     * @param isBold,   boolean, si dicha fuente esta en negrita
     * @return el nuevo objeto fuente creado, en su implementacione especifica
     */
    @Override
    public Font newFont(String filename, float size, boolean isBold) {
        return new AndroidFont(Typeface.createFromAsset(this.assets, filename));
    }

    /**
     * Metodo para actualizar el canvas actual
     *
     * @param canvas
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}