//klasa reprezentująca wroga

package Entities;

import javax.swing.*;
import java.awt.*;

public class Enemy extends Thread{

    private int x=0,y=0,w=50,h=50,speed=1,score_increment=10;
    JPanel panel;

    //konstruktor
    public Enemy(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }

    //metoda rysujaca obiekt
    public void draw(Graphics2D g){
        g.setColor(Color.blue);
        g.fillRect(x,y,w,h);
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

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        while (true){

            //y=y+speed;
            x=x+speed;

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
