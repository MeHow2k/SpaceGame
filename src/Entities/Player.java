//klasa reprezentująca gracza

package Entities;

import javax.swing.*;
import java.awt.*;

public class Player {
    private int x=0,y=0,w=50,h=50,speed=1;
    Image imgPlayer = new ImageIcon(getClass().getClassLoader().getResource("gracz.gif")).getImage();
    //konstruktor
    public Player(int x,int y){
        this.x=x;
        this.y=y;
    }

    //metoda rysujaca obiekt
    public void draw(Graphics2D g){
        g.fillRect(x,y,w,h);
        g.drawImage(imgPlayer,x,y,w,h,null);
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

    //metoda poruszajaca obiekt o zadana wartosc
    public void moveX(int mx) {
        this.x+=mx;
    }
    public void moveY(int my) {
        this.y+=my;
    }
}
