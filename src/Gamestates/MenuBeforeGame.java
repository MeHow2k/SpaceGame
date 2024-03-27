package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuBeforeGame {
    Font customFont;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("logo.gif")).getImage();
    Image imgPlayer = new ImageIcon(getClass().getClassLoader().getResource("player.gif")).getImage();
    Image imgPlayer1 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin1.gif")).getImage();
    Image imgPlayer2 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin2.gif")).getImage();
    Image imgPlayer3 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin3.gif")).getImage();
    Image imgPlayer4 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin4.gif")).getImage();
    Color selectedColor = new Color(255, 233, 12);

    int skinPreviewX =C.FRAME_WIDTH/2-50, skinPreviewY =130;
    public MenuBeforeGame() {
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
       // g.drawImage(logo,C.FRAME_WIDTH/2-260,5,480,280,null);
        g.setFont(customFont.deriveFont(15f));
        g.setColor(Color.white);
        g.setFont(customFont.deriveFont(65f));
        g.drawString("Wybierz swój statek!", C.FRAME_WIDTH/2-260,50);
        g.setFont(customFont.deriveFont(25f));
        g.drawString("Wybrany statek:", C.FRAME_WIDTH/2-300,145);

        //obrazek aktuanego wygladu gracza:
        if(C.playerSkin==0) g.drawImage(imgPlayer, skinPreviewX, skinPreviewY,100,100,null);
        if(C.playerSkin==1) g.drawImage(imgPlayer1,skinPreviewX,skinPreviewY,100,100,null);
        if(C.playerSkin==2) g.drawImage(imgPlayer2,skinPreviewX,skinPreviewY,100,100,null);
        if(C.playerSkin==3) g.drawImage(imgPlayer3,skinPreviewX,skinPreviewY,100,100,null);
        if(C.playerSkin==4) g.drawImage(imgPlayer4,skinPreviewX,skinPreviewY,100,100,null);
        //dodaj ify po wiecej

        //nazwa skórki
        g.drawString("Nazwa:", C.FRAME_WIDTH/2-200,270);
        g.setFont(customFont.deriveFont(20f));
        if(C.playerSkin==0) g.drawString("123456789 123456789 123456789 ", C.FRAME_WIDTH/2-100,270);
        if(C.playerSkin==1) g.drawString("PlayerSkin1 ", C.FRAME_WIDTH/2-100,270);
        if(C.playerSkin==2)g.drawString("PlayerSkin2", C.FRAME_WIDTH/2-100,270);
        if(C.playerSkin==3)g.drawString("PlayerSkin3", C.FRAME_WIDTH/2-100,270);
        if(C.playerSkin==4)g.drawString("PlayerSkin4", C.FRAME_WIDTH/2-100,270);

        //dodaj ify po wiecej
        g.setFont(customFont.deriveFont(35f));

        //lista skinów jako elementy do wybrania przez gracza:
        g.drawString("Dostępne statki:", C.FRAME_WIDTH/2-300,365);
        g.drawImage(imgPlayer, C.FRAME_WIDTH/2-300, 450,50,50,null);
        g.drawImage(imgPlayer1, C.FRAME_WIDTH/2-175, 450,50,50,null);
        g.drawImage(imgPlayer2, C.FRAME_WIDTH/2-50, 450,50,50,null);
        g.drawImage(imgPlayer3, C.FRAME_WIDTH/2+75, 450,50,50,null);
        g.drawImage(imgPlayer4, C.FRAME_WIDTH/2+200, 450,50,50,null);
        //2gi rząd
        //albo zmiana na inny skin selection np wybieranie broni

        g.setFont(customFont.deriveFont(45f));
        g.setColor(selectedColor);
        if(C.cursorBeforeGamePosition==0) g.drawRect(C.FRAME_WIDTH/2-300-25,450-25,100,100);
        if(C.cursorBeforeGamePosition==1) g.drawRect(C.FRAME_WIDTH/2-175-25,450-25,100,100);
        if(C.cursorBeforeGamePosition==2) g.drawRect(C.FRAME_WIDTH/2-50-25,450-25,100,100);
        if(C.cursorBeforeGamePosition==3) g.drawRect(C.FRAME_WIDTH/2+75-25,450-25,100,100);
        if(C.cursorBeforeGamePosition==4) g.drawRect(C.FRAME_WIDTH/2+200-25,450-25,100,100);
        g.setColor(Color.white);
        if(C.cursorBeforeGamePosition==5) g.setColor(selectedColor);
        g.drawString("Graj!",C.FRAME_WIDTH/2-50,725);
        g.setColor(Color.white);

        g.setFont(customFont.deriveFont(18f));
        g.drawString("Aby zatwerdzić naciśnij ENTER",C.FRAME_WIDTH-250,C.FRAME_HEIGHT-70);
        g.setFont(customFont.deriveFont(18f));
        g.drawString("Aby powrócić do menu wciśnij ESC",C.FRAME_WIDTH-250,C.FRAME_HEIGHT-50);
    }

}