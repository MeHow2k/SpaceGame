package Entities;

import Constants.C;

import javax.swing.*;
import java.awt.*;

public class EnemyShot extends Thread{
    private int x, y, w = 5, h = 10;
    JPanel panel;
    //Image imgEnemyShot = new ImageIcon(getClass().getClassLoader().getResource("EnemyShot.png")).getImage();

    public EnemyShot(int x, int y, JPanel panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
}


    public void draw(Graphics2D g) {
        //if (imgEnemyShot != null){
            // g.drawImage(imgPlayerShot, this.getX(), this.getY(), this.getW(), this.getH(), null);
            g.setColor(Color.red);
            g.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
       // }
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
            if(y> C.FRAME_HEIGHT || y<-40) break;
            y = y + 2;
            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
