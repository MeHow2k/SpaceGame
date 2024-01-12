package Gamestates;

import Constants.C;
import java.awt.*;

public class Menu {

    public Menu() {}
    public void draw (Graphics2D g){
        Font title = new Font("Times New Roman",Font.BOLD,60);
        g.setFont(title);
        g.setColor(Color.white);

        Font buttons = new Font("arial",Font.BOLD,40);
        g.setFont(buttons);
        g.drawString("Start", C.FRAME_WIDTH/2-100,350);
        g.drawString("Jak grać?",C.FRAME_WIDTH/2-100,450);
        g.drawString("Opcje",C.FRAME_WIDTH/2-100,550);
        g.drawString("Autorzy",C.FRAME_WIDTH/2-100,650);
        g.drawString("Wyjdź",C.FRAME_WIDTH/2-100,750);

        g.setFont(new Font("arial",Font.BOLD,15));
        g.drawString("Aby zatwerdzić naciśnij ENTER",C.FRAME_WIDTH-250,C.FRAME_HEIGHT-70);

        g.drawString("Team PDM 2023",10,C.FRAME_HEIGHT-70);
    }

}