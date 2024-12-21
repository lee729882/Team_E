package Entities;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Core.EntityConstants;
import Core.GameConstants;
import Core.Hitbox;
import Core.SkillType;
import Entities.Wall;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;
public class Player implements GameConstants, EntityConstants {

	private Tower tower;
	private int teamSide;
	private LinkedList<Creature> creatures = new LinkedList<Creature>();
	private Queue<Creature> summonQueue = new LinkedList<Creature>();
	private String userName;
	private double gold;
    private Point position;
    private int width;
    private int height;
    private long duration;
    private boolean active;
    private Hitbox hitbox;
    

	private int currentEvolution = 0;
	private int evolutionCost;
	private ArrayList<Projectile> projectiles; // 투사체 리스트 필드 추가
	private ArrayList<Wall> walls; // 벽을 관리할 리스트
	

	private BufferedImage[][][] moveSprites;
	private BufferedImage[][][] attackSprites;
	private BufferedImage[][] projectileSprites;
	private BufferedImage[] towerSprites;
	private BufferedImage[] turretSprites;


	public static final Point LEFT_SPAWN = new Point(100, 500);
	public static final Point RIGHT_SPAWN = new Point(1100, 500);

	public Player(int team, String name, BufferedImage[][][] move, BufferedImage[][][] attack,
			BufferedImage[][] projectiles, BufferedImage[] tower, BufferedImage[] turret) {
		this.teamSide = team;
		this.userName = name;
		this.moveSprites = move;
		this.attackSprites = attack;
		this.projectileSprites = projectiles;
		this.towerSprites = tower;
		this.turretSprites = turret;
		this.gold = START_GOLD;
		this.evolutionCost = EVOLVE_COST;
		this.tower = new Tower(team, TOWER_TYPE, this.currentEvolution, this.towerSprites, this.turretSprites,
				projectileSprites[TURRET_PROJECTILES]);
		this.currentEvolution = STARTING_EVOLUTION;
		this.projectiles = new ArrayList<>(); // 초기화
		this.position = new Point(0, 0); // 초기 위치
		this.width = 50; // 기본 너비
		this.height = 100; // 기본 높이
		this.duration = 5000; // 기본 지속 시간

        this.active = true;
        this.hitbox = new Hitbox(position, width, height);
        this.walls = new ArrayList<>(); // 벽 리스트 초기화
        
        // 벽 지속 시간 타이머
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                deactivate(); // 벽 비활성화
            }
        }, duration);
	}

    public void deactivate() {
        active = false;
        System.out.println("Wall deactivated at position: " + position);
    }
    
    public Point getPosition() {
        return position;
    }
    public Hitbox getHitbox() {
        return this.hitbox;
    }
    
    public boolean isActive() {
        return active;
    }
    
	public void queueCreature(int cost, int type) {
	    if (this.summonQueue.size() < MAX_CREATURES_IN_QUEUE) {
	        // 골드 소모 성공 시에만 유닛 소환
	        if (this.spendGold(cost)) {
	            Creature creature = createCreature(type);
	            this.summonQueue.add(creature);
	            System.out.println("유닛 소환 대기열 추가: 타입 " + type + ", 비용: " + cost);
	        } else {
	            System.out.println("골드 부족으로 유닛 소환 실패: 비용 = " + cost);
	        }
	    }
	}

    public Creature createCreature(int type) {
        BufferedImage projectileSprite = null;
        if (type == SECOND_TYPE) {
            projectileSprite = this.projectileSprites[SECOND_PROJECTILES][this.currentEvolution];
        } else if (type == FOURTH_TYPE) {
            projectileSprite = this.projectileSprites[FOURTH_PROJECTILES][this.currentEvolution];
        }

        if (type == FIRST_TYPE || type == THIRD_TYPE) {
            return new Creature(teamSide, type, currentEvolution, moveSprites[type][currentEvolution],
                    attackSprites[type][currentEvolution]);
        } else {
            return new Ranged(teamSide, type, currentEvolution, moveSprites[type][currentEvolution],
                    attackSprites[type][currentEvolution], projectileSprite);
        }
    }
    
    
	public void summonCreature() {
		this.creatures.add(this.summonQueue.poll());
	}

    public boolean evolve() {
        if (this.currentEvolution == 2 || !this.spendGold(this.evolutionCost)) {
            System.out.println("진화 불가능: 현재 진화 단계 = " + this.currentEvolution + ", 골드 = " + this.gold);
            return false;
        }
        this.currentEvolution++;
        this.evolutionCost = this.currentEvolution * EVOLVE_MULTIPLIER * EVOLVE_COST;
        this.tower.setCurrentSprite(towerSprites[this.currentEvolution]);
        System.out.println("진화 성공: 현재 진화 단계 = " + this.currentEvolution);
        return true;
    }

	public void buyTurret(int cost, int spot) {
		this.gold -= cost;
		this.tower.addTurret(spot);
	}

	public void sellTurret(int amount, int spot) {
		this.gold += amount;
		this.tower.getTurrets().set(spot, null);
	}

	public int removeDeadCreatures() {
	    int goldGained = 0;
	    Iterator<Creature> iterator = this.creatures.iterator();
	    while (iterator.hasNext()) {
	        Creature creature = iterator.next();
	        if (creature.getHealth() <= 0) {
	            goldGained += creature.getGoldFromKill();
	            iterator.remove();
	            System.out.println("죽은 유닛 제거: " + creature);
	        }
	    }
	    return goldGained;
	}


	public boolean checkGameOver() {
		if (this.tower.getHealth() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void gainGold() {
		this.gold += GOLD_GAINED;
	}
	// Player 클래스에 추가: 유닛 비용 계산 메서드
	public int calculateUnitCost(int type) {
	    if (type >= 0 && type < GameConstants.UNIT_COSTS.length) {
	        return GameConstants.UNIT_COSTS[type];
	    }
	    throw new IllegalArgumentException("Invalid unit type: " + type);
	}
    // 골드 소모 메서드
    public boolean spendGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold -= amount;
            System.out.println("골드 사용: " + amount + ", 남은 골드: " + this.gold);
            return true;
        } else {
            System.out.println("골드 부족! 요청 금액: " + amount + ", 현재 골드: " + this.gold);
            return false;
        }
    }

    // 골드 증가 메서드
    public void gainGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
            System.out.println("골드 획득: " + amount + ", 현재 골드: " + this.gold);
        }
    }

    // 골드 추가 메서드
    public void addGold(int amount) {
        this.gold += amount;
        // 골드가 음수가 되지 않도록 보장
        if (this.gold < 0) {
            this.gold = 0;
        }
    }
    
    // 골드 설정 메서드
    public void setGold(int initialGold) {
        this.gold = initialGold;
        System.out.println("초기 골드 설정: " + this.gold);
    }

    public int getGold() {
        return (int) this.gold;
    }

	// automate troop movements, turret functions, etc
	public void automate(Player other) {
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

	public void draw(Graphics g) {
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
	
    public void addProjectile(Projectile projectile) {
        if (projectile != null) {
            projectiles.add(projectile);
        }
    }
	
    // 벽 추가
    public void addWall(Point position) {
        if (position == null) {
            position = new Point(200, 200); // 기본값 설정
        }
        Wall newWall = new Wall(position, 50, 200, 5000); // 크기: 50x100, 지속 시간: 5초
        walls.add(newWall);
    }
    
    // 벽을 그리는 메서드
    public void drawWalls(Graphics g) {
        walls.removeIf(wall -> !wall.isActive()); // 비활성화된 벽 제거
        for (Wall wall : walls) {
            wall.draw(g);
        }
    }

    // 플레이어가 관리하는 모든 벽 반환
    public ArrayList<Wall> getWalls() {
        return walls;
    }
    
    // 벽 충돌 체크 (적 투사체와의 충돌 처리)
    public void checkWallCollisions() {
        for (Wall wall : walls) {
            if (!wall.isActive()) continue;
            // 충돌 로직 추가 (투사체 등과의 충돌 처리)
        }
    }
    
    public void stunAllCreatures(long duration) {
        for (Creature creature : creatures) {
            creature.applyStun(duration); // 각 유닛에 스턴 적용
        }
        System.out.println("All creatures stunned for " + duration + "ms");
    
    }
    
    // 모든 유닛의 스턴 상태 업데이트
    public void updateCreatures() {
        for (Creature creature : creatures) {
            creature.updateStatusEffects(); // 상태 효과 업데이트
            creature.updateStunState(); // 기존 스턴 업데이트
            if (creature.isStunned()) {
                System.out.println("Creature is stunned: " + creature);
            } else {
            }
        }
    }
    
    public void damageAllCreaturesByPercentage(double percentage) {
        for (Creature creature : this.creatures) {
            int damage = (int) (creature.getHealth() * percentage);
            creature.takeDamage(damage);
            System.out.println("Dealt " + damage + " damage to creature: " + creature);
        }
    }
    
    public void executeRandomCreature() {
        if (!creatures.isEmpty()) {
            Creature target = creatures.remove(new Random().nextInt(creatures.size()));
            System.out.println("Executed creature: " + target + ". Remaining creatures: " + creatures.size());
        } else {
            System.out.println("No creatures available for execution.");
        }
    }
    

    //
    // 정신 나갈것 같애
    // 공격력 증가
    public void boostAttackOfAllCreatures(double boostPercentage, long duration) {
        for (Creature creature : this.creatures) {
            creature.boostAttack(boostPercentage, duration);
        }
        System.out.println("All creatures' attack boosted by " + (boostPercentage * 100) + "% for " + duration + "ms.");
    }

    //적 속도 감소 
    public void applySlowToAllCreatures(double slowPercentage, long duration) {
        for (Creature creature : this.creatures) {
            creature.applySlow(slowPercentage, duration);
        }
        System.out.println("All enemy creatures slowed by " + (slowPercentage * 100) + "% for " + duration + "ms.");
    }

    
	// ---------------------------------------
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



	public String getUserName() {
		return this.userName;
	}

	public int getCurrentEvolution() {
		return this.currentEvolution;
	}

	public int getEvolutionCost() {
		return evolutionCost;
	}

																																																																								
	public void stunAllCreatures(int i) {
		// TODO Auto-generated method stub
		
	}


	public ArrayList<Projectile> getProjectiles() {
	    if (this.projectiles == null) {
	        return new ArrayList<>(); // null 대신 빈 리스트 반환
	    }
	    return this.projectiles;
	}


// 체력 회복
	public void healAllCreatures(double percentage) {
	    for (Creature creature : this.creatures) {
	        int healAmount = (int) (creature.getMaxHealth() * percentage);
	        creature.recoverHealth(healAmount);
	        System.out.println("Healed " + healAmount + " health for creature: " + creature);
	    }
	}
	
	// 아군 타워 체력 회복
	public void healTower(int healAmount) {
	    Tower tower = this.getTower();
	    if (!tower.isDestroyed()) {
	        tower.recoverHealth(healAmount);
	        System.out.println("Tower healed for " + healAmount + " HP. Current health: " + tower.getHealth() + "/" + tower.getMaxHealth());
	    }
	}
	
	
	// 아군 이동속도 증가
	public void applySpeedBoostToAllCreatures(double boostPercentage, long duration) {
	    for (Creature creature : creatures) {
	        if (!creature.isDestroyed()) {
	            creature.applySpeedBoost(boostPercentage, duration);
	        }
	    }
	    System.out.println("All creatures boosted speed by " + (boostPercentage * 100) + "% for " + duration + "ms.");
	}

	
	
	public void healAllCreatures() {
		// TODO Auto-generated method stub
		
	}


	

}
