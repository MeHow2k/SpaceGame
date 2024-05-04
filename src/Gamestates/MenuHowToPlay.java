package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MenuHowToPlay {
    Color selectedColor = new Color(255, 233, 12);
    Image imgPunkt = new ImageIcon(getClass().getClassLoader().getResource("point.gif")).getImage();
    Image imgWrog = new ImageIcon(getClass().getClassLoader().getResource("enemy.gif")).getImage();
    Image imgStrzalWrog = new ImageIcon(getClass().getClassLoader().getResource("EnemyShot.gif")).getImage();
    Image imgZycie = new ImageIcon(getClass().getClassLoader().getResource("lifeBonus.gif")).getImage();
    Image imgTarcza = new ImageIcon(getClass().getClassLoader().getResource("shieldBonus.gif")).getImage();
    Image imgWeaponUpgrade = new ImageIcon(getClass().getClassLoader().getResource("weaponBonus.gif")).getImage();
    Image imgWeaponFirerate = new ImageIcon(getClass().getClassLoader().getResource("weaponFirerateBoost.gif")).getImage();
    Image imgAlly = new ImageIcon(getClass().getClassLoader().getResource("bonusAllyAId.gif")).getImage();
    Image imgAllyShip = new ImageIcon(getClass().getClassLoader().getResource("allyLeft.gif")).getImage();
    Image imgEnemyLaser = new ImageIcon(getClass().getClassLoader().getResource("enemyLaserRight.gif")).getImage();
    Image imgMeteor = new ImageIcon(getClass().getClassLoader().getResource("meteor.gif")).getImage();
    Font customFont;
    Strings gameStrings;
    public MenuHowToPlay() {
        gameStrings = new Strings();
        //import czcionki
        try {
            //import czcionki
            InputStream fontStream = getClass().getResourceAsStream("/VT323-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

    }

    public void draw (Graphics2D g){
        g.setFont(customFont.deriveFont(60f));
        g.setColor(Color.white);
        if(C.LANGUAGE==1) {//PL
            g.drawString("Jak Grać?", C.FRAME_WIDTH / 2 - 200, 50);

            g.setFont(customFont.deriveFont(25f));
            g.drawString("Poruszasz się za pomocą STRZAŁEK. Strzelasz za pomocą SPACJI.", 30, 100);

            g.drawString("Podczas gry musisz niszczyć statki wrogów, unikając ich strzałów.", 30, 170);
            if (imgWrog != null) g.drawImage(imgWrog, 100, 200, 50, 50, null);
            if (imgStrzalWrog != null) g.drawImage(imgStrzalWrog, 180, 200, 50, 50, null);
            if (imgMeteor != null) g.drawImage(imgMeteor, 340, 200, 50, 50, null);
            if (imgEnemyLaser != null) g.drawImage(imgEnemyLaser, 260, 200, 50, 50, null);
            g.drawString("Gra kończy się po utracie wszystkich żyć.", 30, 300);

            g.drawString("Podczas gry można zebrać rożne bonusy:", 180, 340);

            g.drawString("Dodatkowe punkty", 30, 380);
            if (imgPunkt != null) g.drawImage(imgPunkt, 100, 410, 30, 30, null);

            g.drawString("Dodatkowe życie", 230, 380);
            if (imgZycie != null) g.drawImage(imgZycie, 290, 410, 30, 30, null);

            g.drawString("Dodatkowa tarcza", 480, 380);
            if (imgTarcza != null) g.drawImage(imgTarcza, 550, 410, 30, 30, null);

            g.drawString("Ulepszenie broni", 30, 480);
            if (imgWeaponUpgrade != null) g.drawImage(imgWeaponUpgrade, 100, 510, 30, 30, null);

            g.drawString("Zwiększ szybkostrzelność", 230, 480);
            if (imgWeaponFirerate != null) g.drawImage(imgWeaponFirerate, 290, 510, 30, 30, null);

            g.drawString("Pomoc sojusznika", 480, 480);
            if (imgAlly != null) g.drawImage(imgAlly, 550, 510, 30, 30, null);

            g.drawString("Oczekuj na zrzutu od sojuszniczego statku!", 30, 580);
            if (imgAllyShip != null) g.drawImage(imgAllyShip, 200, 610, 125, 25, null);

            g.setFont(customFont.deriveFont(25f));
            g.drawString("Aby zapauzować/odpauzować naciśnij \"P\".   Aby wyjść z gry naciśnij \"ESC\".", 30, 700);
        } else {////EN
            g.drawString("How To Play?", C.FRAME_WIDTH / 2 - 200, 50);

            g.setFont(customFont.deriveFont(25f));
            g.drawString("You move with the ARROWS. You shoot with the SPACEBAR.", 30, 100);

            g.drawString("You have to destroy enemies' spaceships by avoiding their shots.", 30, 170);
            if (imgWrog != null) g.drawImage(imgWrog, 100, 200, 50, 50, null);
            if (imgStrzalWrog != null) g.drawImage(imgStrzalWrog, 180, 200, 50, 50, null);
            if (imgMeteor != null) g.drawImage(imgMeteor, 340, 200, 50, 50, null);
            if (imgEnemyLaser != null) g.drawImage(imgEnemyLaser, 260, 200, 50, 50, null);
            g.drawString("The game ends when you lose all your lives.", 30, 300);

            g.drawString("Various bonuses can be acquired during the game:", 180, 340);

            g.drawString("Additional points", 30, 380);
            if (imgPunkt != null) g.drawImage(imgPunkt, 100, 410, 30, 30, null);

            g.drawString("Additional life", 230, 380);
            if (imgZycie != null) g.drawImage(imgZycie, 290, 410, 30, 30, null);

            g.drawString("Additional shield", 480, 380);
            if (imgTarcza != null) g.drawImage(imgTarcza, 550, 410, 30, 30, null);

            g.drawString("Weapon upgrade", 30, 480);
            if (imgWeaponUpgrade != null) g.drawImage(imgWeaponUpgrade, 100, 510, 30, 30, null);

            g.drawString("Improved firerate", 230, 480);
            if (imgWeaponFirerate != null) g.drawImage(imgWeaponFirerate, 290, 510, 30, 30, null);

            g.drawString("Ally aid", 480, 480);
            if (imgAlly != null) g.drawImage(imgAlly, 550, 510, 30, 30, null);

            g.drawString("Expect a drop from an allied spaceship!", 30, 580);
            if (imgAllyShip != null) g.drawImage(imgAllyShip, 200, 610, 125, 25, null);

            g.setFont(customFont.deriveFont(25f));
            g.drawString("To pause/unpause press \"P\".   To exit the game press \"ESC\".", 30, 700);
        }
        g.setColor(selectedColor);
        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby powrócićENTER"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);

    }
}
