package Entities;

import Constants.C;

import javax.swing.*;
import java.awt.*;

public class FirerateUpgrade extends Thread{
    private int x=0,y=0,w=50,h=50;
    JPanel panel;
    Image img = new ImageIcon(getClass().getClassLoader().getResource("weaponFirerateBoost.gif")).getImage();

    public FirerateUpgrade(int x, int y, int w, int h, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        if(img!=null)   g.drawImage(img,x,y,w,h,null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public void run() {
        while (true){
            if (y> C.FRAME_HEIGHT) break;
            if (C.GAMESTATE==0 && C.PAUSE != true)
                y=y+2;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
