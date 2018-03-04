package jsapper;

import javax.swing.*;
import java.awt.*;

public class MainClass{

    //Блок полей, постоянных для всех игровых сессий
    private final JFrame frm;                                       //Окно
    private final Color backWindowColor =Color.WHITE;               //Цвет фона окна
    private final int borderW=4;                                    //Горизонтальный отступ (в пикселях)
    private final int borderH=4;                                    //Вертикальный отступ (в пикселях)
    private final int cellW=36;                                     //Ширина клетки (в пикселях)
    private final int cellH=36;                                     //Высота клетки (в пикселях)
    private final int correctionW=6;                                //Горизонтальная поправка высоты окна
    private final int correctionH=29;                               //Вертикальная поправка высоты окна

    //Блок полей-параметров текущей игровой сессии
    private int W;                                            //Щирина окна (в пикселях)
    private int H;                                            //Высота окна (в пикселях)
    private int wCellCount=25;                                //Ширина окна (в клетках) для текущей игры
    private int hCellCount=15;                                //Высота окна (в клетках) для текущей игры
    private Cell[][] cells;                                   //Массив клеток, которые будут отображаться на экране
    private int[][] field;                                    //Модель игрового поля в программе

    //Формируем главное окно программы и выставляем его по центру экрана
    public MainClass() {
        frm = new JFrame("JSapper");
        frm.setLayout(new GridLayout(hCellCount,wCellCount,2*borderH,2*borderW));
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

    //Метод готовит новую игру
    public void gamedInit(){
        //Вначале очищаем старое поле. Предполагаем, что оно может иметь размер, отличный от текущего
        if(cells!=null){
            for(int i=0;i<cells.length;i++)
                for(int j=0;j<cells[i].length;j++)frm.remove(cells[i][j]);
        }
        //Теперь формируем новое игровое поле. Перед первым ходом на нем нет мин

        int valTmp=-1;

        cells=new Cell[hCellCount][wCellCount];
        field=new int[hCellCount][wCellCount];
        for(int i=0;i<hCellCount;i++)
            for(int j=0;j<wCellCount;j++){
                field[i][j]=0;
                cells[i][j]=new Cell(j,i,valTmp);

                valTmp++;
                if(valTmp>9)valTmp=-1;

                frm.add(cells[i][j]);
            }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainClass m;
                m=new MainClass();
                m.gamedInit();
            }
        });
    }

}
