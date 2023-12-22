package Gamestates;

import Constants.C;

import java.awt.*;

public class MenuSettings {
    MenuSettings() {}

    public void draw (Graphics2D g){
        Font title = new Font("arial",Font.BOLD,60);
        g.setFont(title);
        g.setColor(Color.white);
        g.drawString("Opcje",C.FRAME_WIDTH/2-200,50);
        g.setFont(new Font("arial",Font.BOLD,40));
        g.drawString("Wyjście",C.FRAME_WIDTH/2-80,740);
        Font txt = new Font("arial",Font.BOLD,18);
        g.setFont(txt);
        g.drawString("Głośność muzyki:",C.FRAME_WIDTH/2-200,130);
        g.drawString(""+C.musicVolume,C.FRAME_WIDTH/2,130);

        for(int i=0;i<C.musicVolume;i++){
            g.setColor(Color.green);
            g.fillRect(C.FRAME_WIDTH/2-200+i*60,150,50,50);
        }
        g.setColor(Color.white);

        g.drawString("Głośność dźwięków:",C.FRAME_WIDTH/2-200,280);
        g.drawString(""+C.soundVolume,C.FRAME_WIDTH/2,280);
        for(int i=0;i<C.soundVolume;i++){
            g.setColor(Color.green);
            g.fillRect(C.FRAME_WIDTH/2-200+i*60,300,50,50);
        }
        g.setColor(Color.white);
        g.drawString("Wycisz wszystko:",C.FRAME_WIDTH/2-200,430);
        if(C.isMuted){g.drawString("TAK",C.FRAME_WIDTH/2,430);}
        if(!C.isMuted){g.drawString("NIE",C.FRAME_WIDTH/2,430);}

        g.drawString("Zresetuj najlepszy wynik:",C.FRAME_WIDTH/2-200,580);
        if(C.highscorePoints==0) g.drawString("Nie ma zapisanego najlepszego wyniku.",C.FRAME_WIDTH/2,600);
        if(C.highscorePoints!=0 && C.highscoreLevel!=31)g.drawString("Aktualny: "+C.highscorePoints+" punktów, 30 lvl",C.FRAME_WIDTH/2,600);
        if(C.highscoreLevel==31) g.drawString("Aktualny: "+C.highscorePoints+" punktów, 30 lvl",C.FRAME_WIDTH/2,600);

        g.setFont(new Font("arial",Font.BOLD,15));
        g.drawString("Aby zatwierdzić naciśnij ENTER. Aby zmienić wartość użyj strzałek.",C.FRAME_WIDTH-500,C.FRAME_HEIGHT-70);

    }
}
