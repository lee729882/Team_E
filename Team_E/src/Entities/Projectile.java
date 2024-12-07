package Entities;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import Core.EntityConstants;
import Core.GameConstants;
import Core.Hitbox;

//투사체(Projectile) 클래스
//- 게임 내에서 발사체의 동작(이동, 충돌, 그리기 등)을 처리
public class Projectile implements GameConstants, EntityConstants {

	private Hitbox hitbox; // 투사체의 히트박스 (충돌 감지)
    private Point position; // 현재 위치
    private int damage; // 투사체의 공격력
    private int speed; // 투사체의 이동 속도
    private BufferedImage currentSprite; // 현재 스프라이트 (이미지)
    private Point initialPos; // 투사체의 초기 위치 (발사 시점)

    // 생성자: 투사체의 속성을 초기화
    public Projectile(int teamSide, int type, int damage, Point position, BufferedImage sprite) {
    	this.position = position; // 초기 위치 설정
        this.initialPos = new Point(this.position); // 초기 위치 저장
        this.currentSprite = sprite; // 투사체의 이미지 설정
        this.damage = damage; // 투사체의 공격력 설정
        this.speed = PROJECTILE_SPEED * teamSide; // 팀 방향에 따른 속도 설정

        // 타입에 따라 히트박스 크기 설정
        if (type == TURRET_PROJECTILE) {
            this.hitbox = new Hitbox(this.position, MISSILE_WIDTH, MISSILE_HEIGHT);

        } else if (type == SECOND_PROJECTILE) {
            this.hitbox = new Hitbox(this.position, ARROW_WIDTH, ARROW_HEIGHT);

        } else {
            this.hitbox = new Hitbox(this.position, FRUIT_WIDTH, FRUIT_WIDTH);
        }
    }

    // 투사체를 이동시키는 메서드
    public void move() {
        this.position.x += this.speed; // 속도만큼 x좌표를 이동
        this.hitbox.update(this.position); // 히트박스 위치 업데이트
    }
    
    public boolean checkCollide(Destructible enemy) {
        return this.hitbox.checkCollide(enemy.getHitbox());
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