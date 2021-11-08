package es.ucm.gdv.blas.oses.carreau.lib;

import javax.sound.midi.MidiDeviceReceiver;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Music;

public class PCMusic implements Music  {


    public javax.sound.midi.Sequencer seq;
//To do esta clase
    public PCMusic(javax.sound.midi.Sequencer seq){
        this.seq = seq;
    }
    @Override
    public void play() {

        seq.start();
    }

    @Override
    public void stop() {
        seq.stop();

    }

    @Override
    public void pause() {
//seq.se
    }

    @Override
    public void setLooping(boolean looping) {


    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public boolean isPlaying() {
        return seq.isOpen();
    }

    @Override
    public boolean isStopped() {
        return seq.isRunning();
    }

    @Override
    public boolean isLooping() {
        return seq.getLoopCount()>0;
    }

    @Override
    public void dispose() {

    }
}
