package Gamestates;

import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MenuSettings {
    Image option_block = new ImageIcon(getClass().getClassLoader().getResource("option_block.gif")).getImage();
    Font customFont,title;
    Color selectedColor = new Color(255, 233, 12);
    Strings gameStrings;
    MenuSettings() {
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
        g.drawString(gameStrings.getString("Opcje"), C.FRAME_WIDTH/2-200,50);
        g.setFont(customFont.deriveFont(40f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==4) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Wyjście"), C.FRAME_WIDTH/2-80,740);
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==0) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Głośność muzyki:"), C.FRAME_WIDTH/2-200,130);
        g.setFont(title);
        g.drawString(""+C.musicVolume,C.FRAME_WIDTH/2,130);
        for(int i=0;i<C.musicVolume;i++){
            g.drawImage(option_block,C.FRAME_WIDTH/2-200+i*60,150,50,50,null);
        }
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==1) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Głośność dźwięków:"), C.FRAME_WIDTH/2-200,280);
        g.setFont(title);
        g.drawString(""+C.soundVolume,C.FRAME_WIDTH/2,280);
        for(int i=0;i<C.soundVolume;i++){
            g.setColor(Color.green);
            g.drawImage(option_block,C.FRAME_WIDTH/2-200+i*60,300,50,50,null);
        }
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==2) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Wycisz wszystko:"), C.FRAME_WIDTH/2-200,430);
        g.setFont(title);
        if(C.isMuted){
            g.setColor(Color.red);
            g.drawString(gameStrings.getString("TAK"), C.FRAME_WIDTH/2,430);
        }
        if(!C.isMuted){
            g.setColor(Color.green);
            g.drawString(gameStrings.getString("NIE"), C.FRAME_WIDTH/2,430);
        }
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==3) g.setColor(selectedColor);
        g.drawString(gameStrings.getString("Zresetuj wynik"), C.FRAME_WIDTH/2-200,580);
        if(C.highscorePoints==0) g.drawString(gameStrings.getString("Nie ma zapisanego"), C.FRAME_WIDTH/2-100,C.FRAME_HEIGHT-200);
        if(C.highscorePoints!=0 && C.highscoreLevel!=9999)g.drawString(gameStrings.getString("Aktualny:")+" " +C.highscorePoints+" "
                +gameStrings.getString("punktów")+", "+C.highscoreLevel+" "+gameStrings.getString("poziom"),C.FRAME_WIDTH/2-100,C.FRAME_HEIGHT-200);
        if(C.highscoreLevel==9999) g.drawString(gameStrings.getString("Aktualny:")+" "+C.highscorePoints+" "
                +gameStrings.getString("punktów")+", "+C.highscoreLevel+" "+gameStrings.getString("poziom"),C.FRAME_WIDTH/2-100,C.FRAME_HEIGHT-200);
        g.setFont(customFont.deriveFont(18f));
        g.setColor(Color.white);
        g.drawString(gameStrings.getString("Aby zmienić wartość"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
        g.drawString(gameStrings.getString("Aby zatwierdzić"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-50);
    }
}
