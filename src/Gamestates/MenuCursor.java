package Gamestates;

import javax.swing.*;
import java.awt.*;

public class MenuCursor {
    private int x,y,w=50,h=50;
    private JPanel panel;
    //imgCursor=

    public MenuCursor(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }
    public void draw (Graphics2D g){
        g.setColor(Color.blue);
        g.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
        //img
    }

    public int getX() {
        return x;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }
}
