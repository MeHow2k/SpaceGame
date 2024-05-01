package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class LanguageSelection {
    Font customFont;
    Strings gameStrings;
    Color selectedColor = new Color(255, 233, 12);
    Image pl = new ImageIcon(getClass().getClassLoader().getResource("pl.png")).getImage();
    Image en = new ImageIcon(getClass().getClassLoader().getResource("en.png")).getImage();
    public LanguageSelection() {
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

    public void draw(Graphics2D g) {

        g.setFont(customFont.deriveFont(70f));
        String text = gameStrings.getString("Wybierz język")+":";
        int textWidth = g.getFontMetrics().stringWidth(text); // Szerokość tekstu
        g.drawString(text, (C.FRAME_WIDTH - textWidth)/2,  100);

        g.setFont(customFont.deriveFont(60f));
        g.drawImage(en, C.FRAME_WIDTH / 2 - 75 - 200,  300, 150, 100, null);
        if(C.cursorLanguagePosition==0) g.setColor(selectedColor); else g.setColor(Color.white);
        g.drawString(gameStrings.getString("Angielski"), C.FRAME_WIDTH / 2 - 75, 360);
        g.drawImage(pl, C.FRAME_WIDTH / 2 - 75 - 200, 500, 150, 100, null);
        if(C.cursorLanguagePosition==1) g.setColor(selectedColor); else g.setColor(Color.white);
        g.drawString(gameStrings.getString("Polski"), C.FRAME_WIDTH / 2 - 75, 560);

        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby zatwierdzić"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
    }
}
