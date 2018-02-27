package jsapper;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class MainClass {

    private JFrame frm;
    private int W=800;
    private int H=600;
    private Cell c;

    public MainClass() {
        frm = new JFrame("JSapper");
        frm.setLayout(null);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.getContentPane().setBackground(new Color(10,100,200));
        frm.setSize(W, H);
        int xPos= (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-W/2);
        int yPos= (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-H/2);
        frm.setLocation(xPos, yPos);
        frm.setResizable(false);
        frm.setVisible(true);

        c=new Cell();
        c.setBounds(100,100,32,32);
        c.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        frm.add(c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainClass();
            }
        });
    }

}
