package Entities;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import Core.EntityConstants;
import Core.GameConstants;

import java.awt.Font;

public class Player implements GameConstants, EntityConstants {

	private Tower tower; // 플레이어의 타워 객체
    private int teamSide; // 팀 방향 (LEFT_TEAM 또는 RIGHT_TEAM)
    private LinkedList<Creature> creatures = new LinkedList<Creature>(); // 현재 생성된 생명체 목록
    private Queue<Creature> summonQueue = new LinkedList<Creature>(); // 소환 대기열
    private String userName; // 플레이어 이름
    private double gold; // 플레이어의 현재 골드

    private int currentEvolution = 0; // 현재 진화 단계
    private int evolutionCost; // 다음 진화에 필요한 비용

    // 스프라이트 (애니메이션 및 시각적 요소)
    private BufferedImage[][][] moveSprites; // 이동 스프라이트
    private BufferedImage[][][] attackSprites; // 공격 스프라이트
    private BufferedImage[][] projectileSprites; // 투사체 스프라이트
    private BufferedImage[] towerSprites; // 타워 스프라이트
    private BufferedImage[] turretSprites; // 터렛 스프라이트

	// 생성자: 팀, 이름, 스프라이트 정보 초기화
	public Player(int team, String name, BufferedImage[][][] move, BufferedImage[][][] attack,
			BufferedImage[][] projectiles, BufferedImage[] tower, BufferedImage[] turret) {
		this.teamSide = team;
		this.userName = name;
		this.moveSprites = move;
		this.attackSprites = attack;
		this.projectileSprites = projectiles;
		this.towerSprites = tower;
		this.turretSprites = turret;
		this.gold = START_GOLD; // 시작 골드 설정
		this.evolutionCost = EVOLVE_COST; // 초기 진화 비용 설정
		this.tower = new Tower(team, TOWER_TYPE, this.currentEvolution, this.towerSprites, this.turretSprites,
				projectileSprites[TURRET_PROJECTILES]);
		this.currentEvolution = STARTING_EVOLUTION; // 초기 진화 단계 설정
	}

	// 생명체를 소환 대기열에 추가
	public void queueCreature(int cost, int type) {
		if (this.summonQueue.size() < MAX_CREATURES_IN_QUEUE) { // 대기열 제한 확인
			this.gold -= cost; // 골드 차감
			Creature creature = null;
			BufferedImage projectileSprite = null;
			if (type == FIRST_TYPE || type == THIRD_TYPE) {
				creature = new Creature(teamSide, type, currentEvolution, moveSprites[type][currentEvolution],
						attackSprites[type][currentEvolution]);
			} else {
				// 원거리형 생명체 생성
				if (type == SECOND_TYPE) {
					projectileSprite = this.projectileSprites[SECOND_PROJECTILES][this.currentEvolution];
				} else {
					projectileSprite = this.projectileSprites[FOURTH_PROJECTILES][this.currentEvolution];
				}
				creature = new Ranged(teamSide, type, currentEvolution, moveSprites[type][currentEvolution],
						attackSprites[type][currentEvolution], projectileSprite);
			}
			this.summonQueue.add(creature); // 소환 대기열에 추가
		}
	}

	// 대기열에서 생명체를 소환
	public void summonCreature() {
		this.creatures.add(this.summonQueue.poll()); // 대기열에서 제거 후 리스트에 추가
	}

	// 진화를 시도
	public boolean evolve() {
		if (this.currentEvolution == 2) { // 최대 진화 단계 확인
			return false;
		}
		this.currentEvolution++; // 진화 단계 증가
		this.gold -= this.evolutionCost; // 골드 차감
		this.evolutionCost = this.currentEvolution * EVOLVE_MULTIPLIER * EVOLVE_COST; // 다음 진화 비용 계산
		this.tower.setCurrentSprite(towerSprites[this.currentEvolution]); // 타워 스프라이트 업데이트
		return true;
	}

	// 터렛 구매
	public void buyTurret(int cost, int spot) {
		this.gold -= cost; // 골드 차감
		this.tower.addTurret(spot); // 터렛 추가
	}

	// 터렛 판매
	public void sellTurret(int amount, int spot) {
		this.gold += amount; // 골드 추가
		this.tower.getTurrets().set(spot, null); // 해당 터렛 제거
	}

	// 죽은 생명체 제거 및 처치 보상 획득
	public int removeDeadCreatures() {
		int goldGained = 0;
		for (int i = HEAD; i < this.creatures.size(); i++) {
			if (this.creatures.get(i).getHealth() <= 0) { // 체력이 0 이하인 생명체 확인
				Creature creature = this.creatures.remove(i); // 제거
				goldGained += creature.getGoldFromKill(); // 처치 보상 골드 추가
			}
		}
		return goldGained;
	}

	// 게임 오버 상태 확인
	public boolean checkGameOver() {
		if (this.tower.getHealth() <= 0) { // 타워 체력이 0 이하인지 확인
			return true;
		} else {
			return false;
		}
	}

	// 골드 획득 (고정량)
	public void gainGold() {
		this.gold += GOLD_GAINED;
	}

	// 생명체와 터렛의 동작 자동화
	public void gainGold(int amount) {
		this.gold += amount;
	}

	// 생명체와 터렛의 동작 자동화
	public void automate(Player other) {
		// 생명체와 터렛 동작 처리
		Creature creature;
		Turret turret;
		Destructible enemyAhead;
		Creature friendlyAhead = null;
		long currentTime;
		long timeElapsed;

		// Creatures
		for (int currentCreature = HEAD; currentCreature < this.creatures.size(); currentCreature++) {
			creature = this.creatures.get(currentCreature);
			enemyAhead = creature.getEnemyAhead(other);
			creature.setCurrentTarget(enemyAhead);

			if (currentCreature != HEAD) {
				friendlyAhead = creature.getFriendlyAhead(this);
			}

			if (creature instanceof Ranged) {
				// animate projectiles
				for (int i = 0; i < ((Ranged) creature).getProjectiles().size(); i++) {
					Projectile proj = ((Ranged) creature).getProjectiles().get(i);
					proj.move();

					if (proj.checkCollide(creature.getCurrentTarget())) {
						creature.getCurrentTarget().takeDamage(creature.getDamage());
						((Ranged) creature).getProjectiles().remove(i);
					}

					if (creature.getTeamSide() == LEFT_TEAM) {
						if (proj.getPosition().x - proj.getInitialPos().x > ((Ranged) creature).getRangebox()
								.getBoundingBox().getWidth()) {
							((Ranged) creature).getProjectiles().remove(i);
						}
					} else {
						if (proj.getInitialPos().x - proj.getPosition().x > ((Ranged) creature).getRangebox()
								.getBoundingBox().getWidth()) {
							((Ranged) creature).getProjectiles().remove(i);
						}
					}
				}
			}

			// Attack
			// In range of enemy
			if (creature.getRangebox().checkCollide(enemyAhead.getHitbox())) {
				currentTime = System.currentTimeMillis();
				timeElapsed = currentTime - creature.getTimeStartedAttack();
				// Check if they are eligible to attack again
				if (creature.getTimeStartedAttack() == 0 || (timeElapsed >= creature.getAttackSpeed())) {
					creature.attack(enemyAhead);
					creature.setIdling(false);
				}

			} else {
				// Movement/Idle
				if (currentCreature == HEAD) {
					creature.move();
					creature.setIdling(false);

				} else {
					if (this.getTeamSide() == LEFT_TEAM) {
						if ((creature.getPosition().x + creature.getWidth()
								+ creature.getSpeed() < friendlyAhead.getPosition().x)) {
							creature.move();
							creature.setIdling(false);
						} else {
							creature.setIdling(true);
						}

					} else {
						if ((creature.getPosition().x - creature.getWidth()
								+ creature.getSpeed() > friendlyAhead.getPosition().x)) {
							creature.move();
							creature.setIdling(false);
						} else {
							creature.setIdling(true);
						}
					}
				}
			}
		} // End creatures loop

		// Turrets
		for (int i = 0; i < this.tower.getTurrets().size(); i++) {
			turret = this.tower.getTurrets().get(i);
			if (turret != null) {
				enemyAhead = turret.getEnemyAhead(other);
				turret.setCurrentTarget(enemyAhead);
				Projectile proj;

				for (int projectile = 0; projectile < turret.getProjectiles().size(); projectile++) {
					proj = turret.getProjectiles().get(projectile);
					proj.move();
					if (proj.checkCollide(turret.getCurrentTarget())) {
						turret.getCurrentTarget().takeDamage(turret.getDamage());
						turret.getProjectiles().remove(projectile);
					}

					if (this.teamSide == LEFT_TEAM) {
						if (proj.getPosition().x - proj.getInitialPos().x > turret.getRangebox().getBoundingBox()
								.getWidth()) {
							turret.getProjectiles().remove(projectile);
						}
					} else {
						if (proj.getInitialPos().x - proj.getPosition().x > turret.getRangebox().getBoundingBox()
								.getWidth()) {
							turret.getProjectiles().remove(projectile);
						}
					}
				}

				// Turret Attack
				if (turret.getRangebox().checkCollide(enemyAhead.getHitbox())
						&& (turret.getCurrentTarget().getHealth() > 0)) {
					currentTime = System.currentTimeMillis();
					timeElapsed = currentTime - turret.getTimeStartedAttack();
					// Check if they are eligible to attack again
					if (turret.getTimeStartedAttack() == 0 || (timeElapsed >= turret.getAttackSpeed())) {
						turret.attack(enemyAhead);
						turret.setIdling(false);
					}
				} else {
					turret.setIdling(true);
				}
			}
		}
	}

	//플레이어의 시각적 요소를 화면에 그리기
	public void draw(Graphics g) {
		// 타워와 생명체 그리기 (좌측/우측 팀에 따른 표시 포함)
		this.tower.draw(g);
		for (Creature creature : this.creatures) {
			creature.draw(g);
		}

		Font guideFont = new Font("Tahoma", Font.BOLD, 21);
		g.setFont(guideFont);
		if (this.teamSide == LEFT_TEAM) {
			if (this.getGold() >= this.getEvolutionCost()) {
				g.drawString(LEFT_EVOLVE_STRING, 50, SCREEN_HEIGHT - 50);
				g.drawString(Integer.toString(this.getEvolutionCost()), 275, SCREEN_HEIGHT - 50);
			}
			g.drawString(this.userName, 400, 20);
			g.drawString(Double.toString(this.gold), 430, 75);
			g.drawString(Integer.toString(this.currentEvolution), 450, 100);
			for (int i = 0; i < NUM_CC_ICONS; i++) {
				// Left Menu
				g.drawString(Integer.toString(CREATURE_HEALTHS[i] + (STAT_MULTIPLIERS[i] * this.currentEvolution)),
						LEFT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH), LEFT_FIRST_ICON_POS.y + 7 + (2 * NUMBER_SEPARATOR));
				g.drawString(Integer.toString(CREATURE_DAMAGES[i] + (STAT_MULTIPLIERS[i] * this.currentEvolution)),
						LEFT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH), LEFT_FIRST_ICON_POS.y + 7 + (4 * NUMBER_SEPARATOR));
				g.drawString(Integer.toString(CREATURE_RANGES[i]), LEFT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH),
						LEFT_FIRST_ICON_POS.y + 7 + (6 * NUMBER_SEPARATOR));
				g.drawString(LEFT_CREATURE_KEYS[i], LEFT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH),
						LEFT_FIRST_ICON_POS.y + 7 + (8 * NUMBER_SEPARATOR));
			}

			// Rectangles of monsters in queue
			for (int i = 0; i < MAX_IN_QUEUE; i++) {
				for (int j = 0; j < this.summonQueue.size(); j++) {
					g.fillRect(4 + (j * 65) + 5, 10, 45, 25);
				}
				g.drawRect(4 + (i * 65) + 5, 10, 45, 25);
			}

			// right team
		} else {
			if (this.getGold() >= this.getEvolutionCost()) {
				g.drawString(RIGHT_EVOLVE_STRING, 950, SCREEN_HEIGHT - 50);
				g.drawString(Integer.toString(this.getEvolutionCost()), 860, SCREEN_HEIGHT - 50);
			}
			g.drawString(this.userName, 700, 20);
			g.drawString(Double.toString(this.gold), 730, 75);
			g.drawString(Integer.toString(this.currentEvolution), 750, 100);
			for (int i = 0; i < NUM_CC_ICONS; i++) {
				// Right Menu
				g.drawString(Integer.toString(CREATURE_HEALTHS[i] + (STAT_MULTIPLIERS[i] * this.currentEvolution)),
						RIGHT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH),
						RIGHT_FIRST_ICON_POS.y + 7 + (2 * NUMBER_SEPARATOR));
				g.drawString(Integer.toString(CREATURE_DAMAGES[i] + (STAT_MULTIPLIERS[i] * this.currentEvolution)),
						RIGHT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH),
						RIGHT_FIRST_ICON_POS.y + 7 + (4 * NUMBER_SEPARATOR));
				g.drawString(Integer.toString(CREATURE_RANGES[i]), RIGHT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH),
						RIGHT_FIRST_ICON_POS.y + 7 + (6 * NUMBER_SEPARATOR));
				g.drawString(RIGHT_CREATURE_KEYS[i], RIGHT_FIRST_CC_POS.x + (i * 2 * ICON_WIDTH),
						RIGHT_FIRST_ICON_POS.y + 7 + (8 * NUMBER_SEPARATOR));
			}
			// Rectangles of monsters in queue
			for (int i = 0; i < MAX_IN_QUEUE; i++) {
				for (int j = 0; j < this.summonQueue.size(); j++) {
					g.fillRect(SCREEN_WIDTH - (ICON_WIDTH * 2) - 215 + (j * 65), 10, 45, 25);
				}
				g.drawRect(SCREEN_WIDTH - (ICON_WIDTH * 2) - 215 + (i * 65), 10, 45, 25);
			}
		}
	}

	// ---------------------------------------
	 // Getter 메서드
	public Tower getTower() {
		return this.tower;
	}

	public int getTeamSide() {
		return this.teamSide;
	}

	public LinkedList<Creature> getCreatures() {
		return this.creatures;
	}

	public Queue<Creature> getSummonQueue() {
		return summonQueue;
	}

	public double getGold() {
		return gold;
	}

	public String getUserName() {
		return this.userName;
	}

	public int getCurrentEvolution() {
		return this.currentEvolution;
	}

	public int getEvolutionCost() {
		return evolutionCost;
	}

}