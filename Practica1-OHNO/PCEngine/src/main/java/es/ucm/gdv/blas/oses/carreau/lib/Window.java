package es.ucm.gdv.blas.oses.carreau.lib;
import javax.swing.JFrame;

public class Window extends JFrame {

    public Window(String windowName){
        super(windowName);
    }

    public boolean initWindow(int width, int height, boolean fullscreen, int numBuffers){
        //Setteamos el tamano de la ventana
        //No confundir con el tam logico que tenemos que ajustar
        setSize(width,height);

        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Configuracion en pantalla completa
        if(fullscreen){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
        }

        // Vamos a usar renderizado activo. No queremos que Swing llame al
        // método repaint() porque el repintado es continuo en cualquier caso.
        setIgnoreRepaint(true);

        //Hacemos visible la ventana
        setVisible(true);

        // Intentamos crear el buffer strategy con numBuffers buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                createBufferStrategy(numBuffers);
                break;
            }
            catch(Exception e) {
                System.out.println(e.getMessage());

            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No se ha podido crear la BufferStrategy");
            return false;
        }
        return true;

    }
}