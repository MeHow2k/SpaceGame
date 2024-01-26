package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Menu {
    Font customFont;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("logo.gif")).getImage();
    public Menu() {
        try {
            //import czcionki
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/VT323-Regular.ttf")).deriveFont(60f);
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
        g.drawString("Start", C.FRAME_WIDTH/2-100,325);
        g.drawString("Jak grać?",C.FRAME_WIDTH/2-100,425);
        g.drawString("Opcje",C.FRAME_WIDTH/2-100,525);
        g.drawString("Autorzy",C.FRAME_WIDTH/2-100,625);
        g.drawString("Wyjdź",C.FRAME_WIDTH/2-100,725);

        g.setFont(customFont.deriveFont(18f));
        g.drawString("Aby zatwerdzić naciśnij ENTER",C.FRAME_WIDTH-250,C.FRAME_HEIGHT-70);

        g.drawString("Team PDM 2023",10,C.FRAME_HEIGHT-70);
    }

}