//klasa reprezentująca gracza

package Entities;

import Constants.C;
import Gamestates.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Player {
    private int x=0,y=0,w=50,h=50,speed=1;
    Image imgPlayer = new ImageIcon(getClass().getClassLoader().getResource("player.gif")).getImage();
    Image imgPlayer1 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin1.gif")).getImage();
    Image imgPlayer2 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin2.gif")).getImage();
    Image imgPlayer3 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin3.gif")).getImage();
    Image imgPlayer4 = new ImageIcon(getClass().getClassLoader().getResource("playerSkin4.gif")).getImage();
    Image imgShield = new ImageIcon(getClass().getClassLoader().getResource("shield.gif")).getImage();
    //konstruktor
    public Player(int x,int y){
        this.x=x;
        this.y=y;
    }

    //metoda rysujaca obiekt
    public void draw(Graphics2D g){
        if(C.playerSkin==0)g.drawImage(imgPlayer,x,y,w,h,null);
        if(C.playerSkin==1)g.drawImage(imgPlayer1,x,y,w,h,null);
        if(C.playerSkin==2)g.drawImage(imgPlayer2,x,y,w,h,null);
        if(C.playerSkin==3)g.drawImage(imgPlayer3,x,y,w,h,null);
        if(C.playerSkin==4)g.drawImage(imgPlayer4,x,y,w,h,null);
        if(C.shieldActivated && (C.shieldCooldown>=100 || (C.shieldCooldown<100 && C.shieldCooldown%2==0))){
            g.drawImage(imgShield,x-5,y-5,
                    60,60,null);
        }
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
