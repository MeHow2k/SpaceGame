///////////////GamePanel- główny wątek gry + menu itd /////////////

package Gamestates;

import Constants.C;
import Entities.*;
import Manager.SoundManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    Player player;//deklaracja obiektu Gracz
    PlayerShot playerShotUI;
    Life lifeUI;

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
    //boolean islevelCreated=false;
    int level_temp1 =0; int level_temp2 =0;
    int shotCooldown=60;
    //etykiety
    JLabel FPSlabel,labelTotalPoints,labelPlayerLives,labelWeaponUpgrade,labelPause;

    GamePanel(){
        super(null);
        Color color = new Color(132, 142, 220);
        setBackground(color);
        //dodanie nasłuchiwania klawiszy
        addKeyListener(this);
        //inicjalizacja obiektow
        player= new Player(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
        lifeUI= new Life(10,C.FRAME_HEIGHT-100,25,25,this);
        playerShotUI= new PlayerShot(80,C.FRAME_HEIGHT-100,10,20,this);
        menuCursor = new MenuCursor(C.FRAME_WIDTH / 2 - 180, 310, this);
        //etykieta pokazyjąca FPS
        FPSlabel = new JLabel("");
        FPSlabel.setBounds(0, 10, 100, 30);
        FPSlabel.setForeground(Color.white);
        FPSlabel.setText("FPS: ");
        add(FPSlabel);
        labelTotalPoints = new JLabel("");
        labelTotalPoints.setBounds(C.FRAME_WIDTH-100, 10, 100, 30);
        labelTotalPoints.setForeground(Color.white);
        labelTotalPoints.setText("");
        add(labelTotalPoints);
        labelPlayerLives = new JLabel("");
        labelPlayerLives.setBounds(50, C.FRAME_HEIGHT-100, 100, 30);
        labelPlayerLives.setForeground(Color.white);
        labelPlayerLives.setText("");
        add(labelPlayerLives);
        labelWeaponUpgrade = new JLabel("");
        labelWeaponUpgrade.setBounds(100, C.FRAME_HEIGHT-100, 100, 30);
        labelWeaponUpgrade.setForeground(Color.white);
        labelWeaponUpgrade.setText("");
        add(labelWeaponUpgrade);
        labelPause = new JLabel("");
        labelPause.setBounds(C.FRAME_WIDTH / 2 - 130, C.FRAME_HEIGHT / 2 - 50, 300, 30);
        labelPause.setForeground(Color.white);
        add(labelPause);
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
            SoundManager.playMenuBackground();
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
                        ///////////////////////////////////////////// etykieta pauzy
                        if (C.PAUSE == true) labelPause.setText("Aby odpauzować, wciśnij klawisz \"p\".");
                        else labelPause.setText("");

                        //obsluga ruchu gracza STEROWANIE
                        if (RIGHT_PRESSED == true && C.PAUSE!=true) {
                            if (C.FRAME_WIDTH - 60 >= player.getX()) {
                                player.moveX(1); //poruszanie się w prawo
                            }
                            if (C.FRAME_WIDTH - 60 <= player.getX()) {
                                player.moveX(-5); //odbijanie się od krawędzi
                            }
                        }
                        if (LEFT_PRESSED == true && C.PAUSE!=true) {
                            if (0 <= player.getX()) {
                                player.moveX(-1); //poruszanie się w lewo
                            }
                            if (0 >= player.getX()) {
                                player.moveX(5); //odbijanie się od krawędzi
                            }
                        }
                        if (DOWN_PRESSED == true && C.PAUSE!=true) {
                            if (C.FRAME_HEIGHT - 150 >= player.getY()) {
                                player.moveY(1); //poruszanie się w dół
                            }
                        }
                        if (UP_PRESSED == true && C.PAUSE!=true) {
                            if (0 < player.getY()) {
                                player.moveY(-1); //poruszanie się w górę
                            }
                        }
                        if (SHOT_PRESSED == true && C.PAUSE!=true) {
                            isShotOnCooldown = true;
                            newPlayerShot();
                            SHOT_PRESSED = false;
                            try {
                               SoundManager.playPlayerShot();
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
                        if (listEnemy != null && C.PAUSE!=true) {
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

///////////////////////////////////kolizje obiektów//////////////////////////////////////////////////////////////////////
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
                                    C.totalPoints+=30;
                                    updateLabels();
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
                                //usun wrogow ktorych hp jest rowne lub mniejsze 0
                                if (enemy.getHP() <= 0) {
                                    listEnemy.remove(enemy);
                                }
                                //kolizja wroga i gracza
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH())) {
                                        listEnemy.remove(enemy);
                                        //wpisz akcja
                                        playerHit();
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
                                                    SoundManager.playEnemyHit(); //dzwiek zniszczenia wroga
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                if (playershot != null)
                                                    listPlayerShot.remove(playershot);
                                                //wpisz akcja
                                                newPoints(enemy.getX()+12, enemy.getY()+12, 25,25);
                                                C.totalPoints+=50;
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
                                    playerHit();
                                }
                            }
                        }

                        //////////////////////////////////warunek końca gry//////////////////////////////////////////////
                        if (C.playerLives <= 0) {

                            try {
                                SoundManager.playDefeat();
                                SoundManager.stopAllMusic();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            int enddialog = JOptionPane.showConfirmDialog
                                    (null, "Uzyskałeś " + C.totalPoints + " punktów!\nPrzegrałeś! Chcesz zagrać ponownie?",
                                            "Koniec gry", 0);
                            //zresetowanie gry po wybraniu tak
                            if (enddialog == 0) {
                                removeObjects();
                                resetVariables();
                                updateLabels();
                                try {
                                    SoundManager.playBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            //koniec gry po kliknieciu nie, przejscie do menu glownego
                            if (enddialog == 1) {
                                removeObjects();
                                resetVariables();
                                updateLabels();
                                C.GAMESTATE=1;
                                try {
                                    SoundManager.playMenuBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                resetLabels();
                            }
                        }
/////////////////////////////////////////////////////////  Levele   ///////////////////////////////////////////./////////////////
                        if(C.LEVEL==0){

                                if ((level_temp1 == 500 && level_temp2<=10)) {
                                    newEnemy(-30, 70, 50, 50);
                                    level_temp1 = 0;
                                    if(C.PAUSE!=true)level_temp2++;
                                } else if(C.PAUSE!=true)level_temp1++;

                        }

                        updateLabels();
                    }//Gamestate 0 -gra

                    if(C.GAMESTATE==1){
                    //obsługa menu
                        menuCursor.setX(C.FRAME_WIDTH / 2 - 180);
                        if (menu != null && C.cursorPosition == 0) {
                            menuCursor.setY(285);
                        }
                        if (menu != null && C.cursorPosition == 1) {
                            menuCursor.setY(385);
                        }
                        if (menu != null && C.cursorPosition == 2) {
                            menuCursor.setY(485);
                        }
                        if (menu != null && C.cursorPosition == 3) {
                            menuCursor.setY(585);
                        }
                        if (menu != null && C.cursorPosition == 4) {
                            menuCursor.setY(685);
                        }

                    }//GAMESTATE 1 menu

                    if(C.GAMESTATE==2){
                            if (menu != null && C.cursorSettingsPosition == 0) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(100);
                            }
                            if (menu != null && C.cursorSettingsPosition == 1) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(250);
                            }
                            if (menu != null && C.cursorSettingsPosition == 2) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(400);
                            }
                            if (menu != null && C.cursorSettingsPosition == 3) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(550);
                            }
                            if (menu != null && C.cursorSettingsPosition == 4) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 150);
                                menuCursor.setY(700);
                            }

                        
                    }//GAMESTATE 2 menusettings


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
            lifeUI.draw(g2D);//rysowanie obrazka w UI
            playerShotUI.draw(g2D);//rysowanie obrazka w UI
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
            menuSettings = new MenuSettings();
            menuSettings.draw(g2D);
            menuCursor.draw(g2D);
        }//GAMESTATE 2 -menuOpcje

        if (C.GAMESTATE == 4) {//GAMESTATE 3 - autorzy
            menuAuthors = new MenuAuthors();
            menuAuthors.draw(g2D);
        } //GAMESTATE 3 - autorzy
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
        C.totalPoints=0;C.playerLives=3;C.LEVEL=0;C.weaponUpgrade=0;
        level_temp1=0;level_temp2=0;
        player.setX(C.FRAME_WIDTH / 2 - 25); player.setY(C.FRAME_HEIGHT - 150);
        removeObjects();
        C.PAUSE= false;
    }
    public void removeObjects(){//wyczyszczenie zawartosci list obiektów
        //remove lists
        listEnemy.clear();
        listEnemyShot.clear();
        listPoints.clear();
        listPlayerShot.clear();

    }
    public void updateLabels(){
        if(C.GAMESTATE==0){
            labelTotalPoints.setText("Punkty: "+C.totalPoints);
            labelPlayerLives.setText("x "+C.playerLives);
            labelWeaponUpgrade.setText("x "+C.weaponUpgrade);

        }
    }
    public void resetLabels(){
            labelPause.setText("");
            labelTotalPoints.setText("");
            labelPlayerLives.setText("");
            labelWeaponUpgrade.setText("");
    }
    public void playerHit(){
        //if(tarcza==false){ todo
        try {
            SoundManager.playPlayerHit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        C.playerLives--;
            updateLabels();
            //updrage--
            //tarcza =true;

    }
    //keylistener do sterowania
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //////////////////////////////////////////////////////////////////
        if (e.getKeyCode()==27 && C.GAMESTATE==1){  //esc
            C.PAUSE=true;
            LEFT_PRESSED=false;
            RIGHT_PRESSED=false;
            UP_PRESSED=false;
            DOWN_PRESSED=false;
            SHOT_PRESSED=false;
            int enddialog = JOptionPane.showConfirmDialog
                    (null, "Czy chcesz powrócić do menu?",
                            "Pauza", 0);

            //powrót do menu po wybraniu tak
            if (enddialog == 0) {
                C.GAMESTATE=0;
                removeObjects();
                resetVariables();
                //updatePunktacja();
                resetLabels();
                //gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                C.PAUSE = false;
                //stopAllMusic();
                //playMenuBackground();
            }
            //wznowienie po kliknieciu nie
            if (enddialog == 1) {
                C.PAUSE = false;
            }
        }
        if (e.getKeyCode()==80) {//p przycisk pauza
            if (C.PAUSE == true) {
                C.PAUSE = false;
            } else {
                C.PAUSE = true;
                RIGHT_PRESSED = false;
                LEFT_PRESSED = false;
                DOWN_PRESSED = false;
                UP_PRESSED = false;
                SHOT_PRESSED = false;
            }
        }
        ////   ruch gracza
        if (e.getKeyCode()==37){//s w lewo
            LEFT_PRESSED=true;
            //obsługa dla menu opcje
            if(C.GAMESTATE==2){
                if(C.cursorSettingsPosition==0){
                    if(C.musicVolume!=0) {
                        C.musicVolume--;
                        //updateSettings();
                        try {
                            SoundManager.playPlayerShot();
                            SoundManager.stopMenuBackground();
                            SoundManager.playMenuBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                if(C.cursorSettingsPosition==1){
                    if(C.soundVolume!=0) {
                        C.soundVolume--;
                        //updateSettings();
                        try {
                            SoundManager.playPlayerShot();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                if(C.cursorSettingsPosition==2){
                    if(C.isMuted) {
                        C.isMuted=false;
                        //updateSettings();
                        try {
                            SoundManager.playBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
        if (e.getKeyCode()==39){//s w prawo
            RIGHT_PRESSED=true;
            //obsługa dla menu opcje
            if (C.GAMESTATE==2){
                if(C.cursorSettingsPosition==0){
                    if(C.musicVolume!=9) {
                        C.musicVolume++;
                        //updateSettings();
                        try {
                            SoundManager.playPlayerShot();
                            SoundManager.stopMenuBackground();
                            SoundManager.playMenuBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                if(C.cursorSettingsPosition==1){
                    if(C.soundVolume!=9) {
                        C.soundVolume++;
                        //updateSettings();
                        try {
                            SoundManager.playPlayerShot();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

                if(C.cursorSettingsPosition==2){
                    if(!C.isMuted) {
                        C.isMuted=true;
                        //updateSettings();
                        try {
                            SoundManager.stopBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
        if (e.getKeyCode()==40){//s w dol
            DOWN_PRESSED=true;
            //obsługa dla menu i menu opcje
            if(C.GAMESTATE==1 && C.cursorPosition<4){
                C.cursorPosition++;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(C.GAMESTATE==2 && C.cursorSettingsPosition<4){
                C.cursorSettingsPosition++;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (e.getKeyCode()==38){//s w gore
            UP_PRESSED=true;
            //obsługa dla menu i menu opcje
            if(C.GAMESTATE==1 && C.cursorPosition>0){
                C.cursorPosition--;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(C.GAMESTATE==2 && C.cursorSettingsPosition>0){
                C.cursorSettingsPosition--;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (e.getKeyCode()==32 && !isShotOnCooldown){//spacja - strzał
            SHOT_PRESSED=true;
        }
        if (e.getKeyCode()==10){//enter - zatwierdzanie w menu
            switch (C.GAMESTATE) {
                case 1:
                    if (C.cursorPosition == 0) {
                        C.GAMESTATE = 0;//gra
                        SoundManager.stopMenuBackground();
                        try {
                            SoundManager.playBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (C.cursorPosition == 1) {C.GAMESTATE = 3;}//jak grac
                    if (C.cursorPosition == 2) {C.GAMESTATE = 2;}//opcje
                    if (C.cursorPosition == 3) {C.GAMESTATE = 4;}//autorzy
                    if (C.cursorPosition == 4) System.exit(0);
                    break;
                case 2: /// obsluga entera w podmenu OPCJE
                    if(C.cursorSettingsPosition==3) {
                        //reset najlepszego wyniku
                        int enddialog = JOptionPane.showConfirmDialog
                                (null, "Czy na pewno chcesz zresetować najlepszy wynik?",
                                        "?", 0);
                        //zresetowanie po wybraniu tak
                        if (enddialog == 0)  return;//_-->resetHighscore();
                    }
                    if(C.cursorSettingsPosition==4) C.GAMESTATE = 1;
                    if(C.cursorSettingsPosition==2){
                        if (!C.isMuted) {
                            C.isMuted = true;
                            //stopAllMusic
                            //updateSettings
                        } else if (C.isMuted) {
                            C.isMuted = false;
//                                try {
//                                    SoundManager.
//                                } catch (Exception ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                                updateSettings();
                        }
                    }
                    break;
                case 3:
                    C.GAMESTATE = 1;//wyjscie z podmenu
                    break;
                case 4:
                    C.GAMESTATE = 1;//wyjscie z podmenu
                    break;

            }

        }


        if (e.getKeyCode()==27 ){//esc - powrot do menu
            C.GAMESTATE=1;
            resetVariables();
            resetLabels();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==37 && C.PAUSE!=true){//s w lewo
            LEFT_PRESSED=false;
        }
        if (e.getKeyCode()==39 && C.PAUSE!=true){//s w prawo
            RIGHT_PRESSED=false;
        }
        if (e.getKeyCode()==40 && C.PAUSE!=true){//s w dol
            DOWN_PRESSED=false;
        }
        if (e.getKeyCode()==38 && C.PAUSE!=true){//s w gore
            UP_PRESSED=false;
        }
        if (e.getKeyCode()==32 && C.PAUSE!=true){//spacja - strzał
            SHOT_PRESSED=false;
        }
    }
}
