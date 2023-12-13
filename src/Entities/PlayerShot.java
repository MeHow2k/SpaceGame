package Entities;

import javax.swing.*;
import java.awt.*;

public class PlayerShot extends Thread {
    private int x, y, w = 5, h = 10;
    JPanel panel;
   // Image imgPlayerShot = new ImageIcon(getClass().getClassLoader().getResource("PlayerShot.png")).getImage();

    public PlayerShot(int x, int y, JPanel panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
    }


    public void draw(Graphics2D g) {
       // if (imgPlayerShot != null){
           // g.drawImage(imgPlayerShot, this.getX(), this.getY(), this.getW(), this.getH(), null);
            g.setColor(Color.green);
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
            if (y < -10) break;
            y = y - 4;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
