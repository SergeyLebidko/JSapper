package jsapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MainClass{

    //Блок полей, постоянных для всех игровых сессий
    private final JFrame frm;                                       //Окно
    private final Color backWindowColor =Color.WHITE;               //Цвет фона окна
    private final int borderW=2;                                    //Горизонтальный отступ (в пикселях)
    private final int borderH=2;                                    //Вертикальный отступ (в пикселях)
    private final int cellW=36;                                     //Ширина клетки (в пикселях)
    private final int cellH=36;                                     //Высота клетки (в пикселях)
    private final int correctionW=6;                                //Горизонтальная поправка высоты окна
    private final int correctionH=29;                               //Вертикальная поправка высоты окна
    private final int BOMB=9;

    //Перечисление уровней сложности
    private enum Difficult{
        EASY, MEDIUM, HARD
    }

    //Текущий уровень сложности
    private Difficult currentDiff;

    //Блок полей-параметров текущей игровой сессии
    private int W;                                            //Щирина окна (в пикселях)
    private int H;                                            //Высота окна (в пикселях)
    private int wCellCount;                                   //Ширина окна (в клетках) для текущей игры
    private int hCellCount;                                   //Высота окна (в клетках) для текущей игры
    private int bombCount;                                    //Количество бомб в текущей игровой сессии
    private Cell[][] cells;                                   //Массив клеток, которые будут отображаться на экране
    private int[][] field;                                    //Модель игрового поля в программе

    //Обработчик событий от мыши. Он реализует игровую логику
    private MouseAdapter game=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()>1)return;        //Отсекаем двойные/тройные и т.д. клики...
            if(e.getButton()==2)return;           //...и нажатия на колёсико

            //Если нажата правая кнопка мышки, то выводим диалог с выбором уровня сложности
            if(e.getButton()==3){
                Box p=Box.createVerticalBox();
                JRadioButton easyBtn=new JRadioButton("Легко");
                JRadioButton mediumBtn=new JRadioButton("Средне");
                JRadioButton hardBtn=new JRadioButton("Сложно");
                ButtonGroup bg=new ButtonGroup();
                bg.add(easyBtn);
                bg.add(mediumBtn);
                bg.add(hardBtn);
                switch (currentDiff){
                    case EASY:{
                        easyBtn.setSelected(true);
                        break;
                    }
                    case MEDIUM:{
                        mediumBtn.setSelected(true);
                        break;
                    }
                    case HARD:{
                        hardBtn.setSelected(true);
                        break;
                    }
                    default:{
                        easyBtn.setSelected(true);
                    }
                }
                p.add(easyBtn);
                p.add(Box.createVerticalStrut(5));
                p.add(mediumBtn);
                p.add(Box.createVerticalStrut(5));
                p.add(hardBtn);
                JOptionPane.showMessageDialog(frm, p, "Выберите сложность",JOptionPane.QUESTION_MESSAGE);
                if(easyBtn.isSelected())gamedInit(Difficult.EASY);
                if(mediumBtn.isSelected())gamedInit(Difficult.MEDIUM);
                if(hardBtn.isSelected())gamedInit(Difficult.HARD);
                return;
            }

            //Если нажата левая кнопка мышки
            if(e.getButton()==1){
                //Узнаем координаты ячейки, в которую кликнул игрок
                Cell c=(Cell)(e.getSource());
                int x=c.getXCell();
                int y=c.getYCell();

            }

        }
    };

    //Формируем главное окно программы и выставляем его по центру экрана
    public MainClass() {
        frm = new JFrame("Сапёр");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setIconImage(new ImageIcon("logo.png").getImage());
        frm.getContentPane().setBackground(backWindowColor);
        frm.setResizable(false);
        frm.setVisible(true);
    }

    //Метод готовит новую игру в зависимости от выбранного уровня сложности
    private void gamedInit(Difficult diff){
        if(currentDiff!=diff){
            //Очищаем старое игровое поле
            if(cells!=null){
                for(int i=0;i<hCellCount;i++)
                    for(int j=0;j<wCellCount;j++){
                        frm.remove(cells[i][j]);
                    }
            }
            //Выбираем параметры нового поля в зависимости от уровня сложности
            switch (diff){
                case EASY:{
                    wCellCount=10;
                    hCellCount=10;
                    bombCount=6;
                    break;
                }
                case MEDIUM:{
                    wCellCount=15;
                    hCellCount=10;
                    bombCount=11;
                    break;
                }
                case HARD:{
                    wCellCount=20;
                    hCellCount=15;
                    bombCount=16;
                }
            }
            //Изменяем размер окна согласно размеру игрового поля и центрируем его на экране
            frm.setLayout(new GridLayout(hCellCount,wCellCount,2*borderH,2*borderW));
            W=wCellCount*(borderW+cellW+borderW)+correctionW;
            H=hCellCount*(borderH+cellH+borderH)+correctionH;
            frm.setSize(W, H);
            int xPos=(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-W/2);
            int yPos=(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-H/2);
            frm.setLocation(xPos, yPos);
            //Теперь формируем новое игровое поле. Перед первым ходом на нем нет мин
            cells=new Cell[hCellCount][wCellCount];
            field=new int[hCellCount][wCellCount];
            for(int i=0;i<hCellCount;i++)
                for(int j=0;j<wCellCount;j++){
                    cells[i][j]=new Cell(j,i,Cell.HIDE_CELL);
                    cells[i][j].addMouseListener(game);
                    frm.add(cells[i][j]);
                }
        }

        //Сбрасываем параметры ячеек игрового поля
        for(int i=0;i<hCellCount;i++)
            for(int j=0;j<wCellCount;j++){
                field[i][j]=0;
                cells[i][j].setValue(Cell.HIDE_CELL);
            }

        currentDiff=diff;
    }

    //Метод расставляет бомбы и подсчитывает количество бомб по соседству рядом с клетками, в которых самих бомб нет.
    //Можно поставить бомбы во все ячейки, кроме ячейки x0,y0
    private void makeField(int x0, int y0){
        int x;
        int y;
        //Сперва расставляем бомбы
        Random rnd=new Random();
        for(int i=0;i<bombCount;i++){
            do{
                x=rnd.nextInt(wCellCount);
                y=rnd.nextInt(hCellCount);
            }while (((x==x0) & (y==y0)) | (field[y0][x0]==BOMB));
            field[y0][x0]=BOMB;
        }
        //Теперь подсчитываем количество ячеек с бомбами для каждой пустой ячейки
        for(int i=0;i<hCellCount;i++)
            for(int j=0;j<wCellCount;j++){
                if(field[i][j]==BOMB)continue;
                for(int dx=-1;dx<2;dx++){
                    for(int dy=-1;dy<2;dy++){
                        if((dx==0) & (dy==0))continue;
                        x=j+dx;
                        y=i+dy;
                        if((x<0) || (x>=wCellCount) || (y<0) || (y>=hCellCount))continue;
                        if(field[y][x]==BOMB)continue;
                        if(field[y][x]==BOMB)field[i][j]++;
                    }
                }
            }
    }

    //Метод открывает все пустые ячейки, начиная ячейки с координатами x0,y0
    private void openVoidCells(int x0, int y0){

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainClass m;
                m=new MainClass();
                m.gamedInit(Difficult.EASY);
            }
        });
    }

}
