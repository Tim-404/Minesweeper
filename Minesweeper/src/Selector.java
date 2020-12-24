import processing.core.PApplet;

public class Selector
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int ID;
    private boolean isPressed;
    private PApplet applet;

    public Selector(int x_, int y_, int w, int h, PApplet applet_) {
        x = x_;
        y = y_;
        width = w;
        height = h;
        ID = 0;
        isPressed = false;
        applet = applet_;
    }
    public Selector(int x_, int y_, int w, int h, int id, PApplet applet_) {
        this(x_, y_, w, h, applet_);
        ID = id;
    }

    public int getID() {
        return ID;
    }
    public boolean checkPressed() {
        isPressed = applet.mouseX >= x && applet.mouseX <= x + width && applet.mouseY >= y && applet.mouseY <= y + height;
        return isPressed;
    }
    public void unpress() {
        isPressed = false;
    }
    public void draw() {

        // Button design
        applet.noStroke();
        if (!isPressed) {
            applet.fill(255);
            applet.triangle(x, y, x + width, y, x, y + height);
            applet.fill(150);
            applet.triangle(x + width, y + height, x + width, y, x, y + height);
            applet.fill(225);
            applet.rect(x + width * (float) 0.1, y + height * (float) 0.1, width * (float) 0.8, height * (float) 0.8);
        } else {
            applet.fill(150);
            applet.triangle(x, y, x + width, y, x, y + height);
            applet.fill(255);
            applet.triangle(x + width, y + height, x + width, y, x, y + height);
            applet.fill(200);
            applet.rect(x + width * (float) 0.1, y + height * (float) 0.1, width * (float) 0.8, height * (float) 0.8);
        }

        // Text
        applet.fill(0);
        applet.textAlign(applet.CENTER, applet.CENTER);
        applet.textSize(height / 3);
        switch (ID) {   // Quit
            case 1:
                applet.text("Quit Game", x + width / 2, y + height / 2);
                break;
            case 2:     // Beginner
                applet.text("Beginner", x + width / 2, y + height / 2);
                break;
            case 3:     // Easy
                applet.text("Easy", x + width / 2, y + height / 2);
                break;
            case 4:     // Intermediate
                applet.text("Intermediate", x + width / 2, y + height / 2);
                break;
            case 5:     // Expert
                applet.text("Expert", x + width / 2, y + height / 2);
                break;
            default:
                applet.text("Reset", x + width / 2, y + height / 2);
        }
    }
}
