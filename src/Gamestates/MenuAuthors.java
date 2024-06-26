package Gamestates;
import Constants.C;
import Constants.Strings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MenuAuthors {
    Font customFont;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("logo.gif")).getImage();
    Color selectedColor = new Color(255, 233, 12);
    Strings gameStrings;
    public MenuAuthors() {
        gameStrings = new Strings();
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

    }
    public void draw (Graphics2D g){
        g.setFont(customFont.deriveFont(30f));
        g.setColor(Color.white);
        if(C.LANGUAGE==1)//pl
        {
            g.drawString("O Programie", C.FRAME_WIDTH / 2 - 200, 50);

            g.setFont(customFont.deriveFont(25f));
            g.drawString("Autorzy projektu: Michał Pasieka, Daniel Prorok, Paweł Sanocki", C.FRAME_WIDTH / 2 - 370, 100);
            g.drawString("Studenci III roku Informatyki w PANS w Krośnie", C.FRAME_WIDTH / 2 - 370, 130);

            g.drawImage(logo, C.FRAME_WIDTH / 2 - 260, 70, 480, 280, null);

            g.drawString("Gra została napisana jako projekt zaliczeniowy przedmiotu:", 30, 330);
            g.drawString("Projekt zespołowy", 30, 360);
            g.drawString("Data rozpoczęcia projektu: 25.10.2023", 30, 390);
            g.drawString("Data zakończenia projektu: 27.05.2024", 30, 420);
            g.drawString("Grafika została wykonana w programie Paint/GIMP/Libresprite", 30, 460);
            g.drawString("Dźwięki wykonane za pomocą: www.beepbox.co, pobrane ze stron (no copyright)", 30, 490);
            g.drawString("Obróbka dźwięków/muzyki: Audacity", 30, 520);
        } else {//EN todo po prostu bierzesz zamieniasz na ang, ew. jak coś bedzie nie tak to dostosuj położenie albo stwórz nowa linijke
            g.drawString("About the game", C.FRAME_WIDTH / 2 - 200, 50);

            g.setFont(customFont.deriveFont(25f));
            g.drawString("Project authors: Michał Pasieka, Daniel Prorok, Paweł Sanocki", C.FRAME_WIDTH / 2 - 370, 100);
            g.drawString("Third-year Computer Science students at PANS in Krosno, Poland", C.FRAME_WIDTH / 2 - 370, 130);

            g.drawImage(logo, C.FRAME_WIDTH / 2 - 260, 70, 480, 280, null);

            g.drawString("The game was developed as a course assignment:", 30, 330);
            g.drawString("Team Project", 30, 360);
            g.drawString("Project start date: 25.10.2023", 30, 390);
            g.drawString("Project completion date: 27.05.2024", 30, 420);
            g.drawString("The graphics were made in the program Paint/GIMP/Libresprite", 30, 460);
            g.drawString("Sounds made with: www.beepbox.co, downloaded from websites (no copyright)", 30, 490);
            g.drawString("Sound/music processing: Audacity", 30, 520);
        }//todo dalej nie
        g.setColor(selectedColor);
        g.setFont(customFont.deriveFont(18f));
        g.drawString(gameStrings.getString("Aby powrócićENTER"), C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
    }

}