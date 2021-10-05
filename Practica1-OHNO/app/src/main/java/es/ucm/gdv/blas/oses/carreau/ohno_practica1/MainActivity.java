package es.ucm.gdv.blas.oses.carreau.ohno_practica1;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    int touchCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    button = new Button(this);
    button.setText( "Touch me!" );
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
    private void DoSomething(String someValue){
        setMyProperty(someValue);
    }
}