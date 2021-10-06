package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//Imports del juego
import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Screen;
//import es.ucm.gdv.blas.oses.carreau.ohno_practica1.Android.AndroidGame;


public class OhnoGame extends AppCompatActivity implements View.OnClickListener {
/*
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}*/
    Button button;
    int touchCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = new Button(this);
        button.setText("Touch me!");
        button.setOnClickListener(this);
        setContentView(button);
    }

    public void onClick(View v) {
        touchCount++;
        button.setText("Touched me " + touchCount + " time(s)");
    }

    String mystring;

    public void setMyProperty(String mystring) {
        this.mystring = mystring;
    }

    private void DoSomething(String someValue) {
        setMyProperty(someValue);
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

}