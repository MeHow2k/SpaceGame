///////////////GamePanel- główny wątek gry + menu itd /////////////

package Gamestates;

import Constants.C;
import Entities.*;
import Manager.SoundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    Player player;//deklaracja obiektu Gracz

    // deklaracja elementów menu
    Menu menu;MenuCursor menuCursor;MenuSettings menuSettings;MenuHowToPlay menuHowToPlay;MenuAuthors menuAuthors;
    //tu będą listy obiektów
    ArrayList<Enemy> listEnemy = new ArrayList(20);//lista wrogow
    ArrayList<Points> listPoints = new ArrayList(20);//lista punktów
    ArrayList<EnemyShot> listEnemyShot = new ArrayList(20);//lista wrogich strzałów
    ArrayList<PlayerShot> listPlayerShot = new ArrayList(20);//lista punktów

    //zmienne bool zawirajace info czy naciśnięto przycisk
    boolean LEFT_PRESSED, RIGHT_PRESSED, DOWN_PRESSED, UP_PRESSED,SHOT_PRESSED,
            isShotOnCooldown=false;//czas do ponownego strzału
    int shotCooldown=60;
    //etykiety
    JLabel FPSlabel;

    GamePanel(){
        super(null);
        Color color = new Color(132, 142, 220);
        setBackground(color);
        //dodanie nasłuchiwania klawiszy
        addKeyListener(this);
        //inicjalizacja obiektow
        player= new Player(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
        menuCursor = new MenuCursor(C.FRAME_WIDTH / 2 - 180, 310, this);
        //etykieta pokazyjąca FPS
        FPSlabel = new JLabel("");
        FPSlabel.setBounds(0, 10, 100, 30);
        FPSlabel.setForeground(Color.white);
        FPSlabel.setText("FPS: ");
        add(FPSlabel);
        //test stworzenie pounktow
        newPoints(50,50,25,25);
        //test stworzenie strzalu wroga
        newEnemyShot(50,50);
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
                        //obsluga ruchu gracza STEROWANIE
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
                        if (SHOT_PRESSED == true) {
                            isShotOnCooldown = true;
                            newPlayerShot();
                            SHOT_PRESSED = false;
                            try {
                               SoundManager.playShot();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        //player.moveX(1);//test ruchu klasy gracza
                        //System.out.println("x:"+LEFT_PRESSED+" y:"+RIGHT_PRESSED );//test up

                        // obsługa opóźnienia strzelania gracza
                        if(isShotOnCooldown){ shotCooldown--; }
                        if (shotCooldown <= 0) {
                            shotCooldown = 60;
                            isShotOnCooldown = false;
                        }

                        //generowanie strzałów wrogów
                        if (listEnemy != null ) {
                            for (int i = 0; i < listEnemy.size(); i++) {
                                Enemy enemy = listEnemy.get(i);
                                Random random = new Random();
                                int roll = random.nextInt(600);//w kazdej klatce szansa 1/300
                                if (roll == 0) {
                                    newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH());
                                    try {
                                        SoundManager.playEnemyShot();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }

///////////////////////////////////kolizja punktow//////////////////////////////////////////////////////////////////////
                        if (listPoints != null) {
                            for (int i = 0; i < listPoints.size(); i++) {
                                Points points = listPoints.get(i);
//kolizja gracza z Punktem,
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(), 
                                        points.getX(), points.getY(), points.getW(), points.getH())) {
                                    try {
                                        SoundManager.playPoint();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    listPoints.remove(points);
                                } else if (points.getY() > C.FRAME_HEIGHT) {
                                    listPoints.remove(points);
                                }
                            }
                        }
//kolizja wroga i gracza
                        if (listEnemy != null) {
                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                Enemy enemy = listEnemy.get(iw);
                                if (enemy.getHP() <= 0) {
                                    listEnemy.remove(enemy);
                                }
                                //kolizja wroga i gracza
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH())) {
                                        listEnemy.remove(enemy);
                                        //wpisz akcja
                                }
                            }
                        }
//kolizja strzału gracza z obiektami
                        if (listPlayerShot != null) {
                            for (int i = 0; i < listPlayerShot.size(); i++) {
                                PlayerShot playershot = listPlayerShot.get(i);
                                ///////////////////// usuwanie wroga i strzału gdy ten ich trafi i dodanie pkt za to ////////////////////////
                                if (listEnemy != null) {
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        //kolizja wroga i strzału gracza
                                        if (isCollision(playershot.getX(), playershot.getY(), playershot.getW(), playershot.getH(),
                                                enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH())) {
                                            //usunięcie wroga gdy ma 1 hp
                                            if (enemy.getHP() == 1) {
                                                listEnemy.remove(enemy);
                                                try {
                                                    //SoundManager.playEnemyHit(); dzwiek zniszczenia wroga
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                if (playershot != null)
                                                    listPlayerShot.remove(playershot);
                                                //wpisz akcja
                                            } else {
                                                if (playershot != null)
                                                    listPlayerShot.remove(playershot);
                                                enemy.setHP(enemy.getHP() - 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
//kolizja strzału wroga z graczem
                        if (listEnemyShot != null) {
                            for (int iw = 0; iw < listEnemyShot.size(); iw++) {
                                EnemyShot enemyShot = listEnemyShot.get(iw);
                                //kolizja strzału wroga i gracza
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        enemyShot.getX(), enemyShot.getY(), enemyShot.getW(), enemyShot.getH())) {
                                    listEnemyShot.remove(enemyShot);
                                    //wpisz akcja
                                }
                            }
                        }
                    }//Gamestate 0

                    if(C.GAMESTATE==1){
                    //obsługa menu
                        menuCursor.setX(C.FRAME_WIDTH / 2 - 180);
                        if (menu != null && C.cursorPosition == 0) {
                            menuCursor.setY(310);
                        }
                        if (menu != null && C.cursorPosition == 1) {
                            menuCursor.setY(410);
                        }
                        if (menu != null && C.cursorPosition == 2) {
                            menuCursor.setY(510);
                        }
                        if (menu != null && C.cursorPosition == 3) {
                            menuCursor.setY(610);
                        }
                        if (menu != null && C.cursorPosition == 4) {
                            menuCursor.setY(710);
                        }

                    }//GAMESTATE 1 menu

                    if(C.GAMESTATE==2){}//GAMESTATE 2 menusettings
                    try {
                        Thread.sleep(3);//5ms
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
                        FPSlabel.setText("FPS: " + frames + " ");
                        frames = 0;
                    }
                }//wh-true

            }
        }).start();

    }//GamePanel


    ////  Funkcja rysująca obiekty///////////////////////////////////////////////
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D =(Graphics2D) g;
        Image imgBack = new ImageIcon(getClass().getClassLoader().getResource("background.gif")).getImage();
        g.drawImage(imgBack, 0, 0, C.FRAME_WIDTH, C.FRAME_HEIGHT, null);


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
            if (listEnemyShot != null)            //rysowanie wrogich strzałów
                for (int i = 0; i < listEnemyShot.size(); i++) {
                    listEnemyShot.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
                }
            if (listPlayerShot != null)            //rysowanie strzałów gracza
                for (int i = 0; i < listPlayerShot.size(); i++) {
                    listPlayerShot.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
                }

        }//gamestate 0

        if(C.GAMESTATE==1){
            menu = new Menu();
            menu.draw(g2D);
            menuCursor.draw(g2D);
        }//GAMESTATE 1 -menu

        if(C.GAMESTATE==2){

        }//GAMESTATE 2 -menuOpcje
    }
    //////////////////////////////////////////////////////////////////////////////

    //funkcje

    //sprawdzanie kolizji
    public boolean isCollision(int o1x, int o1y, int o1w, int o1h,
                               int o2x, int o2y, int o2w, int o2h){
        if(o1x < o2x + o2w
                && o1x + o1w > o2x
                && o1y < o2y + o2h
                && o1y + o1h > o2y
        )return true;
        else return false;
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
    public void newEnemyShot(int x,int y){//utworzenie obiektu wrogiego strzału
        EnemyShot enemyShot = new EnemyShot(x,y,this);
        enemyShot.start();//start watku
        listEnemyShot.add(enemyShot);//dodanie do listy obiektow enemyShot

    }
    public void newPlayerShot(){//utworzenie obiektu strzału gracza
        PlayerShot playerShot = new PlayerShot(player.getX()+20,player.getY(),this);
        playerShot.start();//start watku
        listPlayerShot.add(playerShot);//dodanie do listy obiektow PlayerShot

    }
    public void resetVariables(){//przywrócenie zmiennych do stanu pierwotnego
        LEFT_PRESSED=false;RIGHT_PRESSED=false;UP_PRESSED=false;DOWN_PRESSED=false;SHOT_PRESSED=false;
        isShotOnCooldown=false;shotCooldown=60;
        player.setX(C.FRAME_WIDTH / 2 - 25); player.setY(C.FRAME_HEIGHT - 150);
        //remove lists

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
            if(C.GAMESTATE==1 && C.cursorPosition<4){C.cursorPosition++;}
        }
        if (e.getKeyCode()==38){//s w gore
            UP_PRESSED=true;
            if(C.GAMESTATE==1 && C.cursorPosition>0){C.cursorPosition--;}
        }
        if (e.getKeyCode()==32 && !isShotOnCooldown){//spacja - strzał
            SHOT_PRESSED=true;
        }
        if (e.getKeyCode()==10){//enter - zatwierdzanie w menu
            if (C.cursorPosition == 0) {//start
                C.GAMESTATE=0;
            }
            if (C.cursorPosition == 1) {//jak grac

            }
            if (C.cursorPosition == 2) {//opcje

            }
            if (C.cursorPosition == 3) {//autorzy

            }
            if (C.cursorPosition == 4) {//wyjdz
                System.exit(1);
            }
        }
        if (e.getKeyCode()==27 ){//esc - powrot do menu
            C.GAMESTATE=1;
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
        if (e.getKeyCode()==32){//spacja - strzał
            SHOT_PRESSED=false;
        }
    }
}
