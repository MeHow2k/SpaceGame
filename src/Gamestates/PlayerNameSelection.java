package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class PlayerNameSelection {
    Font customFont;
    Strings gameStrings;
    Color selectedColor = new Color(255, 233, 12);
    public PlayerNameSelection() {
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
        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(70f));
        String text = gameStrings.getString("Wpisz nazwę gracza");
        int textWidth = g.getFontMetrics().stringWidth(text); // Szerokość tekstu
        g.drawString(text, (C.FRAME_WIDTH - textWidth)/2,  100);
        g.setColor(Color.white);

        if(C.cursorPlayerNamePosition==0) g.setColor(selectedColor);
        if (C.canEnterName) g.setColor(Color.red);
        g.setFont(customFont.deriveFont(80f));
        String playername = C.PLAYER_NAME;
        int playertextWidth = g.getFontMetrics().stringWidth(playername); // Szerokość tekstu
        g.drawString(C.PLAYER_NAME, (C.FRAME_WIDTH - playertextWidth)/2,  330);
        g.setColor(Color.white);

        if (!C.hasPlayerName){
            g.setColor(Color.white);
            String exit = gameStrings.getString("Aby zatwierdzić");
            playertextWidth = g.getFontMetrics().stringWidth(exit); // Szerokość tekstu
            g.setFont(customFont.deriveFont(40f));
            g.setColor(selectedColor);
            g.drawString(exit, (C.FRAME_WIDTH - playertextWidth/2) / 2, 740);
            g.setColor(Color.white);
            g.setFont(customFont.deriveFont(18f));
            g.drawString(gameStrings.getString("Wpisz korzystając"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
        }else {
            String saveandexit = gameStrings.getString("Zapisz i wyjdź");
            playertextWidth = g.getFontMetrics().stringWidth(saveandexit); // Szerokość tekstu
            g.setFont(customFont.deriveFont(40f));
            if (C.cursorPlayerNamePosition == 1) g.setColor(selectedColor);
            g.drawString(saveandexit, (C.FRAME_WIDTH - playertextWidth / 2) / 2, 640);

            g.setColor(Color.white);
            String exit = gameStrings.getString("Wyjście");
            playertextWidth = g.getFontMetrics().stringWidth(exit); // Szerokość tekstu
            g.setFont(customFont.deriveFont(40f));
            if (C.cursorPlayerNamePosition == 2) g.setColor(selectedColor);
            g.drawString(exit, (C.FRAME_WIDTH - playertextWidth) / 2, 740);

        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby zatwierdzić"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
        }
    }
}

