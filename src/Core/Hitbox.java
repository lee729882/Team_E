package Core;
import java.awt.Point;
import java.awt.Rectangle;

public class Hitbox {

    private Point position;
    private int height, width;
    private Rectangle boundingBox; // rectangle that is used for collision detection

    public Hitbox(Point position, int width, int height) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null in Hitbox constructor.");
        }

    	
        this.position = position;
        this.width = width;
        this.height = height;
        this.boundingBox = new Rectangle(position.x, position.y, width, height);
    }

    public boolean checkCollide(Hitbox other) {
        return this.boundingBox.intersects(other.getBoundingBox());
    }

    public void update(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null in update method.");
        }
        this.position = point;
        this.boundingBox.setLocation(this.position);
    }
    
    
    // -----------------------------------------
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null in setPosition method.");
        }
        this.position = position;
        this.boundingBox.setLocation(this.position);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(boundingBox);
    }

}
