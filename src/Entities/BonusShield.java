package Entities;

import Constants.C;

import javax.swing.*;
import java.awt.*;

public class BonusShield extends Thread {
    private int x,y,w=25,h=25;
    JPanel panel;
    Image imgPunkt = new ImageIcon(getClass().getClassLoader().getResource("shieldBonus.gif")).getImage();
    public BonusShield(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        if(imgPunkt!=null)
            g.drawImage(imgPunkt, this.getX(),this.getY(),this.getW(),this.getH(), null);
    }
    public int getY() {
        return y;
    }
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }
    public int getX() {
        return x;
    }

    public void setY(int y) { this.y = y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    //samoczynny ruch w dół
    @Override
    public void run() {
        while (true){
            if (y> C.FRAME_HEIGHT) break;
            if (C.PAUSE!=true)
                y=y+2;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
