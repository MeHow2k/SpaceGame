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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    Player player;//deklaracja obiektu Gracz
    PlayerShot playerShotUI;
    Life lifeUI; FirerateUpgrade firerateUpgradeUI;
    Font customFont;

    // deklaracja elementów menu
    Menu menu;MenuCursor menuCursor;MenuSettings menuSettings;MenuHowToPlay menuHowToPlay;MenuAuthors menuAuthors;Intro intro;
    MenuSkinSelection menuSkinSelection;
    //tu będą listy obiektów
    ArrayList<Enemy> listEnemy = new ArrayList(20);//lista wrogow
    ArrayList<EnemyLaser> listEnemyLaser = new ArrayList(20);//lista wrogow z laserem
    ArrayList<Meteor> listMeteor = new ArrayList(20);//lista meteorów
    ArrayList<Points> listPoints = new ArrayList(20);//lista punktów
    ArrayList<Life> listLife= new ArrayList(20);//lista punktów dodatkowe życie
    ArrayList<WeaponUpgrade> listWeaponUpgrade= new ArrayList(20);//lista punktów ulepszenie broni
    ArrayList<FirerateUpgrade> listFirerateUpgrade= new ArrayList(20);//lista punktów przyspieszenia strzelania
    ArrayList<BonusShield> listBonusShield= new ArrayList(20);//lista punktów dodatkowa tarcza
    ArrayList<EnemyShot> listEnemyShot = new ArrayList(20);//lista wrogich strzałów
    ArrayList<EnemyLaserShot> listEnemyLaserShot = new ArrayList(20);//lista wrogich strzałów laserów
    ArrayList<PlayerShot> listPlayerShot = new ArrayList(20);//lista strzalow gracza

    //zmienne bool zawirajace info czy naciśnięto przycisk
    boolean LEFT_PRESSED, RIGHT_PRESSED, DOWN_PRESSED, UP_PRESSED,SHOT_PRESSED,
            isShotOnCooldown=false;//czas do ponownego strzału
    boolean isMusicPlayed=false, isBossHpTaken= false;
    int level_temp1 =0; int level_temp2 =0;int level_temp3 =0;int enemyCreated =0,tick=0,level_delay=0;
    boolean tickUp=false,level_temp1Up=false,level_temp2Up=false,level_temp3Up=false,shieldCooldownDrop=false;
    int shotCooldown=60;
    int initialBossHP=0;
    //etykiety
    JLabel FPSlabel,labelTotalPoints,labelPlayerLives,labelWeaponUpgrade,labelPause,labelRecord,labelLevel;
    Thread gameThread;

    GamePanel(){
        super(null);
        Color color = new Color(0, 0, 0);
        setBackground(color);
        //import czcionki
        try {
            //import czcionki
            InputStream fontStream = getClass().getResourceAsStream("/VT323-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
        //dodanie nasłuchiwania klawiszy
        addKeyListener(this);
        //inicjalizacja obiektow
        player= new Player(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
        lifeUI= new Life(10,C.FRAME_HEIGHT-100,25,25,this);
        playerShotUI= new PlayerShot(80,C.FRAME_HEIGHT-97,10,20,this);
        firerateUpgradeUI = new FirerateUpgrade(140,C.FRAME_HEIGHT-100,25,25,this);
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
        labelRecord = new JLabel("");
        labelRecord.setBounds(C.FRAME_WIDTH - 100, 40, 100, 30);
        labelRecord.setForeground(Color.white);
        add(labelRecord);
        labelLevel = new JLabel("");
        labelLevel.setBounds(C.FRAME_WIDTH - 100, C.FRAME_HEIGHT-100, 100, 30);
        labelLevel.setForeground(Color.white);
        add(labelLevel);

        gameThread = new Thread(new Runnable() {
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


                    if(C.GAMESTATE==100){
                        if(C.intro_delay>280){
                            //Odtworzenie głównego motywu gry
                            try {
                                SoundManager.playMenuBackground();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            C.GAMESTATE=1;
                        }
                    }

                    //glowny watek gry- mechaniki itd.
                    if (C.GAMESTATE==0){
                        ///////////////////////////////////////////// etykieta pauzy
                        if (C.PAUSE == true) labelPause.setText("Aby odpauzować, wciśnij klawisz \"p\".");
                        else labelPause.setText("");
                        ///etykieta poziomu
                        labelLevel.setText("Poziom: " + C.LEVEL);
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
                        //obsługa strzelania gracza
                        if (SHOT_PRESSED == true && C.PAUSE!=true) {
                            isShotOnCooldown = true;
                            if(C.weaponUpgrade==1) newPlayerShot(player.getX()+20,player.getY());
                            if(C.weaponUpgrade==2){
                                newPlayerShot(player.getX()+40,player.getY());
                                newPlayerShot(player.getX(),player.getY());
                            }
                            if(C.weaponUpgrade==3){
                                newPlayerShot(player.getX()+40,player.getY());
                                newPlayerShot(player.getX(),player.getY());
                                newPlayerShot(player.getX()+22,player.getY());
                            }
                            SHOT_PRESSED = false;
                            try {
                               SoundManager.playPlayerShot();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                        // obsługa opóźnienia strzelania gracza
                        if(isShotOnCooldown){ shotCooldown--; }
                        if (shotCooldown <= 0) {
                            if(!C.isFirerateUpgrade) shotCooldown = 60;
                            if(C.isFirerateUpgrade) shotCooldown = 30;
                            isShotOnCooldown = false;
                        }

                        //opóźnienie wygaszenia tarczy
                        if (C.shieldActivated == true) {
                            shieldCooldownDrop=true;
                        }
                        if (C.shieldCooldown <= 0) {
                            shieldCooldownDrop=false;
                            C.shieldCooldown = C.SHIELD_COOLDOWN_TIME;
                            C.shieldActivated = false;
                        }

                        //generowanie strzałów wrogów
                        if (listEnemy != null && C.PAUSE!=true) {
                            for (int i = 0; i < listEnemy.size(); i++) {
                                Enemy enemy = listEnemy.get(i);
                                if (enemy.getIsBoss()==0){
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
                        }
                        //generowanie strzałów - laserów wrogów
                        if (listEnemyLaser != null && C.PAUSE != true) {
                            for (int i = 0; i < listEnemyLaser.size(); i++) {
                                EnemyLaser enemyLaser = listEnemyLaser.get(i);
                                Random random = new Random();
                                int roll = random.nextInt(300);
                                if (roll == 0) {
                                    if (enemyLaser.getFacingDirection() == 0){
                                        if(enemyLaser.getDirY()==1)
                                        newEnemyLaserShot(enemyLaser.getX() - 355, enemyLaser.getY() + enemyLaser.getH() - 10);
                                        else newEnemyLaserShot(enemyLaser.getX() - 355, enemyLaser.getY() + enemyLaser.getH() - 30);
                                    }
                                    if (enemyLaser.getFacingDirection() == 1){
                                        if(enemyLaser.getDirY()==1)
                                        newEnemyLaserShot(enemyLaser.getX() + enemyLaser.getW(), enemyLaser.getY() + enemyLaser.getH() - 10);
                                        else newEnemyLaserShot(enemyLaser.getX() + enemyLaser.getW(), enemyLaser.getY() + enemyLaser.getH() - 30);
                                    }
                                    try {
                                        SoundManager.playEnemyShot();//todo zmień na dzwiek wystrzału lasera!
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                        //generowanie wystrzalow potrojnych dla bossow 1 (isBoss==1)

                        if (listEnemy != null && C.PAUSE != true) {
                            for (int i = 0; i < listEnemy.size(); i++) {
                                Enemy enemy = listEnemy.get(i);
                                if (enemy.getIsBoss() == 1) {
                                    Random random = new Random();
                                    int roll = random.nextInt(200);
                                    if (roll == 0) {
                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 0);
                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 1);
                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 2);
                                        try {
                                            SoundManager.playEnemyShot();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                        }
                        //generowanie wystrzalow potrojnych dla bossow 2 (isBoss==2)
                        if (listEnemy != null && C.PAUSE != true) {
                            for (int i = 0; i < listEnemy.size(); i++) {
                                Enemy enemy = listEnemy.get(i);
                                if (enemy.getIsBoss() == 2) {
                                    Random random = new Random();
                                    int roll = random.nextInt(200);
                                    if (roll == 0 || (enemy.getHP()<=50 && roll<2)) {
                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                        newEnemyShot((int) (enemy.getX() + enemy.getW()*0.75), (int) (enemy.getY() + enemy.getH()*0.75), 20, 20, 1);//prawo dol
                                        newEnemyShot((int) (enemy.getX() + enemy.getW()*0.25), (int) (enemy.getY() + enemy.getH()*0.75), 20, 20, 2);//lewo dol
                                        newEnemyShot((int) (enemy.getX() + enemy.getW()*0.25), (int) (enemy.getY() + enemy.getH()*0.25), 20, 20, 3);//lewo gora
                                        newEnemyShot((int) (enemy.getX() + enemy.getW()*0.75), (int) (enemy.getY() + enemy.getH()*0.25), 20, 20, 4);//prawo gora
                                        newEnemyShot(enemy.getX() + (enemy.getW()), enemy.getY() + enemy.getH()/2, 20, 20, 5);//prawo
                                        newEnemyShot(enemy.getX(), enemy.getY() + enemy.getH()/2, 20, 20, 6);//lewo
                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY(), 20, 20, 7);//gora
                                        try {
                                            SoundManager.playEnemyShot();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
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
//kolizja gracza z DODADKOWYM ŻYCIEM,
                        if (listLife != null) {
                            for (int i = 0; i < listLife.size(); i++) {
                                Life life = listLife.get(i);
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        life.getX(), life.getY(), life.getW(), life.getH())) {
                                    try {
                                        SoundManager.playPoint();// todo zmien dzwiek!
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    C.playerLives++;
                                    C.totalPoints+=10;
                                    updateLabels();
                                    listLife.remove(life);
                                } else if (life.getY() > C.FRAME_HEIGHT) {
                                    listLife.remove(life);
                                }
                            }
                        }
//kolizja gracza z ULEPSZENIEM BRONI
                        if (listWeaponUpgrade != null) {
                            for (int i = 0; i < listWeaponUpgrade.size(); i++) {
                                WeaponUpgrade weaponUpgrade = listWeaponUpgrade.get(i);
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        weaponUpgrade.getX(), weaponUpgrade.getY(), weaponUpgrade.getW(), weaponUpgrade.getH())) {
                                    try {
                                        SoundManager.playPointBonus();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    upgradeWeapon();
                                    C.totalPoints+=10;
                                    updateLabels();
                                    listWeaponUpgrade.remove(weaponUpgrade);
                                } else if (weaponUpgrade.getY() > C.FRAME_HEIGHT) {
                                    listWeaponUpgrade.remove(weaponUpgrade);
                                }
                            }
                        }
//kolizja gracza z ULEPSZENIEM PRZYSPIESZENIA STRZELANIA
                        if (listFirerateUpgrade != null) {
                            for (int i = 0; i < listFirerateUpgrade.size(); i++) {
                                FirerateUpgrade firerateUpgrade = listFirerateUpgrade.get(i);
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        firerateUpgrade.getX(), firerateUpgrade.getY(), firerateUpgrade.getW(), firerateUpgrade.getH())) {
                                    try {
                                        SoundManager.playPoint();// todo zmien dzwiek!
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    C.isFirerateUpgrade=true;
                                    C.totalPoints+=10;
                                    updateLabels();
                                    listFirerateUpgrade.remove(firerateUpgrade);
                                } else if (firerateUpgrade.getY() > C.FRAME_HEIGHT) {
                                    listFirerateUpgrade.remove(firerateUpgrade);
                                }
                            }
                        }
//kolizja gracza z dodatkowa tarcza
                        if (listBonusShield != null) {
                            for (int i = 0; i < listBonusShield.size(); i++) {
                                BonusShield bonusShield = listBonusShield.get(i);
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        bonusShield.getX(), bonusShield.getY(), bonusShield.getW(), bonusShield.getH())) {
                                    try {
                                        SoundManager.playShield();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    C.shieldActivated=true;
                                    C.shieldCooldown=500;
                                    C.totalPoints+=10;
                                    updateLabels();
                                    listBonusShield.remove(bonusShield);
                                } else if (bonusShield.getY() > C.FRAME_HEIGHT) {
                                    listBonusShield.remove(bonusShield);
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
                                        enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH()) && !C.shieldActivated) {
                                        if(!enemy.isInvincible()) enemy.setHP(enemy.getHP()-1);
                                        //wpisz akcja
                                        playerHit();

                                }
                            }
                        }
                        //kolizja wrogaLasera i gracza
                        if (listEnemyLaser != null) {
                            for (int iw = 0; iw < listEnemyLaser.size(); iw++) {
                                EnemyLaser enemy = listEnemyLaser.get(iw);
                                //usun wrogow ktorych hp jest rowne lub mniejsze 0
                                if (enemy.getHp() <= 0) {
                                    listEnemyLaser.remove(enemy);
                                }
                                //kolizja wroga i gracza
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH()) && !C.shieldActivated) {
                                    enemy.setHp(enemy.getHp()-1);
                                    //wpisz akcja
                                    playerHit();
                                    listEnemyLaser.remove(enemy);
                                }
                            }
                        }
                        //kolizja meteora i gracza
                        if (listMeteor != null) {
                            for (int iw = 0; iw < listMeteor.size(); iw++) {
                                Meteor meteor = listMeteor.get(iw);
                                //kolizja z graczem
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        meteor.getX(), meteor.getY(), meteor.getW(), meteor.getH()) && !C.shieldActivated) {
                                    //wpisz akcja
                                    playerHit();
                                    if(meteor.getW()>=50) {
                                        onMeteorHit(meteor);
                                    }
                                    listMeteor.remove(meteor);
                                }else if (meteor.getY() > C.FRAME_HEIGHT) {
                                    listMeteor.remove(meteor);
                                }
                            }
                        }
//kolizja strzału gracza z obiektami
                        if (listPlayerShot != null) {
                            for (int i = 0; i < listPlayerShot.size(); i++) {
                                PlayerShot playershot = listPlayerShot.get(i);
                                ///////////////////// usuwanie wroga i strzału gdy ten ich trafi i dodanie pkt za to ////////////////////////
                                if(playershot.getY()>-10) {
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
                                                    newDrop(enemy.getX() + 12, enemy.getY() + 12, 13, 10, 10, 10);
                                                    //newPoints(enemy.getX()+12, enemy.getY()+12, 25,25);
                                                    C.totalPoints += 50;
                                                    updateLabels();
                                                } else {
                                                    if (playershot != null)
                                                        listPlayerShot.remove(playershot);
                                                    if (!enemy.isInvincible()) enemy.setHP(enemy.getHP() - 1);
                                                    if (enemy.getIsBoss() == 1 || enemy.getIsBoss() == 2 || enemy.getIsBoss() == 3)//todo remove this log!
                                                        System.out.println("BOSS HEALTH " + enemy.getHP()); //todo remove this log!
                                                }//todo remove this log!
                                            }
                                        }
                                    }//listenemy
                                    if (listMeteor != null) {
                                        for (int iw = 0; iw < listMeteor.size(); iw++) {
                                            Meteor meteor = listMeteor.get(iw);
                                            //kolizja z graczem
                                            if (isCollision(playershot.getX(), playershot.getY(), playershot.getW(), playershot.getH(),
                                                    meteor.getX(), meteor.getY(), meteor.getW(), meteor.getH())) {
                                                try {
                                                    SoundManager.playEnemyHit(); //dzwiek zniszczenia wroga todo
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                if (playershot != null)
                                                    listPlayerShot.remove(playershot);
                                                //wpisz akcja
                                                C.totalPoints += 5;
                                                //dropsystem
                                                if (meteor.getW() >= 50) {
                                                    onMeteorHit(meteor);
                                                }
                                                listMeteor.remove(meteor);
                                            }
                                        }
                                    }//listmeteor
                                    if (listEnemyLaser != null) {
                                        for (int iw = 0; iw < listEnemyLaser.size(); iw++) {
                                            EnemyLaser enemy = listEnemyLaser.get(iw);
                                            //kolizja wroga i strzału gracza
                                            if (isCollision(playershot.getX(), playershot.getY(), playershot.getW(), playershot.getH(),
                                                    enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH())) {
                                                //usunięcie wroga gdy ma 1 hp
                                                if (enemy.getHp() == 1) {
                                                    listEnemyLaser.remove(enemy);
                                                    try {
                                                        SoundManager.playEnemyHit(); //dzwiek zniszczenia wroga
                                                    } catch (Exception e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    if (playershot != null)
                                                        listPlayerShot.remove(playershot);
                                                    //wpisz akcja
                                                    newDrop(enemy.getX() + 12, enemy.getY() + 12, 13, 10, 10, 10);
                                                    C.totalPoints += 50;
                                                    updateLabels();
                                                } else {
                                                    if (playershot != null)
                                                        listPlayerShot.remove(playershot);
                                                    enemy.setHp(enemy.getHp() - 1);
                                                }
                                            }
                                        }
                                    }//listEnemyLaser
                                }
                            }
                        }
//kolizja strzału wroga z graczem
                        if (listEnemyShot != null && !C.shieldActivated) {
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
//kolizja strzału lasera wroga z graczem
                        if (listEnemyLaserShot != null) {
                            for (int iw = 0; iw < listEnemyLaserShot.size(); iw++) {
                                EnemyLaserShot enemyShot = listEnemyLaserShot.get(iw);
                                //kolizja strzału wroga i gracza
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        enemyShot.getX(), enemyShot.getY(), enemyShot.getW(), enemyShot.getH()) && !C.shieldActivated) {
                                    listEnemyLaserShot.remove(enemyShot);
                                    //wpisz akcja
                                    playerHit();
                                }
                                ///////////////////usuwanie laserow po uplywie czasu
                                if (enemyShot.getTime() > 20 && enemyShot != null) {
                                    listEnemyLaserShot.remove(enemyShot);
                                }
                            }
                        }

                        ////////////////////////////najlepszy wynik///////////////////////////////////////////////////////////////////////////
                        if(C.totalPoints>C.highscorePoints){
                            updateHighscore();
                            labelRecord.setText("Nowy rekord!");
                        }
                        updateLabels();
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

                        if (C.LEVEL == 99) { //do testowania
                            if (level_delay > 500) {//po opóźnieniu

                            }
                        }

                        if (C.LEVEL == 0) {
                            if (level_delay > 500) {//po opóźnieniu
                                if (C.isLevelCreated == false) newEnemy(0, 50, 50, 50);//stworz przeciwnika jesli lvl nie stworzony
                                C.isLevelCreated = true;//lvl stworzony
                                if (listEnemy.isEmpty()) {//gdzy nie ma wrogow na ekranie
                                    C.LEVEL++;
                                    System.out.println("LEVEL: " + C.LEVEL);
                                    newLife(500,-10);
                                    resetLevel();
                                }
                            }
                        }
//                        if (C.LEVEL == 0 ) {
//                            if(level_delay>300)//po opoznieniu (liczba klatek)
//                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
//                                    Random random = new Random();
//                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
//                                    newMeteor(random.nextInt( 1000)-500, -70, level_temp1, 1);
//                                    tick = 0;
//                                    enemyCreated++;
//                                    if (C.LEVEL == 0 && enemyCreated == 30) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
//                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy
//
//                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
//                                C.LEVEL++;
//                                System.out.println("LEVEL: " + C.LEVEL);
//                                resetLevel();
//                            }
//                        }

                        if (C.LEVEL == 1 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(0, 50, 50, 50, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 1 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 2 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(0, 0, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 350, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 2 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 3 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 5, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 3 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 4 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(0, 0, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 350, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 4 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 5 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 5, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 5 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 6 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 0, 0, 8, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 6 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 7 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(0, 0, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    newEnemy(C.FRAME_WIDTH - 50, 0, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 7 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 8 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(0, 50, 50, 50);
                                    newEnemy(-30, 100, 50, 50);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 8 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 9 ) {
                            if(level_delay>300)
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {
                                    newEnemy(C.FRAME_WIDTH - 50, 0, 1, 1, 2, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 9 && enemyCreated == 10) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }

                        if (C.LEVEL == 10) {//poziom bossa!
                            SoundManager.stopBackground();
                            if (isMusicPlayed == false) {
                                try {
                                    SoundManager.playBoss();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                           }
                            isMusicPlayed = true;
                            if(level_delay>500)//opoznienie przed pojawieniem sie bossa
                                if (tick > 180 && C.PAUSE != true && C.isLevelCreated == false) {
                                    newBoss(-100, 50, 1, 1, 3, C.FRAME_WIDTH / 2 - 90, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 50);
                                    enemyCreated++;
                                    if (enemyCreated == 1) C.isLevelCreated = true;

                                } else if (C.PAUSE != true && !C.isLevelCreated) tickUp=true;

                                if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                    C.LEVEL++;
                                    C.totalPoints += 500;
                                    System.out.println("LEVEL: " + C.LEVEL);
                                    resetLevel();
                                    isMusicPlayed = false;
                                }
                            }
                        if (C.LEVEL == 11 ) {
                            SoundManager.stopBoss();
                            if (isMusicPlayed == false) {
                                try {
                                    SoundManager.playBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            isMusicPlayed = true;
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 - 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 + 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 11 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 12 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 + 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 12 && enemyCreated == 6) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 13 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 13 && enemyCreated == 5) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 14 ) {
                            //warunek dodatkowy: jeśli hp wroga <=2 to zmien na movingtype 0
                            if (listEnemy != null) {
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if (enemy.getHP() <= 2) {
                                        enemy.setMovingType(0);
                                    }
                                }
                            }
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 3);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 14 && enemyCreated == 5) {C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                //playedMusic = false;?
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 15 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, 60, 1, 1, 2, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 1);
                                    tick = 0;

                                    enemyCreated++;
                                    if (C.LEVEL == 15 && enemyCreated == 10) {
                                        C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                        if (listEnemy != null) {
                                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                                Enemy enemy = listEnemy.get(iw);
                                                enemy.setMovingType(0);
                                            }
                                        }
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                //playedMusic = false;?
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 16 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 16 && enemyCreated == 16) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 17 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(0, C.FRAME_HEIGHT - 50, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    newEnemy(C.FRAME_WIDTH - 50, C.FRAME_HEIGHT - 50, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 17 && enemyCreated == 7) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 18 ) {
                            //warunek dodatkowy
                            if (listEnemy != null) {
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if (enemy.getHP() <= 2) {
                                        enemy.setDirX(enemy.getDirX()*-1);
                                        enemy.setMovingType(5);
                                    }
                                }
                            }
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 3);
                                    tick = 0;

                                    enemyCreated++;
                                    if (C.LEVEL == 18 && enemyCreated == 5) {C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                //playedMusic = false;?
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 19 ) {
                            //warunek dodatkowy
                            if (listEnemy != null) {
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if (enemy.getHP() <= 2) {
                                        enemy.setMovingType(0);
                                        enemy.setVelX(2);
                                    }
                                }
                            }
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-20, level_temp1, 1, 1, 0, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 3);
                                    tick = 0;
                                    level_temp1+=50;
                                    enemyCreated++;
                                    if (C.LEVEL == 19 && enemyCreated == 10) {
                                        C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                        if (listEnemy != null) {
                                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                                Enemy enemy = listEnemy.get(iw);
                                                enemy.setMovingType(0);
                                            }
                                        }
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                //playedMusic = false;?
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 20) {
                            if(SoundManager.clipback!=null)SoundManager.stopBackground();
                              if (isMusicPlayed == false) {
                                try {
                                    SoundManager.playBoss();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            isMusicPlayed = true;
                            if (tick > 500 && C.PAUSE != true && C.isLevelCreated == false) {
                                newBoss20(-100, 50, 1, 1, 3, C.FRAME_WIDTH / 2 - 90, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 200);
                                enemyCreated++;
                                tick = 0;
                                if (enemyCreated == 1) C.isLevelCreated = true;

                            } else if (C.PAUSE != true && C.isLevelCreated != true) tick++;
                            if(C.isLevelCreated==true && !(listEnemy.isEmpty())){
                                level_temp1Up=true;
                            }
                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                C.totalPoints+=2500;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.isLevelCreated = false;
                                enemyCreated = 0;
                                isMusicPlayed = false;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 21 ) {
                            SoundManager.stopBoss();
                            if (isMusicPlayed == false) {
                                try {
                                    SoundManager.playBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            isMusicPlayed = true;
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 - 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 5, C.FRAME_WIDTH / 2 - 25 + 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 21 && enemyCreated == 7) C.isLevelCreated = true;//jezeli stworzono 7 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 22 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 + 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 - 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 22 && enemyCreated == 7) C.isLevelCreated = true;//jezeli stworzono 7 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 23 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 23 && enemyCreated == 11) C.isLevelCreated = true;//jezeli stworzono 11 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 24 ) {
                            //warunek dodatkowy: jeśli hp wroga <=2 to zmien na movingtype 0
                            if (listEnemy != null) {
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if (enemy.getHP() <= 2) {
                                        enemy.setMovingType(0);
                                    }
                                }
                            }
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 4);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 24 && enemyCreated == 8) {C.isLevelCreated = true;//jezeli stworzono 8 przeciwnikow, poziom stworzony
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                //playedMusic = false;?
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 25 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    Random random = new Random();
                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                    newMeteor(random.nextInt(C.FRAME_WIDTH - 25), -75, level_temp1, 0);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 25 && enemyCreated == 30) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 26 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, 60, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 26 && enemyCreated == 10) {
                                        C.isLevelCreated = true;//jezeli stworzono 10 przeciwnikow, poziom stworzony
                                        if (listEnemy != null) {
                                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                                Enemy enemy = listEnemy.get(iw);
                                                enemy.setMovingType(0);
                                            }
                                        }
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                //playedMusic = false;?
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 27 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    Random random = new Random();
                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                    newMeteor(random.nextInt(C.FRAME_WIDTH + 250) + 200, -75, level_temp1, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 27 && enemyCreated == 30) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 28 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemyLaser(C.FRAME_WIDTH / 2 + 130, 0, 1, 1, 0,1,0,0,0,2);
                                    newEnemyLaser(C.FRAME_WIDTH / 2 - 130, 0, 1, 1, 1,1,0,0,0,2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 28 && enemyCreated == 7) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 29 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    Random random = new Random();
                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                    newMeteor(random.nextInt( 1000)-500, -70, level_temp1, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 29 && enemyCreated == 30) C.isLevelCreated = true;//jezeli stworzono 5 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 30) {
                            if(SoundManager.clipback!=null)SoundManager.stopBackground();
                            if (isMusicPlayed == false) {
                                try {
                                    SoundManager.playBoss();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            isMusicPlayed = true;
                            if (tick > 500 && C.PAUSE != true && C.isLevelCreated == false) {
                                newBoss30(-100, -100, 1, 1, 30, C.FRAME_WIDTH / 2 - 90, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 100);
                                enemyCreated++;
                                tick = 0;
                                if (enemyCreated == 1) C.isLevelCreated = true;
                            } else if (C.PAUSE != true && C.isLevelCreated != true) tick++;
                            else if (C.PAUSE != true && C.isLevelCreated){//po stworzeniu bossa -> MECHANIKA BOSSA3


                                if (level_temp1>500) { //zatrzymanie bossa i przywoływanie meteorow
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==3) {
                                            enemy.setMovingType(999);
                                            enemy.setInvincible(true);
                                        }
                                    }
                                    if(level_temp1%150==0 && level_temp1<1501) {//ostrzał meteorami
                                        Random random = new Random();
                                        int roll = random.nextInt(2);
                                        if (roll == 0) {
                                            level_temp2 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                            newMeteor(random.nextInt(C.FRAME_WIDTH - 25), -75, level_temp2, 0);
                                        }
                                        if (roll == 1) {
                                            level_temp2 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                            newMeteor(random.nextInt(1000) - 500, -70, level_temp2, 1);
                                        }
                                        if (roll == 2) {
                                            level_temp2 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                            newMeteor(random.nextInt(C.FRAME_WIDTH + 250) + 200, -75, level_temp2, 2);
                                        }
                                    }
                                }
                                if (level_temp1>1900){ //wznowienie ruchu bossa i zakończenie niewrażliwości
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==3) {
                                            enemy.setMovingType(30);
                                            enemy.setInvincible(false);
                                        }
                                    }
                                }
                                if (level_temp1>2000){//zresetowanie sekwencji
                                    level_temp1=0;
                                }

                            }
                            if(C.isLevelCreated==true && !(listEnemy.isEmpty())){
                                level_temp1Up=true;
                                level_temp3Up=true;
                            }
                            if (listEnemy.isEmpty() && listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                C.totalPoints+=2500;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.isLevelCreated = false;
                                enemyCreated = 0;
                                isMusicPlayed = false;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 31) { /// ostatni poziom koniec gry
                            SoundManager.stopBoss();
                            if (isMusicPlayed == false) {
                                try {
                                    SoundManager.playBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            removeEnemyObjects();
                            if (tick == 700) {
                                //okno dialogowe konca gry
                                int eenddialog = JOptionPane.showConfirmDialog
                                        (null, "Uzyskałeś " + C.totalPoints + " punktów!\nGratulacje! Czy chcesz zagrać ponownie?",
                                                "Ukończyłeś grę!", 0);
                                //zresetowanie gry po wybraniu tak
                                if (eenddialog == 0) {
                                    removeObjects();
                                    resetVariables();
                                    updateLabels();
                                    player = new Player(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                                    C.PAUSE = false;
                                    try {
                                        SoundManager.playBackground();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                //koniec gry po kliknieciu nie, przejscie do menu glownego
                                if (eenddialog == 1) {
                                    C.PAUSE = false;
                                    removeObjects();
                                    resetVariables();
                                    updateLabels();
                                    player = new Player(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                                    C.GAMESTATE = 1;
                                    try {
                                        SoundManager.playMenuBackground();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    resetLabels();
                                }
                            } else {
                                SoundManager.stopAllMusic();
                                if (isMusicPlayed == false) {
                                    try {
                                        SoundManager.playWin();
                                        isMusicPlayed = true;
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                tickUp = true;
                            }
                        }


                        updateLabels();
                    }//Gamestate 0 -gra

                    if(C.GAMESTATE==1){
                    //obsługa menu
                        menuCursor.setX(C.FRAME_WIDTH / 2 - 200);
                        if (menu != null && C.cursorPosition == 0) {
                            menuCursor.setY(290);
                        }
                        if (menu != null && C.cursorPosition == 1) {
                            menuCursor.setY(390);
                        }
                        if (menu != null && C.cursorPosition == 2) {
                            menuCursor.setY(490);
                        }
                        if (menu != null && C.cursorPosition == 3) {
                            menuCursor.setY(590);
                        }
                        if (menu != null && C.cursorPosition == 4) {
                            menuCursor.setY(690);
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
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 200);
                                menuCursor.setY(700);
                            }

                        
                    }//GAMESTATE 2 menusettings

                            if(C.GAMESTATE==5){
                        if (menuSkinSelection != null && C.cursorBeforeGamePosition == 5) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 170);
                            menuCursor.setY(690);
                        }



                    }//GAMESTATE 5 skin selection

                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (delta >= 1) {
                        repaint();
                        delta--;
                        frames++;

                        if(!C.PAUSE){
                            if(shieldCooldownDrop) C.shieldCooldown--;
                            level_delay++;
                            if(C.GAMESTATE==100) C.intro_delay++;
                            if(tickUp) tick++;
                            if(level_temp1Up) level_temp1++;
                            if(level_temp2Up) level_temp2++;
                            if(level_temp3Up) level_temp3++;
                        }
                    }
                    if (System.currentTimeMillis() - timer > 1000) { //co 1 s sprawdza liczbe narysowanych klatek
                        timer += 1000;
                        //wypisywanie liczby klatek na sekundę w etykiecie
                        FPSlabel.setText("FPS: " + frames + " ");
                        frames = 0;
                    }
                }//wh-true

            }
        });
        gameThread.start();
    }//GamePanel


    ////  Funkcja rysująca obiekty///////////////////////////////////////////////
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.white);
        Graphics2D g2D =(Graphics2D) g;
        Image imgBack = new ImageIcon(getClass().getClassLoader().getResource("background.gif")).getImage();
        if(C.GAMESTATE!=100) g.drawImage(imgBack, 0, 0, C.FRAME_WIDTH, C.FRAME_HEIGHT, null);

        if(C.GAMESTATE==0){
            player.draw(g2D);//rysowanie gracza

//tu będą pętle rysujące obiekty z list zadeklarowanych na początku
            if (listEnemy != null)            //rysowanie wrogow
                for (int i = 0; i < listEnemy.size(); i++) {
                    listEnemy.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
                }
            if (listEnemyLaser != null)            //rysowanie wrogow z laserem
                for (int i = 0; i < listEnemyLaser.size(); i++) {
                    listEnemyLaser.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
                }
            if (listMeteor != null)            //rysowanie meteorów
                for (int i = 0; i < listMeteor.size(); i++) {
                    listMeteor.get(i).draw(g2D);  //na każdym elemencie listy wykonanie draw()
                }
            if (listPoints != null)            //rysowanie punktow
                for (int i = 0; i < listPoints.size(); i++) {
                    listPoints.get(i).draw(g2D);
                }
            if (listLife != null)            //rysowanie dodadkowych zyc
                for (int i = 0; i < listLife.size(); i++) {
                    listLife.get(i).draw(g2D);
                }
            if (listWeaponUpgrade != null)            //rysowanie punktow ulepszenia broni
                for (int i = 0; i < listWeaponUpgrade.size(); i++) {
                    listWeaponUpgrade.get(i).draw(g2D);
                }
            if (listFirerateUpgrade != null)            //rysowanie punktow przyspieszenia strzelania
                for (int i = 0; i < listFirerateUpgrade.size(); i++) {
                    listFirerateUpgrade.get(i).draw(g2D);
                }
            if (listBonusShield != null)            //rysowanie punktow dodatkowej tarczy
                for (int i = 0; i < listBonusShield.size(); i++) {
                    listBonusShield.get(i).draw(g2D);
                }
            if (listEnemyShot != null)            //rysowanie wrogich strzałów
                for (int i = 0; i < listEnemyShot.size(); i++) {
                    listEnemyShot.get(i).draw(g2D);
                }
            if (listEnemyLaserShot != null)            //rysowanie wrogich strzałów laserem
                for (int i = 0; i < listEnemyLaserShot.size(); i++) {
                    listEnemyLaserShot.get(i).draw(g2D);
                }
            if (listPlayerShot != null)            //rysowanie strzałów gracza
                for (int i = 0; i < listPlayerShot.size(); i++) {
                    listPlayerShot.get(i).draw(g2D);
                }

            if(C.LEVEL==31){
                g.setColor(Color.white);
                g.setFont(customFont.deriveFont(40f));
                g.drawString("Gratulacje! Ukończyłeś grę!", C.FRAME_WIDTH/2 - 200, 200);
                g.drawString("Wynik końcowy: "+C.totalPoints, C.FRAME_WIDTH/2 - 170, C.FRAME_HEIGHT - 200);
            }
    //////////////////////UI i elementy//////////////////////////////////////////////////////////////
            if(C.GODMODE) g.drawString("GODMODE", 0, 200);
            lifeUI.draw(g2D);//rysowanie obrazka w UI
            playerShotUI.draw(g2D);//rysowanie obrazka w UI
            if(C.isFirerateUpgrade) firerateUpgradeUI.draw(g2D);

            //IMPLEMENTACJA PASKÓW I UI GDY NA PLANSZY JEST BOSS
            if(isBossInGame()) {
                if (!isBossHpTaken) {//przy pierwszym zczytaniu hp bossa
                    initialBossHP = getBossesHp();
                    isBossHpTaken = true;
                }
                ////PASKI HP BOSSÓW

                int fullbarWidth = initialBossHP * 3; // Szerokość paska pod paskiem HP
                int fullbarX = (C.FRAME_WIDTH - initialBossHP * 3) / 2;
                int barWidth = getBossesHp() * 3; // Szerokość paska HP zależna od ilości HP bossa
                int barX = (C.FRAME_WIDTH - barWidth) / 2; //  X paska HP dla wyśrodkowania
                g.setColor(Color.GRAY);
                g2D.fillRect(fullbarX, 35, fullbarWidth, 20);// pasek pod paskiem
                g.setColor(Color.RED);
                g2D.fillRect(barX, 35, barWidth, 20); // Wyśrodkowany pasek HP

                //// STRING NA PASKU HP BOSSOW
                String hpString = getBossesHp() + " / " + initialBossHP;
                int textWidth = g2D.getFontMetrics().stringWidth(hpString); // Szerokość tekstu
                int textX = (C.FRAME_WIDTH - textWidth) / 2; //  x tekstu dla wyśrodkowania
                g.setColor(Color.white);
                g.setFont(customFont.deriveFont(20f));
                g2D.drawString(hpString, textX, 50);
            }
            g.setColor(Color.white);

        }//gamestate 0

        if(C.GAMESTATE==100){
            intro = new Intro();
            intro.draw(g2D);
        }//GAMESTATE 100 - intro

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

        if(C.GAMESTATE==3){
            menuHowToPlay = new MenuHowToPlay();
            menuHowToPlay.draw(g2D);
        }

        if (C.GAMESTATE == 4) {//GAMESTATE 4 - autorzy
            menuAuthors = new MenuAuthors();
            menuAuthors.draw(g2D);
        } //GAMESTATE 4 - autorzy
        if (C.GAMESTATE == 5) {//GAMESTATE 5- skin selection
            menuSkinSelection = new MenuSkinSelection();
            menuSkinSelection.draw(g2D);
            if(C.cursorBeforeGamePosition==5)menuCursor.draw(g2D);
        } //GAMESTATE 5 - wybieranie skórek przed rozp. gry
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
    public void newDrop(int x,int y, int weaponUpgradeChance, int weaponFirerateChance, int bonusShieldChance, int bonusLifeChance){//funkcja losująca drop z przeciwnika
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1; // losuj liczbe od 1 do 100
        if (randomNumber <= weaponUpgradeChance) {//ulepszenie broni
            newWeaponUpgrade(x,y);
            System.out.println("weapon upg");
        } else if (randomNumber <= weaponUpgradeChance+ weaponFirerateChance) {//przyspieszenie strzelania
            newFirerateUpgrade(x,y);
            System.out.println("weapon firerate");
        } else if (randomNumber <= weaponUpgradeChance + weaponFirerateChance + bonusShieldChance) {//dodatkowa tarcza
            newBonusShield(x,y);
            System.out.println("shield");
        }else if (randomNumber <= weaponUpgradeChance + weaponFirerateChance + bonusShieldChance + bonusLifeChance) {//dodatkowe życie
            newLife(x,y);
            System.out.println("life");
        } else {
            newPoints(x,y,50,50);
        }
    }
    public boolean isBossInGame(){//sprawdza czy w grze jest boss czy nie
        if (listEnemy != null) {
            for (int iw = 0; iw < listEnemy.size(); iw++) {
                Enemy enemy = listEnemy.get(iw);
                if (enemy.getIsBoss()!=0) {
                    return true; //zwaraca true jesli boss jest na planszy
                }
            }
            return false;
        }
        return false;
    }
    public int getBossesHp(){//pobiera aktualną liczbę hp bossów
        if (listEnemy != null) {
            for (int iw = 0; iw < listEnemy.size(); iw++) {
                Enemy enemy = listEnemy.get(iw);
                if (enemy.getIsBoss()!=0) {
                    return enemy.getHP();
                }
            }
            return 0;
        }
        return 0;
    }
    public void newEnemy(int x,int y,int w,int h){//utworzenie obiektu wroga
        Enemy enemy = new Enemy(x,y,this);
        enemy.start();//start watku
        listEnemy.add(enemy);//dodanie do listy obiektow enemy
    }
    public void newEnemy(int x,int y,int w,int h, int movingType){//utworzenie obiektu wroga
        Enemy enemy = new Enemy(x,y,this);
        enemy.setMovingType(movingType);
        enemy.start();//start watku
        listEnemy.add(enemy);//dodanie do listy obiektow enemy
    }
    public void newMeteor(int x,int y,int size){//utworzenie obiektu meteoru
        Meteor meteor = new Meteor(x,y,size,size,this);
        meteor.start();
        listMeteor.add(meteor);
    }
    public void newMeteor(int x,int y,int size,int movingtype){//utworzenie obiektu meteoru
        Meteor meteor = new Meteor(x,y,size,size,this);
        meteor.setMovingType(movingtype);
        meteor.start();
        listMeteor.add(meteor);
    }
    //utworzenie obiektu wroga wraz z wszystkimi jego parametrami
    public void newEnemy(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        enemy.setMovingType(movingType);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.start();
        listEnemy.add(enemy);
    }
    //utworzenie obiektu wroga z laserem wraz z wszystkimi jego parametrami
    public void newEnemyLaser(int x,int y,int velX,int velY,int facingDirection,int movingType,int centerX,int centerY,int radius,int hp){
        EnemyLaser enemy = new EnemyLaser(x,y,50,50,velX,velY,facingDirection,movingType,centerX,centerY,radius,hp,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHp(hp);
        enemy.setMovingType(movingType);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.start();
        listEnemyLaser.add(enemy);
    }
    public void newBoss(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        enemy.setIsBoss(1);
        enemy.setMovingType(movingType);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(180);
        enemy.setH(90);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void newBoss20(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        enemy.setIsBoss(2);
        enemy.setMovingType(20);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(180);
        enemy.setH(180);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void newBoss30(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        enemy.setIsBoss(3);
        enemy.setMovingType(30);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(180);
        enemy.setH(180);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void onMeteorHit(Meteor meteor){//rozłupanie meteoru na 2
        if(meteor.getMovingType()==1) {
            newMeteor(meteor.getX() + meteor.getW() / 2, meteor.getY(), meteor.getW() / 2, 2);
            newMeteor(meteor.getX() + meteor.getW() / 2, meteor.getY(), meteor.getW() / 2, 1);
        }
        if(meteor.getMovingType()==2) {
            newMeteor(meteor.getX() + meteor.getW() / 2, meteor.getY(), meteor.getW() / 2, 1);
            newMeteor(meteor.getX() + meteor.getW() / 2, meteor.getY(), meteor.getW() / 2, 2);
        }
        if(meteor.getMovingType()==0){
            newMeteor(meteor.getX()+meteor.getW()/2,meteor.getY(),meteor.getW()/2,1);
            newMeteor(meteor.getX()+meteor.getW()/2,meteor.getY(),meteor.getW()/2,2);
        }
    }
    public void newPoints(int x,int y,int w,int h){//utworzenie obiektu wroga
        Points point = new Points(x,y,this);
        point.start();//start watku
        listPoints.add(point);//dodanie do listy obiektow
    }
    public void newLife(int x,int y){//utworzenie obiektu dodatkowego zycia
        Life life = new Life(x,y,25,25,this);
        life.start();//start watku
        listLife.add(life);//dodanie do listy obiektow
    }
    public void newEnemyLaserShot(int x,int y){//utworzenie obiektu lasera
        EnemyLaserShot enemyLaserShot = new EnemyLaserShot(x,y,this);
        enemyLaserShot.start();
        listEnemyLaserShot.add(enemyLaserShot);
    }
    public void newFirerateUpgrade(int x,int y){//utworzenie obiektu przyspieszenia strzelania
        FirerateUpgrade firerateUpgrade = new FirerateUpgrade(x,y,25,25,this);
        firerateUpgrade.start();//start watku
        listFirerateUpgrade.add(firerateUpgrade);//dodanie do listy obiektow
    }
    public void newBonusShield(int x,int y){//utworzenie obiektu dodatkowego zycia
        BonusShield bonusShield = new BonusShield(x,y,this);
        bonusShield.start();//start watku
        listBonusShield.add(bonusShield);//dodanie do listy obiektow
    }
    public void newWeaponUpgrade(int x,int y){//utworzenie obiektu ulepszenia broni
        WeaponUpgrade weaponUpgrade = new WeaponUpgrade(x,y,25,25,this);
        weaponUpgrade.start();//start watku
        listWeaponUpgrade.add(weaponUpgrade);//dodanie do listy obiektow
    }
    public void newEnemyShot(int x,int y){//utworzenie obiektu wrogiego strzału
        EnemyShot enemyShot = new EnemyShot(x,y,this);
        enemyShot.start();//start watku
        listEnemyShot.add(enemyShot);//dodanie do listy obiektow enemyShot
    }
    public void newEnemyShot(int x,int y, int w, int h, int movingType){//utworzenie obiektu wrogiego strzału
        EnemyShot enemyShot = new EnemyShot(x,y,this);
        enemyShot.setW(w);
        enemyShot.setH(h);
        enemyShot.setMovingType(movingType);
        enemyShot.start();//start watku
        listEnemyShot.add(enemyShot);//dodanie do listy obiektow enemyShot
    }
    public void newPlayerShot(int x, int y){//utworzenie obiektu strzału gracza
        PlayerShot playerShot = new PlayerShot(x,y,this);
        playerShot.start();//start watku
        listPlayerShot.add(playerShot);//dodanie do listy obiektow PlayerShot

    }
    public void resetVariables(){//przywrócenie zmiennych do stanu pierwotnego
        LEFT_PRESSED=false;RIGHT_PRESSED=false;UP_PRESSED=false;DOWN_PRESSED=false;SHOT_PRESSED=false;
        isShotOnCooldown=false;shotCooldown=60;C.shieldCooldown=C.SHIELD_COOLDOWN_TIME;
        C.totalPoints=0;C.playerLives=3;C.LEVEL=0;C.weaponUpgrade=1;C.shieldActivated=false;C.isFirerateUpgrade=false;
        player.setX(C.FRAME_WIDTH / 2 - 25); player.setY(C.FRAME_HEIGHT - 150);
        removeObjects();
        resetLevel();
        C.PAUSE= false;
    }
    public void resetLevel(){//przywrócenie zmiennych dot opóźnień w poziomach do stanu pierwotnego
        tick=0; tickUp=false; level_delay=0;
        level_temp3=0; level_temp3Up=false;
        level_temp2=0; level_temp2Up=false;
        level_temp1=0; level_temp1Up=false;
        enemyCreated=0; C.isLevelCreated=false;
        initialBossHP=0; isBossHpTaken=false;
    }
    public void removeObjects(){//wyczyszczenie zawartosci list obiektów
        //remove lists
        listEnemy.clear();
        listEnemyShot.clear();
        listPoints.clear();
        listPlayerShot.clear();
        listLife.clear();
        listWeaponUpgrade.clear();
        listMeteor.clear();
        listBonusShield.clear();
        listEnemyLaser.clear();
        listFirerateUpgrade.clear();
        listEnemyLaserShot.clear();

    }
    //usuwanie wrogich obiektow
    public void removeEnemyObjects() {
        if (listEnemy != null)
            for (int i = 0; i < listEnemy.size(); i++) {
                Enemy e = listEnemy.get(i);
                e.setHP(e.getHP()-10);
            }
        if (listEnemyLaser != null)
            for (int i = 0; i < listEnemyLaser.size(); i++) {
                EnemyLaser e = listEnemyLaser.get(i);
                e.setHp(e.getHp()-10);
            }
        if (listEnemyLaserShot != null) listEnemyLaserShot.clear();
        if (listEnemyShot != null) listEnemyShot.clear();
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
            labelRecord.setText("");
            labelLevel.setText("");
    }
    public void playerHit(){
        if(!C.GODMODE) {
            if (C.shieldActivated == false) {
                try {
                    SoundManager.playPlayerHit();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                C.playerLives--;
                if(C.weaponUpgrade>1) C.weaponUpgrade--;
                C.isFirerateUpgrade=false;
                updateLabels();
            }
            C.shieldActivated = true;
        }
    }
    public void upgradeWeapon(){
        if(C.weaponUpgrade<=2) C.weaponUpgrade++;
    }
    ////////akutualizacja pliku ustawien
    public void updateSettings(){
        try {
            //otwarcie pliku ustawień
            File config = new File("config.txt");
            FileWriter out = new FileWriter(config);
            //wpisanie aktualnych ustawień do pliku ustawień
            out.write(C.musicVolume + "\n" + C.soundVolume+"\n"+C.isMuted+"\n"+C.playerSkin);
            out.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
    /////////aktualizacja pliku z najlepszym wynikiem
    public void updateHighscore(){
        try {
            C.highscorePoints=C.totalPoints;
            C.highscoreLevel=C.LEVEL;
            //otwarcie pliku highscore
            File config = new File("highscore.txt");
            FileWriter out = new FileWriter(config);
            //wpisanie aktualnego najlepszego wyniku
            out.write(C.highscorePoints + "\n" + C.highscoreLevel);
            out.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
    public void resetHighscore(){
        try {
            C.highscorePoints=0;
            C.highscoreLevel=0;
            //otwarcie pliku highscore
            File config = new File("highscore.txt");
            FileWriter out = new FileWriter(config);
            //wpisanie aktualnego najlepszego wyniku
            out.write(C.highscorePoints + "\n" + C.highscoreLevel);
            out.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
    //keylistener do sterowania
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //////////////////////////////////////////////////////////////////
        if (e.getKeyCode()==32 && C.GAMESTATE==100){//spacja
            C.GAMESTATE=1;//skip intro
            try {
                SoundManager.playMenuBackground();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getKeyCode()==27 && C.GAMESTATE==0){  //esc
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
                C.GAMESTATE=1;
                removeObjects();
                resetVariables();
                updateLabels();
                resetLabels();
                C.PAUSE = false;
                SoundManager.stopAllMusic();
                try {
                    SoundManager.playMenuBackground();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //wznowienie po kliknieciu nie
            if (enddialog == 1) {
                C.PAUSE = false;
            }
        }
        if (e.getKeyCode()==27 && C.GAMESTATE==5){
            C.GAMESTATE=1;//powrot do menu z menu before game
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
        if (e.getKeyCode()==71) {//g przycisk wł/wył godmode
            if (C.GODMODE) {
                C.GODMODE = false;
            } else {
                C.GODMODE=true;
            }
        }
        if (e.getKeyCode()==84) {//T przycisk do testów testowania debug
            //newMeteor(100,0,50,0);
            //newMeteor(300,0,150,1);
            //newMeteor(500,-50,350,2);
            //newBonusShield(250,0);
             //newEnemyLaser(130, 0, 1, 1, 1,0,0,0,0,2);
            //newEnemyLaser(C.FRAME_WIDTH / 2 - 30, 0, 1, 1, 0,0,0,0,0,2);
            //newFirerateUpgrade(100,-10);
            //newWeaponUpgrade(100,-10);


             //-10hp kazdemu wrogowi
            if (listEnemy != null) {
                for (int iw = 0; iw < listEnemy.size(); iw++) {
                    Enemy enemy = listEnemy.get(iw);
                    //usun wrogow ktorych hp jest rowne lub mniejsze 0
                    if (enemy.getHP() <= 0) {
                        listEnemy.remove(enemy);
                    } else enemy.setHP(enemy.getHP() - 10);
                }
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
                        updateSettings();
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
                        updateSettings();
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
                        updateSettings();
                        try {
                            SoundManager.playMenuBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
            //obsługa dla menu wyboru statku
            if(C.GAMESTATE==5 && C.cursorBeforeGamePosition!=5){
                if (C.cursorBeforeGamePosition==0) C.cursorBeforeGamePosition=4;
                else C.cursorBeforeGamePosition--;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
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
                        updateSettings();
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
                        updateSettings();
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
                        updateSettings();
                        try {
                            SoundManager.stopAllMusic();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
            //obsługa dla menu wyboru statku
            if(C.GAMESTATE==5 && C.cursorBeforeGamePosition!=5){
                if (C.cursorBeforeGamePosition==4) C.cursorBeforeGamePosition=0;
               else C.cursorBeforeGamePosition++;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
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
            //obsługa dla menu wyboru statku
            if(C.GAMESTATE==5){
                if (C.cursorBeforeGamePosition!=5) C.cursorBeforeGamePosition=5;
                else C.cursorBeforeGamePosition=0;
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
            //obsługa dla menu wyboru statku
            if(C.GAMESTATE==5){
                if (C.cursorBeforeGamePosition==5) C.cursorBeforeGamePosition=0;
                else C.cursorBeforeGamePosition=5;
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
                        C.GAMESTATE = 5;//menu before game
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
                        if (enddialog == 0) resetHighscore();
                    }
                    if(C.cursorSettingsPosition==4) C.GAMESTATE = 1;
                    if(C.cursorSettingsPosition==2){
                        if (!C.isMuted) {
                            C.isMuted = true;
                            SoundManager.stopAllMusic();
                            updateSettings();
                        } else if (C.isMuted) {
                            C.isMuted = false;
                                try {
                                    SoundManager.playMenuBackground();
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                                updateSettings();
                        }
                    }
                    break;
                case 3:
                    C.GAMESTATE = 1;//wyjscie z podmenu
                    break;
                case 4:
                    C.GAMESTATE = 1;//wyjscie z podmenu
                    break;
                case 5:
                    try {
                        SoundManager.playPlayerShot();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if(C.cursorBeforeGamePosition==0) C.playerSkin=0;//skin select:
                    if(C.cursorBeforeGamePosition==1) C.playerSkin=1;
                    if(C.cursorBeforeGamePosition==2) C.playerSkin=2;
                    if(C.cursorBeforeGamePosition==3) C.playerSkin=3;
                    if(C.cursorBeforeGamePosition==4) C.playerSkin=4;
                    if(C.cursorBeforeGamePosition==5) {//graj
                        C.GAMESTATE=0;
                        updateSettings();
                        SoundManager.stopMenuBackground();
                        try {
                            SoundManager.playBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    break;

            }

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
