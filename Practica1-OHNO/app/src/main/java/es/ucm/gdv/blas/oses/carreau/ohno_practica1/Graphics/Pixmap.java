package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.PixmapFormat;

    public interface Pixmap {
        public int getWidth();
        public int getHeight();
        public PixmapFormat getFormat();
        public void dispose();
}
