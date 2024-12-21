package Entities;
import java.awt.Point;

import Core.Hitbox;

public abstract class Destructible extends Entity {

	protected int health;
	protected int maxHealth;
    private Hitbox hitbox;
    private int width, height;

    public Destructible(int teamSide, int type, int evolution) {
        super(teamSide, type, evolution);
        if (type == TOWER_TYPE) {
            this.maxHealth = TOWER_HEALTH; // 타워의 최대 체력 설정
            this.width = TOWER_WIDTH;
            this.height = TOWER_HEIGHT;
        } else {
            this.width = CREATURE_WIDTH;
            this.height = CREATURE_HEIGHT;

            // 유닛 타입에 따른 최대 체력 계산
            switch (type) {
                case FIRST_TYPE:
                    this.maxHealth = FIRST_HEALTH + (evolution * FIRST_MULTIPLIER);
                    break;
                case SECOND_TYPE:
                    this.maxHealth = SECOND_HEALTH + (evolution * SECOND_MULTIPLIER);
                    break;
                case THIRD_TYPE:
                    this.maxHealth = THIRD_HEALTH + (evolution * THIRD_MULTIPLIER);
                    break;
                case FOURTH_TYPE:
                    this.maxHealth = FOURTH_HEALTH + (evolution * FOURTH_MULTIPLIER);
                    break;
            }
        }

        // 초기 체력은 최대 체력으로 설정
        this.health = this.maxHealth;

        // 히트박스 초기화
        this.hitbox = new Hitbox(new Point(this.getPosition()), this.width, this.height);
    }

    public void takeDamage(int damage) {
        if (this.health > 0) {
            this.health -= damage;
            System.out.println(this.getClass().getSimpleName() + " 데미지: " + damage + " 현재 체력: " + this.health);

            if (this.health <= 0) {
                this.health = 0; // 체력을 0으로 고정
                System.out.println(this.getClass().getSimpleName() + " 파괴됨.");
            }
        }
    }



    // 현재 체력 반환
    public int getHealth() {
        return this.health;
    }

    // 최대 체력 반환
    public int getMaxHealth() {
        return this.maxHealth;
    }

    // 히트박스 반환
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    // 객체의 너비 반환
    public int getWidth() {
        return this.width;
    }

    // 객체의 높이 반환
    public int getHeight() {
        return this.height;
    }
 // 객체가 파괴되었는지 확인
    public boolean isDestroyed() {
        return this.health <= 0;
    }

    
}
