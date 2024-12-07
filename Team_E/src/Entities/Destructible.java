package Entities;
import java.awt.Point;

import Core.Hitbox;

//파괴 가능한 객체(Destructible) 클래스
//Entity를 상속받아 체력, 히트박스, 크기 등을 관리
public abstract class Destructible extends Entity {

	private int health; // 객체의 체력
    private Hitbox hitbox; // 충돌 감지를 위한 히트박스
    private int width, height; // 객체의 너비와 높이

    // 생성자: 객체의 팀, 타입, 진화 단계를 받아 초기화
    public Destructible(int teamSide, int type, int evolution) {
        super(teamSide, type, evolution);
        if (type == TOWER_TYPE) {
            this.health = TOWER_HEALTH; // 타워의 초기 체력
            this.width = TOWER_WIDTH; // 타워의 너비
            this.height = TOWER_HEIGHT; // 타워의 높이

        } else {
            this.width = CREATURE_WIDTH; // 생명체의 기본 너비
            this.height = CREATURE_HEIGHT; // 생명체의 기본 높이

            // 타입에 따라 체력을 설정
            switch (type) {
                case FIRST_TYPE:
                    this.health = FIRST_HEALTH + (evolution * FIRST_MULTIPLIER);
                    break;

                case SECOND_TYPE:
                    this.health = SECOND_HEALTH + (evolution * SECOND_MULTIPLIER);
                    break;

                case THIRD_TYPE:
                    this.health = THIRD_HEALTH + (evolution * THIRD_MULTIPLIER);
                    break;

                case FOURTH_TYPE:
                    this.health = FOURTH_HEALTH + (evolution * FOURTH_MULTIPLIER);
                    break;
            }
        }
     // 히트박스 초기화 (객체의 위치와 크기를 기반으로 생성)
        this.hitbox = new Hitbox(new Point(this.getPosition()), this.width, this.height);
    }

    // 데미지를 받아 체력을 감소시키는 메서드
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    // 현재 체력을 반환하는 메서드
    public int getHealth() {
        return this.health;
    }

    // 히트박스를 반환하는 메서드
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    // 객체의 너비를 반환하는 메서드
    public int getWidth() {
        return this.width;
    }

    // 객체의 높이를 반환하는 메서드
    public int getHeight() {
        return this.height;
    }

}