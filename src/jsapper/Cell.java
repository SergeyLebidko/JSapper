package jsapper;

import javax.swing.*;
import java.awt.*;
import java.beans.Transient;

public class Cell extends JPanel{

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2;
        g2=(Graphics2D)g;

        g2.setColor(new Color(150, 150, 150));

        int w=this.getSize().width;
        int h=this.getSize().height;

        g2.fillRect(0,0, w, h);

    }
}
