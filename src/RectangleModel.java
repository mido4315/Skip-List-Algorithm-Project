import java.awt.Rectangle;

@SuppressWarnings("serial")
public class RectangleModel extends Rectangle {

    public RectangleModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public String toString() {
        return this.x + ", " + this.y + ", " + this.width + ", " + this.height;
    }
}
