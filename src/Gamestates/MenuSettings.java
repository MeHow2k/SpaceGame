package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuSettings {
    Image option_block = new ImageIcon(getClass().getClassLoader().getResource("option_block.gif")).getImage();
    Font customFont,title;
    Color selectedColor = new Color(255, 233, 12);
    MenuSettings() {
        title = new Font("arial",Font.BOLD,18);
        //import czcionki
        try {
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
        g.setFont(customFont);
        g.setColor(Color.white);
        g.drawString("Opcje",C.FRAME_WIDTH/2-200,50);
        g.setFont(customFont.deriveFont(40f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==4) g.setColor(selectedColor);
        g.drawString("Wyjście",C.FRAME_WIDTH/2-80,740);
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==0) g.setColor(selectedColor);
        g.drawString("Głośność muzyki:",C.FRAME_WIDTH/2-200,130);
        g.setFont(title);
        g.drawString(""+C.musicVolume,C.FRAME_WIDTH/2,130);
        for(int i=0;i<C.musicVolume;i++){
            g.drawImage(option_block,C.FRAME_WIDTH/2-200+i*60,150,50,50,null);
        }
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==1) g.setColor(selectedColor);
        g.drawString("Głośność dźwięków:",C.FRAME_WIDTH/2-200,280);
        g.setFont(title);
        g.drawString(""+C.soundVolume,C.FRAME_WIDTH/2,280);
        for(int i=0;i<C.soundVolume;i++){
            g.setColor(Color.green);
            g.drawImage(option_block,C.FRAME_WIDTH/2-200+i*60,300,50,50,null);
        }
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==2) g.setColor(selectedColor);
        g.drawString("Wycisz wszystko:",C.FRAME_WIDTH/2-200,430);
        g.setFont(title);
        if(C.isMuted){
            g.setColor(Color.red);
            g.drawString("TAK",C.FRAME_WIDTH/2,430);
        }
        if(!C.isMuted){
            g.setColor(Color.green);
            g.drawString("NIE",C.FRAME_WIDTH/2,430);
        }
        g.setFont(customFont.deriveFont(25f));
        g.setColor(Color.white);
        if(C.cursorSettingsPosition==3) g.setColor(selectedColor);
        g.drawString("Zresetuj najlepszy wynik:",C.FRAME_WIDTH/2-200,580);
        if(C.highscorePoints==0) g.drawString("Nie ma zapisanego najlepszego wyniku.",C.FRAME_WIDTH/2-100,C.FRAME_HEIGHT-200);
        if(C.highscorePoints!=0 && C.highscoreLevel!=9999)g.drawString("Aktualny: "+C.highscorePoints+" punktów, "+C.highscoreLevel+" lvl",C.FRAME_WIDTH/2-100,C.FRAME_HEIGHT-200);
        if(C.highscoreLevel==9999) g.drawString("Aktualny: "+C.highscorePoints+" punktów, "+C.highscoreLevel+ " lvl",C.FRAME_WIDTH/2-100,C.FRAME_HEIGHT-200);
        g.setFont(customFont.deriveFont(18f));
        g.setColor(Color.white);
        g.drawString("Aby zmienić wartość użyj strzałek.",C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
        g.drawString("Aby zatwierdzić naciśnij ENTER.",C.FRAME_WIDTH-300,C.FRAME_HEIGHT-50);
    }
}
