//// klasa odpowiedzialna za dźwięk

package Manager;

import Constants.C;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    static public Clip clipback;
    static public Clip clipbackmenu;
    static public Clip clipboss;
    static float checkSoundVolume() {
        if (C.isMuted==false) {
            if (C.soundVolume == 0) {
                return (float) -80.0;
            } else if (C.soundVolume == 1) {
                return (float) -45.0;
            } else if (C.soundVolume == 2) {
                return (float) -35.0;
            } else if (C.soundVolume == 3) {
                return (float) -25.0;
            } else if (C.soundVolume == 4) {
                return (float) -20.0;
            } else if (C.soundVolume == 5) {
                return (float) -16.5;
            } else if (C.soundVolume == 6) {
                return (float) -13.0;
            } else if (C.soundVolume == 7) {
                return (float) -10.0;
            } else if (C.soundVolume == 8) {
                return (float) -5.0;
            } else if (C.soundVolume == 9) {
                return (float) 0.0;
            } else return (float) -20.0;
        }else return (float) -80.0;
    }
    static float checkMusicVolume() {
        if (C.isMuted==false) {
            if (C.musicVolume == 0) {
                return (float) -80.0;
            } else if (C.musicVolume == 1) {
                return (float) -50.0;
            } else if (C.musicVolume == 2) {
                return (float) -45.0;
            } else if (C.musicVolume == 3) {
                return (float) -35.0;
            } else if (C.musicVolume == 4) {
                return (float) -30.0;
            } else if (C.musicVolume == 5) {
                return (float) -25.0;
            } else if (C.musicVolume == 6) {
                return (float) -20.0;
            } else if (C.musicVolume == 7) {
                return (float) -15.0;
            } else if (C.musicVolume == 8) {
                return (float) -10.0;
            } else if (C.musicVolume == 9) {
                return (float) -5.0;
            } else return (float) -40.0;
        }
        return (float) -80.0;
    }
    public static void playBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/backgroundTheme.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        clipback = AudioSystem.getClip();//utworzenie obiektu clip
        clipback.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clipback.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkMusicVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkMusicVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
        clipbackmenu.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
        clipbackmenu.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void playBoss() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/backgroundBoss.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        clipboss = AudioSystem.getClip();//utworzenie obiektu clip
        clipboss.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clipboss.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkMusicVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
        clipboss.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
        clipboss.start();//rozpoczęcie odtwarzania dźwieku
    }
    public static void stopBackground(){
        if(clipback!=null)clipback.stop();//zatrzymanie dźwięku
    }
    public static void stopMenuBackground(){
        if(clipbackmenu!=null)clipbackmenu.stop();//zatrzymanie dźwięku
    }
    public static void stopBoss(){if(clipboss!=null)clipboss.stop();}//zatrzymanie muzyki bossa
    public static void stopAllMusic(){stopBackground();stopMenuBackground();}
    public static void playDefeat() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(("Sound/defeat.wav")); //pobranie pliku ze ścieżki
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        Clip clip;
        clip = AudioSystem.getClip();//utworzenie obiektu clip
        clip.open(ais);//otworzenie strumienia audio
        FloatControl gainControl = (FloatControl)//kontrola głośności
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
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
        gainControl.setValue(checkSoundVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
        clip.start();//rozpoczęcie odtwarzania dźwieku
    }
}
