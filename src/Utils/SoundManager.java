//// klasa odpowiedzialna za dźwięk

package Utils;

import Constants.C;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    //todo zrobić zmiany (zamiast file -> getResource) by zrobić JAR (grafika jest jeszcze czcionka), playMenuBackground zrobione dla przykładu
    public static void playBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //File file = new File(("Sound/backgroundTheme.wav")); //pobranie pliku ze ścieżki
        String filename="backgroundTheme.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        //AudioInputStream ais = AudioSystem.getAudioInputStream(file);//utworzenie strumienia audio
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clipback = AudioSystem.getClip();//utworzenie obiektu clip
            clipback.open(ais);//otworzenie strumienia audio
            FloatControl gainControl = (FloatControl)//kontrola głośności
                    clipback.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
            clipback.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
            clipback.start();//rozpoczęcie odtwarzania dźwieku
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playMenuBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //File file = new File(("Sound/backgroundMenuTheme.wav")); //pobranie pliku ze ścieżki //todo stara wersja do usunbiecia
        String filename="backgroundMenuTheme.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename); //todo nowa wersja, do zmiany we wszystkim!
        //AudioInputStream ais = AudioSystem.getAudioInputStream(file); todo stara wersja! do usuniecia
        if(is!=null) {//todo dodaj warunek czy poprawnie wczytano plik
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));//todo nowa wersja,do zmiany we wszystkich! zmiana z (file) -> new buffered(is)
            clipbackmenu = AudioSystem.getClip();//utworzenie obiektu clip
            clipbackmenu.open(ais);//otworzenie strumienia audio
            FloatControl gainControl = (FloatControl)//kontrola głośności
                    clipbackmenu.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
            clipbackmenu.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
            clipbackmenu.start();//rozpoczęcie odtwarzania dźwieku
        }else {
            System.out.println("Cannot read sound file: "+filename);//todo //wyswietl log gdy nie wczytano
        }
    }
    public static void playBoss() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="backgroundBoss.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clipboss = AudioSystem.getClip();//utworzenie obiektu clip
            clipboss.open(ais);//otworzenie strumienia audio
            FloatControl gainControl = (FloatControl)//kontrola głośności
                    clipboss.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());//ustawienie wartości głośności | 0 min, -80.0 max |
            clipboss.loop(Clip.LOOP_CONTINUOUSLY);//ustawienie odtwarzania w pętli
            clipboss.start();//rozpoczęcie odtwarzania dźwieku
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void stopBackground(){
        if(clipback!=null)clipback.stop();//zatrzymanie dźwięku
    }
    public static void stopMenuBackground(){
        if(clipbackmenu!=null)clipbackmenu.stop();//zatrzymanie dźwięku
    }
    public static void stopBoss(){if(clipboss!=null)clipboss.stop();}//zatrzymanie muzyki bossa
    public static void stopAllMusic(){stopBackground();stopMenuBackground();stopBoss();}
    public static void playDefeat() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="defeat.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playEnemyHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="enemyHit.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playEnemyShot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="enemyShot.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playMeteorHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="meteorHit.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playPlayerHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="playerHit.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playPoint() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="point.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playPointBonus() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="pointBonus.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playPlayerShot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="playerShot.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playShield() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="shield.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playWeaponUpgrade() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="weaponUpgrade.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
    public static void playWin() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filename="win.wav";
        InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(filename);
        Clip clip;
        if(is!=null) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(checkMusicVolume());
            clip.start();
        }else {
            System.out.println("Cannot read sound file: "+filename);
        }
    }
}
