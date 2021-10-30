package es.ucm.gdv.blas.oses.carreau.lib;


import java.util.List;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Input;

public class PCInput implements Input {

    private List<TouchEvent> events;

    public PCInput(){

    }

    @Override
    public boolean isTouchDown(int pointer) {
        return false;
    }

    @Override
    public int getTouchX(int pointer) {
        return 0;
    }

    @Override
    public int getTouchY(int pointer) {
        return 0;
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return events;
    }
}
