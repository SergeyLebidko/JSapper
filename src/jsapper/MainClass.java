package jsapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
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

    //Предопределенные константы для ячеек массива field
    private final int BOMB=9;                                       //Константа для обозначения ячеек-бомб
    private final int EMPTY=0;                                      //Константа для обозначения пустых ячеек

    //Предопределенные константы для текущих состояний игровой сессии
    private final int START_STATE=0;                                //Игра началась, но первый ход еще не сделан. Необходима расстановка бомб
    private final int CONTINUE_STATE=1;                             //Первый ход сделан, бомбы расставлены
    private final int END_STATE=2;                                  //Игра окончена, ходы запрещены

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
    private int[][] field;                                    //Модель игрового поля в программе. Ячейки массива field могут принимать только значения BOMB и EMPTY
    private int state;                                        //Текущее состояние игровой сессии
    private int countOpenCells;                               //Количество открытых в текущей сессии ячеек
    private int totalEmptyCells;                              //Количество пустых ячеек в текущей игровой сессии

    //Обработчик событий от мыши. Он реализует игровую логику
    private MouseAdapter game=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()>1)return;        //Отсекаем двойные/тройные и т.д. клики...
            //получаем координаты ячейки. в которую кликнул игрок
            Cell c=(Cell)(e.getSource());
            int x=c.getXCell();
            int y=c.getYCell();

            //Если нажата правая кнопка мышки, то в ячейке выставляем/снимаем флажок
            if(e.getButton()==3){
                if(cells[y][x].getValue()==Cell.HIDE_CELL){
                    cells[y][x].setValue(Cell.FLAG_CELL);
                    return;
                }
                if(cells[y][x].getValue()==Cell.FLAG_CELL){
                    cells[y][x].setValue(Cell.HIDE_CELL);
                    return;
                }
            }

            //Если нажата средняя кнопка мышки, то выводим диалог с выбором уровня сложности, затем начинаем новую игру
            if(e.getButton()==2){
                Difficult diff=showNewGameDialog();
                gamedInit(diff);
                return;
            }

            //Если нажата левая кнопка мышки, то обрабатываем попытку открытия очередной клетки
            if(e.getButton()==1){
                // ********** Вставить код **********
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
            currentDiff=diff;
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
                    cells[i][j]=new Cell(j,i);
                    cells[i][j].addMouseListener(game);
                    frm.add(cells[i][j]);
                }
        }

        //Сбрасываем параметры ячеек игрового поля
        for(int i=0;i<hCellCount;i++)
            for(int j=0;j<wCellCount;j++){
                field[i][j]=EMPTY;
                cells[i][j].setValue(Cell.HIDE_CELL);
            }

        state=START_STATE;
        countOpenCells=0;
        totalEmptyCells=(wCellCount*hCellCount)-bombCount;
    }

    //Метод расставляет бомбы в ячейки игрового поля. При этом ячейка x0,y0 является запрещенной для установки бомбы
    private void setBombs(int x0, int y0){
        int x;
        int y;
        Random rnd;
        rnd=new Random();
        for (int i=0; i<bombCount;i++){
            do {
                x=rnd.nextInt(wCellCount);
                y=rnd.nextInt(hCellCount);
            }while(((x==x0) & (y==y0)) | field[y][x]==BOMB);
            field[y][x]=BOMB;
        }
    }

    //Метод показывает все скрытые и помеченные флажком ячейки игрового поля
    private void showAllHideCells(){
        for (int i=0;i<hCellCount;i++)
            for (int j=0;j<wCellCount;j++)
                if((cells[i][j].getValue()==Cell.HIDE_CELL) | (cells[i][j].getValue()==Cell.FLAG_CELL)){
                    if(field[i][j]==BOMB)cells[i][j].setValue(Cell.BOMB_CELL);
                    if(field[i][j]==EMPTY){
                        int val=getCountBombsAround(j,i);
                        if(val==0)cells[i][j].setValue(Cell.EMPTY_CELL);
                        if(val>0)cells[i][j].setValue(val);
                    }
                }
    }

    //Метод возвращает количество бомб возле ячейки с координатами x0,y0
    private int getCountBombsAround(int x0, int y0){
        int count=0;
        int x;
        int y;
        for (int dx=-1;dx<2;dx++)
            for (int dy=-1;dy<2;dy++){
                if ((dx==0) & (dy==0))continue;
                x=x0+dx;
                y=y0+dy;
                if((x<0) | (y<0) | (x>=wCellCount) | (y>=hCellCount))continue;
                if(field[y][x]!=BOMB)continue;
                count++;
            }
        return count;
    }

    //Метод открывает все пустые ячейки, раположенные рядом с ячейкой x0,y0 (включая саму ячейку x0,y0). Возвращает количество открытых ячеек
    //Метод также предполагает, что клетка x0,y0 изначально пуста
    private int openEmptyCells(int x0, int y0){
        int countOpenCells=0;
        int val;
        val=getCountBombsAround(x0,y0);

        //Случай, когда исходная пустная клетка находится рядом с бомбой
        if(val>0){
            cells[y0][x0].setValue(val);
            countOpenCells=1;
        }

        //Случай, когда рядом с клеткой нет бомб
        if(val==0){
            //Объявляем вспомотательный внутренний класс для хранения координат
            class Coord{
                int x;
                int y;

                public Coord(int x, int y) {
                    this.x = x;
                    this.y = y;
                }
            }
            Coord c;
            int x;
            int y;
            LinkedList<Coord> pool=new LinkedList<>();
            pool.add(new Coord(x0,y0));
            do {
                //Извлекаем координаты очередной ячейки из пула
                c=pool.poll();
                val=getCountBombsAround(c.x,c.y);
                if(val==0){
                    cells[c.y][c.x].setValue(Cell.EMPTY_CELL);
                    field[c.y][c.x]=-1;
                    countOpenCells++;
                }
                if(val>0){
                    cells[c.y][c.x].setValue(val);
                    field[c.y][c.x]=-1;
                    countOpenCells++;
                    continue;
                }
                //Добавляем новые ячейки в пул
                for (int dx=-1; dx<2;dx++)
                    for (int dy=-1;dy<2;dy++){
                        if((dx==0) & (dy==0))continue;
                        x=c.x+dx;
                        y=c.y+dy;
                        if((x<0) | (x>=wCellCount) | (y<0) | (y>hCellCount))continue;
                        if(field[y][x]==-1)continue;
                        pool.add(new Coord(x,y));
                    }
            }while (!pool.isEmpty());
            //Очищаем массив field от вспомогательных ячеек
            for (int i=0;i<hCellCount;i++)
                for (int j=0;j<wCellCount;j++)if(field[i][j]==-1)field[i][j]=0;
        }

        //Возвращаем количество открытых клеток
        return countOpenCells;
    }

    //Метод показывает диалог выбора сложности игры
    private Difficult showNewGameDialog(){
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
        if(easyBtn.isSelected())return Difficult.EASY;
        if(mediumBtn.isSelected())return Difficult.MEDIUM;
        if(hardBtn.isSelected())return Difficult.HARD;
        return Difficult.EASY;
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
