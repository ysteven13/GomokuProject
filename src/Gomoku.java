import processing.core.*;

import java.util.*;
public class Gomoku extends PApplet{
    //////
    int heightBoard = 15;
    int widthBoard = 15;
    private point[][] grid = new point[heightBoard][widthBoard];
    Integer turnNumber = 0;
    PImage board;
    PImage blackStone;
    PImage whiteStone;
    final int ROW_SCORE = 100000;
    final int WIN_SCORE = 1000;
    public int currentPlayer = 1;
    public ArrayList<point> moves = new ArrayList<>(); //history of moves;
    int rowLength = 5;
    //////
    class point {
        int x; int y; int value; int turn;
        public point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String... args) {
        PApplet.main("Gomoku");
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter in the desired length and width");
    }
    public void settings() {
        smooth(3);
        size(750,750);

        for(int i = 0; i < width; i += width/widthBoard) {
            for(int w = 0; w<height; w+= height/heightBoard) {
                point p = new point(i,w);
                grid[i/(width/widthBoard)][w/(height/heightBoard)] = p;
            }
        }
    }
    public void setup() {
        frameRate(4);
        String url1 = "https://woodflooringtrends.files.wordpress.com/2012/02/natural-maple-unfinished.jpg";
        String url2 = "https://raw.githubusercontent.com/zpmorgan/gostones-render/master/b.png";
        String url3 = "https://opengameart.org/sites/default/files/w_1.png";
        board = loadImage(url1, "jpg");
        blackStone = loadImage(url2,"png");
        whiteStone = loadImage(url3, "png");
        image(board,0,0);
    }
    public void draw() {
        int widthSpace = width/widthBoard;
        int heightSpace = height/heightBoard;
        for(int i = 0; i < width; i += widthSpace) {
            strokeWeight((float)1.1);
            line(i,0,i,height);
        }
        for(int w = 0; w<height; w+=heightSpace) {
            strokeWeight((float) 1.1);
            line(0,w,width,w);
        }
        for(int i = 0; i < widthBoard;  i++) {
            for(int x = 0; x < heightBoard; x++) {
                if(grid[i][x].value == 1) {
                    image(whiteStone,grid[i][x].x,grid[i][x].y,width/widthBoard-5,height/heightBoard-5);
                } else if(grid[i][x].value == -1) {
                    image(blackStone,grid[i][x].x,grid[i][x].y,width/widthBoard-5,height/heightBoard-5);
                }

            }
        }
        if(getMoves(grid).isEmpty()) {
            System.out.println("Game Over!");
            stop();
        }
        textSize(45);
        fill(255);
    }

    public point moveCPU() {
        AlphaBetaSearch worker = new AlphaBetaSearch(grid);
        worker.searchMoves();
        return worker.getBestMove();
    }
    public void mousePressed() {
        int x = mouseX/(width/widthBoard);
        int y =  mouseY/(height/heightBoard);
        int cirX = grid[x][y].x;
        int cirY = grid[x][y].y;
        if(grid[x][y].value == 0) {
            String turn = String.valueOf(turnNumber);
            grid[x][y].value = 1;
            grid[x][y].turn = turnNumber;
            moves.add(grid[x][y]);
            turnNumber++;
            currentPlayer = -currentPlayer;
            point cpu = moveCPU();
            if(!getMoves(grid).isEmpty()) {
                doMove(cpu, grid);
            }
        }
    }
    public ArrayList<point> getMoves(point[][] grid) {
        ArrayList<point> moves = new ArrayList<>();
        int evaluation =  evaluate(grid);
        if(evaluation > WIN_SCORE || evaluation < -WIN_SCORE) {
            return moves;
        }
        for (int x = 0; x < widthBoard; x++) {
            for (int y = 0; y < heightBoard; y++) {
                if (grid[x][y].value == 0) {
                    moves.add(new point(x, y));
                }
            }
        }
        return moves;
    }
    public void doMove(point move, point[][] grid) {
        grid[move.x][move.y].value = currentPlayer;
        currentPlayer = -currentPlayer;
        moves.add(move);
    }

    public void undoMove(point[][] grid) {
        point m = moves.remove(moves.size()-1);
        grid[m.x][m.y].value = 0;
        currentPlayer = -currentPlayer;
    }
     public int evaluate(point[][] grid) {//I don't remember how this works
         //TODO: figure out how this works again
        int score = 0;
        for (int player = -1; player <= 1; player += 2) {
            for (int y = 0; y < heightBoard; y++) {
                for (int x = 0; x <= widthBoard - rowLength; x++) {
                    int i = 0;
                    while (i < rowLength && grid[x + i][y].value == player) {
                        i++;
                    }
                    if (i == rowLength) {
                        score += player * ROW_SCORE;
                    } else if ((x + i < widthBoard && grid[x + i][y].value == 0) || (x - 1 > 0 && grid[x - 1][y].value == 0)) {
                        score += player * i;
                    }
                }
            }
            for (int y = 0; y <= heightBoard - rowLength; y++) {
                for (int x = 0; x < widthBoard; x++) {
                    int i = 0;
                    while (i < rowLength && grid[x][y + i].value == player) {
                        i++;
                    }
                    if (i == rowLength) {
                        score += player * ROW_SCORE;
                    } else if ((y + i < heightBoard && grid[x][y + i].value == 0) || (y - 1 > 0 && grid[x][y - 1].value == 0)) {
                        score += player * i;
                    }
                }
                for (int x = 0; x <= widthBoard - rowLength; x++) {
                    int i = 0;
                    while (i < rowLength && grid[x + i][y + i].value == player) {
                        i++;
                    }
                    if (i == rowLength) {
                        score += player * ROW_SCORE;
                    } else if ((x + i < widthBoard && y + i < heightBoard && grid[x + i][y + i].value == 0) || (x - 1 > 0 && y - 1 > 0 && grid[x - 1][y - 1].value == 0)) {
                        score += player * i;
                    }
                    i = 0;
                    while (i < rowLength && grid[x + rowLength - 1 - i][y + i].value == player) {
                        i++;
                    }
                    if (i == rowLength) {
                        score += player * ROW_SCORE;
                    } else if ((x + rowLength - 1 - i > 0 && y + 1 < heightBoard && grid[x + rowLength - 1 - i][y + i].value == 0) || (x + 1 < widthBoard && y - 1 > 0 && grid[x + 1][y - 1].value == 0)) {
                        score += player * i;
                    }
                }
            }
        }
        return score;
    }

}
