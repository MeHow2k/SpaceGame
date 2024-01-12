//// klasa odpowiedzialna za dźwięk

package Manager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    static public Clip clipback;
    static public Clip clipbackmenu;
    public static void playBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/backgroundTheme.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        clipback = AudioSystem.getClip();//utworzenie obiektu clip
        clipback.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clipback.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clipback.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
        clipback.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playMenuBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/backgroundMenuTheme.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        clipbackmenu = AudioSystem.getClip();//utworzenie obiektu clip
        clipbackmenu.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clipbackmenu.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clipbackmenu.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
        clipbackmenu.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void stopBackground(){
        clipback.stop();//zatrzymanie dźwięku
    }
    public static void stopMenuBackground(){
        clipbackmenu.stop();//zatrzymanie dźwięku
    }
    public static void stopAllMusic(){clipback.stop();clipbackmenu.stop();}
    public static void playDefeat() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/defeat.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playEnemyHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/enemyHit.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playEnemyShot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/enemyShot.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playPlayerHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/playerHit.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playPoint() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/point.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playPlayerShot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/playerShot.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playWin() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/win.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) -10.0);//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
}
