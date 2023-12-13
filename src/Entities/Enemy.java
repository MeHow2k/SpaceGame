//klasa reprezentująca wroga

package Entities;
import Constants.C;
import javax.swing.*;
import java.awt.*;

public class Enemy extends Thread{

    private int x=0,y=0,w=50,h=50,velX=1,velY=1,dirX=1,dirY=1,hp=1, score_increment=10;
    JPanel panel;

    Image imgEnemy = new ImageIcon(getClass().getClassLoader().getResource("wrog.gif")).getImage();

    //konstruktor
    public Enemy(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }

    //metoda rysujaca obiekt
    public void draw(Graphics2D g){
        g.setColor(Color.blue);
        g.drawImage(imgEnemy,x,y,w,h,null);
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

    public int getHP() {
        return hp;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    @Override
    public void run() {
        while (true){
            if(x<0){
                dirX=1;
            }
            if (x> C.FRAME_WIDTH-getW()) {
                dirX=-1;
            }
            //y=y+;
            x=x+velX*dirX;

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
