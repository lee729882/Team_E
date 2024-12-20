package Entities;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Core.EntityConstants;
import Core.GameConstants;
import Core.Hitbox;

public class Projectile implements GameConstants, EntityConstants {

    private Hitbox hitbox;
    private Point position;
    private int damage;
    private int speed;
    private BufferedImage currentSprite;
    private Point initialPos;

    public Projectile(int teamSide, int type, int damage, Point position, BufferedImage sprite) {
        this.position = position;
        this.initialPos = new Point(this.position);
        this.currentSprite = sprite;
        this.damage = damage;
        this.speed = PROJECTILE_SPEED * teamSide;

        if (type == TURRET_PROJECTILE) {
            this.hitbox = new Hitbox(this.position, MISSILE_WIDTH, MISSILE_HEIGHT);

        } else if (type == SECOND_PROJECTILE) {
            this.hitbox = new Hitbox(this.position, ARROW_WIDTH, ARROW_HEIGHT);

        } else {
            this.hitbox = new Hitbox(this.position, FRUIT_WIDTH, FRUIT_WIDTH);
        }
    }

    public void move() {
        this.position.x += this.speed;
        this.hitbox.update(this.position);
    }

    public boolean checkCollide(Destructible enemy) {
        return this.hitbox.checkCollide(enemy.getHitbox());
    }

 // 새로운 벽과의 충돌 확인 메서드
    public boolean checkCollideWithWall(Wall wall) {
        // 벽의 히트박스와 투사체의 히트박스가 충돌하는지 확인
        return this.hitbox.getBoundingBox().intersects(
            new Rectangle(wall.getPosition().x, wall.getPosition().y, wall.getWidth(), wall.getHeight())
        );
    }
    
    public void draw(Graphics g) {
        g.drawImage(this.currentSprite, this.position.x, this.position.y, null);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public Point getPosition() {
        return this.position;
    }

    public Point getInitialPos() {
        return this.initialPos;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public BufferedImage getCurrentSprite() {
        return this.currentSprite;
    }

}
