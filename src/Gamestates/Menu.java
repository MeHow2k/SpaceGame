package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Menu {
    Font customFont;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("logo.gif")).getImage();
    Color selectedColor = new Color(255, 233, 12);
    Strings gameStrings;
    public Menu() {
        gameStrings = new Strings();
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
        if(C.cursorPosition==0 && C.cursorPositionColumn==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Start"), C.FRAME_WIDTH/2-200,325);
        g.setColor(Color.white);
        if(C.cursorPosition==1 && C.cursorPositionColumn==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Jak grać?"),C.FRAME_WIDTH/2-200,425);
        g.setColor(Color.white);
        if(C.cursorPosition==2 && C.cursorPositionColumn==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Opcje"),C.FRAME_WIDTH/2-200,525);
        g.setColor(Color.white);
        if(C.cursorPosition==3 && C.cursorPositionColumn==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Autorzy"),C.FRAME_WIDTH/2-200,625);
        g.setColor(Color.white);
        if(C.cursorPositionColumn==1) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Osiągnięcia"), C.FRAME_WIDTH/2+100,625);
        g.setColor(Color.white);
        if(C.cursorPosition==4 && C.cursorPositionColumn==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Wyjście"),C.FRAME_WIDTH/2-200,725);
        g.setColor(Color.white);

        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby zatwierdzić"),C.FRAME_WIDTH-250,C.FRAME_HEIGHT-70);
        g.setFont(customFont.deriveFont(22f));
        g.drawString("Team PDM 2023/24   ver. "+C.VERSION,10,C.FRAME_HEIGHT-70);
    }

}