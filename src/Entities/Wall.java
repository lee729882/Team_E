package Entities;

import Entities.Player;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import Core.Hitbox;

public class Wall {
    private Point position;
    private int width;
    private int height;
    private long duration; // 지속 시간 (밀리초)
    private boolean active;
    private Hitbox hitbox;
    

    public Wall(Point position, int width, int height, long duration) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.duration = duration;
        this.active = true;
        this.hitbox = new Hitbox(position, width, height);
        
        // 벽 지속 시간 타이머
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                active = false; // 지속 시간이 지나면 비활성화
                System.out.println("Wall expired.");
            }
        }, duration);
    }

    public void draw(Graphics g) {
        if (active) {
            g.setColor(Color.GRAY);
            g.fillRect(position.x, position.y, width, height);
            System.out.println("Wall created at: x = " + position.x + ", y = " + position.y + 
                    ", width = " + width + ", height = " + height);
        }
    }
    
    public void deactivate() {
        active = false;
        System.out.println("Wall deactivated.");
    }

    public boolean isActive() {
        return active;
    }

    public Point getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Hitbox getHitbox() {
        return this.hitbox;
    }
}
