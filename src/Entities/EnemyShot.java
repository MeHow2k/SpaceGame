package Entities;

import Constants.C;

import javax.swing.*;
import java.awt.*;

public class EnemyShot extends Thread {
    private int x, y, w = 10, h = 10, movingType = 0;
    private double angle = 0;
    private double radius = 2;
    int rounds=0;

    public int getCircleCenterX() {
        return circleCenterX;
    }

    public void setCircleCenterX(int circleCenterX) {
        this.circleCenterX = circleCenterX;
    }

    public int getCircleCenterY() {
        return circleCenterY;
    }

    public void setCircleCenterY(int circleCenterY) {
        this.circleCenterY = circleCenterY;
    }

    int circleCenterX; int circleCenterY;

    public int getMovingType() {
        return movingType;
    }

    public void setMovingType(int movingType) {
        this.movingType = movingType;
    }

    JPanel panel;
    Image imgEnemyShot = new ImageIcon(getClass().getClassLoader().getResource("EnemyShot.gif")).getImage();

    public EnemyShot(int x, int y, JPanel panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
    }


    public void draw(Graphics2D g) {
        if (imgEnemyShot != null) {
            g.drawImage(imgEnemyShot, this.getX(), this.getY(), this.getW(), this.getH(), null);
        } else {
            g.setColor(Color.red);
            g.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
        }
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

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public void run() {
        while (true) {
            if(y>C.FRAME_HEIGHT || y<-100) break;
            if (C.PAUSE!=true && movingType==0)//w dol
                y=y+2;
            else if (C.PAUSE!=true && movingType==1){//prawo dol
                y=y+2;
                x=x+2;
            }else if (C.PAUSE!=true && movingType==2){//lewo dol
                y=y+2;
                x=x-2;
            }else if (C.PAUSE!=true && movingType==3){//lewo gora
                y=y-2;
                x=x-2;
            }else if (C.PAUSE!=true && movingType==4){//prawo gora
                y=y-2;
                x=x+2;
            }else if (C.PAUSE!=true && movingType==5){//prawo
                x=x+2;
            }else if (C.PAUSE!=true && movingType==6){//lewo
                x=x-2;
            }else if (C.PAUSE!=true && movingType==7){//gora
                y=y-2;
            }else if (C.PAUSE!=true && movingType==8){//po okregu po 2 okrazeniach na doł za wskaz. zegara

                x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                if (angle == 0) {
                    angle = 360;
                    rounds++;
                } else angle--;

                if (radius<100)radius += 0.5;


            }else if (C.PAUSE!=true && movingType==9){//po okregu po 2 okrazeniach na doł przeciw wskaz. zegrara

                x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));

                if (angle == 360) {
                    angle = 0;
                    } else angle++;
                if (radius<100) radius++;

            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
