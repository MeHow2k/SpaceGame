package Entities;

import Constants.C;

import javax.swing.*;
import java.awt.*;

public class Meteor extends Thread {
    private int x,y,w,h,velX,velY,movingType=0;
    JPanel panel;
    Image imgMeteor = new ImageIcon(getClass().getClassLoader().getResource("meteor.gif")).getImage();
    Image imgMeteorLeft = new ImageIcon(getClass().getClassLoader().getResource("meteorLeft.gif")).getImage();
    Image imgMeteorRight = new ImageIcon(getClass().getClassLoader().getResource("meteorRight.gif")).getImage();
    public Meteor(int x, int y,int w,int h, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        if(movingType==0 && imgMeteor!=null) g.drawImage(imgMeteor, this.getX(),this.getY(),this.getW(),this.getH(), null);
        if(movingType==1 && imgMeteorRight!=null) g.drawImage(imgMeteorRight, this.getX(),this.getY(),this.getW(),this.getH(), null);
        if(movingType==2 && imgMeteorLeft!=null) g.drawImage(imgMeteorLeft, this.getX(),this.getY(),this.getW(),this.getH(), null);
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
    public int getVelX() {
        return velX;
    }
    public int getVelY() {
        return velY;
    }
    public int getMovingType() {
        return movingType;
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
    public void setVelX(int velX) {
        this.velX = velX;
    }
    public void setVelY(int velY) {
        this.velY = velY;
    }
    public void setMovingType(int movingType) {
        this.movingType = movingType;
    }

    //samoczynny ruch w dół
    @Override
    public void run() {
        while (true){
            if(y>C.FRAME_HEIGHT) break;
            if (C.PAUSE!=true && movingType==0)//z góry na dół
                y=y+1;
            else if (C.PAUSE!=true && movingType==1){//z prawej na dół
                y=y+1;
                x=x+1;
            }else if (C.PAUSE!=true && movingType==2){//z lewej na dół
                y=y+1;
                x=x-1;
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
