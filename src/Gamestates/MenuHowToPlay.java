package Gamestates;

import Constants.C;

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
    Image imgZycie = new ImageIcon(getClass().getClassLoader().getResource("life.png")).getImage();

    Font customFont;
    public MenuHowToPlay() {
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
        g.drawString("Jak grać?", C.FRAME_WIDTH/2-200,50);

        g.setFont(customFont.deriveFont(25f));
        g.drawString("Poruszasz się za pomocą STRZAŁEK. Strzelasz za pomocą SPACJI.",30,100);

        g.drawString("Podczas gry musisz niszczyć statki wrogów, unikając ich strzałów.",30,170);
        if(imgWrog !=null) g.drawImage(imgWrog, 100,200,50,50, null);
        if(imgStrzalWrog !=null)g.drawImage(imgStrzalWrog, 180,200,50,50, null);
        g.drawString("Gra kończy się po utracie wszystkich żyć.",30,300);

        g.drawString("Podczas gry można zebrać rożne bonusy:",180,340);

        g.drawString("Dodatkowe punkty",30,380);
        if(imgPunkt !=null)g.drawImage(imgPunkt, 30,410,30,30, null);

        g.drawString("Dodatkowe życie",230,380);
        if(imgZycie !=null)g.drawImage(imgZycie, 230,410,30,30, null);
        g.setFont(customFont.deriveFont(25f));
        g.drawString("Aby zapauzować/odpauzować naciśnij \"P\".   Aby wyjść z gry naciśnij \"ESC\".",30,700);

        g.setColor(selectedColor);
        g.setFont(customFont.deriveFont(18f));
        g.drawString("Aby powrócić do menu naciśnij ENTER",C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);

    }
}
