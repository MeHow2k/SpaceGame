//klasa odpowiedzialna za ramkę aplikacji i tworzącą w niej gamepanel
package Gamestates;

import Constants.C;
import Constants.Scores;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


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
        // Wczytanie ustawień z pliku ustawień
        File configFile = new File("config.dat");
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }
        try {
            byte[] configBytes = readEncryptedFile(configFile);
            String decryptedConfig = decrypt(configBytes, C.SECRETKEY);

                // Przetworzenie zdekodowanych ustawień
                String[] configLines = decryptedConfig.split("\n");
                C.musicVolume = Integer.parseInt(configLines[0]);
                C.soundVolume = Integer.parseInt(configLines[1]);
                C.isMuted = Boolean.parseBoolean(configLines[2]);
                C.playerSkin = Integer.parseInt(configLines[3]);
                C.LANGUAGE = Integer.parseInt(configLines[4]);
                C.isFPSon = Boolean.parseBoolean(configLines[5]);
        }catch (Exception e){
            System.out.println("error loading file: "+configFile.getName());C.musicVolume=0;C.soundVolume=0;C.isMuted=false;C.playerSkin=0;C.LANGUAGE=999;C.isFPSon=false;
        }
        // Wczytanie najlepszego wyniku i nazwy gracza z pliku
        File dataFile = new File("data.dat");
        if (!dataFile.exists()) {
            createDefaultData(dataFile);
        }
        try {
            byte[] dataBytes = readEncryptedFile(dataFile);
            String decryptedData = decrypt(dataBytes, C.SECRETKEY);

            // Przetworzenie zdekodowanego wyniku
            String[] dataLines = decryptedData.split("\n");
            C.highscorePoints = Integer.parseInt(dataLines[0]);
            C.highscoreLevel = Integer.parseInt(dataLines[1]);
            C.PLAYER_NAME = dataLines[2];
        }catch (Exception e){System.out.println("error loading file: "+dataFile.getName());C.PLAYER_NAME="";C.highscoreLevel=0;C.highscorePoints=0;}

        // Wczytanie najlepszych wynikow
        File scoreFile = new File("data2.dat");
        if (!scoreFile.exists()) {
            createDefaultData2(scoreFile);
        }
        try {
            byte[] scoreBytes = readEncryptedFile(scoreFile);
            String decryptedScore = decrypt(scoreBytes, C.SECRETKEY);

            // Przetworzenie zdekodowanych ustawień
            String[] scoreLines = decryptedScore.split("\n");
            for (String line : scoreLines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String playerName = parts[0];
                    int totalPoints = Integer.parseInt(parts[1]);
                    String playTime = parts[2];
                    C.scoresList.add(new Scores(playerName, totalPoints, playTime));
                }
            }
        }catch (Exception e){
            System.out.println("error loading file: "+scoreFile.getName());
        }

        //wczytanie osiągnięć
        File data1File = new File("data1.dat");
        if (!data1File.exists()) {
            createDefaultData1(data1File);
        }
        try {
            byte[] dataBytes = readEncryptedFile(data1File);
            String decryptedData = decrypt(dataBytes, C.SECRETKEY);

            // Przetworzenie zdekodowanego wyniku
            String[] dataLines = decryptedData.split("\n");
            C.ARCH_PLAYER_NAME = dataLines[0];

            if(C.ARCH_PLAYER_NAME.equals(C.PLAYER_NAME)) {
                String achievementsString = dataLines[1];
                for (int i = 0; i < achievementsString.length(); i++) {
                    C.achievements[i] = Integer.parseInt(achievementsString.charAt(i) + ""); // 2 linijka,tablica achieve
                }
                //załadowanie osiagniec z wczytanej tablicy
                if (C.achievements[0]==1) C.isAchivement0done=true;
                if (C.achievements[1]==1) C.isAchivement1done=true;
                if (C.achievements[2]==1) C.isAchivement2done=true;
                if (C.achievements[3]==1) C.isAchivement3done=true;
                if (C.achievements[4]==1) C.isAchivement4done=true;
                if (C.achievements[5]==1) C.isAchivement5done=true;
                if (C.achievements[6]==1) C.isAchivement6done=true;
                if (C.achievements[7]==1) C.isAchivement7done=true;
                if (C.achievements[8]==1) C.isAchivement8done=true;
                if (C.achievements[9]==1) C.isAchivement9done=true;
                if (C.achievements[10]==1) C.isAchivement10done=true;
                if (C.achievements[11]==1) C.isAchivement11done=true;
                if (C.achievements[12]==1) C.isAchivement12done=true;
                if (C.achievements[13]==1) C.isAchivement13done=true;
                if (C.achievements[14]==1) C.isAchivement14done=true;
                if (C.achievements[15]==1) C.isAchivement15done=true;
                if (C.achievements[16]==1) C.isAchivement16done=true;
                if (C.achievements[17]==1) C.isAchivement17done=true;
                if (C.achievements[18]==1) C.isAchivement18done=true;
                if (C.achievements[19]==1) C.isAchivement19done=true;
                if (C.achievements[20]==1) C.isAchivement20done=true;
                if (C.achievements[21]==1) C.isAchivement21done=true;
                if (C.achievements[22]==1) C.isAchivement22done=true;
                if (C.achievements[23]==1) C.isAchivement23done=true;
            }else {
                System.out.println("Incorrect player's achievements... Resetting file");
                createDefaultData1(data1File);
            }
        }catch (Exception e){
            System.out.println("error loading file: "+data1File.getName());
            // Ustawienie domyślnych wartości
            for (int i = 0; i < C.achievements.length; i++) {
                C.achievements[i] = 0;
            }
        }
        new Frame();
    }



    private static void createDefaultConfig(File configFile) {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            // Domyślne ustawienia
            String defaultConfig = "4\n4\nfalse\n0\n999\nfalse\n";
            byte[] encryptedConfig = encrypt(defaultConfig, C.SECRETKEY);
            fos.write(encryptedConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDefaultData(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Domyślne wyniki
            String defaultData = "0\n0\n"+C.PLAYER_NAME+"\n";
            byte[] encryptedData = encrypt(defaultData, C.SECRETKEY);
            fos.write(encryptedData);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static void createDefaultData1(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Domyślne wyniki
            String defaultData = C.PLAYER_NAME+"\n000000000000000000000000000000\n";
            byte[] encryptedData = encrypt(defaultData, C.SECRETKEY);
            fos.write(encryptedData);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static void createDefaultData2(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Domyślne wyniki
            String defaultData = "Canis Majoris,35000,10:30\n" +
                    "Betelgeuse,32000,09:30\n" +
                    "Sirius,27000,09:00\n" +
                    "Jupiter,25000,08:45\n" +
                    "Saturn,20000,08:30\n" +
                    "Neptune,17000,08:15\n" +
                    "Uranus,14000,08:00\n" +
                    "Venus,10000,07:45\n" +
                    "Mars,5000,07:30\n" +
                    "Alpha Centauri,2000,07:00\n";
            byte[] encryptedData = encrypt(defaultData, C.SECRETKEY);
            fos.write(encryptedData);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] readEncryptedFile(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try (FileInputStream fis = new FileInputStream(file)) {
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        return baos.toByteArray();
    }

    private static byte[] encrypt(String input, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String decrypt(byte[] encryptedData, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}