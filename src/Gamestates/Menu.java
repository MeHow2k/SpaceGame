package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Menu {
    Font customFont;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("logo.gif")).getImage();
    Color selectedColor = new Color(255, 233, 12);
    public Menu() {
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
        g.drawImage(logo,C.FRAME_WIDTH/2-260,50,480,280,null);
        g.setFont(customFont.deriveFont(15f));
        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(35f));
        if(C.cursorPosition==0) g.setColor(selectedColor);
        g.drawString("Start", C.FRAME_WIDTH/2-100,325);
        g.setColor(Color.white);
        if(C.cursorPosition==1) g.setColor(selectedColor);
        g.drawString("Jak grać?",C.FRAME_WIDTH/2-100,425);
        g.setColor(Color.white);
        if(C.cursorPosition==2) g.setColor(selectedColor);
        g.drawString("Opcje",C.FRAME_WIDTH/2-100,525);
        g.setColor(Color.white);
        if(C.cursorPosition==3) g.setColor(selectedColor);
        g.drawString("Autorzy",C.FRAME_WIDTH/2-100,625);
        g.setColor(Color.white);
        if(C.cursorPosition==4) g.setColor(selectedColor);
        g.drawString("Wyjdź",C.FRAME_WIDTH/2-100,725);
        g.setColor(Color.white);

        g.setFont(customFont.deriveFont(18f));
        g.drawString("Aby zatwerdzić naciśnij ENTER",C.FRAME_WIDTH-250,C.FRAME_HEIGHT-70);
        g.setFont(customFont.deriveFont(22f));
        g.drawString("Team PDM 2023/24   ver. "+C.VERSION,10,C.FRAME_HEIGHT-70);
    }

}