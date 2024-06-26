//klasa reprezentująca wroga

package Entities;
import Constants.C;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends Thread{

    private boolean isInvincible=false;
    private int x=0,y=0,w=50,h=50,velX=1,velY=1,dirX=1,dirY=1,hp=1, score_increment=10, movingType=0, angle=270,
            circleCenterX=C.FRAME_WIDTH/2-w/2,circleCenterY=C.FRAME_HEIGHT/2-100-h/2, radius=300,isBoss = 0,bossPhase=0;
    JPanel panel;

    Image imgEnemy = new ImageIcon(getClass().getClassLoader().getResource("enemy.gif")).getImage();
    Image imgEnemy2 = new ImageIcon(getClass().getClassLoader().getResource("enemyhp2.gif")).getImage();
    Image imgEnemy3 = new ImageIcon(getClass().getClassLoader().getResource("enemyhp3.gif")).getImage();
    Image imgEnemy4 = new ImageIcon(getClass().getClassLoader().getResource("enemyhp4.gif")).getImage();
    Image imgBoss = new ImageIcon(getClass().getClassLoader().getResource("boss.gif")).getImage();
    Image imgBoss2 = new ImageIcon(getClass().getClassLoader().getResource("boss2.gif")).getImage();
    Image imgBoss2rage = new ImageIcon(getClass().getClassLoader().getResource("boss2_rage.gif")).getImage();

    Image imgBoss3 = new ImageIcon(getClass().getClassLoader().getResource("boss3.gif")).getImage();
    Image imgBoss3rage = new ImageIcon(getClass().getClassLoader().getResource("boss3_rage.gif")).getImage();

    Image imgSpikeBall = new ImageIcon(getClass().getClassLoader().getResource("spikeball.gif")).getImage();
    Image imgBoss4 = new ImageIcon(getClass().getClassLoader().getResource("boss4.gif")).getImage();
    Image imgBoss4phase2 = new ImageIcon(getClass().getClassLoader().getResource("boss4_rage.gif")).getImage();

    Image imgBoss5 = new ImageIcon(getClass().getClassLoader().getResource("boss5_glowny.gif")).getImage();
    Image imgBoss5ship = new ImageIcon(getClass().getClassLoader().getResource("boss5.gif")).getImage();
    Image imgBoss5turret = new ImageIcon(getClass().getClassLoader().getResource("turret2.gif")).getImage();

    //konstruktor
    public Enemy(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }

    //metoda rysujaca obiekt
    public void draw(Graphics2D g){
        if (isBoss==1)
        g.drawImage(imgBoss,x,y,w,h,null);//rys bossa1
        else if (isBoss==2 && imgBoss2!=null) {
            if (hp <= 100) g.drawImage(imgBoss2rage, x, y, w, h, null);
            else g.drawImage(imgBoss2, x, y, w, h, null);//rys bossa2
        }else if (isBoss==3 && imgBoss3!=null) {
            if (movingType==999) g.drawImage(imgBoss3rage, x, y, w, h, null);
            else g.drawImage(imgBoss3, x, y, w, h, null);//rys bossa3
        }else if (isBoss==4 ) {
            if (bossPhase==2 && imgBoss4phase2!=null) g.drawImage(imgBoss4phase2, x, y, w, h, null);
            else if(imgBoss4!=null) g.drawImage(imgBoss4, x, y, w, h, null);//rys bossa4
        }else if (isBoss==41 && imgSpikeBall!=null) {
            g.setColor(Color.red);
            g.fillRect(getX()-50,getY()+20,10,70);
            g.setColor(Color.gray);
            g.fillRect(getX()-50,getY()+20,10,70-getHP());
            g.drawImage(imgSpikeBall, x, y, w, h, null);//rys lewej spikeball bossa4 z paskeim hp
        }else if (isBoss==42 && imgSpikeBall!=null) {
            g.setColor(Color.red);
            g.fillRect(getX()+50+getW(),getY()+20,10,70);
            g.setColor(Color.gray);
            g.fillRect(getX()+50+getW(),getY()+20,10,70-getHP());
            g.drawImage(imgSpikeBall, x, y, w, h, null);//rys prawej spikeball bossa4 z paskiem hp
        }else if (isBoss==5 && imgBoss5!=null) {
            g.drawImage(imgBoss5, x, y, w, h, null);
        } else if (isBoss==51 && imgBoss5ship!=null) {
            g.drawImage(imgBoss5ship, x, y, w, h, null);
        }else if (isBoss==52 && imgBoss5turret!=null) {
            g.setColor(Color.red);
            g.fillRect(getX()-20,getY()+20,10,60);
            g.setColor(Color.gray);
            g.fillRect(getX()-20,getY()+20,10,60-getHP()*2);
            g.drawImage(imgBoss5turret, x, y, w, h, null);//rys lewej turreta bossa5 z paskeim hp
        }else if (isBoss==53 && imgBoss5turret!=null) {
            g.setColor(Color.red);
            g.fillRect(getX()+20+getW(),getY()+20,10,60);
            g.setColor(Color.gray);
            g.fillRect(getX()+20+getW(),getY()+20,10,60-getHP()*2);
            g.drawImage(imgBoss5turret, x, y, w, h, null);//rys prawej turreta boss5 z paskiem hp
        }else {
            if (getHP()==1) g.drawImage(imgEnemy,x,y,w,h,null);
            if (getHP()==2) g.drawImage(imgEnemy2,x,y,w,h,null);
            if (getHP()==3) g.drawImage(imgEnemy3,x,y,w,h,null);
            if (getHP()>=4) g.drawImage(imgEnemy4,x,y,w,h,null);
        }
    }

    @Override
    public void run() {
        while (true){
            if(C.PAUSE != true) {
                if (x < 0) {
                    if(movingType!=51) dirX = 1;
                }
                if (x > C.FRAME_WIDTH - getW()) {
                    if(movingType!=1000) dirX = -1;
                }
                if (y<0) {
                    if(movingType!=51)dirY = 1;
                }
                if (y>C.FRAME_HEIGHT-150) {
                    if(movingType!=51)dirY = -1;
                }
                //y=y+;
                //x = x + velX * dirX;
                if(movingType==999) {}//stanie w miejscu
                if(movingType==1000) {x = x + velX * dirX;}//dla wroga w menu
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
                if(movingType==20) {// dla bossa 2
                    if(getHP()>100){
                        x=x+1* dirX *velX;
                        y=y+1* dirY *velY;
                    }
                    else{
                        if(getHP()>50) x=x+2* dirX *velX;
                        else x=x+5* dirX *velX;
                        y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                        if(angle==360) angle=0; else angle++;
                    }
                }
                if(movingType==30) {// dla bossa 3
                    if(getHP()>50){
                        if(y<100) y=y+1* dirY *velY;
                        x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                        if(angle==360) angle=0; else angle++;
                    }
                    else{
                        y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                        if(angle==360) angle=0; else angle++;
                    }
                }
                if(movingType==40) {// dla bossa 4

                        if(y<100) y=y+1* dirY *velY;
                        x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                        if(angle==360) angle=0; else angle++;

                }
                if(movingType==44) {// dla spike ball bossa 4 - na dól
                    if(getY()<C.FRAME_HEIGHT-100){
                        y=y+3 * dirY *velY;
                    }
                    else{
                        setMovingType(45);
                    }
                }
                if(movingType==45) {// dla spike ball bossa 4 - w góre
                    if(getY()<100){
                        setMovingType(999);
                    }else y=y-1 * dirY *velY;
                }
                if(movingType==46) {// dla 2 fazy bossa 4- zlec do środka potem na boki
                    if(y<C.FRAME_HEIGHT/2-getH()) y=y+1* dirY *velY;
                    if(y>C.FRAME_HEIGHT/2-getH()) y=y-1* dirY *velY;
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==47) {// dla 2 fazy bossa 4- po okregu i strzelaj
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==50) {// dla bossa 50 - teleport

                }
                if(movingType==51) {// dla duzego statku bossa 5
                    if(y<=0)
                        y=y+1* dirY *velY;
                    else{
                        x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                        if(angle==360) angle=0; else angle++;
                    }
                }
                if(movingType==52) {// dla duzego statku bossa 5 faza 2
                    y = y - velY * dirY;
                }

            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void setBoss5randomlocation(Player player) {
        int px = player.getX();
        int py = player.getY();
        int ph = player.getH();
        int pw = player.getW();

        Random random = new Random();
        boolean tooClose;
        do {
            tooClose = false;

            int newX = random.nextInt(C.FRAME_WIDTH - getW());
            int newY = random.nextInt(C.FRAME_HEIGHT - getH()-100);

            if((px-200 < newX + 180
                    && px-200 + 450 > newX
                    && py-200 < newY + 180
                    && py-200 + 450 > newY
            )==true) {
                tooClose = true;
            }else {
                // Jeśli odległość jest wystarczająco duża, ustaw nową lokalizację dla bossa
                x = newX;
                y = newY;
            }
        } while (tooClose);
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
    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBossPhase() {
        return bossPhase;
    }

    public void setBossPhase(int bossPhase) {
        this.bossPhase = bossPhase;
    }
}
