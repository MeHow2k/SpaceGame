///////////////GamePanel- główny wątek gry + menu itd /////////////

package Gamestates;

import Constants.C;
import Constants.Scores;
import Constants.Strings;
import Entities.*;
import Utils.DataSender;
import Utils.SoundManager;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Random;

import static Gamestates.Frame.createDefaultData2;
import static Gamestates.Frame.loadData2;

public class GamePanel extends JPanel implements KeyListener {
    static Date startDate;

    Player player;//deklaracja obiektu Gracz
    PlayerShot playerShotUI;
    Life lifeUI; FirerateUpgrade firerateUpgradeUI;
    Font customFont;
    Strings gameStrings;

    // deklaracja elementów menu
    Menu menu;MenuCursor menuCursor;MenuSettings menuSettings;MenuHowToPlay menuHowToPlay;MenuAuthors menuAuthors;Intro intro;
    LanguageSelection languageSelection;
    MenuSkinSelection menuSkinSelection; PlayerNameSelection playerNameSelection;MenuPlayerSettings menuPlayerSettings;MenuAchievements menuAchievements;
    MenuLeaderboard menuLeaderboard;
    //tu będą listy obiektów
    ArrayList<Enemy> listEnemy = new ArrayList(20);//lista wrogow
    ArrayList<EnemyLaser> listEnemyLaser = new ArrayList(20);//lista wrogow z laserem
    ArrayList<Enemy> listEnemyMenu = new ArrayList<>(20);//lista wrogów pojawiajacych sie w menu
    ArrayList<Meteor> listMeteor = new ArrayList(20);//lista meteorów
    ArrayList<Points> listPoints = new ArrayList(20);//lista punktów
    ArrayList<Life> listLife= new ArrayList(20);//lista punktów dodatkowe życie
    ArrayList<WeaponUpgrade> listWeaponUpgrade= new ArrayList(20);//lista punktów ulepszenie broni
    ArrayList<FirerateUpgrade> listFirerateUpgrade= new ArrayList(20);//lista punktów przyspieszenia strzelania
    ArrayList<BonusShield> listBonusShield= new ArrayList(20);//lista punktów dodatkowa tarcza
    ArrayList<BonusAllyAid> listBonusAllyAid= new ArrayList(20);//lista punktów pomoc sojusznika
    ArrayList<AllyAid> listAllyAid= new ArrayList(20);//lista punktów statków sojusznika
    ArrayList<EnemyShot> listEnemyShot = new ArrayList(20);//lista wrogich strzałów
    ArrayList<EnemyLaserShot> listEnemyLaserShot = new ArrayList(20);//lista wrogich strzałów laserów
    ArrayList<PlayerShot> listPlayerShot = new ArrayList(20);//lista strzalow gracza

    //zmienne bool zawirajace info czy naciśnięto przycisk
    boolean LEFT_PRESSED, RIGHT_PRESSED, DOWN_PRESSED, UP_PRESSED,SHOT_PRESSED,
            isShotOnCooldown=false;//czas do ponownego strzału

    boolean isMusicPlayed=false;
    boolean isBossHpTaken= false;
    boolean isBossAid=true;
    static boolean isBossDefeated=false;
    int level_temp1 =0; int level_temp2 =0;int level_temp3 =0;int enemyCreated =0,tick=0,level_delay=0,menudelay=1000;
    boolean tickUp=false;
    boolean level_temp1Up=false;
    boolean level_temp2Up=false;
    boolean level_temp3Up=false;
    boolean shieldCooldownDrop=false;
    static boolean isPlayerTookHit=false;
    int shotCooldown=60;
    int initialBossHP=0;
    //etykiety
    JLabel FPSlabel,labelTotalPoints,labelPlayerLives,labelWeaponUpgrade,labelPause,labelRecord,labelLevel;
    Thread gameThread;
    ///do statystyk
    static int livesEarned=0;
    static  int weaponUpgradeEarned=0;
    static  int firerateUpgradeEarned=0;
    static  int shieldEarned=0;
    static  int pointBoxEarned=0;
    static  int allyAidEarned=0;
    static  int shotsNumber=0;

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
        //dodanie obsługi tekstu
        gameStrings = new Strings();
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
                            if(C.LANGUAGE!=0 && C.LANGUAGE!=1) {
                                C.LANGUAGE=0;
                                C.GAMESTATE=99;
                            }
                            else if(C.PLAYER_NAME ==""){C.hasPlayerName=false; C.GAMESTATE=22;C.canEnterName=true;}else {C.hasPlayerName=true;C.GAMESTATE=1;}
                        }
                    }


                    //glowny watek gry- mechaniki itd.
                    if (C.GAMESTATE==0){

                        ///////////////////////////////////////////// etykieta pauzy

                        if (C.PAUSE == true) labelPause.setText(gameStrings.getString("Aby odpauzować"));
                        else labelPause.setText("");
                        ///etykieta poziomu
                        if(C.LEVEL==C.LAST_LEVEL) labelLevel.setText(" ");
                        else labelLevel.setText(gameStrings.getString("poziom")+" " + C.LEVEL);

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
                            if(C.weaponUpgrade==1) {
                                newPlayerShot(player.getX()+20,player.getY());
                                shotsNumber++;
                            }
                            if(C.weaponUpgrade==2){
                                newPlayerShot(player.getX()+40,player.getY());
                                newPlayerShot(player.getX(),player.getY());
                                shotsNumber+=2;
                            }
                            if(C.weaponUpgrade==3){
                                newPlayerShot(player.getX()+40,player.getY());
                                newPlayerShot(player.getX(),player.getY());
                                newPlayerShot(player.getX()+22,player.getY());
                                shotsNumber+=3;
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
                                        SoundManager.playLaser();
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
                                    pointBoxEarned++;
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
                                        SoundManager.playPointLife();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    C.playerLives++;
                                    livesEarned++;
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
                                        SoundManager.playWeaponUpgrade();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    upgradeWeapon();
                                    weaponUpgradeEarned++;
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
                                        SoundManager.playFirerateUpgrade();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    C.isFirerateUpgrade=true;
                                    firerateUpgradeEarned++;
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
                                    shieldEarned++;
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
//kolizja gracza z bonusem pomoc sojusznika,
                        if (listBonusAllyAid != null) {
                            for (int i = 0; i < listBonusAllyAid.size(); i++) {
                                BonusAllyAid bonusAllyAid = listBonusAllyAid.get(i);
                                if (isCollision(player.getX(), player.getY(), player.getW(), player.getH(),
                                        bonusAllyAid.getX(), bonusAllyAid.getY(), bonusAllyAid.getW(), bonusAllyAid.getH())) {
                                    try {
                                        SoundManager.playPointBonus();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //tutaj wpisz akcja
                                    C.totalPoints+=10;
                                    allyAidEarned++;
                                    newAllyAid(-40,20,0);
                                    updateLabels();
                                    listBonusAllyAid.remove(bonusAllyAid);
                                } else if (bonusAllyAid.getY() > C.FRAME_HEIGHT) {
                                    listBonusAllyAid.remove(bonusAllyAid);
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
                                                    //newDrop(enemy.getX() + 12, enemy.getY() + 12, 13, 10, 10, 10,4);
                                                    if(enemy.getIsBoss()>10){
                                                        newDrop(enemy.getX() + 12, enemy.getY() + 12, 0, 0, 0, 0,100);
                                                    }else newDrop(enemy.getX() + 12, enemy.getY() + 12, 13, 10, 10, 10,4);
                                                    C.totalPoints += 50;
                                                    updateLabels();
                                                } else {
                                                    if (playershot != null)
                                                        listPlayerShot.remove(playershot);
                                                    if (!enemy.isInvincible()) enemy.setHP(enemy.getHP() - 1);
                                                }
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
                                                    SoundManager.playMeteorHit();
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
                                                    newDrop(enemy.getX() + 12, enemy.getY() + 12, 13, 10, 10, 10,4);
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
                                }//if player shot
                            }//for listplayershot
                        }//if shot null
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
//generowanie bonusów przez statek sojusznika
                        if (listAllyAid != null) {
                            for (int i = 0; i < listAllyAid.size(); i++) {
                                AllyAid allyAid = listAllyAid.get(i);
                                if(allyAid.getX()==C.FRAME_WIDTH/2-allyAid.getW()/2 && allyAid.getIsAidDropped()==false){
                                    int upgradeChance=20,firerateChance=20,shieldChance=30,lifeChance=30;
                                    if(C.weaponUpgrade>=3){
                                        upgradeChance=0;shieldChance+=10;lifeChance+=10;
                                    } else upgradeChance=20;
                                    if(C.isFirerateUpgrade) {
                                        firerateChance=0;shieldChance+=10;lifeChance+=10;
                                    } else firerateChance=20;
                                    newDrop(allyAid.getX()+allyAid.getW()/2,allyAid.getY(),upgradeChance,firerateChance,shieldChance,lifeChance,0);
                                    newDrop(allyAid.getX(),allyAid.getY(),upgradeChance,firerateChance,shieldChance,lifeChance,0);
                                    allyAid.setIsAidDropped(true);
                                }
                                if(allyAid.getX()<-100 || allyAid.getX()>C.FRAME_HEIGHT+allyAid.getW()){
                                    listAllyAid.remove(allyAid);
                                }
                            }
                        }

                        ////////////////////////////najlepszy wynik///////////////////////////////////////////////////////////////////////////
                        if(C.totalPoints>C.highscorePoints){
                            updateHighscore();
                            labelRecord.setText(gameStrings.getString("Nowy rekord!"));
                        }
                        updateLabels();
                        //////////////////////////////////warunek końca gry//////////////////////////////////////////////
                        if (C.playerLives <= 0) {
                            checkAchievements();
                            try {
                                SoundManager.playDefeat();
                                SoundManager.stopAllMusic();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            updateLeaderboard();
                            sendData();

                            int enddialog = JOptionPane.showConfirmDialog
                                    (null, gameStrings.getString("Uzyskałeś") + " " + C.totalPoints+ " " + gameStrings.getString("punktów") +"\n"+ gameStrings.getString("Przegrałeś!")
                                            + " " +gameStrings.getString("Czy chcesz zagrać ponownie?"),
                                            gameStrings.getString("Koniec gry"), 0);
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
                                    level_temp1Up=true;
                                    if(level_temp1>500){
                                        C.LEVEL++;
                                        resetLevel();
                                    }
                                }
                            }
                        }

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

                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                Enemy enemy = listEnemy.get(iw);
                                if(enemy.getIsBoss()==1 && enemy.getHP()%10==0){
                                    if(isBossAid)newAllyAid(-40,20,0);
                                    isBossAid=false;
                                }
                                else isBossAid=true;
                            }

                            if(level_delay>500)//opoznienie przed pojawieniem sie bossa
                                if (tick > 180 && C.PAUSE != true && C.isLevelCreated == false) {
                                    newBoss(-100, 50, 1, 1, 3, C.FRAME_WIDTH / 2 - 90, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 50);
                                    enemyCreated++;
                                    if (enemyCreated == 1) C.isLevelCreated = true;

                                } else if (C.PAUSE != true && !C.isLevelCreated) tickUp=true;

                                if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                    isBossDefeated=true;
                                    checkAchievements();
                                    C.LEVEL++;
                                    C.totalPoints += 500;
                                    resetLevel();
                                    isMusicPlayed = false;
                                }
                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                Enemy enemy = listEnemy.get(iw);
                                if(enemy.getIsBoss()==1 && enemy.getHP()%10==0){
                                    if(isBossAid)newAllyAid(-40,20,0);
                                    isBossAid=false;
                                }
                                else isBossAid=true;
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
                                isBossDefeated=true;
                                checkAchievements();
                                C.LEVEL++;
                                C.totalPoints+=2500;
                                C.isLevelCreated = false;
                                enemyCreated = 0;
                                isMusicPlayed = false;
                                resetLevel();
                            }
                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                Enemy enemy = listEnemy.get(iw);
                                if(enemy.getIsBoss()==2 && enemy.getHP()%10==0){
                                    if(isBossAid)newAllyAid(-40,20,0);
                                    isBossAid=false;
                                }
                                else isBossAid=true;
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
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 26 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, 60, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
                                    newEnemy(-60, -10, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
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
                                isBossDefeated=true;
                                checkAchievements();
                                C.LEVEL++;
                                C.totalPoints+=2500;
                                C.isLevelCreated = false;
                                enemyCreated = 0;
                                isMusicPlayed = false;
                                resetLevel();
                            }
                            for (int iw = 0; iw < listEnemy.size(); iw++) {
                                Enemy enemy = listEnemy.get(iw);
                                if(enemy.getIsBoss()==3 && enemy.getHP()%20==0){
                                    if(isBossAid)newAllyAid(-40,20,0);
                                    isBossAid=false;
                                }
                                else isBossAid=true;
                            }
                        }
                        if (C.LEVEL == 31 ) {
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
                                    if (C.LEVEL == 31 && enemyCreated == 8) C.isLevelCreated = true;//jezeli stworzono 8 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 32 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 + 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 - 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 32 && enemyCreated == 8) C.isLevelCreated = true;//jezeli stworzono 8 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 33 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 33 && enemyCreated == 12) C.isLevelCreated = true;//jezeli stworzono 12 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 34 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemyLaser(C.FRAME_WIDTH - 100, 0, 1, 1, 0,0,0,0,0,2);
                                    //newEnemyLaser(50, 0, 1, 1, 1,0,0,0,0,2);
                                    newEnemy(C.FRAME_WIDTH+50, 50, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 34 && enemyCreated == 5) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemyLaser.isEmpty() && listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 35 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    Random random = new Random();
                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                    newMeteor(random.nextInt(C.FRAME_WIDTH - 25), -75, level_temp1, 0);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 35 && enemyCreated == 40) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 36 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, 60, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
                                    newEnemy(-60, 0, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 36 && enemyCreated == 12) {
                                        C.isLevelCreated = true;
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
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 37 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemyLaser(C.FRAME_WIDTH / 2 + 130, 0, 1, 1, 0,1,0,0,0,2);
                                    newEnemyLaser(C.FRAME_WIDTH / 2 - 130, 0, 1, 1, 1,1,0,0,0,2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 37 && enemyCreated == 8) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 38 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 70 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    Random random=new Random();
                                    if(level_temp1%2==0)
                                        newEnemyLaser(C.FRAME_WIDTH - 100, 0, 1, 1,
                                                random.nextInt(2),7,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,270,2);
                                    else newEnemy(C.FRAME_WIDTH - 100, 0, 1, 1,7,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,180,2);
                                    level_temp1++;
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 38 && enemyCreated == 10) {
                                        C.isLevelCreated = true;
                                        if(listEnemyLaser!=null){
                                            for (int iw = 0; iw < listEnemyLaser.size(); iw++) {
                                                EnemyLaser enemy = listEnemyLaser.get(iw);
                                                enemy.setMovingType(0);
                                            }
                                        }
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemyLaser.isEmpty() && listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 39 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    Random random = new Random();
                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                    newMeteor(random.nextInt( 1000)-500, -70, level_temp1, 1);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 39 && enemyCreated == 40) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 40) {
                            boolean isStoppedMoving=false;
                            int phase=1;
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
                                newBoss40(-100, -100, 1, 1, 40, C.FRAME_WIDTH / 2 - 90, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 100);
                                newSpikeBall(-100, -100, 1, 1, 40, C.FRAME_WIDTH / 2 - 90-220, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 70,0);
                                newSpikeBall(-100, -100, 1, 1, 40, C.FRAME_WIDTH / 2 - 90+220, C.FRAME_HEIGHT / 2 - 45 - 100, 200, 70,1);
                                enemyCreated++;
                                tick = 0;
                                if (enemyCreated == 1) C.isLevelCreated = true;
                            } else if (C.PAUSE != true && C.isLevelCreated != true) tick++;
                            else if (C.PAUSE != true && C.isLevelCreated){//po stworzeniu bossa -> MECHANIKA BOSSA4

                                if(listEnemy.size()<=1){//gdy zostanie sam boss zmien faze na 2
                                    phase=2;
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        enemy.setInvincible(false);
                                        enemy.setBossPhase(2);
                                    }
                                }
                                if(level_temp1%100==0) { //generowanie strzałów bossa
                                    level_temp1++;
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==4) {
                                            try {
                                                SoundManager.playEnemyShot();
                                            } catch (Exception e) {}
                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                        newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.75), (int) (enemy.getY() + enemy.getH() * 0.75), 20, 20, 1);//prawo dol
                                        newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.25), (int) (enemy.getY() + enemy.getH() * 0.75), 20, 20, 2);//lewo dol
                                        }
                                    }
                                }
                                if (level_temp1>500) { //zatrzymanie bossa i atak spikeball
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);

                                        //Random random = new Random();
                                        //int roll= random.nextInt(2);
                                        if(!isStoppedMoving){
                                            if(enemy.getIsBoss()==4) {
                                                if(phase==2)enemy.setMovingType(47);
                                                if(phase==1)enemy.setMovingType(999);

                                            }
                                            if(enemy.getIsBoss()==41 && phase==1) {
                                                enemy.setMovingType(999);
                                                enemy.setInvincible(false);
                                            }
                                            else if(enemy.getIsBoss()==42&& phase==1) {
                                                enemy.setMovingType(999);
                                                enemy.setInvincible(false);
                                            }
                                            isStoppedMoving=true;
                                        }
                                        if(enemy.getIsBoss()==41&& phase==1) {
                                            enemy.setMovingType(44);
                                            enemy.setInvincible(false);
                                        }
                                        else if(enemy.getIsBoss()==42&& phase==1) {
                                            enemy.setMovingType(44);
                                            enemy.setInvincible(false);
                                        }

                                    }
                                    if(level_temp1%101==0 && level_temp1<1000) {
                                        level_temp1++;
                                        for (int iw = 0; iw < listEnemy.size(); iw++) {
                                            Enemy enemy = listEnemy.get(iw);

                                                if ( phase==1 && (enemy.getIsBoss() == 41 || enemy.getIsBoss() == 42)) {
                                                    try {
                                                        SoundManager.playEnemyShot();
                                                    } catch (Exception e) {}
                                                    newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                                    newEnemyShot((int) (enemy.getX() + enemy.getW()*0.75), (int) (enemy.getY() + enemy.getH()*0.75), 20, 20, 1);//prawo dol
                                                    newEnemyShot((int) (enemy.getX() + enemy.getW()*0.25), (int) (enemy.getY() + enemy.getH()*0.75), 20, 20, 2);//lewo dol
                                                    newEnemyShot((int) (enemy.getX() + enemy.getW()*0.25), (int) (enemy.getY() + enemy.getH()*0.25), 20, 20, 3);//lewo gora
                                                    newEnemyShot((int) (enemy.getX() + enemy.getW()*0.75), (int) (enemy.getY() + enemy.getH()*0.25), 20, 20, 4);//prawo gora
                                                    newEnemyShot(enemy.getX() + (enemy.getW()), enemy.getY() + enemy.getH()/2, 20, 20, 5);//prawo
                                                    newEnemyShot(enemy.getX(), enemy.getY() + enemy.getH()/2, 20, 20, 6);//lewo
                                                    newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY(), 20, 20, 7);//gora
                                                }
                                                if (enemy.getIsBoss() == 4) {
                                                    if(phase==2) {
                                                        Random random = new Random();
                                                        try {
                                                            SoundManager.playLaser();
                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                        for(int i=0;i<=5;i++){
                                                            newEnemyLaserShot(-200,random.nextInt(C.FRAME_HEIGHT-40)+30);
                                                            newEnemyLaserShot(C.FRAME_WIDTH-150,random.nextInt(C.FRAME_HEIGHT-40)+30);
                                                        }
                                                    }
                                                    try {
                                                        SoundManager.playEnemyShot();
                                                    } catch (Exception e) {}
                                                        newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                                        newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.75), (int) (enemy.getY() + enemy.getH() * 0.75), 20, 20, 1);//prawo dol
                                                        newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.25), (int) (enemy.getY() + enemy.getH() * 0.75), 20, 20, 2);//lewo dol
                                                }

                                        }
                                    }
                                }
                                if (level_temp1>1000){ //wznowienie ruchu bossa
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==4) {
                                            if(phase==1) enemy.setMovingType(40);
                                            else enemy.setMovingType(46);
                                        }
                                        if( phase==1 && (enemy.getIsBoss()==41 || enemy.getIsBoss()==42)) {
                                            enemy.setMovingType(40);
                                        }

                                    }
                                }
                                if (level_temp1>1100){//zresetowanie sekwencji
                                    level_temp1=0;
                                }

                            }
                            if(C.isLevelCreated==true && !(listEnemy.isEmpty())){
                                level_temp1Up=true;
                                level_temp3Up=true;
                            }
                            if (listEnemy.isEmpty() && listMeteor.isEmpty() && C.isLevelCreated == true) {
                                isBossDefeated=true;
                                checkAchievements();
                                C.LEVEL++;
                                C.totalPoints+=2500;
                                C.isLevelCreated = false;
                                enemyCreated = 0;
                                isMusicPlayed = false;
                                resetLevel();
                            }

                            if(phase==2) {
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if ((enemy.getIsBoss() == 4) && enemy.getHP() % 20 == 0) {
                                        if (isBossAid) newAllyAid(-40, 20, 0);
                                        isBossAid = false;

                                    } else isBossAid = true;
                                }
                            } else {
                                int totalBosseshp=0;
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if (enemy.getIsBoss() == 41 || enemy.getIsBoss()==42) {
                                        totalBosseshp+=enemy.getHP();
                                    }
                                }
                                    if(totalBosseshp% 20 == 0){
                                        if (isBossAid) newAllyAid(-40, 20, 0);
                                        isBossAid = false;
                                    } else isBossAid = true;
                            }
                        }
                        if (C.LEVEL == 41 ) {
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
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 - 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 3);
                                    newEnemy(-60, -60, 1, 1, 5, C.FRAME_WIDTH / 2 - 25 + 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 3);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 41 && enemyCreated == 8) C.isLevelCreated = true;//jezeli stworzono 8 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 42 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 + 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                    newEnemy(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                    newEnemy(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25 - 200, C.FRAME_HEIGHT / 2 - 25 - 200, 200, 2);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 42 && enemyCreated == 10) C.isLevelCreated = true;//jezeli stworzono 8 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 43 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 60 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 3);
                                    newEnemy(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 3);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 43 && enemyCreated == 12) C.isLevelCreated = true;//jezeli stworzono 12 przeciwnikow, poziom stworzony
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 44 ) {
                            Random random=new Random();
                            if(listEnemy!=null){
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if(enemy.getHP()<2) {
                                        int roll=random.nextInt(2);
                                        if(roll==0) enemy.setDirX(-1);
                                        if(roll==1) enemy.setDirX(1);
                                        enemy.setMovingType(1);
                                    }
                                }
                            }
                            if(level_delay>300)//po opoznieniu
                                if (tick > 70 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz

                                    if(level_temp1%2==0)
                                        newEnemy(C.FRAME_WIDTH - 100, 0, 1, 1,7,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,270,4);
                                    else newEnemy(C.FRAME_WIDTH - 100, 0, 1, 1,7,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,180,4);
                                    level_temp1++;
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 44 && enemyCreated == 10) {
                                        C.isLevelCreated = true;
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 45 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    Random random = new Random();
                                    level_temp1 = random.nextInt(160) + 40;//losowanie rozmiaru meteora
                                    newMeteor(random.nextInt(C.FRAME_WIDTH - 25), -75, level_temp1, 0);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 45 && enemyCreated == 40) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listMeteor.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 46 ) {
                            if(level_delay>300)//po opoznieniu (liczba klatek)
                                if (tick >100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 ticków powtórz
                                    newEnemy(-60, 60, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 4);
                                    newEnemy(-60, 0, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 200, 200, 4);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 46 && enemyCreated == 11) {
                                        C.isLevelCreated = true;
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
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 47 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 100 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemyLaser(C.FRAME_WIDTH / 2 + 130, 0, 1, 1, 0,1,0,0,0,3);
                                    newEnemyLaser(C.FRAME_WIDTH / 2 - 130, 0, 1, 1, 1,1,0,0,0,4);
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 47 && enemyCreated == 9) C.isLevelCreated = true;
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 48 ) {
                            if(level_delay>300)//po opoznieniu
                                if (tick > 70 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    Random random=new Random();
                                    if(level_temp1%2==0)
                                        newEnemyLaser(C.FRAME_WIDTH - 100, 0, 1, 1,
                                                random.nextInt(2),7,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,270,4);
                                    else newEnemy(C.FRAME_WIDTH - 100, 0, 1, 1,7,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,180,4);
                                    level_temp1++;
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 48 && enemyCreated == 10) {
                                        C.isLevelCreated = true;
                                        if(listEnemyLaser!=null){
                                            for (int iw = 0; iw < listEnemyLaser.size(); iw++) {
                                                EnemyLaser enemy = listEnemyLaser.get(iw);
                                                enemy.setMovingType(0);
                                            }
                                        }
                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemyLaser.isEmpty() && listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }
                        if (C.LEVEL == 49 ) {
                            Random random=new Random();
                            if(listEnemyLaser!=null){
                                for (int iw = 0; iw < listEnemyLaser.size(); iw++) {
                                    EnemyLaser enemy = listEnemyLaser.get(iw);
                                    if(enemy.getHp()==3) enemy.setVelY(2);
                                    if(enemy.getHp()==2) enemy.setVelY(3);
                                    if(enemy.getHp()==2) enemy.setVelY(4);
                                }
                            }

                            if(level_delay>300)//po opoznieniu
                                if (tick > 200 && C.PAUSE != true && C.isLevelCreated == false) {//co 100 klatek powtórz
                                    newEnemyLaser(random.nextInt(C.FRAME_WIDTH/2), -10, 1, 1,1,0,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,270,4);
                                    newEnemyLaser(random.nextInt(C.FRAME_WIDTH/2 -100 )+ C.FRAME_WIDTH/2, -10, 1, 1,0,0,C.FRAME_WIDTH/2,C.FRAME_HEIGHT/2-100,180,4);
                                    level_temp1++;
                                    tick = 0;
                                    enemyCreated++;
                                    if (C.LEVEL == 49 && enemyCreated == 5) {
                                        C.isLevelCreated = true;

                                    }
                                } else if (C.PAUSE != true && C.isLevelCreated != true) tickUp=true;//zwiekszaj tick gdy nie ma pauzy

                            if (listEnemy.isEmpty() && listEnemyLaser.isEmpty() && C.isLevelCreated == true) {
                                C.LEVEL++;
                                resetLevel();
                            }
                        }

                        if (C.LEVEL == 50) {
                            int phase=1;
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
                                newBoss50ship(-100,-170,1,1,100,100);
                                newBoss50turret(100,-270,1,1,999,0,0,30,0);
                                newBoss50turret(500,-270,1,1,999,0,0,30,1);
                                newBoss50(300,-270,1,1,999,100,0,100,60);
                                enemyCreated++;
                                tick = 0;
                                if (enemyCreated == 1) C.isLevelCreated = true;
                            } else if (C.PAUSE != true && C.isLevelCreated != true) tick++;
                            else if (C.PAUSE != true && C.isLevelCreated){//po stworzeniu bossa -> MECHANIKA BOSSA5

                                if(listEnemy.size()<=2 && phase==1){//gdy zostanie sam boss zmien faze na 2
                                    phase=2;
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        enemy.setInvincible(false);
                                    }
                                }
                                //generowanie ruchu wież boss5
                                if (listEnemy != null && C.PAUSE != true && phase==1) {
                                    for (int i = 0; i < listEnemy.size(); i++) {
                                        Enemy enemy = listEnemy.get(i);
                                        if (enemy.getIsBoss() == 51) {
                                            for (int j = 0; j < listEnemy.size(); j++) {
                                                Enemy turret = listEnemy.get(j);
                                                if (turret.getIsBoss()==52){
                                                    //ruch dla lewego turreta
                                                    turret.setX(enemy.getX()+150);
                                                    turret.setY(enemy.getY()+40);
                                                }else if (turret.getIsBoss()==53){
                                                    //ruch prawego turreta
                                                    turret.setX(enemy.getX()+enemy.getW()-305);
                                                    turret.setY(enemy.getY()+40);
                                                }else if (turret.getIsBoss()==5){
                                                    //ruch bossa
                                                    turret.setX(enemy.getX()+enemy.getW()/2-90);
                                                    turret.setY(enemy.getY());
                                                }
                                            }
                                        }
                                    }
                                }
                                if (listEnemy != null && C.PAUSE != true && phase==2) {
                                    for (int i = 0; i < listEnemy.size(); i++) {
                                        Enemy enemy = listEnemy.get(i);
                                        if (enemy.getIsBoss() == 51) {
                                            enemy.setMovingType(52);
                                            if(enemy.getY()<-200) listEnemy.remove(enemy);
                                        }
                                    }
                                }
                                if(level_temp1%200==0 && phase==2) { //random tp
                                    level_temp1++;
                                    int upordown =0;//0 up 1 down 2 eq
                                    int leftorright=0;//0 left 1 right 2 eq
                                    int bossX=C.FRAME_WIDTH/2;
                                    int bossY=C.FRAME_HEIGHT/2;
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy!=null && enemy.getIsBoss()==5) {
                                            bossY=enemy.getY();
                                            bossX=enemy.getX();
                                            enemy.setBoss5randomlocation(player);
                                        }
                                    }

                                    for (int iw = 0; iw < listEnemyShot.size(); iw++) { //wypuszczenie pociskow
                                        EnemyShot enemyShot = listEnemyShot.get(iw);
                                        if(enemyShot.getMovingType()==8 || enemyShot.getMovingType()==9) {
                                            if(player.getX()+25<bossX) leftorright=0; else if(player.getX()+25>bossX+180) leftorright=1; else leftorright=2;
                                            if(player.getY()+25<bossY) upordown=0; else if(player.getY()+25>bossY+180) upordown=1; else upordown=2;
                                            if(upordown==0){
                                                if(leftorright==0) enemyShot.setMovingType(3);
                                                else if(leftorright==1) enemyShot.setMovingType(4);
                                                else enemyShot.setMovingType(7);
                                            }else if (upordown==1){
                                                if(leftorright==0) enemyShot.setMovingType(2);
                                                else if(leftorright==1) enemyShot.setMovingType(1);
                                                else enemyShot.setMovingType(0);
                                            }else {
                                                if(leftorright==0) enemyShot.setMovingType(6);
                                                else if(leftorright==1) enemyShot.setMovingType(5);
                                                else enemyShot.setMovingType(0);
                                            }
                                        }
                                    }
                                    for (int iw = 0; iw < listEnemy.size(); iw++) { //tworzenie okregu z pociksow
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==5) {
                                            Random random= new Random();
                                            int roll=random.nextInt(2);
                                            int moveType=8;
                                            if (roll == 0) moveType=8; else moveType =9;
                                            newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,0,2,1);
                                            newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,90,2,1);
                                            newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,180,2,1);
                                            newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,270,2,1);
                                            if (enemy.getHP()<31) {
                                                newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,45,2,1);
                                                newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,135,2,1);
                                                newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,225,2,1);
                                                newEnemyShot(enemy.getX()+90-20,enemy.getY()+90-20,40,40,moveType,enemy.getX()+90-20,enemy.getY()+90-20,315,2,1);
                                            }
                                        }
                                    }
                                }

                                if(level_temp1%100==0 && phase==1) { //generowanie strzałów działek
                                    level_temp1++;
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==52) {
                                            try {
                                                SoundManager.playEnemyShot();
                                            } catch (Exception e) {}
                                            newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 40, 40, 0);//dol
                                            newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.75), (int) (enemy.getY() + enemy.getH() * 0.75), 40, 40, 1);//prawo dol
                                            newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.25), (int) (enemy.getY() + enemy.getH() * 0.75), 40, 40, 2);//lewo dol
                                        }
                                        if(enemy.getIsBoss()==53) {
                                            try {
                                                SoundManager.playEnemyShot();
                                            } catch (Exception e) {}
                                            newEnemyShot(enemy.getX() + (enemy.getW()) / 2, enemy.getY() + enemy.getH(), 40, 40, 0);//dol
                                            newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.75), (int) (enemy.getY() + enemy.getH() * 0.75), 40, 40, 1);//prawo dol
                                            newEnemyShot((int) (enemy.getX() + enemy.getW() * 0.25), (int) (enemy.getY() + enemy.getH() * 0.75), 40, 40, 2);//lewo dol
                                        }
                                    }
                                }

                                if(level_temp1%201==0 && phase==1) { //generowanie strzałów statku
                                    level_temp1++;
                                    for (int iw = 0; iw < listEnemy.size(); iw++) {
                                        Enemy enemy = listEnemy.get(iw);
                                        if(enemy.getIsBoss()==51) {
                                            try {
                                                SoundManager.playEnemyShot();
                                            } catch (Exception e) {}
                                            newEnemyShot(enemy.getX() + (enemy.getW()) / 2-10, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                            newEnemyShot(enemy.getX() + (enemy.getW()) / 2+70, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                            newEnemyShot(enemy.getX() + (enemy.getW()) / 2-90, enemy.getY() + enemy.getH(), 20, 20, 0);//dol
                                        }
                                    }
                                }
//
                                if (level_temp1>1100){//zresetowanie sekwencji
                                    level_temp1=0;
                                }

                            }
                            if(C.isLevelCreated==true && !(listEnemy.isEmpty())){
                                level_temp1Up=true;
                                level_temp3Up=true;
                            }
                            if (listEnemy.isEmpty() && listMeteor.isEmpty() && C.isLevelCreated == true) {
                                isBossDefeated=true;
                                checkAchievements();
                                C.LEVEL++;
                                C.totalPoints+=2500;
                                C.isLevelCreated = false;
                                enemyCreated = 0;
                                isMusicPlayed = false;
                                resetLevel();
                            }
                            if(phase==2) {
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if ((enemy.getIsBoss() == 4) && enemy.getHP() % 20 == 0) {
                                        if (isBossAid) newAllyAid(-40, 20, 0);
                                        isBossAid = false;

                                    } else isBossAid = true;
                                }
                            } else {
                                int totalBosseshp=0;
                                for (int iw = 0; iw < listEnemy.size(); iw++) {
                                    Enemy enemy = listEnemy.get(iw);
                                    if (enemy.getIsBoss() == 52 || enemy.getIsBoss()==53) {
                                        totalBosseshp+=enemy.getHP();
                                    }
                                }
                                if(totalBosseshp% 20 == 0){
                                    if (isBossAid) newAllyAid(-40, 20, 0);
                                    isBossAid = false;
                                } else isBossAid = true;
                            }
                        }


                        if (C.LEVEL == C.LAST_LEVEL) { /// ostatni poziom koniec gry
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
                                updateLeaderboard();
                                sendData();
                                //okno dialogowe konca gry
                                int eenddialog = JOptionPane.showConfirmDialog
                                        (null, gameStrings.getString("Uzyskałeś") +" " + C.totalPoints+" "
                                                        + gameStrings.getString("punktów") +"\n"+
                                                        gameStrings.getString("Gratulacje!")+" "
                                                        +gameStrings.getString("Czy chcesz zagrać ponownie?"),
                                                gameStrings.getString("Koniec gry"), 0);
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
                        ////////////////////////generowane wrogow w menu i usuwanie gdy wyjda poza okno//////////////////////////////
                        menudelay--;
                        if(menudelay<0){
                            menudelay=1000;
                            Random r = new Random();
                            if(r.nextInt(5)==0)newEnemyMenu();
                        }
                        if (listEnemyMenu != null) {
                            for (int iw = 0; iw < listEnemyMenu.size(); iw++) {
                                Enemy enemy = listEnemyMenu.get(iw);
                                if (enemy.getX() > C.FRAME_WIDTH+60) {
                                    listEnemyMenu.remove(enemy);
                                }
                            }
                        }
///////////////////////////////////////////////////////////////////////////////////obsługa kursorów menu

                        if (menu != null && C.cursorPosition == 0 && C.cursorPositionColumn==0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(290);
                        }
                        if (menu != null && C.cursorPosition == 1 && C.cursorPositionColumn==0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(390);
                        }
                        if (menu != null && C.cursorPosition == 2 && C.cursorPositionColumn==0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(490);
                        }
                        if (menu != null && C.cursorPosition == 3 && C.cursorPositionColumn==0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(590);
                        }
                        if (menu != null && C.cursorPosition == 4 && C.cursorPositionColumn==0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(690);
                        }
                        if (menu != null && C.cursorPositionColumn==1 && C.cursorPosition==0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2);
                            menuCursor.setY(390);
                        }
                        if (menu != null && C.cursorPositionColumn==1 && C.cursorPosition==1) {
                            menuCursor.setX(C.FRAME_WIDTH / 2);
                            menuCursor.setY(590);
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
                                menuCursor.setY(485);
                            }
                            if (menu != null && C.cursorSettingsPosition == 4) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(550);
                            }
                            if (menu != null && C.cursorSettingsPosition == 5) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(620);
                            }
                            if (menu != null && C.cursorSettingsPosition == 6) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 200);
                                menuCursor.setY(705);
                            }


                        
                    }//GAMESTATE 2 menusettings
                            if(C.GAMESTATE==5){
                        if (menuSkinSelection != null && C.cursorBeforeGamePosition == 5) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 170);
                            menuCursor.setY(690);
                        }
                    }//GAMESTATE 5 skin selection
                    if(C.GAMESTATE==6){
                        if (menuAchievements != null && C.cursorAchievementsRow ==4) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 170);
                            menuCursor.setY(690);
                        }else {
                            menuCursor.setX(-300);
                        }
                    }//GAMESTATE 6 osiagniecia
                    if(C.GAMESTATE==7){
                        if (menuLeaderboard != null) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 170);
                            menuCursor.setY(690);
                        }else {
                            menuCursor.setX(-300);
                        }
                    }//GAMESTATE 7 leaderboard

                    if(C.GAMESTATE==22){
                        if(C.hasPlayerName) {
                            if (playerNameSelection != null && C.cursorPlayerNamePosition == 0) {
                                menuCursor.setX(50);
                                menuCursor.setY(300);
                            }
                            if (playerNameSelection != null && C.cursorPlayerNamePosition == 1) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(605);
                            }
                            if (playerNameSelection != null && C.cursorPlayerNamePosition == 2) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(705);
                            }
                        }else {
                            if (playerNameSelection != null) {
                                menuCursor.setX(50);
                                menuCursor.setY(300);
                            }
                        }
                    }//GAMESTATE 22 playersettings

                    if(C.GAMESTATE==21){
                            if (menuPlayerSettings != null && C.cursorPlayerSettingsPosition == 0) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(100);
                            }
                            if (menuPlayerSettings != null && C.cursorPlayerSettingsPosition == 1) {
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(250);
                            }
                            if (menuPlayerSettings != null && C.cursorPlayerSettingsPosition == 2) {//ex
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(400);
                            }
                            if (menuPlayerSettings != null && C.cursorPlayerSettingsPosition == 3) {//ex
                                menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                                menuCursor.setY(705);
                            }

                    }//GAMESTATE 21 playersettings

                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (delta >= 1) {
                        checkAchievements();
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

                         if(C.isFPSon) FPSlabel.setText("FPS: " + frames + " ");
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
            if (listBonusAllyAid != null)            //rysowanie punktow pomocy sojusznika
                for (int i = 0; i < listBonusAllyAid.size(); i++) {
                    listBonusAllyAid.get(i).draw(g2D);
                }
            if (listAllyAid != null)            //rysowanie statkowsojusznika
                for (int i = 0; i < listAllyAid.size(); i++) {
                    listAllyAid.get(i).draw(g2D);
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

            if(C.LEVEL==C.LAST_LEVEL){
                g.setColor(Color.white);
                g.setFont(customFont.deriveFont(40f));
                g.drawString(gameStrings.getString("Gratulacje!")+" "+C.PLAYER_NAME+"!", C.FRAME_WIDTH/2-300, 200);
                g.drawString(gameStrings.getString("Ukończyłeś grę!"), C.FRAME_WIDTH/2-300, 250);
                g.drawString(gameStrings.getString("Wynik końcowy:")+C.totalPoints, C.FRAME_WIDTH/2 - 170, C.FRAME_HEIGHT - 200);
            }
            if(C.LEVEL==0){
                g.setColor(Color.white);
                if(level_temp1==0) {
                    g.drawString(gameStrings.getString("lvl0_użyj"), 400, C.FRAME_HEIGHT - 400);
                    g.drawString(gameStrings.getString("lvl0_trafiaj"), 200, 200);
                }
                if(level_temp1>0) {
                    g.drawString(gameStrings.getString("lvl0_podczas"), 400, C.FRAME_HEIGHT - 400);
                    g.drawString(gameStrings.getString("lvl0_po"), 200, 200);
                    //g.drawString("Użyj 'R', aby wystrzelić rakietę rażącą wszystkich przeciwników.", 240, C.FRAME_HEIGHT - 80); //możliwe w przyszłości
                    //g.drawString("Nie otzymasz za to bonusów. Zbierz 50 monet by otrzymać rakietę.", 240, C.FRAME_HEIGHT - 50); //możliwe w przyszłości
                }
                g.drawString(gameStrings.getString("lvl0_gra"),20,C.FRAME_HEIGHT-100);
            }
    //////////////////////UI i elementy//////////////////////////////////////////////////////////////
            g.setFont(customFont.deriveFont(20f));
            if(C.GODMODE) g.drawString("GODMODE", 0, 200);
            lifeUI.draw(g2D);//rysowanie obrazka w UI
            playerShotUI.draw(g2D);//rysowanie obrazka w UI
            if(C.isFirerateUpgrade) firerateUpgradeUI.draw(g2D);

            //IMPLEMENTACJA PASKA NAŁADOWANIA TARCZY
            g.setColor(Color.GRAY);
            g2D.fillRect(-10, C.FRAME_HEIGHT-60, C.FRAME_WIDTH, 40); // pasek tarczy do wypełnienia
            if(C.shieldActivated) {
                int barWidth = C.shieldCooldown * 2; // Szerokość paska
                int barX = (C.FRAME_WIDTH - barWidth) / 2; //  X paska
                g.setColor(Color.ORANGE);
                g2D.fillRect(barX, C.FRAME_HEIGHT-60, barWidth, 40); // Wyśrodkowany pasek
                g.setColor(Color.BLACK);
                g2D.fillRect(-10, C.FRAME_HEIGHT-64, C.FRAME_WIDTH+40, 4); // ramka od góry
                //// STRING NA PASKU
                String shieldString =  gameStrings.getString("TarczaON") ;
                int textWidth = g2D.getFontMetrics().stringWidth(shieldString); // Szerokość tekstu
                int textX = (C.FRAME_WIDTH - textWidth) / 2; //  x tekstu dla wyśrodkowania
                g.setColor(Color.white);
                g.setFont(customFont.deriveFont(20f));
                g2D.drawString(shieldString, textX, C.FRAME_HEIGHT-45);
            }else {
                String shieldString =  gameStrings.getString("TarczaOFF") ;
                int textWidth = g2D.getFontMetrics().stringWidth(shieldString); // Szerokość tekstu
                int textX = (C.FRAME_WIDTH - textWidth) / 2; //  x tekstu dla wyśrodkowania
                g.setColor(Color.white);
                g.setFont(customFont.deriveFont(20f));
                g2D.drawString(shieldString, textX, C.FRAME_HEIGHT-45);
            }

            g.setColor(Color.white);

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
        if(C.GAMESTATE==99){
            languageSelection = new LanguageSelection();
            languageSelection.draw(g2D);
        }//GAMESTATE 99 - wybor jezyka

        if(C.GAMESTATE==1){
            menu = new Menu();
            if (listEnemyMenu != null)
                for (int i = 0; i < listEnemyMenu.size(); i++) {
                    listEnemyMenu.get(i).draw(g2D);
                }
            menu.draw(g2D);
            menuCursor.draw(g2D);
            g.setFont(customFont.deriveFont(30f));
            g.drawString("Witaj ponownie "+C.PLAYER_NAME+" !",50,20);
            g.setFont(customFont.deriveFont(20f));
        }//GAMESTATE 1 -menu

        if(C.GAMESTATE==2){
            menuSettings = new MenuSettings();
            menuSettings.draw(g2D);
            menuCursor.draw(g2D);
        }//GAMESTATE 2 -menuOpcje
        if(C.GAMESTATE==21){
            menuPlayerSettings= new MenuPlayerSettings();
            menuPlayerSettings.draw(g2D);
            menuCursor.draw(g2D);
        }//GAMESTATE 21 -menu playersettings
        if(C.GAMESTATE==22){
            playerNameSelection = new PlayerNameSelection();
            playerNameSelection.draw(g2D);
            menuCursor.draw(g2D);
        }//GAMESTATE 22 -wpisywanie nazwy gracza

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
        if (C.GAMESTATE == 6) {//GAMESTATE 6- menu osiagniec
            menuAchievements = new MenuAchievements();
            menuAchievements.draw(g2D);
            if(C.cursorBeforeGamePosition==5) menuCursor.draw(g2D);
        } //GAMESTATE 6 - osiagniec
        if (C.GAMESTATE == 7) {//GAMESTATE 7- menu leaderboards
            menuLeaderboard = new MenuLeaderboard();
            menuLeaderboard.draw(g2D);
            menuCursor.draw(g2D);
        } //GAMESTATE 7 - leaderboadrs
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
    public void newDrop(int x,int y, int weaponUpgradeChance, int weaponFirerateChance, int bonusShieldChance, int bonusLifeChance, int bonusAllyAidChance){//funkcja losująca drop z przeciwnika
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1; // losuj liczbe od 1 do 100
        if (randomNumber <= weaponUpgradeChance) {//ulepszenie broni
            newWeaponUpgrade(x,y);
        } else if (randomNumber <= weaponUpgradeChance+ weaponFirerateChance) {//przyspieszenie strzelania
            newFirerateUpgrade(x,y);
        } else if (randomNumber <= weaponUpgradeChance + weaponFirerateChance + bonusShieldChance) {//dodatkowa tarcza
            newBonusShield(x,y);
        }else if (randomNumber <= weaponUpgradeChance + weaponFirerateChance + bonusShieldChance + bonusLifeChance) {//dodatkowe życie
            newLife(x,y);
        }else if (randomNumber <= weaponUpgradeChance + weaponFirerateChance + bonusShieldChance + bonusLifeChance + bonusAllyAidChance) {//pomoc sojusznika
            newBonusAllyAid(x,y);
        } else {
            newPoints(x,y,50,50);
        }
    }
    public void newEnemyMenu(){//utworzenie obiektu wroga w menu
        Random random=new Random();
        Enemy enemy = new Enemy(-60,random.nextInt(C.FRAME_HEIGHT-100),this);
        enemy.setMovingType(1000);
        enemy.start();
        listEnemyMenu.add(enemy);
    }
    public void deleteEnemyMenu(){//usuniecie wszystkich wrogow w menu
        if (listEnemyMenu != null) {
            for (int iw = 0; iw < listEnemyMenu.size(); iw++) {
                Enemy enemy = listEnemyMenu.get(iw);
                listEnemyMenu.remove(enemy);
            }
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
                if (enemy.getIsBoss()!=0 && enemy.getIsBoss()!=51 && enemy.getIsBoss()!=52 && enemy.getIsBoss()!=53) {
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
    public void newBoss40(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        enemy.setIsBoss(4);
        enemy.setMovingType(40);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(180);
        enemy.setH(180);
        enemy.setInvincible(true);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void newBoss50(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        enemy.setIsBoss(5);
        enemy.setMovingType(999);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(180);
        enemy.setH(180);
        enemy.setInvincible(true);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void newBoss50ship(int x,int y,int velX,int velY,int radius,int hp){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(0-100);
        enemy.setCircleCenterY(0-100);
        enemy.setHP(hp);
        enemy.setIsBoss(51);
        enemy.setMovingType(51);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(1000);
        enemy.setH(220);
        enemy.setInvincible(true);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void newSpikeBall(int x, int y, int velX, int velY, int movingType, int centerX, int centerY, int radius, int hp, int orientation){
        Enemy enemy = new Enemy(x, y,this);
        enemy.setRadius(radius);
        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        if(orientation==0)
            enemy.setIsBoss(41);
        else enemy.setIsBoss(42);
        enemy.setMovingType(40);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setW(150);
        enemy.setH(150);
        enemy.start();
        listEnemy.add(enemy);
    }
    public void newBoss50turret(int x, int y, int velX, int velY, int movingType, int centerX, int centerY, int hp, int orientation){
        Enemy enemy = new Enemy(x, y,this);

        enemy.setCircleCenterX(centerX);
        enemy.setCircleCenterY(centerY);
        enemy.setHP(hp);
        if(orientation==0)
            enemy.setIsBoss(52);
        else enemy.setIsBoss(53);
        enemy.setMovingType(movingType);
        enemy.setVelX(velX);
        enemy.setVelY(velY);
        enemy.setRadius(100);
        enemy.setW(150);
        enemy.setH(200);
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
    public void newBonusAllyAid(int x,int y){//utworzenie obiektu bonusu pomocy sojusznika
        BonusAllyAid bonusAllyAid = new BonusAllyAid(x,y,this);
        bonusAllyAid.start();//start watku
        listBonusAllyAid.add(bonusAllyAid);//dodanie do listy obiektow
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
    public void newEnemyShot(int x,int y, int w, int h, int movingType,int circleCenterX,int circleCenterY,int angle,int radius,int imgType){//utworzenie obiektu wrogiego strzału
        EnemyShot enemyShot = new EnemyShot(x,y,this);
        enemyShot.setW(w);
        enemyShot.setH(h);
        enemyShot.setAngle(angle);
        enemyShot.setRadius(radius);
        enemyShot.setCircleCenterX(circleCenterX);
        enemyShot.setCircleCenterY(circleCenterY);
        enemyShot.setMovingType(movingType);
        enemyShot.setImgType(imgType);
        enemyShot.start();//start watku
        listEnemyShot.add(enemyShot);//dodanie do listy obiektow enemyShot
    }
    public void newPlayerShot(int x, int y){//utworzenie obiektu strzału gracza
        PlayerShot playerShot = new PlayerShot(x,y,this);
        playerShot.start();//start watku
        listPlayerShot.add(playerShot);//dodanie do listy obiektow PlayerShot

    }

    public void newAllyAid(int x, int y, int facingDirection){//utworzenie obiektu strzału gracza
        AllyAid allyAid = new AllyAid(x,y,facingDirection,this);
        allyAid.start();//start watku
        listAllyAid.add(allyAid);//dodanie do listy obiektow PlayerShot

    }
    public void resetVariables(){//przywrócenie zmiennych do stanu pierwotnego
        LEFT_PRESSED=false;RIGHT_PRESSED=false;UP_PRESSED=false;DOWN_PRESSED=false;SHOT_PRESSED=false;
        isShotOnCooldown=false;shotCooldown=60;C.shieldCooldown=C.SHIELD_COOLDOWN_TIME;
        C.totalPoints=0;C.playerLives=3;C.LEVEL=0;C.weaponUpgrade=1;C.shieldActivated=false;C.isFirerateUpgrade=false;
        player.setX(C.FRAME_WIDTH / 2 - 25); player.setY(C.FRAME_HEIGHT - 150);
        removeObjects();
        resetLevel();
        C.PAUSE= false;
        livesEarned=0;weaponUpgradeEarned=0;firerateUpgradeEarned=0;shieldEarned=0;allyAidEarned=0;pointBoxEarned=0;shotsNumber=0;
    }
    public void resetLevel(){//przywrócenie zmiennych dot opóźnień w poziomach do stanu pierwotnego
        tick=0; tickUp=false; level_delay=0;
        level_temp3=0; level_temp3Up=false;
        level_temp2=0; level_temp2Up=false;
        level_temp1=0; level_temp1Up=false;
        enemyCreated=0; C.isLevelCreated=false;isMusicPlayed=false;
        initialBossHP=0; isBossHpTaken=false;isPlayerTookHit=false;isBossDefeated=false;
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
            labelTotalPoints.setText(gameStrings.getString("Punkty:")+" "+C.totalPoints);
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
                if(C.LEVEL==10 || C.LEVEL==20 || C.LEVEL==30 || C.LEVEL==40 || C.LEVEL==50) isPlayerTookHit=true;
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
    public static void updateSettings() {
        try {
            // Zapisanie ustawień do pliku
            File configFile = new File("config.dat");
            String configData = String.format("%d\n%d\n%b\n%d\n%d\n%b\n", C.musicVolume, C.soundVolume, C.isMuted, C.playerSkin, C.LANGUAGE, C.isFPSon);
            byte[] encryptedConfig = encrypt(configData, C.SECRETKEY);
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                fos.write(encryptedConfig);
            }
        } catch (Exception e) {
            // Obsługa błędów zapisu ustawień
            e.printStackTrace();
        }
    }

    public static void updateHighscore() {
        try {
            C.highscorePoints = C.totalPoints;
            C.highscoreLevel = C.LEVEL;
            // Zapisanie postępów i nicku do pliku
            File highscoreFile = new File("data.dat");
            String highscoreData = String.format("%d\n%d\n%s\n", C.highscorePoints, C.highscoreLevel, C.PLAYER_NAME);
            byte[] encryptedHighscore = encrypt(highscoreData, C.SECRETKEY);
            try (FileOutputStream fos = new FileOutputStream(highscoreFile)) {
                fos.write(encryptedHighscore);
            }
        }catch (Exception e){
            System.out.println("Error updating highscore: "+e);
        }

    }

    public static void updateLeaderboard() {
        try {
            // Wczytanie obecnych wyników
            File scoreFile = new File("data2.dat");
            if (!scoreFile.exists()) {
                createDefaultData2(scoreFile);
            }
            byte[] scoreBytes = readEncryptedFile(scoreFile);
            String decryptedScore = decrypt(scoreBytes, C.SECRETKEY);
            String[] scoreLines = decryptedScore.split("\n");
            List<Scores> scoreEntries = new ArrayList<>();

            for (String line : scoreLines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String playerName = parts[0];
                    int totalPoints = Integer.parseInt(parts[1]);
                    String playTime = parts[2];
                    scoreEntries.add(new Scores(playerName, totalPoints, playTime));
                }
            }

            // Ustalenie czasu gry przez porównanie daty startowej i końcowej
            Date endDate = new Date();
            long diffInMilliseconds = endDate.getTime() - startDate.getTime();
            long diffInSeconds = diffInMilliseconds / 1000;
            C.currentPlaytime = diffInSeconds;
            long currentPlaytime = C.currentPlaytime;
            long hours = currentPlaytime / 3600;
            long minutes = (currentPlaytime % 3600) / 60;
            long secs = currentPlaytime % 60;
            String playtimeFormatted="";
            if(hours>0) playtimeFormatted = String.format("%02d:%02d:%02d", hours, minutes, secs);
            else playtimeFormatted = String.format("%02d:%02d", minutes, secs);

            // Sprawdzenie, czy nowy wynik kwalifikuje się do top 10
            Scores newEntry = new Scores(C.PLAYER_NAME, C.totalPoints, playtimeFormatted);
            scoreEntries.add(newEntry);
            scoreEntries.sort((a, b) -> Integer.compare(b.getTotalPoints(), a.getTotalPoints())); // Sortowanie malejąco wg punktów

            // Sprawdzenie, czy lista przekracza 10 elementów i zachowanie tylko top 10
            if (scoreEntries.size() > 10) {
                scoreEntries = new ArrayList<>(scoreEntries.subList(0, 10));
            }

            // Aktualizacja listy wyników
            C.scoresList = scoreEntries;

            // Zapisanie listy wyników do pliku
            StringBuilder updatedScoreData = new StringBuilder();
            for (Scores entry : scoreEntries) {
                updatedScoreData.append(String.format("%s,%d,%s\n", entry.getPlayerName(), entry.getTotalPoints(), entry.getPlayTime()));
            }
            byte[] encryptedScore = encrypt(updatedScoreData.toString(), C.SECRETKEY);
            try (FileOutputStream fos = new FileOutputStream(scoreFile)) {
                fos.write(encryptedScore);
            }
        } catch (Exception e) {
            System.out.println("Error updating leaderboard: " + e);
        }
    }


    public static void updateAchievements() {
        //załadowanie osiagniec z wczytanej tablicy
        if (C.achievements[0]==1) C.isAchivement0done=true;
        if (C.achievements[1]==1) C.isAchivement1done=true;
        if (C.achievements[2]==1) C.isAchivement2done=true;
        if (C.achievements[3]==1) C.isAchivement3done=true;
        if (C.achievements[4]==1) C.isAchivement4done=true;
        if (C.achievements[5]==1) C.isAchivement5done=true;
        try {
            String achievementsString = "";
            for (int i=0;i<C.achievements.length;i++){
                achievementsString += String.valueOf(C.achievements[i]);
            }
            // Zapisanie postępów i nicku do pliku
            File achievementsFile = new File("data1.dat");
            String achievementsData = String.format("%s\n%s\n", C.PLAYER_NAME,achievementsString);
            byte[] encryptedHighscore = encrypt(achievementsData, C.SECRETKEY);
            try (FileOutputStream fos = new FileOutputStream(achievementsFile)) {
                fos.write(encryptedHighscore);
            }
        } catch (Exception e) {
            // Obsługa błędów zapisu najlepszego wyniku
            e.printStackTrace();
        }
    }
    public static void checkAchievements() {
        //sprawdzenie warunków osiągnięć
        if(!C.isAchivement0done && C.LEVEL==C.LAST_LEVEL) {//ach0 end game
            System.out.println("Achievement 0: End game");
            C.achievements[0]=1;
            C.isAchivement0done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement1done && C.LEVEL>10) {//ach1 beat 10 lvl
            System.out.println("Achievement 1: Beat 10 lvl");
            C.achievements[1]=1;
            C.isAchivement1done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement2done && C.LEVEL>20) {//ach2 beat 20 lvl
            System.out.println("Achievement 2: Beat 20 lvl");
            C.achievements[2]=1;
            C.isAchivement2done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement3done && C.LEVEL>30) {//ach3 beat 30 lvl
            System.out.println("Achievement 3: Beat 30 lvl");
            C.achievements[3]=1;
            C.isAchivement3done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement4done && C.LEVEL>40) {//ach4 beat 40 lvl
            System.out.println("Achievement 4: Beat 40 lvl");
            C.achievements[4]=1;
            C.isAchivement4done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement5done && C.LEVEL>50) {//ach5 beat 50 lvl
            System.out.println("Achievement 5: Beat 50 lvl");
            C.achievements[5]=1;
            C.isAchivement5done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement6done && C.LEVEL==10 && !isPlayerTookHit && isBossDefeated) {//ach6
            System.out.println("Achievement 6: Beat boss 1 hitless");
            C.achievements[6]=1;
            C.isAchivement6done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement7done && C.LEVEL==20 && !isPlayerTookHit && isBossDefeated) {//ach7
            System.out.println("Achievement 7: Beat boss 2 hitless");
            C.achievements[7]=1;
            C.isAchivement7done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement8done && C.LEVEL==30 && !isPlayerTookHit && isBossDefeated) {//ach8
            System.out.println("Achievement 8: Beat boss 3 hitless");
            C.achievements[8]=1;
            C.isAchivement8done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement9done && C.LEVEL==40 && !isPlayerTookHit && isBossDefeated) {//ach9
            System.out.println("Achievement 9: Beat boss 4 hitless");
            C.achievements[9]=1;
            C.isAchivement9done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement10done && C.LEVEL==50 && !isPlayerTookHit && isBossDefeated) {//ach10
            System.out.println("Achievement 10: Beat boss 5 hitless");
            C.achievements[10]=1;
            C.isAchivement10done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement11done && livesEarned>=50) {//ach11
            System.out.println("Achievement 11: Earn 50 lives");
            C.achievements[11]=1;
            C.isAchivement11done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement12done && firerateUpgradeEarned>=50) {//ach12
            System.out.println("Achievement 12: Earn 50 firerate updgrades");
            C.achievements[12]=1;
            C.isAchivement12done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement13done && shieldEarned>=50) {//ach13
            System.out.println("Achievement 13: Earn 50 shields");
            C.achievements[13]=1;
            C.isAchivement13done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement14done && allyAidEarned>=50) {//ach14
            System.out.println("Achievement 14: Earn 50 ally aid bonuses");
            C.achievements[14]=1;
            C.isAchivement14done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement15done && weaponUpgradeEarned>=50) {//ach15
            System.out.println("Achievement 15: Earn 50 weapon upgrades");
            C.achievements[15]=1;
            C.isAchivement15done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement16done && pointBoxEarned>=50) {//ach16
            System.out.println("Achievement 16: Earn 50 point boxes");
            C.achievements[16]=1;
            C.isAchivement16done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement17done && C.totalPoints>=10000) {//ach17
            System.out.println("Achievement 17: Earn 10k points");
            C.achievements[17]=1;
            C.isAchivement17done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement18done && C.totalPoints>=20000) {//ach18
            System.out.println("Achievement 18: Earn 20k points");
            C.achievements[18]=1;
            C.isAchivement18done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement19done && C.totalPoints>=30000) {//ach19
            System.out.println("Achievement 19: Earn 30k points");
            C.achievements[19]=1;
            C.isAchivement19done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement20done && C.totalPoints>=50000) {//ach20
            System.out.println("Achievement 20: Earn 50k points");
            C.achievements[20]=1;
            C.isAchivement20done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement21done && shotsNumber>=7777) {//ach21
            System.out.println("Achievement 21: Shot 7777 times");
            C.achievements[21]=1;
            C.isAchivement21done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement22done && C.gamesPlayed>=5) {//ach22
            System.out.println("Achievement 22: Play 5 times in one session");
            C.achievements[22]=1;
            C.isAchivement22done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }
        if(!C.isAchivement23done && C.LEVEL==0 && C.playerLives==0) {//ach23
            System.out.println("Achievement 23: End game in tutorial level.");
            C.achievements[23]=1;
            C.isAchivement23done=true;
            try{
                SoundManager.playAchievement();
            }catch (Exception e){}
            updateAchievements();
        }

    }
    private static void resetAchievements() {
            C.ARCH_PLAYER_NAME=C.PLAYER_NAME;
            //otwarcie pliku
            File file = new File("data1.dat");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                // Domyślne wyniki
                String defaultData = C.PLAYER_NAME+"\n000000000000000000000000000000\n";
                byte[] encryptedData = encrypt(defaultData, C.SECRETKEY);
                fos.write(encryptedData);
                C.isAchivement0done=false;
                C.isAchivement1done=false;
                C.isAchivement2done=false;
                C.isAchivement3done=false;
                C.isAchivement4done=false;
                C.isAchivement5done=false;
                C.isAchivement6done=false;
                C.isAchivement7done=false;
                C.isAchivement8done=false;
                C.isAchivement9done=false;
                C.isAchivement10done=false;
                C.isAchivement11done=false;
                C.isAchivement12done=false;
                C.isAchivement13done=false;
                C.isAchivement14done=false;
                C.isAchivement15done=false;
                C.isAchivement16done=false;
                C.isAchivement17done=false;
                C.isAchivement18done=false;
                C.isAchivement19done=false;
                C.isAchivement20done=false;
                C.isAchivement21done=false;
                C.isAchivement22done=false;
                C.isAchivement23done=false;
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
    // Metoda do odczytu zaszyfrowanego pliku
    private static byte[] readEncryptedFile(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try (FileInputStream fis = new FileInputStream(file)) {
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        return baos.toByteArray();
    }

    // Metoda do szyfrowania
    private static byte[] encrypt(String input, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    // Metoda do deszyfrowania
    private static String decrypt(byte[] encryptedData, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }
    private static void createDefaultData(File highscoreFile) {
        try (FileOutputStream fos = new FileOutputStream(highscoreFile)) {
            // Domyślne wyniki
            String defaultHighscore = "0\n0\n\n";
            byte[] encryptedHighscore = encrypt(defaultHighscore, C.SECRETKEY);
            fos.write(encryptedHighscore);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void resetData(){
        C.highscorePoints=0;
        C.highscoreLevel=0;
        //otwarcie pliku highscore
        File file = new File("data.dat");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Domyślne wyniki
            String defaultData = "0\n0\n"+C.PLAYER_NAME+"\n";
            byte[] encryptedData = encrypt(defaultData, C.SECRETKEY);
            fos.write(encryptedData);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        createDefaultData2(new File("data2.dat"));
        C.scoresList.clear();
        loadData2();
    }

    public void sendData(){
        new Thread(() -> {
            try {
                //ustalenie czasu gry przez porównanie daty startowej i końcowej
                Date endDate= new Date();
                long diffInMilliseconds = endDate.getTime() - startDate.getTime();
                long diffInSeconds = diffInMilliseconds / 1000;
                C.playtime= diffInSeconds;

                DataSender dataSender = new DataSender();
                JSONObject jsonObject = new JSONObject();

                // Dodanie nowych danych
                jsonObject.put("Data_receive_date", getCurrentDate());
                jsonObject.put("Data_receive_time", getCurrentTime());
                jsonObject.put("Operating_system", System.getProperty("os.name")+" "
                        +System.getProperty("os.version")+" "+System.getProperty("os.arch"));
                jsonObject.put("Java_runtime_version", System.getProperty("java.runtime.version"));
                jsonObject.put("Game_version",C.VERSION);
                jsonObject.put("Playtime_seconds", C.playtime);
                jsonObject.put("End_level",C.LEVEL);
                jsonObject.put("Lifes_left",C.playerLives);
                jsonObject.put("Total_points",C.totalPoints);
                jsonObject.put("Chosen_skin_ID",C.playerSkin);
                jsonObject.put("life_earned",livesEarned);
                jsonObject.put("weaponUpgrade_earned",weaponUpgradeEarned);
                jsonObject.put("firerateUpgrade_earned",firerateUpgradeEarned);
                jsonObject.put("shield_earned",shieldEarned);
                jsonObject.put("pointBox_earned",pointBoxEarned);
                jsonObject.put("game_in_session",C.gamesPlayed);

                dataSender.sendData(jsonObject);

            } catch (Exception e) {
                // Obsługa błędów podczas wysyłania danych
                System.out.println("Error sending data");
            }
        }).start();

    }
    // Metoda do pobierania aktualnej daty
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    // Metoda do pobierania aktualnej godziny
    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        return timeFormat.format(currentTime);
    }

    //keylistener do sterowania
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //////////////////////////////////////////////////////////////////
        if (e.getKeyCode()==32 && C.GAMESTATE==100){//spacja
            if(C.LANGUAGE!=0 && C.LANGUAGE!=1) {
                C.LANGUAGE=0;
                C.GAMESTATE=99;
            }
            else if(C.PLAYER_NAME ==""){C.hasPlayerName=false; C.GAMESTATE=22;C.canEnterName=true;}else {C.hasPlayerName=true;C.GAMESTATE=1;}//skip intro
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
                    (null, gameStrings.getString("Czy chcesz powrócić do menu?"),
                            gameStrings.getString("Pauza"), 0);

            //powrót do menu po wybraniu tak
            if (enddialog == 0) {
                updateLeaderboard();
                sendData();
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
        if (e.getKeyCode()==80 && C.GAMESTATE==0) {//p przycisk pauza
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
        if (e.getKeyCode()==71 && C.DEVELOPMENT) {//g przycisk wł/wył godmode
            if (C.GODMODE) {
                C.GODMODE = false;
            } else {
                C.GODMODE=true;
            }
        }
        if (e.getKeyCode()==84 && C.DEVELOPMENT) {//T przycisk do testów testowania debug
            //newMeteor(100,0,50,0);
            //newMeteor(300,0,150,1);
            //newMeteor(500,-50,350,2);
            newBonusShield(250,0);
             //newEnemyLaser(130, 0, 1, 1, 1,0,0,0,0,2);
            //newFirerateUpgrade(100,-10);
            //newWeaponUpgrade(100,-10);
            //newBonusAllyAid(100,-10);
            //newAllyAid(C.FRAME_WIDTH+50,60,1);
             //-10hp kazdemu wrogowi
//            C.isAchivement0done=true;
//            C.isAchivement1done=true;
//            C.isAchivement2done=true;
//            C.isAchivement3done=true;
//            C.isAchivement4done=true;
//            C.isAchivement5done=true;
            C.totalPoints+=5000;
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
            //obsługa dla menu
            if (C.GAMESTATE==1){
                if(C.cursorPositionColumn==1) {
                    C.cursorPositionColumn=0;
                    if(C.cursorPosition==0) C.cursorPosition=1;
                    else if(C.cursorPosition==1) C.cursorPosition=3;
                }
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
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
                if(C.cursorSettingsPosition==3){
                    if(C.LANGUAGE==0) {
                        C.LANGUAGE=1;
                    }else C.LANGUAGE=0;
                    updateSettings();
                    try {
                        SoundManager.playPlayerShot();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(C.cursorSettingsPosition==4){
                    if(C.isFPSon) {
                        C.isFPSon=false;
                        FPSlabel.setText("");
                        updateSettings();
                        try {
                            SoundManager.playPlayerShot();
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
            //obsługa dla menu osiagniec
            if(C.GAMESTATE==6 && C.cursorAchievementsColumn!=6 && C.cursorAchievementsRow!=4){
                if (C.cursorAchievementsColumn==0) C.cursorAchievementsColumn=5;
                else C.cursorAchievementsColumn--;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (e.getKeyCode()==39){//s w prawo
            RIGHT_PRESSED=true;
            //obsługa dla menu
            if (C.GAMESTATE==1){
                if(C.cursorPositionColumn==0) {
                    C.cursorPositionColumn=1;
                    if(C.cursorPosition<=2) C.cursorPosition=0;
                    else C.cursorPosition=1;
                }
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
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
                if(C.cursorSettingsPosition==3){
                    if(C.LANGUAGE==0) {
                        C.LANGUAGE=1;
                    }else C.LANGUAGE=0;
                    updateSettings();
                    try {
                        SoundManager.playPlayerShot();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(C.cursorSettingsPosition==4){
                    if(!C.isFPSon) {
                        C.isFPSon=true;
                        updateSettings();
                        try {
                            SoundManager.playPlayerShot();
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
            //obsługa dla menu wyboru statku
            if(C.GAMESTATE==6 && C.cursorAchievementsColumn!=6 && C.cursorAchievementsRow!=4){
                if (C.cursorAchievementsColumn==5) C.cursorAchievementsColumn=0;
                else C.cursorAchievementsColumn++;
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
            if(C.GAMESTATE==1 && C.cursorPosition<4 && C.cursorPositionColumn!=1){
                C.cursorPosition++;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (C.GAMESTATE==1 && C.cursorPosition!=1 && C.cursorPositionColumn==1) {
                C.cursorPosition=1;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(C.GAMESTATE==2 && C.cursorSettingsPosition<6){
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
            //obsługa dla menu osiagniec
            if(C.GAMESTATE==6){
                if (C.cursorAchievementsRow<4) C.cursorAchievementsRow++;
                //else C.cursorBeforeGamePosition=0;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //obsługa dla menu player settings
            if(C.GAMESTATE==21 && C.cursorPlayerSettingsPosition<3){
                C.cursorPlayerSettingsPosition++;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //obsługa dla menu wpisywania nazwy gracza
            if(C.GAMESTATE==22 && C.cursorPlayerNamePosition<2){
                C.cursorPlayerNamePosition++;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //obsługa dla menu wyboru jezyka
            if(C.GAMESTATE==99){
                if (C.cursorLanguagePosition!=1) C.cursorLanguagePosition=1;
                else C.cursorLanguagePosition=0;
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
            if(C.GAMESTATE==1 && C.cursorPosition>0 ){
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
            //obsługa dla menu wyboru statku
            if(C.GAMESTATE==6){
                if (C.cursorAchievementsRow>0) C.cursorAchievementsRow--;
                //else C.cursorAchievementsRow=5;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //obsługa dla menu player settings
            if(C.GAMESTATE==21 && C.cursorPlayerSettingsPosition>0){
                C.cursorPlayerSettingsPosition--;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //dla wpisywania znawy gracza
            if(C.GAMESTATE==22 && C.cursorPlayerNamePosition>0){
                C.cursorPlayerNamePosition--;
                try {
                    SoundManager.playPlayerShot();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //obsługa dla menu wyboru jezyka
            if(C.GAMESTATE==99){
                if (C.cursorLanguagePosition==1) C.cursorLanguagePosition=0;
                else C.cursorLanguagePosition=1;
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
                    if (C.cursorPosition == 0 && C.cursorPositionColumn==0) {
                        deleteEnemyMenu();
                        C.GAMESTATE = 5;//menu before game
                    }
                    if (C.cursorPosition == 1 && C.cursorPositionColumn==0) {C.GAMESTATE = 3;deleteEnemyMenu();}//jak grac
                    if (C.cursorPosition == 2 && C.cursorPositionColumn==0) {C.GAMESTATE = 2;}//opcje
                    if (C.cursorPosition == 3 && C.cursorPositionColumn==0) {C.GAMESTATE = 4;}//autorzy
                    if (C.cursorPositionColumn==1 && C.cursorPosition==1) {C.GAMESTATE = 6;}//menu osiagniec
                    if (C.cursorPositionColumn==1 && C.cursorPosition==0) {C.GAMESTATE = 7;}//menu leaderboards
                    if (C.cursorPosition == 4 && C.cursorPositionColumn==0) System.exit(0);
                    break;
                case 2: /// obsluga entera w podmenu OPCJE
                    if(C.cursorSettingsPosition==5) {
                        //wejscie do podmenu PlayerSettings
                        C.GAMESTATE=21;
                    }
                    if(C.cursorSettingsPosition==6) C.GAMESTATE = 1;
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
                        resetLevel();
                        //resetVariables();
                        startDate= new Date(); //ustawienie daty poczatku gry
                        C.gamesPlayed++;
                        updateSettings();
                        SoundManager.stopMenuBackground();
                        try {
                            SoundManager.playBackground();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    break;
                case 6:
                case 7:
                    try {
                        SoundManager.playPlayerShot();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    C.GAMESTATE=1;
                    break;
                case 21:
                    if(C.cursorPlayerSettingsPosition==0) {C.GAMESTATE=22;}
                    if(C.cursorPlayerSettingsPosition==1) {resetData();}
                    if(C.cursorPlayerSettingsPosition==2) {resetData();resetAchievements();}//reset osiagniec i highscore
                    if(C.cursorPlayerSettingsPosition==3) {C.GAMESTATE=2;}//powrot do ustawien
                    break;
                case 22:
                    if(!C.hasPlayerName){updateHighscore();updateAchievements();C.GAMESTATE=1;C.hasPlayerName=true;break;}
                    if(C.canEnterName && C.cursorPlayerNamePosition==0) C.canEnterName=false;
                    else if(!C.canEnterName && C.cursorPlayerNamePosition==0) C.canEnterName=true;
                    else if(C.cursorPlayerNamePosition==1) {updateHighscore();updateAchievements(); C.GAMESTATE=2;}
                        else C.GAMESTATE=1;
                    break;
                case 99:
                    try {
                        SoundManager.playPlayerShot();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if (C.cursorLanguagePosition==1) {
                        C.LANGUAGE=1;
                    } else {
                        C.LANGUAGE=0;
                    }
                    updateSettings();
                    if(C.PLAYER_NAME ==""){C.hasPlayerName=false; C.GAMESTATE=22;C.canEnterName=true;}else {C.hasPlayerName=true;C.GAMESTATE=1;}
                    break;
            }

        }

        if (C.GAMESTATE==22 && C.canEnterName) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { // Usuwanie ostatniego znaku
                if (C.PLAYER_NAME.length() > 0) {
                    C.PLAYER_NAME = C.PLAYER_NAME.substring(0, C.PLAYER_NAME.length() - 1);
                }
            }  else if (Character.isLetterOrDigit(e.getKeyChar())) { // Dodawanie znaków do nazwy
                C.PLAYER_NAME += e.getKeyChar();
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
