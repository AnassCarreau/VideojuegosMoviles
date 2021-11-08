package es.ucm.gdv.blas.oses.carreau.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Audio;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Music;
import es.ucm.gdv.blas.oses.carreau.lib.Engine.Interfaces.Sound;


import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDeviceReceiver;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import  javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PCAudio implements Audio {
    @Override
    //TO DO
    public Music newMusic(String filename) {


        PCMusic m = null;

        try {
           // javax.sound.midi.MidiFileFormat img = javax.sound.midi.MidiSystem.getMidiFileFormat((new File("assets/" +filename)));
            //MidiDevice d;
            MidiFileFormat s = MidiSystem.getMidiFileFormat((new File("assets/" +filename)));





            //m = new PCMusic(img);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public Sound newSound(String filename) {
        PCSound s = null;

        try {
            AudioInputStream au = javax.sound.sampled.AudioSystem.getAudioInputStream(new File("assets/" + filename));
            Clip clip = AudioSystem.getClip();
            clip.open(au);
            s = new PCSound(clip);


        } catch (Exception e) {
            System.err.println(e);
        }
        return s;
    }

}
