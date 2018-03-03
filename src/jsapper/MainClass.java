package jsapper;

import javax.swing.*;
import java.awt.*;

public class MainClass{

    private JFrame frm;                                       //Окно

    private int W;                                            //Щирина окна (в пикселях)
    private int H;                                            //Высота окна (в пикселях)
    private int wCellCount=20;                                //Ширина окна (в клетках)
    private int hCellCount=15;                                //Высота окна (в клетках)
    private int borderW=2;                                    //Горизонтальный отступ (в пикселях)
    private int borderH=2;                                    //Вертикальный отступ (в пикселях)
    private int cellW=48;                                     //Ширина клетки (в пикселях)
    private int cellH=48;                                     //Высота клетки (в пикселях)
    private int correctionW=6;                                //Горизонтальная поправка высоты окна
    private int correctionH=29;                               //Вертикальная поправка высоты окна

    private Cell[][] cells;                                   //Массив Клеток, которые будут отображаться на экране
    private int[][] field;                                    //Модель игрового поля в программе

    private Color backWindowColor =Color.WHITE;               //Цвет фона окна


    //Формируем главное окно программы и выставляем его по центру экрана
    public MainClass() {
        frm = new JFrame("JSapper");
        frm.setLayout(null);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.getContentPane().setBackground(backWindowColor);
        W=wCellCount*(borderW+cellW+borderW)+correctionW;
        H=hCellCount*(borderH+cellH+borderH)+correctionH;
        frm.setSize(W, H);
        int xPos=(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-W/2);
        int yPos=(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-H/2);
        frm.setLocation(xPos, yPos);
        frm.setResizable(false);
        frm.setVisible(true);
    }

    //Формируем массив клеток и добавляем их на главное окно. Очищаем моедль игрового поля
    public void gameInit(){

        int valTmp=0;

        cells=new Cell[hCellCount][wCellCount];
        field=new int[hCellCount][wCellCount];
        for(int i=0;i<hCellCount;i++)
            for(int j=0;j<wCellCount;j++){
                field[i][j]=0;
                cells[i][j]=new Cell(j,i,valTmp);

                valTmp++;
                if(valTmp==10)valTmp=0;

                cells[i][j].setBounds(j*(borderW+cellW+borderW)+borderW,i*(borderH+cellH+borderH)+borderH,cellW,cellH);

                frm.add(cells[i][j]);
            }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainClass m;
                m=new MainClass();
                m.gameInit();
            }
        });
    }

}
