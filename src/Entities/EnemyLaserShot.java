package Entities;

import javax.swing.*;
import java.awt.*;
import Constants.C;
public class EnemyLaserShot extends Thread {
    private int x,y,w=350,h=10,movingType=0,time=0;
    JPanel panel;
    Image imgLaser = new ImageIcon(getClass().getClassLoader().getResource("laser.gif")).getImage();
    public EnemyLaserShot(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }
    public EnemyLaserShot(int x, int y, int w, int h, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.panel=panel;
    }
    public EnemyLaserShot(int x, int y, int w, int h, int m, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.movingType=m;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        if(imgLaser !=null)
            g.drawImage(imgLaser, this.getX(),this.getY(),this.getW(),this.getH(), null);
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
    public int getMovingType() {
        return movingType;
    }
    public int getTime() { return time;}
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
    public void setMovingType(int m) {
        this.movingType = m;
    }
    @Override
    public void run() {
        while (true){
            if(y>C.FRAME_HEIGHT) break;
            if (C.PAUSE!=true && movingType==0) time++;
            if(time==101) break;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
