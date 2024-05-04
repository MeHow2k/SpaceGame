//klasa odpowiedzialna za ramkę aplikacji i tworzącą w niej gamepanel
package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Frame extends JFrame {
    Frame(){
        super("Space Game");
        //Image icon = new ImageIcon(getClass().getClassLoader().getResource(".png")).getImage();
        //setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(C.FRAME_WIDTH,C.FRAME_HEIGHT); // szerokosc i wysokość okna do zmiany w Constants.C
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        GamePanel gamePanel=new GamePanel();
        setContentPane(gamePanel);
        gamePanel.setFocusable(true);
        setVisible(true);
    }
    public static void main(String[] args)  {
        //wczytanie ustawień z pliku ustawień
        try {
            File config = new File("config.txt");
            String absolutePath = config.getAbsolutePath();
            if (!config.exists()) {
                config.createNewFile();
                // jesli nie ma pliku - stwórz go
                try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
                    // Ustaw domyślne wartości głośności muzyki, głośności dzwięków i wyciszenia
                    fos.write("4\n".getBytes()); // Domyślna głośność muzyki
                    fos.write("4\n".getBytes()); // Domyślna głośność dzwięków
                    fos.write("false\n".getBytes());  // Domyślne brak wyciszenia
                    fos.write("0\n".getBytes());  // Domyślny skin 0
                    fos.write("999\n".getBytes());  // Domyślny język 999- żaden, aby przy uruchomieniu został wybrany przez użytkownika
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Scanner scanner = new Scanner(new FileInputStream(absolutePath),"UTF-8");

            // sprawdzanie czy linia tekstu istnieje

            while(scanner.hasNextLine()){
                //kazda linia pliku odpowiada za inne ustawienie
                //pierwsza za glosnosc muzyki
                C.musicVolume=Integer.parseInt(scanner.nextLine());
                //druga za glosnosc dzwiekow
                C.soundVolume=Integer.parseInt(scanner.nextLine());
                //trzecia calkowite wyciszenie
                String muteOption = scanner.nextLine();
                if(muteOption.equals("false")) C.isMuted=false;
                else C.isMuted=true;
                //wybrana skórka
                C.playerSkin=Integer.parseInt(scanner.nextLine());
                //wybrany język
                C.LANGUAGE=Integer.parseInt(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Cant find config file.");
        }catch (NoSuchElementException e) {
            System.out.println("Config file is broken. Restoring default config...");
            C.musicVolume=4;C.soundVolume=4;C.isMuted=false;C.playerSkin=0;C.LANGUAGE=99;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //wczytanie najlepszego wyniku z ustawień
        try {
            File highscore = new File("highscore.txt");
            String absolutePath = highscore.getAbsolutePath();
            // Jeśli plik nie istnieje, stwórz go
            if (!highscore.exists()) {
                highscore.createNewFile();
                try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
                    // Ustaw domyślne wartości
                    fos.write("0\n".getBytes()); // rekord punktów
                    fos.write("0\n".getBytes()); // rekord poziomu
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Scanner scanner = new Scanner(new FileInputStream(absolutePath),"UTF-8");
            // sprawdzanie czy linia tekstu istnieje
            while(scanner.hasNextLine()){
                //ilosc punktow
                C.highscorePoints=Integer.parseInt(scanner.nextLine());
                //jaki poziom
                C.highscoreLevel=Integer.parseInt(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cant find highscore file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Frame();
    }
}