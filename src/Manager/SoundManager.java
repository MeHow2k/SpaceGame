//// klasa odpowiedzialna za dźwięk

package Manager;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    static public Clip clipback;
    public static void playBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/MMbackground.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        clipback = AudioSystem.getClip();//utworzenie obiektu clip
        clipback.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clipback.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -40.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clipback.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
        clipback.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void stopBackground(){
        clipback.stop();//zatrzymanie dźwięku
    }

}
