///////////////GamePanel- główny wątek gry + menu itd /////////////

package Gamestates;

import Constants.C;
import Entities.Enemy;
import Entities.Player;
import Entities.Points;
import Manager.SoundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener {

    Player player;//deklaracja obiektu Gracz

    //tu będą listy obiektów
    ArrayList<Enemy> listEnemy = new ArrayList(20);//lista wrogow
    ArrayList<Points> listPoints = new ArrayList(20);//lista punktów

    //zmienne bool zawirajace info czy naciśnięto przycisk
    boolean LEFT_PRESSED, RIGHT_PRESSED, DOWN_PRESSED, UP_PRESSED;
    //etykiety
    JLabel FPSlabel;

    GamePanel(){
        super(null);
        Color color = new Color(132, 142, 220);
        setBackground(color);
        //dodanie nasłuchiwania klawiszy
        addKeyListener(this);
        //deklaracja obiektow
        //gracz
        player= new Player(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
        //etykieta pokazyjąca FPS
        FPSlabel = new JLabel("");
        FPSlabel.setBounds(0, 10, 100, 30);
        FPSlabel.setForeground(Color.white);
        FPSlabel.setText("FPS: ");
        add(FPSlabel);
        //test stworzenie pounktow
        newPoints(50,50,25,25);
        newPoints(100,50,25,25);
        newPoints(150,50,25,25);
        //test rysowania obiektow
        newEnemy(100,50,50,50);
        newEnemy(200,100,50,50);
        newEnemy(300,150,50,50);
        //Odtworzenie głównego motywu gry
        try {
            SoundManager.playBackground();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
                        //obsluga ruchu gracza
                        if (RIGHT_PRESSED == true) {
                            if (C.FRAME_WIDTH - 60 >= player.getX()) {
                                player.moveX(1); //poruszanie się w prawo
                            }
                            if (C.FRAME_WIDTH - 60 <= player.getX()) {
                                player.moveX(-5); //odbijanie się od krawędzi
                            }
                        }
                        if (LEFT_PRESSED == true) {
                            if (0 <= player.getX()) {
                                player.moveX(-1); //poruszanie się w lewo
                            }
                            if (0 >= player.getX()) {
                                player.moveX(5); //odbijanie się od krawędzi
                            }
                        }
                        if (DOWN_PRESSED == true) {
                            if (C.FRAME_HEIGHT - 150 >= player.getY()) {
                                player.moveY(1); //poruszanie się w dół
                            }
                        }
                        if (UP_PRESSED == true) {
                            if (0 < player.getY()) {
                                player.moveY(-1); //poruszanie się w górę
                            }
                        }

                        //player.moveX(1);//test ruchu klasy gracza
                        //System.out.println("x:"+LEFT_PRESSED+" y:"+RIGHT_PRESSED );//test up


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
                        //wypisywanie liczby klatek na sekundę w etykiecie
                        FPSlabel.setText("FPS: " + frames);
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
            if (listPoints != null)            //rysowanie wrogow
                for (int i = 0; i < listPoints.size(); i++) {
                    listPoints.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
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
    public void newPoints(int x,int y,int w,int h){//utworzenie obiektu wroga
        Points point = new Points(x,y,this);
        point.start();//start watku
        listPoints.add(point);//dodanie do listy obiektow enemy
    }

    //keylistener do sterowania
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        ////   ruch gracza
        if (e.getKeyCode()==37){//s w lewo
            LEFT_PRESSED=true;
        }
        if (e.getKeyCode()==39){//s w prawo
            RIGHT_PRESSED=true;
        }
        if (e.getKeyCode()==40){//s w dol
            DOWN_PRESSED=true;
        }
        if (e.getKeyCode()==38){//s w gore
            UP_PRESSED=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==37){//s w lewo
            LEFT_PRESSED=false;
        }
        if (e.getKeyCode()==39){//s w prawo
            RIGHT_PRESSED=false;
        }
        if (e.getKeyCode()==40){//s w dol
            DOWN_PRESSED=false;
        }
        if (e.getKeyCode()==38){//s w gore
            UP_PRESSED=false;
        }
    }
}
