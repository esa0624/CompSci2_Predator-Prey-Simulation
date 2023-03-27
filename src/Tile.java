import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Here is a bare-bones class that holds some relevant information for each tile in your world.
 * It is intended to be used as a starting point - depending on your design choices, you may decide
 * that you want different attributes, to add some more specific methods, or to scrap the whole thing.
 *
 * The biggest reason that I am providing this file is for the "initializeTiles" function, which steps
 * through the reasonably straight-forward (but tedious) code for going from a 2D array of chars (such
 * as you may read from a file) and actually building objects in the same fashion as those from the
 * previous homework.
 */

public class Tile {
    // we'll need to store the Animals that live in this particular tile
    // two separate lists vs. one combined list is up to you!
    ArrayList<Animal> preds;
    ArrayList<Animal> preys;
    // in keeping with the Graphs HW, we can directly store the neighbor tiles
    ArrayList<Tile> neighbors;
    // there are different ways to store all of your tiles in a Sim class a 2D array is a
    // natural choice, but tedious to loop over and manage.
    // on the other hand, an ArrayList<Tile> is very convenient, and as long as we have the
    // adjacency information directly captured in each tile we don't need the 2D structure
    // to manage movement.
    // however, we may still want to know something about the 2D position in order to draw
    // our tiles in a GUI; therefore we can store the (x,y) coordinate of each tile
    int x;
    int y;
    // finally, the type of each tile is useful to know for logic and for rendering
    char type;
    public Tile(char type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
        // initialize empty ArrayLists
        neighbors = new ArrayList<>();
        // we'll need to populate our tiles somewhere! Sim?
        preds = new ArrayList<>();
        preys = new ArrayList<>();
    }

    // a check to see what counts as passable; easy to update if we add new tile types!
    public static boolean isPassable(char tileType) {
        if (tileType == 'G') {
            return true;
        } else {
            return false;
        }
    }

    // helper function to track where the Tile for a (row, col) has ended up in our 1D ArrayList
    public static int convert2d(int row, int col, int nCols) {
        // convince yourself that this is correct!
        return (row * nCols) + col;
    }

    // helper function to simplify adding an edge
    public static void addNeighbors(ArrayList<Tile> tiles, int indexA, int indexB) {
        Tile tileA = tiles.get(indexA);
        Tile tileB = tiles.get(indexB);
        // note that we only add the edge from A to B!
        // as the initialization function proceeds, we will get to tileB, and it will ask to add an edge to tileA
        tileA.neighbors.add(tileB);
    }

    public static ArrayList<Tile> initializeTiles(char[][] worldChars) {
        // initialize a list to store all of our world's tiles
        ArrayList<Tile> tiles = new ArrayList<>();
        // assuming a square world, we can get the number of columns by checking the length of the first row
        int nRows = worldChars.length;
        int nCols = worldChars[0].length;
        // first, we must create all of our Tile objects based on the terrain types
        // after we're done creating the objects, we can add them directly to each other's neighbors lists
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                tiles.add(new Tile(worldChars[row][col], row, col));
            }
        }
        // okay, now lets loop over our grid and add adjacency info for neighboring passable tiles
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                // we only need to compute adjacency lists for passable tiles
                if (isPassable(worldChars[row][col])) {
                    int curTileIndex = convert2d(row, col, nCols);
                    // check the tile above (minus one row)
                    if (row > 0 && isPassable(worldChars[row - 1][col])) {
                        addNeighbors(tiles, curTileIndex, convert2d(row - 1, col, nCols));
                    }
                    // check the tile below (plus one row)
                    if (row < nRows - 1 && isPassable(worldChars[row + 1][col])) {
                        addNeighbors(tiles, curTileIndex, convert2d(row + 1, col, nCols));
                    }
                    // check the tile to the left (minus one col)
                    if (col > 0 && isPassable(worldChars[row][col - 1])) {
                        addNeighbors(tiles, curTileIndex, convert2d(row, col - 1, nCols));
                    }
                    // check the tile to the right (plus one col)
                    if (col < nCols - 1 && isPassable(worldChars[row][col + 1])) {
                        addNeighbors(tiles, curTileIndex, convert2d(row, col + 1, nCols));
                    }
                }
            }
        }
        return tiles;
    }

    //不確定
    public ArrayList<Tile> getNeighbors() {
        ArrayList<Tile> arr = new ArrayList<>();
        for (int i = 0; i < neighbors.size(); i++) {
            arr.add(neighbors.get(i));
        }
        return arr;
    }

    public static void main(String[] args) {
        // proof of concept test!
        char[][] exampleTiles = {{'G', 'G', 'G', 'R', 'R', 'M'},
                {'G', 'G', 'G', 'G', 'R', 'M'},
                {'G', 'G', 'G', 'G', 'R', 'M'},
                {'G', 'G', 'G', 'G', 'R', 'M'},
                {'G', 'G', 'G', 'G', 'R', 'M'},
                {'G', 'G', 'G', 'R', 'M', 'M'}};
        ArrayList<Tile> tiles = initializeTiles(exampleTiles);
        // try dropping a debugger breakpoint on the print statement to inspect the resulting tile list
        for (Tile neighbor : tiles.get(7).neighbors) {
            System.out.println(neighbor.x);
            System.out.println(neighbor.y);
        }

        System.out.println(tiles.get(2).neighbors.size());
        System.out.println("Finished running the example!");
    }

}




