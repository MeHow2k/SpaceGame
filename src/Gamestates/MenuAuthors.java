package Gamestates;
import Constants.C;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuAuthors {
    Font customFont;
    public MenuAuthors() {
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/VT323-Regular.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

    }
    public void draw (Graphics2D g){

        Font title = new Font("Arial",Font.BOLD,60);
        g.setFont(customFont.deriveFont(20f));
        g.setColor(Color.white);
        g.drawString("O Programie",C.FRAME_WIDTH/2-200,50);

        Font txt = new Font("Arial",Font.BOLD,18);
        g.setFont(customFont.deriveFont(20f));
        g.drawString("Autorzy projektu: Michał Pasieka, Daniel Prorok, Paweł Sanocki",C.FRAME_WIDTH/2-200,100);
        g.drawString("Studenci III roku Informatyki w PANS w Krośnie",C.FRAME_WIDTH/2-200,130);


        g.drawString("Gra została napisana jako projekt zaliczeniowy przedmiotu:",30,330);
        g.drawString("Projekt zespołowy",30,360);
        g.drawString("Data rozpoczęcia projektu: 25.10.2023",30,390);
        g.drawString("Data zakończenia projektu: ?",30,420);
        g.drawString("Grafika została wykonana w programie Paint/GIMP/Libresprite",30,460);
        g.drawString("Dźwięki wykonane za pomocą: www.beepbox.co ,pobrane ze stron (no copyright)",30,490);
        g.drawString("Obróbka dźwięków/muzyki: Audacity",30,520);

        g.setFont(customFont.deriveFont(20f));
        g.drawString("Aby powrócić do menu naciśnij ENTER",C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
    }

}