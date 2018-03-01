package jsapper;

import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel{

    private Color darkColor=new Color(120,120,120);
    private Color lightColor=new Color(250,250,250);



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2;
        g2=(Graphics2D)g;

        int r=(int)(Math.random()*100);
        if(r<50)g2.setColor(darkColor); else g2.setColor(lightColor);

        int w=this.getSize().width;
        int h=this.getSize().height;
        g2.fillRect(0,0, w, h);
    }

}
