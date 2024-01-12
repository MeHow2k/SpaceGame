//klasa odpowiedzialna za ramkę aplikacji i tworzącą w niej gamepanel
package Gamestates;

import Constants.C;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Frame extends JFrame {
    Frame(){
        super("Space Game");
        //Image icon = new ImageIcon(getClass().getClassLoader().getResource(".png")).getImage();
        //setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(C.FRAME_WIDTH,C.FRAME_HEIGHT); // szerokosc i wysokość okna do zmiany w Constants.C
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        GamePanel gamePanel=new GamePanel();
        setContentPane(gamePanel);
        gamePanel.setFocusable(true);
        setVisible(true);
    }
    public static void main(String[] args) throws IOException, FontFormatException {
        //create the font

        try {
            //create the font to use. Specify the size!
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("/VT323-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }



        new Frame();
    }
}