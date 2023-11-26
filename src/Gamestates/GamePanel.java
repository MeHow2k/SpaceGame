///////////////GamePanel- główny wątek gry + menu itd /////////////

package Gamestates;

import Constants.C;
import Entities.Enemy;
import Entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener {

    Player player;//deklaracja obiektu Gracz

    //tu będą listy obiektów
    ArrayList<Enemy> listEnemy = new ArrayList(20);//lista wrogow

    GamePanel(){
        super(null);
        Color color = new Color(132, 142, 220);
        setBackground(color);
        //deklaracja obiektow
        player= new Player(200,200);

        //test rysowania obiektow
        newEnemy(100,100,50,50);
        newEnemy(200,100,50,50);
        newEnemy(300,100,50,50);

        new Thread(new Runnable() {
            @Override
            public void run() {

                long lastTime = System.nanoTime();
                double tickpersec = 60.0;
                double ns = 1000000000 / tickpersec;
                double delta = 0;
                int frames = 0;
                long timer = System.currentTimeMillis();


                //główna pętla gry
                while(true){

                    long nowTime = System.nanoTime();
                    delta += (nowTime - lastTime) / ns;
                    lastTime = nowTime;

                    //glowny watek gry- mechaniki itd.
                    if (C.GAMESTATE==0){
                        player.moveX(1);//test ruchu klasy gracza
                       // System.out.println("x:"+player.getX()+" y:"+player.getY() );//test up


                    }
                    try {
                        Thread.sleep(1);//5ms
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (delta >= 1) {
                        repaint();
                        delta--;
                        frames++;
                    }
                    if (System.currentTimeMillis() - timer > 1000) { //co 1 s sprawdza liczbe narysowanych klatek
                        timer += 1000;
                        System.out.println("FPS: " + frames);
                        frames = 0;
                    }
                }//wh-true

            }
        }).start();

    }//GamePanel


    ////  Funkcja rysująca obiekty///////////////////////////////////////////////////////
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D =(Graphics2D) g;
        //Image imgBack = new ImageIcon(getClass().getClassLoader().getResource("background.gif")).getImage();
        //g.drawImage(imgBack, 0, 0, C.FRAME_WIDTH, C.FRAME_HEIGHT, null);


        if(C.GAMESTATE==0){
            player.draw(g2D);//rysowanie gracza

//tu będą pętle rysujące obiekty z list zadeklarowanych na początku
            if (listEnemy != null)            //rysowanie wrogow
                for (int i = 0; i < listEnemy.size(); i++) {
                    listEnemy.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
                }


        }//gamestate 0


    }
    //////////////////////////////////////////////////////////////////////////////

    //funkcje

    //sprawdzanie kolizji
    public boolean isCollision(){
        if(true)//TODO warunek kolizji obiektow
        return true; else return false;
    }
    //test nowy wrog
    public void newEnemy(int x,int y,int w,int h){//utworzenie obiektu wroga
        Enemy enemy = new Enemy(x,y,this);
        enemy.start();//start watku
        listEnemy.add(enemy);//dodanie do listy obiektow enemy

    }


    //keylistener do sterowania
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
