package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.io.InputStream;

public class Intro  {
    Font customFont;
    Color textColor;
    float logoTransparency=0; // Zmienna dla przezroczystości logo
    int textTransparency=0;
    int textWidth;
    String text;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("teamlogo.png")).getImage();

    public Intro() {
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
        // Obliczanie przezroczystości na podstawie czasu dla tekstu
            if (C.intro_delay <= 254) {
                if (C.intro_delay <= 127) {
                    textTransparency = C.intro_delay * 2; // Stopniowo zwiększaj przezroczystość
                } else {
                    textTransparency = (254 - C.intro_delay) * 2; // Stopniowo zmniejszaj przezroczystość
                }
                textColor = new Color(255, 255, 255, textTransparency);
            } else {
                textColor = new Color(255, 255, 255, 0);//zabezpieczenie przed przekroczeniem alpha 255
            }

            // Obliczanie przezroczystości na podstawie czasu dla obrazka
            if (C.intro_delay <= 254) {
                if (C.intro_delay <= 127) {
                    logoTransparency = C.intro_delay / 127.0f; // Stopniowo zwiększaj przezroczystość
                } else {
                    logoTransparency = (254 - C.intro_delay) / 127.0f; // Stopniowo zmniejszaj przezroczystość
                }
            }else logoTransparency = 0f; //zabezpieczenie przed przekroczeniem alpha 255

        // Rysowanie logo z uwzględnieniem przezroczystości
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, logoTransparency);
        g.setComposite(ac);
        g.drawImage(logo, C.FRAME_WIDTH / 2 - 240, C.FRAME_HEIGHT / 2 - 150, 480, 280, null);

        // Rysowanie tekstu z uwzględnieniem przezroczystości
        g.setComposite(AlphaComposite.SrcOver); // Resetowanie composite
        g.setColor(textColor);
        g.setFont(customFont.deriveFont(35f));
        text = "Game developed by:";
        textWidth = g.getFontMetrics().stringWidth(text); // Szerokość tekstu
        g.drawString(text, (C.FRAME_WIDTH - textWidth) / 2 - 100 , C.FRAME_HEIGHT / 2 - 200);

        g.setFont(customFont.deriveFont(25f));
        text = "Team PDM  2023 - 2024";
        textWidth = g.getFontMetrics().stringWidth(text); // Szerokość tekstu
        g.drawString(text, (C.FRAME_WIDTH - textWidth)/2, C.FRAME_HEIGHT - 100);
    }
}
