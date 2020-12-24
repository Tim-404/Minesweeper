import processing.core.PApplet;

public class Grid
{
    private int width;
    private int height;
    private int shiftDown;
    private Tile[][] grid;
    private int NUM_MINES;
    private int numMinesLeft;
    private int tileSize;
    public boolean isOpened;
    public boolean isFinished;
    public boolean isDestroyed;
    public double timer;
    private PApplet applet;

    public Grid(int w, int h, int numMines_, PApplet applet_) {
        width = w;
        height = h;
        shiftDown = 0;
        grid = new Tile[width][height];
        NUM_MINES = numMines_;
        numMinesLeft = numMines_;
        isOpened = false;
        isFinished = false;
        isDestroyed = false;
        timer = 0;
        applet = applet_;
        tileSize = applet.width / width;
        setup(NUM_MINES);
    }
    public Grid(int w, int h, int numMines_, int shiftDown_, PApplet applet_) {
        this(w, h, numMines_, applet_);
        shiftDown = shiftDown_;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getShift() { return shiftDown; }
    public int getNumMines() { return numMinesLeft; }
    public void setNumMines(int numMines_) { numMinesLeft = numMines_; }
    private void setup(int numMines) {  // Main setup command
        setTiles();
        setMines(numMines);
        setNumbers();
        isOpened = false;
        numMinesLeft = NUM_MINES;
        isFinished = false;
        isDestroyed = false;
        timer = 0;
    }
    private void setTiles() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Tile(i, j, applet);
            }
        }
    }
    private void setMines(int numBombs) {
        for (int i = 0; i < numBombs; i++) {
            plantMine();
        }
    }

    // Plants mines, makes sure that a mine isn't placed on the same tile twice
    private void plantMine() {
        Tile t = grid[(int)(Math.random() * width)][(int)(Math.random() * height)];
        if (t.getRisk() == 0) {
            t.setBomb();
        } else {
            plantMine();
        }
    }

    // Counts number of mines surrounding each tile
    private void setNumbers() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j].setRisk(this);
            }
        }
    }
    public Tile[][] getTiles() {
        return grid;
    }
    public void revealTile() {
        if (!isFinished && !isDestroyed) {
            if (isOpened || getTile().getRisk() == 0) {
                getTile().reveal();
                isOpened = true;
            } else {    // Protects the first tile of the grid from being near a bomb
                replaceGrid();
                revealTile();
            }
            update();
        }
    }
    public void replaceGrid() {
        grid = new Tile[width][height];
        setup(NUM_MINES);
    }

    // Sweeps the grid to check for uncovered mines; kills player if mine is uncovered
    private void update() {
        int count = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!grid[i][j].isCovered()) {
                    count++;
                    if (grid[i][j].getRisk() == -1) {
                        isDestroyed = true;
                        grid[i][j].detonate();
                        revealMines();
                        break;
                    }
                }
            }
            if (isDestroyed) {
                break;
            }
        }
        if (count == width * height - NUM_MINES && !isDestroyed) {
            isFinished = true;
        }
    }
    private void revealMines() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j].getRisk() == -1 && !grid[i][j].isFlagged()) {
                    grid[i][j].uncover();
                }
                if (grid[i][j].isFlagged() && grid[i][j].getRisk() != -1) {
                    grid[i][j].isWrong();
                }
            }
        }
    }
    public void flagTile(Grid g) {
        getTile().toggleFlag(g);
    }
    public Tile getTile() {
        if (applet.mouseY >= shiftDown) {
            return grid[applet.mouseX / tileSize][(applet.mouseY - shiftDown) / tileSize];
        } else {
            return null;
        }
    }
    public void draw() {
        for (Tile[] row : grid) {
            for (Tile t : row) {
                t.draw(tileSize, shiftDown);
            }
        }
    }
}
