package Entities;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import Core.Hitbox;

//터렛(Turret) 클래스
//- Entity를 상속받아 터렛의 동작(공격, 그리기, 상태 관리)을 처리
public class Turret extends Entity {
	private int goldFromSell; // 터렛 판매 시 얻는 골드
    private int damage; // 터렛의 공격력
    private long attackSpeed; // 터렛의 공격 속도 (밀리초 단위)
    private Hitbox rangebox; // 터렛의 공격 범위 히트박스
    private long timeStartedAttack; // 마지막 공격 시작 시간
    private Destructible currentTarget; // 현재 공격 중인 대상
    private boolean idling; // 터렛이 대기 상태인지 여부

    private BufferedImage currentSprite; // 현재 터렛 스프라이트
    private BufferedImage projectileSprite; // 터렛의 투사체 스프라이트
    private LinkedList<Projectile> projectiles = new LinkedList<Projectile>(); // 발사된 투사체 리스트

    // 생성자: 터렛 초기화
    public Turret(int teamSide, int turretSpot, int evolution, BufferedImage currentSprite,
            BufferedImage projectileSprite) {
        super(teamSide, TURRET_TYPE, evolution); // 부모 클래스(Entity) 생성자 호출
        this.goldFromSell = BASE_GOLD_SELL + (evolution * TURRET_UPGRADE); // 판매 시 얻는 골드 계산
        this.attackSpeed = TURRET_ATTACK_SPEED; // 공격 속도 설정
        this.currentSprite = currentSprite; // 터렛 스프라이트 설정
        this.projectileSprite = projectileSprite; // 투사체 스프라이트 설정
        this.damage = TURRET_ATTACK + (evolution * TURRET_UPGRADE); // 터렛의 공격력 계산

        // 터렛 위치 및 히트박스 초기화
		switch (turretSpot) {
			case FIRST_TURRET:
				if (teamSide == LEFT_TEAM) {
					this.setPosition(LEFT_FIRST_TURRET_POS);
					this.rangebox = new Hitbox(new Point(this.getPosition().x + TURRET_WIDTH, this.getPosition().y),
							TURRET_RANGE, TURRET_RANGE);
				} else {
					this.setPosition(RIGHT_FIRST_TURRET_POS);
					this.rangebox = new Hitbox(new Point(this.getPosition().x - TURRET_RANGE, this.getPosition().y),
							TURRET_RANGE, TURRET_RANGE);
				}
				break;

			case SECOND_TURRET:
				if (teamSide == LEFT_TEAM) {
					this.setPosition(LEFT_SECOND_TURRET_POS);
					this.rangebox = new Hitbox(new Point(this.getPosition().x + TURRET_WIDTH, this.getPosition().y),
							TURRET_RANGE, TURRET_RANGE);
				} else {
					this.setPosition(RIGHT_SECOND_TURRET_POS);
					this.rangebox = new Hitbox(new Point(this.getPosition().x - TURRET_RANGE, this.getPosition().y),
							TURRET_RANGE, TURRET_RANGE);
				}
				break;

			case THIRD_TURRET:
				if (teamSide == LEFT_TEAM) {
					this.setPosition(LEFT_THIRD_TURRET_POS);
					this.rangebox = new Hitbox(new Point(this.getPosition().x + TURRET_WIDTH, this.getPosition().y),
							TURRET_RANGE, TURRET_RANGE);
				} else {
					this.setPosition(RIGHT_THIRD_TURRET_POS);
					this.rangebox = new Hitbox(new Point(this.getPosition().x - TURRET_RANGE, this.getPosition().y),
							TURRET_RANGE, TURRET_RANGE);
				}
				break;
		}
	}

    // 공격 동작: 투사체를 생성하고 공격 타겟 설정
	public void attack(Destructible target) {
		this.currentTarget = target; // 공격 타겟 설정
        Point projPos; // 투사체 생성 위치
        
        // 팀 방향에 따라 투사체 위치 설정
		if (this.getTeamSide() == LEFT_TEAM) {
			projPos = new Point(this.getPosition().x + TURRET_WIDTH, this.getPosition().y);
		} else {
			projPos = new Point(this.getPosition());
		}
		
		// 투사체 생성 및 리스트에 추가
		Projectile projectile = new Projectile(this.getTeamSide(), TURRET_PROJECTILE, this.getDamage(), projPos,
				projectileSprite);
		this.projectiles.add(projectile);
		
		// 공격 시작 시간 업데이트
		this.setTimeStartedAttack(System.currentTimeMillis());
	}

	// 터렛 및 투사체를 화면에 그리기
	public void draw(Graphics g) {
		// 터렛 스프라이트 그리기
		g.drawImage(this.currentSprite, this.getPosition().x, this.getPosition().y, null);
		// 투사체 그리기
		for (int i = 0; i < this.projectiles.size(); i++) {
			this.projectiles.get(i).draw(g);
		}
	}

	// Getter 및 Setter 메서드
    public int getGoldFromSell() {
        return this.goldFromSell; // 판매 시 얻는 골드 반환
    }

    public long getTimeStartedAttack() {
        return this.timeStartedAttack; // 마지막 공격 시작 시간 반환
    }

    public void setTimeStartedAttack(long timeStarted) {
        this.timeStartedAttack = timeStarted; // 마지막 공격 시작 시간 설정
    }

    public Destructible getCurrentTarget() {
        return this.currentTarget; // 현재 공격 타겟 반환
    }

    public void setCurrentTarget(Destructible target) {
        this.currentTarget = target; // 현재 공격 타겟 설정
    }

    public double getAttackSpeed() {
        return this.attackSpeed; // 공격 속도 반환
    }

    public Hitbox getRangebox() {
        return this.rangebox; // 공격 범위 히트박스 반환
    }

	public LinkedList<Projectile> getProjectiles() {
		return this.projectiles; // 투사체 리스트 반환
	}

	public int getDamage() {
		return this.damage; // 터렛의 공격력 반환
	}

	public boolean getIdling() {
		return this.idling; // 대기 상태 반환
	}

	public void setIdling(boolean state) {
		this.idling = state; // 대기 상태 설정
	} 

	@Override
	public BufferedImage getCurrentSprite() {
		return this.currentSprite; // 현재 스프라이트 반환
	}

}