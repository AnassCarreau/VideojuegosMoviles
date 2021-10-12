package es.ucm.gdv.blas.oses.carreau.ohno_practica1.Android;
import android.app.Activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;


import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Graphics;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Image;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Input;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.Engine;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;

public abstract class AndroidGame extends AppCompatActivity implements Engine, Runnable {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Input input;
    Screen screen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        boolean isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics( getAssets(), frameBuffer);
        //input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getStartScreen();

        //Image im =  graphics.newImage("q42.png");
        //graphics.drawImage(im,100,200);
        setContentView(renderView);
    }

    public Input getInput() {
        return input;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Screen getCurrentScreen() {
        return screen;
    }

     @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        //renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //renderView.pause();
        screen.pause();
        if (isFinishing()) {
            screen.dispose();
        }
    }

    //TO ERASE
    Bitmap _sprite;
    int _imageWidth;


}