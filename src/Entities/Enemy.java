//klasa reprezentująca wroga

package Entities;
import Constants.C;
import javax.swing.*;
import java.awt.*;

public class Enemy extends Thread{

    private int x=0,y=0,w=50,h=50,velX=1,velY=1,dirX=1,dirY=1,hp=1, score_increment=10, movingType=0, angle=270, circleCenterX=C.FRAME_WIDTH/2-w/2,circleCenterY=C.FRAME_HEIGHT/2-100-h/2, radius=300;
    JPanel panel;

    Image imgEnemy = new ImageIcon(getClass().getClassLoader().getResource("enemy.gif")).getImage();
    Image imgBoss = new ImageIcon(getClass().getClassLoader().getResource("boss.gif")).getImage();

    //konstruktor
    public Enemy(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }

    //metoda rysujaca obiekt
    public void draw(Graphics2D g){
        if (isBoss==1)
        g.drawImage(imgBoss,x,y,w,h,null);
        else g.drawImage(imgEnemy,x,y,w,h,null);
    }

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

    public void setMovingType(int movingType) {
        this.movingType = movingType;
    }

    public void setCircleCenterX(int circleCenterX) {
        this.circleCenterX = circleCenterX;
    }

    public void setCircleCenterY(int circleCenterY) {
        this.circleCenterY = circleCenterY;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    int isBoss = 0;

    public int getIsBoss() {
        return isBoss;
    }

    public void setIsBoss(int isBoss) {
        this.isBoss = isBoss;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    @Override
    public void run() {
        while (true){
            if(C.PAUSE != true) {
                if (x < 0) {
                    dirX = 1;
                }
                if (x > C.FRAME_WIDTH - getW()) {
                    dirX = -1;
                }
                if (y<0) {
                    dirY = 1;
                }
                if (y>C.FRAME_HEIGHT-150) {
                    dirY = -1;
                }
                //y=y+;
                //x = x + velX * dirX;

                if(movingType==0)//norrmal x+
                    x = x + velX * dirX;
                if(movingType==1) {//x+y+ odbijanie od krawedzi
                    x = x + velX * dirX;
                    y = y + velY * dirY;
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
                    x = x + velX * dirX;
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==6) {//poruszanie po torze sinusoidalnym po Y
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    y = y + velY * dirY;
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
