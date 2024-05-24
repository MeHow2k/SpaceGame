package Gamestates;
import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class MenuLeaderboard {
    Font customFont;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("logo.gif")).getImage();
    Color selectedColor = new Color(255, 233, 12);
    Strings gameStrings;
    int initialX= C.FRAME_WIDTH / 2 - 300;
    public MenuLeaderboard() {
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
        g.setFont(customFont.deriveFont(45f));
        g.setColor(Color.white);
        int initialY=170;
            g.drawString(gameStrings.getString("Rekordy"), C.FRAME_WIDTH / 2 - 200, 50);

            g.setFont(customFont.deriveFont(33f));
            g.drawString("PLAYERNAME", initialX, initialY-70);
            g.drawString("SCORE", initialX+300, initialY-70);
            g.drawString("Date", initialX+500, initialY-70);

            g.setFont(customFont.deriveFont(25f));

            for (int i = 0; i < 10; i++){
                g.drawString("Player "+(i+1), initialX, initialY);
                g.drawString("SCORE", initialX+300, initialY);
                g.drawString("date", initialX+500, initialY);
                initialY+=50;
            }

        g.setFont(customFont.deriveFont(40f));
        g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Wyjście"), C.FRAME_WIDTH / 2 - 50, 725);
        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby powrócićENTER"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
    }

}