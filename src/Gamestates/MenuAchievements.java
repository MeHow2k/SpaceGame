package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MenuAchievements {
    Font customFont;
    Image imgAch0 = new ImageIcon(getClass().getClassLoader().getResource("ach0.png")).getImage();//win game
    Image imgAch1 = new ImageIcon(getClass().getClassLoader().getResource("ach1.png")).getImage();//10lv
    Image imgAch2 = new ImageIcon(getClass().getClassLoader().getResource("ach2.png")).getImage();//20lv
    Image imgAch3 = new ImageIcon(getClass().getClassLoader().getResource("ach3.png")).getImage();//30lv
    Image imgAch4 = new ImageIcon(getClass().getClassLoader().getResource("ach4.png")).getImage();//40lv
    Image imgAch5 = new ImageIcon(getClass().getClassLoader().getResource("ach5.png")).getImage();//50lv
    Image imgAch6 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();//tutaj i niżej nie zaimplementowane
    Image imgAch7 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch8 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch9 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch10 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch11 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch12 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch13 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch14 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch15 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch16 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch17 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch18 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch19 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch20 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch21 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch22 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Image imgAch23 = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
    Color selectedColor = new Color(255, 233, 12);
    Strings gameStrings;

    int achPreviewX = 100, achPreviewY = 100, achDescriptionX = C.FRAME_WIDTH / 2 - 100, achDescriptionY = 140;


    public MenuAchievements() {
        loadAchievementImages();
        gameStrings = new Strings();
        try {
            //import czcionki
            InputStream fontStream = getClass().getResourceAsStream("/VT323-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g) {
        // g.drawImage(logo,C.FRAME_WIDTH/2-260,5,480,280,null);
        g.setFont(customFont.deriveFont(15f));
        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(65f));
        g.drawString(gameStrings.getString("Osiągnięcia"), C.FRAME_WIDTH / 2 - 260, 50);
        //g.drawString(C.cursorAchievementsRow + " / " + C.cursorAchievementsColumn, C.FRAME_WIDTH - 300, 50);//todo licznik zrob
        g.setFont(customFont.deriveFont(25f));
        //g.drawString("achivement title", C.FRAME_WIDTH / 2 - 100, 120);

        //obrazek aktuanego wygladu gracza:
        if (C.cursorAchievementsRow == 0) {
            if (C.cursorAchievementsColumn == 0) g.drawImage(imgAch0, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 1) g.drawImage(imgAch1, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 2) g.drawImage(imgAch2, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 3) g.drawImage(imgAch3, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 4) g.drawImage(imgAch4, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 5) g.drawImage(imgAch5, achPreviewX, achPreviewY, 100, 100, null);
        } else if (C.cursorAchievementsRow == 1) {
            if (C.cursorAchievementsColumn == 0) g.drawImage(imgAch6, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 1) g.drawImage(imgAch7, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 2) g.drawImage(imgAch8, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 3) g.drawImage(imgAch9, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 4) g.drawImage(imgAch10, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 5) g.drawImage(imgAch11, achPreviewX, achPreviewY, 100, 100, null);
        }else if (C.cursorAchievementsRow == 2) {
            if (C.cursorAchievementsColumn == 0) g.drawImage(imgAch12, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 1) g.drawImage(imgAch13, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 2) g.drawImage(imgAch14, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 3) g.drawImage(imgAch15, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 4) g.drawImage(imgAch16, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 5) g.drawImage(imgAch17, achPreviewX, achPreviewY, 100, 100, null);
        }else if (C.cursorAchievementsRow == 3) {
            if (C.cursorAchievementsColumn == 0) g.drawImage(imgAch18, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 1) g.drawImage(imgAch19, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 2) g.drawImage(imgAch20, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 3) g.drawImage(imgAch21, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 4) g.drawImage(imgAch22, achPreviewX, achPreviewY, 100, 100, null);
            if (C.cursorAchievementsColumn == 5) g.drawImage(imgAch23, achPreviewX, achPreviewY, 100, 100, null);
        }

        //opis ach
        g.setFont(customFont.deriveFont(30f));

        if (C.cursorAchievementsRow == 0) {
            if (C.cursorAchievementsColumn == 0) g.drawString(gameStrings.getString("Ach0opis"), achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 1) g.drawString(gameStrings.getString("Ach1opis"), achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 2) g.drawString(gameStrings.getString("Ach2opis"), achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 3) g.drawString(gameStrings.getString("Ach3opis"), achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 4) g.drawString(gameStrings.getString("Ach4opis"), achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 5) g.drawString(gameStrings.getString("Ach5opis"), achDescriptionX, achDescriptionY);
        } else if (C.cursorAchievementsRow == 1) {
            if (C.cursorAchievementsColumn == 0) g.drawString("? ", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 1) g.drawString("? ", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 2) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 3) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 4) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 5) g.drawString("?", achDescriptionX, achDescriptionY);
        }else if (C.cursorAchievementsRow == 2) {
            if (C.cursorAchievementsColumn == 0) g.drawString("? ", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 1) g.drawString("? ", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 2) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 3) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 4) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 5) g.drawString("?", achDescriptionX, achDescriptionY);
        }else if (C.cursorAchievementsRow == 3) {
            if (C.cursorAchievementsColumn == 0) g.drawString("? ", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 1) g.drawString("? ", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 2) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 3) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 4) g.drawString("?", achDescriptionX, achDescriptionY);
            if (C.cursorAchievementsColumn == 5) g.drawString("?", achDescriptionX, achDescriptionY);
        }

        g.setFont(customFont.deriveFont(35f));

        //lista skinów jako elementy do wybrania przez gracza:
        g.drawString(gameStrings.getString("Lista osiągnięć")+": " , C.FRAME_WIDTH / 2 - 350, 250);
        g.drawImage(imgAch0, C.FRAME_WIDTH / 2 - 350, 300, 50, 50, null);
        g.drawImage(imgAch1, C.FRAME_WIDTH / 2 - 225, 300, 50, 50, null);
        g.drawImage(imgAch2, C.FRAME_WIDTH / 2 - 100, 300, 50, 50, null);
        g.drawImage(imgAch3, C.FRAME_WIDTH / 2 + 25, 300, 50, 50, null);
        g.drawImage(imgAch4, C.FRAME_WIDTH / 2 + 150, 300, 50, 50, null);
        g.drawImage(imgAch5, C.FRAME_WIDTH / 2 + 275, 300, 50, 50, null);
        //2gi rząd
        g.drawImage(imgAch6, C.FRAME_WIDTH / 2 - 350, 400, 50, 50, null);
        g.drawImage(imgAch7, C.FRAME_WIDTH / 2 - 225, 400, 50, 50, null);
        g.drawImage(imgAch8, C.FRAME_WIDTH / 2 - 100, 400, 50, 50, null);
        g.drawImage(imgAch9, C.FRAME_WIDTH / 2 + 25, 400, 50, 50, null);
        g.drawImage(imgAch10, C.FRAME_WIDTH / 2 + 150, 400, 50, 50, null);
        g.drawImage(imgAch11, C.FRAME_WIDTH / 2 + 275, 400, 50, 50, null);
        //3ci rząd
        g.drawImage(imgAch12, C.FRAME_WIDTH / 2 - 350, 500, 50, 50, null);
        g.drawImage(imgAch13, C.FRAME_WIDTH / 2 - 225, 500, 50, 50, null);
        g.drawImage(imgAch14, C.FRAME_WIDTH / 2 - 100, 500, 50, 50, null);
        g.drawImage(imgAch15, C.FRAME_WIDTH / 2 + 25, 500, 50, 50, null);
        g.drawImage(imgAch16, C.FRAME_WIDTH / 2 + 150, 500, 50, 50, null);
        g.drawImage(imgAch17, C.FRAME_WIDTH / 2 + 275, 500, 50, 50, null);
        //4ty rząd
        g.drawImage(imgAch18, C.FRAME_WIDTH / 2 - 350, 600, 50, 50, null);
        g.drawImage(imgAch19, C.FRAME_WIDTH / 2 - 225, 600, 50, 50, null);
        g.drawImage(imgAch20, C.FRAME_WIDTH / 2 - 100, 600, 50, 50, null);
        g.drawImage(imgAch21, C.FRAME_WIDTH / 2 + 25, 600, 50, 50, null);
        g.drawImage(imgAch22, C.FRAME_WIDTH / 2 + 150, 600, 50, 50, null);
        g.drawImage(imgAch23, C.FRAME_WIDTH / 2 + 275, 600, 50, 50, null);
        g.setFont(customFont.deriveFont(45f));
        g.setColor(selectedColor);
        if (C.cursorAchievementsRow == 0) {
            if (C.cursorAchievementsColumn == 0) g.drawRect(C.FRAME_WIDTH / 2 - 350 - 25, 300 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 1) g.drawRect(C.FRAME_WIDTH / 2 - 225 - 25, 300 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 2) g.drawRect(C.FRAME_WIDTH / 2 - 100 - 25, 300 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 3) g.drawRect(C.FRAME_WIDTH / 2 + 25 - 25, 300 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 4) g.drawRect(C.FRAME_WIDTH / 2 + 150 - 25, 300 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 5) g.drawRect(C.FRAME_WIDTH / 2 + 275 - 25, 300 - 25, 100, 100);
        } else if (C.cursorAchievementsRow == 1) {
            if (C.cursorAchievementsColumn == 0) g.drawRect(C.FRAME_WIDTH / 2 - 350 - 25, 400 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 1) g.drawRect(C.FRAME_WIDTH / 2 - 225 - 25, 400 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 2) g.drawRect(C.FRAME_WIDTH / 2 - 100 - 25, 400 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 3) g.drawRect(C.FRAME_WIDTH / 2 + 25 - 25, 400 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 4) g.drawRect(C.FRAME_WIDTH / 2 + 150 - 25, 400 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 5) g.drawRect(C.FRAME_WIDTH / 2 + 275 - 25, 400 - 25, 100, 100);
        } else if (C.cursorAchievementsRow == 2) {
            if (C.cursorAchievementsColumn == 0) g.drawRect(C.FRAME_WIDTH / 2 - 350 - 25, 500 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 1) g.drawRect(C.FRAME_WIDTH / 2 - 225 - 25, 500 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 2) g.drawRect(C.FRAME_WIDTH / 2 - 100 - 25, 500 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 3) g.drawRect(C.FRAME_WIDTH / 2 + 25 - 25, 500 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 4) g.drawRect(C.FRAME_WIDTH / 2 + 150 - 25, 500 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 5) g.drawRect(C.FRAME_WIDTH / 2 + 275 - 25, 500 - 25, 100, 100);
        } else if (C.cursorAchievementsRow == 3) {
            if (C.cursorAchievementsColumn == 0) g.drawRect(C.FRAME_WIDTH / 2 - 350 - 25, 600 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 1) g.drawRect(C.FRAME_WIDTH / 2 - 225 - 25, 600 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 2) g.drawRect(C.FRAME_WIDTH / 2 - 100 - 25, 600 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 3) g.drawRect(C.FRAME_WIDTH / 2 + 25 - 25, 600 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 4) g.drawRect(C.FRAME_WIDTH / 2 + 150 - 25, 600 - 25, 100, 100);
            if (C.cursorAchievementsColumn == 5) g.drawRect(C.FRAME_WIDTH / 2 + 275 - 25, 600 - 25, 100, 100);
        }
        g.setColor(Color.white);
        if (C.cursorAchievementsRow == 4) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Wyjście"), C.FRAME_WIDTH / 2 - 50, 725);
        g.setColor(Color.white);

        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby zatwierdzić"), C.FRAME_WIDTH - 250, C.FRAME_HEIGHT - 70);
        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby powrócićESC"), C.FRAME_WIDTH - 250, C.FRAME_HEIGHT - 50);
    }

    private Image convertToLocked(Image originalImage) {
        Image locked = new ImageIcon(getClass().getClassLoader().getResource("locked.png")).getImage();
        return locked;
    }
    private void loadAchievementImages() {
        if (!C.isAchivement0done) {
            imgAch0 = convertToLocked(imgAch0);
        }
        if (!C.isAchivement1done) {
            imgAch1 = convertToLocked(imgAch1);
        }
        if (!C.isAchivement2done) {
            imgAch2 = convertToLocked(imgAch2);
        }
        if (!C.isAchivement3done) {
            imgAch3 = convertToLocked(imgAch3);
        }
        if (!C.isAchivement4done) {
            imgAch4 = convertToLocked(imgAch4);
        }
        if (!C.isAchivement5done) {
            imgAch5 = convertToLocked(imgAch5);
        }
        //todo dodaj reszte
    }
}
