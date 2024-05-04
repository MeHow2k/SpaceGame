package Entities;

import Constants.C;
import Gamestates.GamePanel;

import javax.swing.*;
import java.awt.*;

public class AllyAid extends Thread {
    private int x;
    private int y;
    private int w=124;
    private int h=25;
    private int dirX =1;
    private int dirY =1;
    private int velX=1;
    private int velY=1;
    private int movingType=1;
    private int facingDirection =0;
    private int angle=270;
    private boolean isAidDropped=false;
    private int aidType = 0; //

    public int getDirX() {
        return dirX;
    }

    public void setDirX(int dirX) {
        this.dirX = dirX;
    }

    public int getDirY() {
        return dirY;
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }

    private int circleCenterX= C.FRAME_WIDTH/2-w/2;
    private int circleCenterY=C.FRAME_HEIGHT/2-100-h/2;
    private int radius=300;
    private int time=0;


    JPanel panel;
    Image imgAllyLeft = new ImageIcon(getClass().getClassLoader().getResource("allyLeft.gif")).getImage();
    Image imgAllyRight = new ImageIcon(getClass().getClassLoader().getResource("allyRight.gif")).getImage();
    public AllyAid(int x, int y, int facingDirection, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.facingDirection = facingDirection;
        velX=velY=1;
        this.panel=panel;
    }
    public AllyAid(int x, int y, int w, int h, int velX, int velY, int facingDirection, int movingType, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.velX=velX;
        this.velY=velY;
        this.facingDirection = facingDirection;
        this.movingType=movingType;
        this.panel=panel;

    }

    public AllyAid(int x, int y, int w, int h, int velX, int velY, int facingDirection, int movingType, int centerX, int centerY, int radius, int hp, GamePanel gamePanel) {
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.velX=velX;
        this.velY=velY;
        this.facingDirection = facingDirection;
        this.movingType=movingType;
        this.panel=panel;
    }

    public void draw(Graphics2D g){
        if(imgAllyLeft !=null && getFacingDirection()==0)
            g.drawImage(imgAllyLeft, this.getX(),this.getY(),this.getW(),this.getH(), null);
        else if (imgAllyLeft !=null && getFacingDirection()==1) g.drawImage(imgAllyRight, this.getX(),this.getY(),this.getW(),this.getH(), null);
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
    public boolean getIsAidDropped() {
        return isAidDropped;
    }
    public int getVelX() {
        return velX;
    }
    public int getVelY() {
        return velY;
    }
    public int getRadius() {
        return radius;
    }
    public int getCircleCenterX() {
        return circleCenterX;
    }
    public int getCircleCenterY() {
        return circleCenterY;
    }
    public int getMovingType() {
        return movingType;
    }
    public int getFacingDirection() {
        return facingDirection;
    }
    public int getTime() {
        return time;
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
    public void setIsAidDropped(boolean isAidDropped) {
        this.isAidDropped = isAidDropped;
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
    public void setCircleCenterY(int circleCenterY) {this.circleCenterY=circleCenterY;}
    public void setCircleCenterX(int circleCenterX) {this.circleCenterX=circleCenterX;}
    public void setRadius(int radius) {this.radius=radius;}
    public void setFacingDirection(int facingDirection) {this.facingDirection = facingDirection;}
    //metody zmieniajace polozenie o zadana wartosc
    public void moveX(int move_x) {
        this.x+=move_x;
    }
    public void moveY(int move_y) {
        this.x+=move_y;
    }

    //samoczynny ruch
    @Override
    public void run() {
        while (true){

            if (facingDirection==0){
                dirX =1;
            }
            if (facingDirection==1) {
                dirX =-1;
            }
            if (y<0) {
                dirY =1;
            }
            if (y>C.FRAME_HEIGHT-150) {
                dirY =-1;
            }
            if (C.PAUSE!=true){
                if(movingType==0)//norrmal x+
                    y=y+1* dirY *velY;
                if(movingType==1) {//x+y+ odbijanie od krawedzi
                    x=x+2* dirX *velX;
                }
                if(movingType==2) {//po torze kola
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==3) {//lewo prawo gibanie
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==4) {//gora dol gibanie
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==5) {//poruszanie po torze sinusoidalnym po X
                    x=x+1* dirX *velX;
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==6) {//poruszanie po torze sinusoidalnym po Y
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    y=y+1* dirY *velY;
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==7) {//poruszanie po torze 8 po X
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(2*angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==8) {//poruszanie po torze 8 po Y
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(2*angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==9) {//poruszanie po torze elipsy po X
                    x = (int) (circleCenterX + radius * 2 * Math.cos(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==10) {//poruszanie skosie z lewo gora na prawy dol w granicach
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==11) {//poruszanie skosie z lewo gora na prawy dol
                    x = (int) (circleCenterX + radius * Math.tan(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.tan(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==12) {//poruszanie skosie z prawo gora na lewy dol
                    x = (int) (circleCenterX + -radius * Math.tan(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.tan(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==13) {// ?
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
