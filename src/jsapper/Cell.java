package jsapper;

import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel{

    public static final int HIDE_CELL=-1;   //Ячейка отображается как закрытая. Ее содержимое еще не известно игроку
    public static final int EMPTY_CELL=0;   //Ячейка отображается как пустая
    public static final int BOMB_CELL=9;    //Ячейка отображается, как ячейка с бомбой

    private int x;                 //x-координата данной клетки
    private int y;                 //y-координата данной клетки
    private int value;             //Содержимое клетки

    //Пресеты цветов
    private Color borderColor =new Color(50,50,50);
    private Color darkBackColor =new Color(150,150,150);
    private Color lightBackColor =new Color(250,250,250);
    private Color bombBackColor=Color.RED;
    private Color bombColor=Color.BLACK;


    //Конструктор
    public Cell(int x, int y, int value) {
        this.x=x;
        this.y=y;
        this.value=value;
        setBorder(BorderFactory.createLineBorder(borderColor));
    }

    public void setValue(int value){
        this.value=value;
        repaint();
    }

    //Метод отрисовывает содержимое клетки
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2;
        g2=(Graphics2D)g;
        int w=this.getSize().width;
        int h=this.getSize().height;

        if(value==HIDE_CELL){
            g2.setColor(darkBackColor);
            g2.fillRect(0,0, w, h);
            return;
        }
        if(value==EMPTY_CELL){
            g2.setColor(lightBackColor);
            g2.fillRect(0,0, w, h);
            return;
        }
        if((value>EMPTY_CELL)&(value<BOMB_CELL)){
            g2.setColor(darkBackColor);
            g2.fillRect(0,0, w, h);
            g2.setColor(lightBackColor);
            g2.fillOval((int)(w*0.1),(int)(h*0.1),(int)(w*0.8),(int)(h*0.8));
            g2.setColor(borderColor);
            g2.drawOval((int)(w*0.1),(int)(h*0.1),(int)(w*0.8),(int)(h*0.8));
            g2.setColor(darkBackColor);
            g2.setFont(new Font("Arial",Font.BOLD,(int)(w*0.7)));
            g2.drawString(""+value,(int)(w*0.33),(int)(h*0.73));
            return;
        }
        if(value==BOMB_CELL){
            g2.setColor(bombBackColor);
            g2.fillRect(0,0, w, h);
            g2.setColor(bombColor);
            g2.fillOval((int)(w*0.2),(int)(h*0.2),(int)(w*0.6),(int)(h*0.6));

            double r=w*0.4;
            double d=360.0/16.0;
            double x0=w/2;
            double y0=h/2;

            double[] x=new double[16];
            double[] y=new double[16];
            double a;
            int i;
            for(a=-(d/2),i=0; a<(360-(d/2)); a+=d, i++){
                x[i]=x0+r*Math.cos(Math.toRadians(a));
                y[i]=y0+r*Math.sin(Math.toRadians(a));
            }
            int[] xp=new int[4];
            int[] yp=new int[4];

            for(int j=0;j<7;j+=2){
                xp[0]=(int)x[j];
                yp[0]=(int)y[j];
                xp[1]=(int)x[j+1];
                yp[1]=(int)y[j+1];
                xp[2]=(int)x[j+8];
                yp[2]=(int)y[j+8];
                xp[3]=(int)x[j+9];
                yp[3]=(int)y[j+9];
                g2.fillPolygon(xp,yp,4);
            }

            return;
        }

    }

}
