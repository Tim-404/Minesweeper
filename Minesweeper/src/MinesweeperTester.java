import processing.core.PApplet;

public class MinesweeperTester extends PApplet
{
    private Grid g;
    private Tile t;

    public static void main(String[] args) {
        PApplet.main("MinesweeperTester");
    }

    public void settings() {
        size(1875, 1000);
    }

    public void setup() {
        g = new Grid(30, 16, 99, this);
    }

    public void draw() {
        g.draw();
    }
    public void mousePressed() {
        t = g.getTile();
        if (mouseButton == LEFT) {
            t.press();
        }
    }
    public void mouseReleased() {
        if (t == g.getTile()) {
            if (mouseButton == LEFT) {
                g.revealTile();
            } else {
                g.flagTile(g);
            }
        }
        t.unpress();
    }
}
