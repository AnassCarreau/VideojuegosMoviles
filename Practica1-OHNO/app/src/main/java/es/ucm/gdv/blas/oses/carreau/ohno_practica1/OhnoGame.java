package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//Imports del juego
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Android.AndroidGame;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game.MainMenuScreen;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Interfaces.FileIO;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Game.LoadingScreen;


public class OhnoGame extends AndroidGame {


    //Button button;
    int touchCount;
    Screen screen;

    public FileIO getFileIO() {
        return null;
    }


    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hebra = new Thread(this);
       // hebra.start();


        //button = new Button(this);
        //screen = getStartScreen();
        //button.setText("Touch me!");
        //button.setOnClickListener(this);
        //setContentView(button);
    }

    public void onClick(View v) {
        touchCount++;
        //button.setText("Touched me " + touchCount + " time(s)");
    }

    String mystring;

    public void setMyProperty(String mystring) {
        this.mystring = mystring;
    }

    private void DoSomething(String someValue) {
        setMyProperty(someValue);
    }

    @Override
    public void run() {
        while(!parar){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        parar = true;
        hebra = new Thread(this);
        hebra.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        parar = false;
        while (true) {
            try {
                hebra.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }


    /*createWindowAndUIComponent(); Input input = new Input();
    Graphics graphics = new Graphics(); Audio audio = new Audio();
    Screen currentScreen = new MainMenu(); float lastFrameTime = currentTime();
while( !userQuit() ) {
        float deltaTime = currentTime() – lastFrameTime;
        lastFrameTime = currentTime();
        currentScreen.updateState(input, deltaTime);
        currentScreen.present(graphics, audio, deltaTime);
    }
    cleanupResources();*/

    //TO DO: revisar
    Thread hebra;

    //Para avisar al compilador que el valor de este atributo
    //puede ser cambiado en una hebra
    volatile boolean parar = false;
}