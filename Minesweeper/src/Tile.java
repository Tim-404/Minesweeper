import processing.core.PApplet;

public class Tile
{
    private int x;
    private int y;
    private int risk;
    private boolean isCovered;
    private boolean isFlagged;
    private boolean isPressed;
    private boolean isFlaggedWrong;
    private boolean isDetonated;
    private Tile[] neighbors;
    private PApplet applet;

    public Tile(int x_, int y_, PApplet applet_) {
        x = x_;
        y = y_;
        risk = 0;
        isCovered = true;   // Default settings
        isFlagged = false;  // Will change with player interaction
        isPressed = false;
        isFlaggedWrong = false;
        isDetonated = false;
        applet = applet_;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isCovered() {
        return isCovered;
    }
    public void uncover() { isCovered = false; }
    public boolean isFlagged() { return isFlagged; }
    public void isWrong() { isFlaggedWrong = true; }

    // Risk of -1 is a bomb
    public void setBomb() {
        risk = -1;
    }

    // Changes boolean for background fx, only applies to bombs
    public void detonate() {
        isDetonated = true;
    }

    // Risk is the number of bombs surrounding that tile, including corners
    public void setRisk(Grid grid) {
        setNeighbors(grid);
        if (risk > -1) {
            for (Tile square : neighbors) {
                if (square.getRisk() == -1) {
                    risk++;
                }
            }
        }
    }

    // Checks surrounding tiles in grid
    // Count is not 8 by default because border tiles are different
    private void setNeighbors(Grid grid) {
        int count = 0;
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                if ((Math.abs(x - grid.getTiles()[i][j].getX()) <= 1 && Math.abs(y - grid.getTiles()[i][j].getY()) <= 1) && !(x == grid.getTiles()[i][j].getX() && y == grid.getTiles()[i][j].getY())) {
                    count++;
                }
            }
        }
        neighbors = new Tile[count];
        count = 0;
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                if ((Math.abs(x - grid.getTiles()[i][j].getX()) <= 1 && Math.abs(y - grid.getTiles()[i][j].getY()) <= 1) && !(x == grid.getTiles()[i][j].getX() && y == grid.getTiles()[i][j].getY())) {
                    neighbors[count] = grid.getTiles()[i][j];
                    count++;
                }
            }
        }
    }
    public int getRisk() {
        return risk;
    }

    // Keeps player interaction from uncovering a tile
    public void toggleFlag(Grid g) {
        if (!isFlagged && isCovered) {
            isFlagged = true;
            g.setNumMines(g.getNumMines() - 1);
        } else if (isFlagged){
            isFlagged = false;
            g.setNumMines(g.getNumMines() + 1);
        }
    }
    public void reveal() {
        if (!isFlagged) {
            if (!isCovered || risk == 0) {
                isCovered = false;
                if (flagCount() == risk) {
                    revealNeighbors();
                }
            } else {
                isCovered = false;
            }
        }
    }
    private int flagCount() {
        int flags = 0;
        for (Tile square : neighbors) {
            if (square.isFlagged) {
                flags++;
            }
        }
        return flags;
    }
    private void revealNeighbors() {
        for (Tile square : neighbors) {
            if (!square.isFlagged) {
                if (square.isCovered) {
                    square.isCovered = false;
                    if (square.risk == 0) {
                        square.revealNeighbors();
                    }
                }
            }
        }
    }
    public void draw(int tileSize, int shift) {
        if (isCovered) {
            if (isPressed) {
                press(tileSize, shift);
            } else {
                drawDefaultCovered(tileSize, shift);
                if (isFlagged) {    // Flag
                    applet.fill(0);
                    applet.rect((float) (x + 0.25) * tileSize, (float) (y + 0.75) * tileSize + shift, tileSize / (float) 2, tileSize / (float) 10);
                    applet.stroke(0);
                    applet.strokeWeight(2);
                    applet.line((float) (x + 0.65) * tileSize, (float) (y + 0.75) * tileSize + shift, (float) (x + 0.65) * tileSize, (float) (y + 0.15) * tileSize + shift);
                    applet.noStroke();
                    applet.fill(200, 0, 0);
                    applet.triangle((float) (x + 0.65) * tileSize, (float) (y + 0.15) * tileSize + shift, (float) (x + 0.65) * tileSize, (float) (y + 0.55) * tileSize + shift, (float) (x + 0.25) * tileSize, (float) (y + 0.35) * tileSize + shift);
                    if (isFlaggedWrong) {
                        applet.stroke(255, 0, 0);
                        applet.line(x * tileSize, y * tileSize + shift, (x + 1) * tileSize, (y + 1) * tileSize + shift);
                        applet.line((x + 1) * tileSize, y * tileSize + shift, x * tileSize, (y + 1) * tileSize + shift);
                    }
                }
            }
        } else {
            drawDefaultUncovered(tileSize, shift);
            applet.textSize(tileSize);
            switch (risk) {
                case 0:
                    break;
                case 1:
                    applet.fill(0, 0, 200);
                    drawNumber(tileSize, shift);
                    break;
                case 2:
                    applet.fill(0, 150, 0);
                    drawNumber(tileSize, shift);
                    break;
                case 3:
                    applet.fill(200, 0, 0);
                    drawNumber(tileSize, shift);
                    break;
                case 4:
                    applet.fill(100, 0, 100);
                    drawNumber(tileSize, shift);
                    break;
                case 5:
                    applet.fill(100, 70, 30);
                    drawNumber(tileSize, shift);
                    break;
                case 6:
                    applet.fill(0, 200, 200);
                    drawNumber(tileSize, shift);
                    break;
                case 7:
                    applet.fill(0);
                    drawNumber(tileSize, shift);
                    break;
                case 8:
                    applet.fill(75);
                    drawNumber(tileSize, shift);
                    break;
                default:
                    if (isDetonated) {
                        applet.stroke(100);
                        applet.strokeWeight(2);
                        applet.fill(250, 0, 0);
                        applet.rect(x * tileSize, y * tileSize + shift, tileSize, tileSize);
                    }
                    applet.stroke(0);
                    applet.strokeWeight(2);
                    applet.fill(0);
                    applet.ellipse((float)(x + 0.5) * tileSize, (float)(y + 0.5) * tileSize + shift, tileSize * (float)0.6, tileSize * (float)0.6);
                    applet.line((float)(x + 0.5) * tileSize, (float)(y + 0.1) * tileSize + shift, (float)(x + 0.5) * tileSize, (float)(y + 0.9) * tileSize + shift);
                    applet.line((float)(x + 0.1) * tileSize, (float)(y + 0.5) * tileSize + shift, (float)(x + 0.9) * tileSize, (float)(y + 0.5) * tileSize + shift);
                    applet.line((float)(x + 0.2) * tileSize, (float)(y + 0.2) * tileSize + shift, (float)(x + 0.8) * tileSize, (float)(y + 0.8) * tileSize + shift);
                    applet.line((float)(x + 0.2) * tileSize, (float)(y + 0.8) * tileSize + shift, (float)(x + 0.8) * tileSize, (float)(y + 0.2) * tileSize + shift);
                    applet.noStroke();
                    applet.fill(255);
                    applet.rect((float)(x + 0.6) * tileSize, (float)(y + 0.3) * tileSize + shift, tileSize * (float)0.1, tileSize * (float)0.1);
                    // opened bomb
            }
        }
    }
    private void drawDefaultCovered(int tileSize, int shift) {
        applet.noStroke();
        applet.fill(255);
        applet.triangle(x * tileSize, y * tileSize + shift, (x + 1) * tileSize, y * tileSize + shift, x * tileSize, (y + 1) * tileSize + shift);
        applet.fill(150);
        applet.triangle((x + 1) * tileSize, (y + 1) * tileSize + shift, (x + 1) * tileSize, y * tileSize + shift, x * tileSize, (y + 1) * tileSize + shift);
        applet.fill(225);
        applet.rect((float)(x + 0.1) * tileSize, (float)(y + 0.1) * tileSize + shift, tileSize * (float)0.8, tileSize * (float)0.8);
    }
    private void drawDefaultUncovered(int tileSize, int shift) {
        applet.stroke(100);
        applet.strokeWeight(2);
        applet.fill(175);
        applet.rect(x * tileSize, y * tileSize + shift, tileSize, tileSize);
    }
    private void drawNumber(int tileSize, int shift) {
        applet.textAlign(applet.CENTER, applet.CENTER);
        applet.text(risk, (float)(x + 0.5) * tileSize, (float)(y + 0.4) * tileSize + shift);
    }
    private void press(int tileSize, int shift) {
        if (isCovered && !isFlagged) {
            applet.noStroke();
            applet.fill(150);
            applet.triangle(x * tileSize, y * tileSize + shift, (x + 1) * tileSize, y * tileSize + shift, x * tileSize, (y + 1) * tileSize + shift);
            applet.fill(255);
            applet.triangle((x + 1) * tileSize, (y + 1) * tileSize + shift, (x + 1) * tileSize, y * tileSize + shift, x * tileSize, (y + 1) * tileSize + shift);
            applet.fill(200);
            applet.rect((float)(x + 0.1) * tileSize, (float)(y + 0.1) * tileSize + shift, tileSize * (float)0.8, tileSize * (float)0.8);
        }
    }
    public void press() {
        isPressed = true;
    }
    public void unpress() {
        isPressed = false;
    }
}
