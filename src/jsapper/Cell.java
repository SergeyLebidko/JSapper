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
    private Color darkColor=new Color(150,150,150);
    private Color lightColor=new Color(250,250,250);

    //Конструктор
    public Cell(int x, int y, int value) {
        this.x=x;
        this.y=y;
        this.value=value;
    }

    //Метод отрисовывает содержимое клетки
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2;
        g2=(Graphics2D)g;

        if(value==HIDE_CELL)g2.setColor(darkColor);
        if(value==EMPTY_CELL)g2.setColor(lightColor);

        int w=this.getSize().width;
        int h=this.getSize().height;
        g2.fillRect(0,0, w, h);
    }

}
