package Entities;

import javax.swing.*;
import java.awt.*;

public class Life {
    private int x=0,y=0,w=50,h=50;
    JPanel panel;
    Image img = new ImageIcon(getClass().getClassLoader().getResource("life.png")).getImage();

    public Life(int x, int y,int w, int h, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        g.drawImage(img,x,y,w,h,null);
    }

}
