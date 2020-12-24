import processing.core.PApplet;

public class Minesweeper extends PApplet
{
    private int page;   // Page 1 = start screen, 2 = beginner, 3 = easy, 4 = intermediate, 5 = expert
    private Selector[] options; // For the start screen
    private Selector reset;     // For the game
    private Selector exit;
    private Selector s1;
    private Selector s2;
    private Grid g;
    private Tile t;

    public static void main(String[] args) {
        PApplet.main("Minesweeper");
    }

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        surface.setTitle("Minesweeper");
        page = 1;
        options = new Selector[5];
        options[0] = new Selector(50, 200, 150, 50, 2, this);
        options[1] = new Selector(50, 300, 150, 50, 3, this);
        options[2] = new Selector(300, 200, 150, 50, 4, this);
        options[3] = new Selector(300, 300, 150, 50, 5, this);
        options[4] = new Selector(175, 400, 150, 50, 1, this);
        noLoop();
    }

    public void draw() {
        if (page == 1) {
            textSize(70);
            textAlign(CENTER);
            fill(175);
            text("Minesweeper", 260, 130);
            fill(0, 250, 0);
            text("Minesweeper", 250, 125);

            for (Selector s : options) {
                s.draw();
            }
        } else {
            if (g.isOpened && !g.isFinished && !g.isDestroyed && g.timer < 1000) {
                g.timer += (float) 1 / frameRate;
            }
            fill(240);
            rect(0, 0, width, g.getShift());
            reset.draw();
            exit.draw();
            fill(0);
            rect(10, g.getShift() / 4, 70, g.getShift() / 2);  // Number of flags to place
            fill(200, 0, 0);
            textAlign(CENTER, CENTER);
            textSize(g.getShift() / 3);
            text(g.getNumMines(), 45, g.getShift() / 2);
            fill(0);
            rect(100, 70, 35, 7);
            stroke(0);
            strokeWeight(2);
            line(125, 70, 125, 30);
            noStroke();
            fill(200, 0, 0);
            triangle(125, 30, 125, 55, 100, 45);

            fill(0);
            rect(width - 80, g.getShift() / 4, 70, g.getShift() / 2);  // Timer
            fill(200, 0, 0);
            text((int)g.timer + "", width - 45, g.getShift() / 2);

            g.draw();
        }
    }
    public void screenChange() {
        if (page == 1) {
            g = null;
            reset = null;
            exit = null;
            surface.setSize(500, 500);
            background(200);
            options = new Selector[5];
            options[0] = new Selector(50, 200, 150, 50, 2, this);
            options[1] = new Selector(50, 300, 150, 50, 3, this);
            options[2] = new Selector(300, 200, 150, 50, 4, this);
            options[3] = new Selector(300, 300, 150, 50, 5, this);
            options[4] = new Selector(175, 400, 150, 50, 1, this);
            noLoop();
            draw();
        } else {
            options = null;
            switch (page) {
                case 2:
                    surface.setSize(450, 550);
                    g = new Grid(9, 9, 10, 100,this);
                    break;
                case 3:
                    surface.setSize(800, 900);
                    g = new Grid(16, 16, 40, 100,this);
                    break;
                case 4:
                    surface.setSize(840, 940);
                    g = new Grid(21, 21, 65, 100,this);
                    break;
                case 5:
                    surface.setSize(990, 630);
                    g = new Grid(30, 16, 99, 100,this);
            }
            reset = new Selector(width / 2 - 50, 5, 100, 40, this);
            exit = new Selector(width / 2 - 50, 55, 100, 40, 1, this);
            loop();
        }
    }
    public void mousePressed() {
        if (page == 1) {
            if (mouseButton == LEFT) {
                for (Selector s_ : options) {
                    if (s_.checkPressed()) {
                        s1 = s_;
                        redraw();
                        break;
                    }
                }
            }
        } else {
            t = g.getTile();
            if (mouseButton == LEFT) {
                if (t != null) {
                    t.press();
                } else if (reset.checkPressed()) {
                    s1 = reset;
                } else if (exit.checkPressed()) {
                    s1 = exit;
                }
            }
        }
    }
    public void mouseReleased() {
        if (page == 1) {
            if (mouseButton == LEFT) {
                for (Selector s_ : options) {
                    if (s_.checkPressed()) {
                        s2 = s_;
                        if (s1 == s2) {
                            if (s1.getID() == 1) {
                                exit();
                            } else {
                                page = s1.getID();
                                screenChange();
                            }
                        }
                        s1.unpress();
                        s2.unpress();
                    }
                }
            }
        } else {
            if (t != null) {
                t.unpress();
                if (t == g.getTile()) {
                    if (mouseButton == LEFT) {
                        g.revealTile();
                    }
                    else {
                        g.flagTile(g);
                    }
                }
            }
            if (reset.checkPressed()) {
                s2 = reset;
                if (s1 == s2) {
                    g.replaceGrid();
                }
            }
            reset.unpress();
            exit.unpress();
            if (exit.checkPressed()) {
                s2 = exit;
                if (s1 == s2) {
                    page = 1;
                    screenChange();
                }
            }
        }
        redraw();
    }
}
