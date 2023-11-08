///////////////GamePanel /////////////

package Gamestates;

import Constants.C;
import Entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {

    Player player;

    GamePanel(){
        super(null);
        Color color = new Color(132, 142, 220);
        setBackground(color);
        //deklaracja obiektow
        player= new Player(200,200);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    //glowny watek gry- mechaniki itd.
                    if (C.GAMESTATE==0){
                        player.moveX(1);
                        System.out.println("x:"+player.getX()+" y:"+player.getY() );
                    }
                    try {
                        Thread.sleep(100);//1second
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    repaint();
                }//wh-true

            }
        }).start();

    }//GamePanel

    //funckja rysujaca
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D =(Graphics2D) g;
        //Image imgBack = new ImageIcon(getClass().getClassLoader().getResource("background.gif")).getImage();
        //g.drawImage(imgBack, 0, 0, C.FRAME_WIDTH, C.FRAME_HEIGHT, null);
        if(C.GAMESTATE==0){
            player.draw(g2D);
        }
    }
    //funkcje
    //sprawdzanie kolizji
    public boolean isCollision(){
        if(true)//TODO
        return true; else return false;
    }
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
