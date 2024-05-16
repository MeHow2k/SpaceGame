package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class MenuPlayerSettings {
    Font customFont,title;
    Color selectedColor = new Color(255, 233, 12);
    Strings gameStrings;
    MenuPlayerSettings() {
        gameStrings=new Strings();
        title = new Font("arial",Font.BOLD,18);
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
        g.setFont(customFont);
        g.setColor(Color.white);
        g.drawString(gameStrings.getString("Ustawienia gracza"), C.FRAME_WIDTH/2-200,50);

        g.setFont(customFont.deriveFont(25f));
        if(C.cursorPlayerSettingsPosition==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Zmień nazwę gracza"), C.FRAME_WIDTH/2-200,130);

        g.drawString(gameStrings.getString("Aktualny:")+" "+C.PLAYER_NAME,C.FRAME_WIDTH/2-100,180);

        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorPlayerSettingsPosition==1) g.setColor(selectedColor);

        g.drawString(gameStrings.getString("Zresetuj wynik"), C.FRAME_WIDTH/2-200,280);
        if(C.highscorePoints==0) g.drawString(gameStrings.getString("Nie ma zapisanego"), C.FRAME_WIDTH/2-100,330);
        if(C.highscorePoints!=0 && C.highscoreLevel!=9999)g.drawString(gameStrings.getString("Aktualny:")+" " +C.highscorePoints+" "
                +gameStrings.getString("punktów")+", "+C.highscoreLevel+" "+gameStrings.getString("poziom"),C.FRAME_WIDTH/2-100,330);
        if(C.highscoreLevel==9999) g.drawString(gameStrings.getString("Aktualny:")+" "+C.highscorePoints+" "
                +gameStrings.getString("punktów")+", "+C.highscoreLevel+" "+gameStrings.getString("poziom"),C.FRAME_WIDTH/2-100,330);

        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorPlayerSettingsPosition==2) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Zresetuj wszystko"), C.FRAME_WIDTH/2-200,430);
        g.drawString(gameStrings.getString("Zresetuj wszystko2"), C.FRAME_WIDTH/2-100,480);
//        String achievementsString="";
//        for(int i=0;i<C.achievements.length;i++){
//            achievementsString += C.achievements[i];
//        }
//        g.drawString(achievementsString, C.FRAME_WIDTH/2-100,530);
        g.setFont(customFont.deriveFont(40f));
        g.setColor(Color.white);

        if(C.cursorPlayerSettingsPosition==3) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Wyjście"), C.FRAME_WIDTH/2-80,740);
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);

        g.setFont(customFont.deriveFont(18f));
        g.setColor(Color.white);
//        g.drawString(gameStrings.getString("Aby zmienić wartość"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
        g.drawString(gameStrings.getString("Aby zatwierdzić"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-50);
    }
}
