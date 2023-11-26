package Entities;

import java.awt.*;

public class Points {

    int x,y;
    int value;
    String owner;

    public Points(){
        this.x=30;
        this.y=30;
        this.value=0;
        this.owner="Gracz";
    }
    public Points(int x, int y, int value, String owner) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.owner = owner;
    }

    public void draw(Graphics2D g){
        g.setColor(Color.white);
        g.drawString(Integer.toString(getValue()),x,y);
    }
    public int getValue() {
        return value;
    }

    public String getOwner() {
        return owner;
    }

    public void setValue(int value, int score_increment) {
        this.value = value + score_increment;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
