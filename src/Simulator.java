import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Simulator extends JPanel implements Runnable{
    int xPanel = 1000;
    int yPanel = 1000;
    int blocksize = 100;
    int xWidth = 1000 / blocksize;
    int yHeight = 1000 / blocksize;
    int[][] grid = new int[xWidth][yHeight];
    int rows;
    int cols;
    ArrayList<Tile> tiles;
    ArrayList<Animal>[][] hare;
    ArrayList<Animal>[][] lynx;
    ArrayList<Integer>[][] hareData;
    ArrayList<Integer>[][] lynxData;
    char[][] world;
    int year = 0;
    int total_years;
    ArrayList<Integer>[] data = new ArrayList[3];
    ArrayList<Integer> arr_hare = new ArrayList<>();
    ArrayList<Integer> arr_lynx = new ArrayList<>();
    int h;
    int l;
    public Simulator(int total_years, int numHare, int numLynx, int rows, int cols) throws IOException, InterruptedException {
        this.total_years = total_years;
        this.rows = rows;
        this.cols = cols;
        hare = new ArrayList[cols][rows];
        lynx = new ArrayList[cols][rows];
        hareData = new ArrayList[cols][rows];
        lynxData = new ArrayList[cols][rows];
        char[][] exampleTiles = {
                {'R', 'R', 'G', 'G', 'G', 'M'},
                {'G', 'R', 'R', 'G', 'G', 'G'},
                {'G', 'G', 'R', 'G', 'G', 'G'},
                {'G', 'G', 'R', 'R', 'G', 'G'},
                {'M', 'G', 'G', 'G', 'G', 'M'},
                {'M', 'M', 'G', 'G', 'M', 'M'}
        };
        this.tiles = Tile.initializeTiles(exampleTiles);
        world = exampleTiles;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                hare[i][j] = new ArrayList<>();
                lynx[i][j] = new ArrayList<>();
                hareData[i][j] = new ArrayList<>();
                lynxData[i][j] = new ArrayList<>();
            }
        }
        DataManager();
        setAnimal(numHare, numLynx, 6, 6);
        setBackground(Color.WHITE);
        setSize(xPanel, yPanel);
        setLayout(null);
        simulate(total_years, 6, 6);
        Thread thr = new Thread((Runnable) this);
        thr.start();
        writeData("myData.csv");

    }

    public void DataManager() {
        ArrayList<Integer>[] datapoint = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            datapoint[i] = new ArrayList<Integer>();
        }
        this.data = datapoint;
    }

    public void storeData() {
        data[0].add(year);
        int sum_hare = 0;
        int sum_lynx = 0;
        //add every element to the first one
        for (int i = 0; i < arr_hare.size(); i++) {
            sum_hare += arr_hare.get(i);
        }
        arr_hare.clear();
        data[1].add(sum_hare);

        for (int i = 0; i < arr_lynx.size(); i++) {
            sum_lynx += arr_lynx.get(i);
        }
        arr_lynx.clear();
        data[2].add(sum_lynx);
        //System.out.println("Population of hare is " + sum_hare);
        //System.out.println("Population of lynx is " + sum_lynx);
    }

    public void writeData(String filename) throws IOException {
        FileWriter outFile = new FileWriter(filename);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < data[i].size(); j++) {
                int[] data2 = new int[3];
                data2[i] = data[i].get(j);
                outFile.write(data2[i] + ",");
            }
            outFile.write("\n");
        }
        outFile.close();
    }

    private void grid(Graphics g) {
        //draw grid
        for (int i = 0; i < grid.length; i++) {
            g.drawLine(0, i * blocksize, xPanel, i * blocksize);
            g.drawLine(i * blocksize, 0, i * blocksize + 1, yPanel);
        }
    }

    private void drawBlock(Graphics g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char type = world[j][i];
                if (type == ('G')) {
                    g.setColor(Color.black);
                    g.drawRect(i * blocksize, j * blocksize, blocksize, blocksize);
                    Color ForestGreen = new Color(34, 139, 34);
                    g.setColor(ForestGreen);
                    g.fillRect(i * blocksize, j * blocksize, blocksize, blocksize);
                } else if (type == 'R') {
                    g.setColor(Color.black);
                    g.drawRect(i * blocksize, j * blocksize, blocksize, blocksize);
                    Color LightSkyBlue = new Color(135, 206, 250);
                    g.setColor(LightSkyBlue);
                    g.fillRect(i * blocksize, j * blocksize, blocksize, blocksize);
                } else if (type == 'M') {
                    g.setColor(Color.black);
                    g.drawRect(i * blocksize, j * blocksize, blocksize, blocksize);
                    g.setColor(Color.GRAY);
                    g.fillRect(i * blocksize, j * blocksize, blocksize, blocksize);
                } else {
                    g.setColor(Color.black);
                    g.drawRect(i * blocksize, j * blocksize, blocksize, blocksize);
                    g.setColor(Color.white);
                    g.fillRect(i * blocksize, j * blocksize, blocksize, blocksize);
                }
            }
        }
    }

    private void drawAnimal(Graphics g) {
        for (int x = 0; x < hare.length; x++) { //col
            for (int y = 0; y < hare[x].length; y++) { //row
                int index = y + (x * hare.length);
                if (tiles.get(index).type == ('G')) {
                    g.setColor(Color.YELLOW);
                    //the size of circle depends on the ratio
                    int rad = (blocksize * hare[x][y].size()) / (hare[x][y].size() + lynx[x][y].size());
                    //int rad = (blocksize * h) / (h + l);
                    g.fillOval((y + 1) * blocksize - rad, x * blocksize, rad, rad);
                }
            }
        }
        for (int x = 0; x < lynx.length; x++) { //col
            for (int y = 0; y < lynx[x].length; y++) { //row
                int index = y + (x * lynx.length);
                if (tiles.get(index).type == ('G')) {
                    g.setColor(Color.white);
                    int rad2 = (blocksize * lynx[x][y].size()) / (hare[x][y].size() + lynx[x][y].size());
                    //int rad2 = (blocksize * l) / (h + l);
                    g.fillOval(y * blocksize, x * blocksize, rad2, rad2);
                }
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        grid(g);
        drawBlock(g);
        drawAnimal(g);
    }
    public void simulate(int total_years, int cols, int rows) {
        for (int i = 0; i < total_years; i++) {
            simOneYear(cols, rows);
            moveAnimal(cols, rows);
            year++;
            repaint();
        }
    }
    public void simOneYear(int cols, int rows) {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                int i = x + (y * cols);
                char type = tiles.get(i).type;
                if (type == 'G') {
                    simOneYearHare(x, y);
                    simOneYearLynx(x, y);
                }
            }
        }
        storeData();
    }

    public void simOneYearHare(int col, int row) {
        int Grass = 80000;
        ArrayList<Animal> newhare = new ArrayList<>();
        for (int i = 0; i < hare[col][row].size(); i++) {
            Animal hare1 = hare[col][row].get(i);
            int curr_age = hare1.getAge();
            if (hare1.age() == true) {
                Hare older = new Hare(curr_age + 1);
                newhare.add(older);
            }
            if (Grass > 39) {
                if (hare[col][row].size() > 2) {
                    if (hare1.birth() == true) {
                        Random rand2 = new Random();
                        //give birth to 3-4 rabbits
                        for (int j = rand2.nextInt(3, 4); 0 < j; j--) {
                            Hare baby = new Hare(0);
                            newhare.add(baby);
                            Grass -= 40;
                        }
                    }
                }
            }
        }
        hare[col][row] = newhare;
        arr_hare.add(hare[col][row].size());
    }

    public void simOneYearLynx(int col, int row) {
        ArrayList<Animal> newlynx = new ArrayList<>();
        for (int i = 0; i < lynx[col][row].size(); i++) {
            Animal lynx1 = lynx[col][row].get(i);
            int curr_age = lynx1.getAge();
            Lynx lynx2 = new Lynx(curr_age);
            if (lynx2.age() == true) {
                Lynx older = new Lynx(curr_age + 1);
                newlynx.add(older);
            }
            for (int x = 0; x < hare[col][row].size(); x++) {
                if (lynx2.eat() == true) {
                    Random rand3 = new Random();
                    int y = rand3.nextInt(hare[col][row].size());
                    hare[col][row].remove(y);
                }
            }
            if (hare[col][row].size() * lynx[col][row].size() * 0.01 > 30) {
                if (lynx[col][row].size() > 2) {
                    if (lynx2.birth() == true) {
                        //give birth to only one lynx
                        Lynx baby = new Lynx(0);
                        newlynx.add(baby);
                    }
                }
            }
        }
        lynx[col][row] = newlynx;
        arr_lynx.add(lynx[col][row].size());
    }

    public void setAnimal(int numHare, int numLynx, int rows, int cols) {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                int i = x + (y * cols);
                char type = tiles.get(i).type;
                if (type == 'G') {
                    setHare(numHare, y, x);
                    setLynx(numLynx, y, x);
                }
            }
        }
    }

    public void setHare(int n, int col, int row) {
        for (int i = 0; i < n; i++) {
            Random random1 = new Random();
            int x = random1.nextInt(3); // no age larger than 3
            Hare hare1 = new Hare(x);
            hare[col][row].add(hare1);
        }
    }

    public void setLynx(int n, int col, int row) {
        for (int i = 0; i < n; i++) {
            Random random2 = new Random();
            int x = random2.nextInt(6); // no age larger than 6
            Lynx lynx1 = new Lynx(x);
            lynx[col][row].add(lynx1);
        }
    }

    public void moveAnimal(int cols, int rows) {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                int i = x + (y * cols);
                char type = tiles.get(i).type;
                if (type == 'G') {
                    moveHare(y, x, cols, rows);
                    moveLynx(y, x, cols, rows);
                }
            }
        }
    }

    public void moveHare(int col, int row, int cols, int rows) {
        int Grass = 80000;
        int i = row + (col * cols);
        for (int x = 0; x < tiles.get(i).neighbors.size(); x++) {
            int x_neighbor = tiles.get(i).neighbors.get(x).x;
            int y_neighbor = tiles.get(i).neighbors.get(x).y;

            //loop through all the hare in hare[][]
            for (int a = 0; a < hare[col][row].size(); a++) {
                //ratio of grass and hares in the neighborhood
                int r_neighbor = Grass / hare[x_neighbor][y_neighbor].size();
                if (Grass / hare[col][row].size() < r_neighbor) {
                    hare[x_neighbor][y_neighbor].add(hare[col][row].get(a));
                    hare[col][row].remove(a);
                }
            }
        }
    }

    public void moveLynx(int col, int row, int cols, int rows) {
        int i = row + (col * cols);
        for (int x = 0; x < tiles.get(i).neighbors.size(); x++) {
            int x_neighbor = tiles.get(i).neighbors.get(x).x;
            int y_neighbor = tiles.get(i).neighbors.get(x).y;

            //loop through all the lynx in lynx[][]
            for (int a = 0; a < lynx[col][row].size(); a++) {
                //ratio of two animals in the neighborhood
                int r_neighbor = hare[x_neighbor][y_neighbor].size() / lynx[x_neighbor][y_neighbor].size();
                int r = hare[col][row].size() / lynx[col][row].size();
                if (r < r_neighbor) {
                    lynx[x_neighbor][y_neighbor].add(lynx[col][row].get(a));
                    lynx[col][row].remove(a);
                }
            }
        }
    }

    @Override
    public void run() {
        simulate(total_years, cols, rows);
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        /**
        try{
            writeData("myData.csv");
        }catch(IOException e){
            throw new RuntimeException(e);
        }**/
    }
}