package Entities;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import Core.Hitbox;

//생명체(Creature) 클래스: 파괴 가능한 객체(Destructible)를 상속받아 생명체의 동작을 정의
public class Creature extends Destructible {

	private int speed; // 생명체의 이동 속도
    private Hitbox rangebox; // 생명체의 공격 범위를 나타내는 히트박스
    private int damage; // 생명체의 공격력
    private double goldFromKill; // 이 생명체를 처치했을 때 얻는 골드
    private long attackSpeed; // 공격 속도 (밀리초 단위)
    private int currentActivity = IDLE_ACTIVITY; // 현재 상태(대기, 이동, 공격 등)
    private long timeStartedAttack; // 마지막 공격 시작 시간
    private Destructible currentTarget; // 현재 공격 타겟

    private BufferedImage[] moveSprites; // 이동 애니메이션 스프라이트 배열
    private BufferedImage[] attackSprites; // 공격 애니메이션 스프라이트 배열
    private BufferedImage currentSprite; // 현재 표시 중인 스프라이트
    private int currentMoveIndex; // 현재 이동 스프라이트 인덱스
    private int currentAttackIndex; // 현재 공격 스프라이트 인덱스
    private boolean idling = false; // 생명체가 대기 중인지 여부

    // 생성자: 생명체를 초기화
	public Creature(int teamSide, int type, int evolution, BufferedImage[] move, BufferedImage[] attack) {
		super(teamSide, type, evolution); // 부모 클래스(Destructible) 생성자 호출
		this.moveSprites = move;
		this.attackSprites = attack;
		this.currentSprite = this.moveSprites[INITIAL_SPRITE_INDEX];
		this.currentMoveIndex = INITIAL_SPRITE_INDEX;
		int range = 0;

		 // 타입에 따라 스탯 설정
		switch (type) {
			case FIRST_TYPE:
				this.damage = FIRST_ATTACK + (evolution * FIRST_MULTIPLIER);
				this.speed = FIRST_SPEED * this.getTeamSide();
				this.attackSpeed = FIRST_ATTACK_SPEED;
				this.goldFromKill = FIRST_KILL_GOLD;
				range = FIRST_RANGE;
				break;

			case SECOND_TYPE:
				this.damage = SECOND_ATTACK + (evolution * SECOND_MULTIPLIER);
				this.speed = SECOND_SPEED * this.getTeamSide();
				this.attackSpeed = SECOND_ATTACK_SPEED;
				this.goldFromKill = SECOND_KILL_GOLD;
				range = SECOND_RANGE;
				break;

			case THIRD_TYPE:
				this.damage = THIRD_ATTACK + (evolution * THIRD_MULTIPLIER);
				this.speed = THIRD_SPEED * this.getTeamSide();
				this.attackSpeed = THIRD_ATTACK_SPEED;
				this.goldFromKill = THIRD_KILL_GOLD;
				range = THIRD_RANGE;
				break;

			case FOURTH_TYPE:
				this.damage = FOURTH_ATTACK + (evolution * FOURTH_MULTIPLIER);
				this.speed = FOURTH_SPEED * this.getTeamSide();
				this.attackSpeed = FOURTH_ATTACK_SPEED;
				this.goldFromKill = FOURTH_KILL_GOLD;
				range = FOURTH_RANGE;
				break;
		}

		// 팀에 따라 공격 범위 설정
		if (teamSide == LEFT_TEAM) {
			Point point = new Point(this.getPosition().x + this.getWidth(), this.getPosition().y);
			this.rangebox = new Hitbox(point, range, range);
		} else {
			Point point = new Point(this.getPosition().x - range, this.getPosition().y);
			this.rangebox = new Hitbox(point, range, range);
		}
	}

	// 공격 동작
	public void attack(Destructible target) {
		this.currentTarget = target;
		if (this.currentAttackIndex == LAST_ATTACK_SPRITE) {
			this.currentAttackIndex = FIRST_SPRITE;
		} else {
			this.currentAttackIndex++;
		}
		this.currentSprite = attackSprites[currentAttackIndex];

		target.takeDamage(this.damage); // 타겟에 데미지를 입힘
        this.timeStartedAttack = System.currentTimeMillis(); // 공격 시작 시간 갱신
	}

	 // 이동 동작
	public void move() {
		if (this.currentMoveIndex == LAST_MOVE_SPRITE) {
			this.currentMoveIndex = FIRST_SPRITE;
		} else {
			this.currentMoveIndex++;
		}
		this.currentSprite = moveSprites[currentMoveIndex];
		this.updatePositions(); // 히트박스와 위치 업데이트
	}

	// 생명체의 위치 및 히트박스 업데이트
    public void updatePositions() {
        Point creaturePos = this.getPosition();
        Point hitboxPos = this.getHitbox().getPosition();
        Point rangeboxPos = this.rangebox.getPosition();

        // 위치를 이동 속도만큼 갱신
        creaturePos.setLocation(creaturePos.x + this.speed, creaturePos.y);
        hitboxPos.setLocation(hitboxPos.x + this.speed, hitboxPos.y);
        rangeboxPos.setLocation(rangeboxPos.x + this.speed, rangeboxPos.y);

        this.setPosition(creaturePos);
        this.getHitbox().update(hitboxPos);
        this.getRangebox().update(rangeboxPos);
    }
    
    // 자신 앞에 있는 우호적인 생명체 반환
	public Creature getFriendlyAhead(Player self) {
		LinkedList<Creature> creatures = self.getCreatures();
		int thisIndex = creatures.indexOf(this);
		return creatures.get(thisIndex - 1);
	}

	// 생명체를 화면에 그리기
	public void draw(Graphics g) {
		if (this.idling) {
			this.currentSprite = this.moveSprites[IDLE_SPRITE];
		}
		g.drawImage(this.currentSprite, this.getPosition().x, this.getPosition().y, null);
	}

	// Getter 및 Setter 메서드
	public int getDamage() {
		return this.damage;
	}

	public Destructible getCurrentTarget() {
		return this.currentTarget;
	}

	public void setCurrentTarget(Destructible target) {
		this.currentTarget = target;
	}

	public long getTimeStartedAttack() {
		return this.timeStartedAttack;
	}

	public void setTimeStartedAttack(long startTime) {
		this.timeStartedAttack = startTime;
	}

	public int getSpeed() {
		return this.speed;
	}

	public Hitbox getRangebox() {
		return this.rangebox;
	}

	public BufferedImage[] getMoveSprites() {
		return this.moveSprites;
	}

	public BufferedImage[] getAttackSprites() {
		return this.attackSprites;
	}

	public BufferedImage getCurrentSprite() {
		return this.currentSprite;
	}

	public void setCurrentSprite(BufferedImage currentSprite) {
		this.currentSprite = currentSprite;
	}

	public void setCurrentSpriteIndex(int currentSpriteIndex) {
		this.currentMoveIndex = currentSpriteIndex;
	}

	public double getGoldFromKill() {
		return goldFromKill;
	}

	public long getAttackSpeed() {
		return attackSpeed;
	}

	public int getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(int currentActivity) {
		this.currentActivity = currentActivity;
	}

	public int getCurrentMoveIndex() {
		return currentMoveIndex;
	}

	public void setCurrentMoveIndex(int currentMoveIndex) {
		this.currentMoveIndex = currentMoveIndex;
	}

	public int getCurrentAttackIndex() {
		return currentAttackIndex;
	}

	public void setCurrentAttackIndex(int currentAttackIndex) {
		this.currentAttackIndex = currentAttackIndex;
	}

	public boolean getIdling() {
		return idling;
	}

	public void setIdling(boolean idling) {
		this.idling = idling;
		if (idling) {
			this.setCurrentSprite(this.getMoveSprites()[HEAD]);
		}
	}

}