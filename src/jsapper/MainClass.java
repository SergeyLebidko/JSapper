package jsapper;

import javax.swing.*;
import java.awt.*;

public class MainClass{

    //Блок полей, постоянных для всех игровых сессий
    private JFrame frm;                                       //Окно
    private Color backWindowColor =Color.WHITE;               //Цвет фона окна
    private int borderW=2;                                    //Горизонтальный отступ (в пикселях)
    private int borderH=2;                                    //Вертикальный отступ (в пикселях)
    private int cellW=32;                                     //Ширина клетки (в пикселях)
    private int cellH=32;                                     //Высота клетки (в пикселях)
    private int correctionW=6;                                //Горизонтальная поправка высоты окна
    private int correctionH=29;                               //Вертикальная поправка высоты окна

    //Блок полей-параметров текущей игровой сессии
    private int W;                                            //Щирина окна (в пикселях)
    private int H;                                            //Высота окна (в пикселях)
    private int wCellCount=20;                                //Ширина окна (в клетках) для текущей игры
    private int hCellCount=15;                                //Высота окна (в клетках) для текущей игры
    private Cell[][] cells;                                   //Массив клеток, которые будут отображаться на экране
    private int[][] field;                                    //Модель игрового поля в программе

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

    //Метод готовит новую игру
    public void gamedInit(){
        //Вначале очищаем старое поле. Предполагаем, что оно может иметь размер, отличный от текущего
        if(cells!=null){
            for(int i=0;i<cells.length;i++)
                for(int j=0;j<cells[i].length;j++)frm.remove(cells[i][j]);
        }
        //Теперь формируем новое игровое поле. Перед первым ходом на нем нет мин
        cells=new Cell[hCellCount][wCellCount];
        field=new int[hCellCount][wCellCount];
        for(int i=0;i<hCellCount;i++)
            for(int j=0;j<wCellCount;j++){
                field[i][j]=0;
                cells[i][j]=new Cell(j,i,Cell.HIDE_CELL);
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
                m.gamedInit();
            }
        });
    }

}
