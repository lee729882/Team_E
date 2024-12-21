package Entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import Core.StatusEffect;

import Core.StunEffect;
import Core.Hitbox;

public class Creature extends Destructible {

    private int speed;
    private Hitbox rangebox;
    private int damage;
    private double goldFromKill;
    private long attackSpeed;
    private int currentActivity = IDLE_ACTIVITY;
    private long timeStartedAttack;
    private Destructible currentTarget;

    private BufferedImage[] moveSprites;
    private BufferedImage[] attackSprites;
    private BufferedImage currentSprite;
    private int currentMoveIndex;
    private int currentAttackIndex;
    private boolean idling = false;
    private StunEffect stunEffect; // 스턴 효과를 관리하는 객체
    private boolean isStunned = false; // 유닛이 스턴 상태인지 여부
    private long stunEndTime = 0;      // 스턴 상태가 종료되는 시간 (밀리초)
    private List<StatusEffect> statusEffects = new ArrayList<>();
    private boolean isSlowed = false;
    private double originalSpeed;    // 원래 속도 저장
    private long slowEndTime = 0;    // 슬로우 효과 종료 시간
    private boolean isSpeedModified = false;
    

    public Creature(int teamSide, int type, int evolution, BufferedImage[] move, BufferedImage[] attack) {
        super(teamSide, type, evolution);
        this.moveSprites = move;
        this.attackSprites = attack;
        this.currentSprite = this.moveSprites[INITIAL_SPRITE_INDEX];
        this.currentMoveIndex = INITIAL_SPRITE_INDEX;
        this.stunEffect = new StunEffect(); // 스턴 효과 초기화
        
        
        int range = 0;

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

        if (teamSide == LEFT_TEAM) {
            Point point = new Point(this.getPosition().x + this.getWidth(), this.getPosition().y);
            this.rangebox = new Hitbox(point, range, range);
        } else {
            Point point = new Point(this.getPosition().x - range, this.getPosition().y);
            this.rangebox = new Hitbox(point, range, range);
        }

        // 디버깅: 유닛 초기화 정보 출력
        System.out.println("Creating unit at position: " + this.getPosition() +
                           ", Health: " + this.getHealth() + "/" + this.getMaxHealth());
    }  


    
    public void applySlow(double slowPercentage, long duration) {
        if (isSlowed) return;

        isSlowed = true;
        originalSpeed = this.speed;
        this.speed = (int) (this.speed * (1.0 - slowPercentage)); // double 값을 int로 변환
        slowEndTime = System.currentTimeMillis() + duration;

        System.out.println("Creature slowed by " + (slowPercentage * 100) + "% for " + duration + "ms.");
    }
    
    public void updateSlowState() {
        if (isSlowed && System.currentTimeMillis() > slowEndTime) {
            isSlowed = false;
            this.speed = (int) originalSpeed; // double -> int 변환
            System.out.println("Slow effect expired for creature.");
        }
    }
    
    public void applyStun(long duration) {
        this.isStunned = true;
        this.stunEndTime = System.currentTimeMillis() + duration; // 현재 시간에 지속 시간 추가

        System.out.println("Stun applied to creature for " + duration + " ms");
    }
    
    
    // 스턴 상태 업데이트 메서드
    public void updateStunState() {
        if (isStunned && System.currentTimeMillis() > stunEndTime) {
            this.isStunned = false; // 스턴 상태 해제
            System.out.println("Stun expired for creature: " + this);
        }
    }
    
    // 스턴 상태 여부 확인
    public boolean isStunned() {
        return isStunned;
    }
    
    public void setStunned(boolean stunned) {
        this.isStunned = stunned;
    }
    

    public void attack(Destructible target) {
        if (isStunned()) {
            System.out.println("Creature is stunned and cannot attack.");
            return; // 스턴 상태에서는 공격하지 않음
        }
        
        if (target != null && !target.isDestroyed()) {
            // 공격 모션 시작
            if (this.currentActivity != ATTACK_ACTIVITY) {
                this.currentActivity = ATTACK_ACTIVITY;
                this.currentAttackIndex = 0; // 공격 모션 초기화
                this.setCurrentSprite(this.attackSprites[this.currentAttackIndex]);
            }

            // 공격 애니메이션 진행
            if (this.currentAttackIndex < this.attackSprites.length - 1) {
                this.currentAttackIndex++;
            } else {
                // 공격 완료, 데미지 적용
                target.takeDamage(this.damage);
                this.timeStartedAttack = System.currentTimeMillis();
                this.currentActivity = IDLE_ACTIVITY; // 다시 기본 상태로 전환
                this.setCurrentSprite(this.moveSprites[FIRST_SPRITE]);
            }

            // 디버깅 출력
            System.out.println("[DEBUG] Melee attack by " + this.getType() +
                               ", Target: " + target + 
                               ", Damage: " + this.damage);

            if (target.isDestroyed()) {
                System.out.println("[DEBUG] Target destroyed.");
                this.currentTarget = null;
            }
        }
    }


    public void move() {
        if (isStunned()) {
            System.out.println("Creature is stunned and cannot move.");
            return; // 스턴 상태에서는 이동하지 않음
        }
        
        Point previousPosition = new Point(this.getPosition());

        if (this.currentMoveIndex == LAST_MOVE_SPRITE) {
            this.currentMoveIndex = FIRST_SPRITE;
        } else {
            this.currentMoveIndex++;
        }
        this.currentSprite = moveSprites[currentMoveIndex];
        this.updatePositions();

        // 충돌 방지 로직
        if (this.getCurrentTarget() != null) {
            if (this.getHitbox().checkCollide(this.getCurrentTarget().getHitbox())) {
                System.out.println("Collision detected: reverting position.");
                this.setPosition(previousPosition);
            }
        }

    }

    public void updatePositions() {
        Point creaturePos = this.getPosition();
        Point hitboxPos = this.getHitbox().getPosition();
        Point rangeboxPos = this.rangebox.getPosition();

        creaturePos.setLocation(creaturePos.x + this.speed, creaturePos.y);
        hitboxPos.setLocation(hitboxPos.x + this.speed, hitboxPos.y);
        rangeboxPos.setLocation(rangeboxPos.x + this.speed, rangeboxPos.y);

        this.setPosition(creaturePos);
        this.getHitbox().update(hitboxPos);
        this.getRangebox().update(rangeboxPos);
    }

    public Creature getFriendlyAhead(Player self) {
        LinkedList<Creature> creatures = self.getCreatures();
        int thisIndex = creatures.indexOf(this);
        return creatures.get(thisIndex - 1);
    }

    public void draw(Graphics g) {
        // 유닛이 체력이 없을 경우 그리지 않음
        if (this.getHealth() <= 0) {
            return;
        }

        if (this.currentSprite == null) {
            System.err.println("[ERROR] Cannot draw unit. Sprite is null for unit at position: " + this.getPosition());
            return;
        }

        // 이미지 높이에 따라 조정 (기준선 유지)
        int groundLevel = 560; // 바닥 기준선
        int adjustedY = groundLevel - this.currentSprite.getHeight(null);

        // 현재 활동 상태에 따른 스프라이트 선택
        if (this.currentActivity == ATTACK_ACTIVITY) {
            g.drawImage(this.attackSprites[this.currentAttackIndex], this.getPosition().x, adjustedY, null);
        } else {
            g.drawImage(this.moveSprites[this.currentMoveIndex], this.getPosition().x, adjustedY, null);
        }


        // 체력바 크기 및 위치 설정
        int healthBarWidth = (int) (this.currentSprite.getWidth(null) * 0.8); // 유닛 너비의 80%
        int healthBarHeight = 5; // 고정 높이
        int xOffset = (this.currentSprite.getWidth(null) - healthBarWidth) / 2;
        int yOffset = -healthBarHeight - 10;


        // 체력바 배경 (빨간색)
        g.setColor(Color.RED);
        g.fillRect(
            this.getPosition().x + xOffset,
            adjustedY + yOffset,
            healthBarWidth,
            healthBarHeight
        );

        // 체력바 현재 체력 (녹색)
        g.setColor(Color.GREEN);
        int currentHealthBarWidth = (int) ((double) this.getHealth() / this.getMaxHealth() * healthBarWidth);
        g.fillRect(
            this.getPosition().x + xOffset,
            adjustedY + yOffset,
            currentHealthBarWidth,
            healthBarHeight
        );
    }




    public void debugUnitState() {
        System.out.println("Debugging Unit:");
        System.out.println("Position: " + this.getPosition());
        System.out.println("Health: " + this.getHealth() + "/" + this.getMaxHealth());
        System.out.println("Sprite: " + this.currentSprite);
        System.out.println("Target: " + (this.getCurrentTarget() != null ? this.getCurrentTarget().getPosition() : "None"));
    }

    public void applyStatusEffect(String type, long duration) {
        statusEffects.add(new StatusEffect(type, duration));
        if (type.equals("stun")) {
            this.isStunned = true;
        }
    }

    public void updateStatusEffects() {
        Iterator<StatusEffect> iterator = statusEffects.iterator();
        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next();
            if (effect.isExpired()) {
                if (effect.getType().equals("stun")) {
                    this.isStunned = false;
                }
                iterator.remove();
            }
        }
    }
    
    //아군 체력 회복
    public void recoverHealth(int amount) {
        this.health = Math.min(this.health + amount, this.maxHealth);
        System.out.println("Creature recovered health: " + amount + ". Current health: " + this.health);
    }
    
    //이동속도 증가
    public void applySpeedBoost(double boostPercentage, long duration) {
        if (isSpeedModified) return;
        isSpeedModified = true;
        originalSpeed = this.speed;
        this.speed = (int) (this.speed * (1.0 + boostPercentage));
        System.out.println("Speed boosted by " + (boostPercentage * 100) + "% for " + duration + "ms.");

        // 지속 시간 이후 원래 속도로 복원
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                restoreOriginalSpeed();
            }
        }, duration);
    }

    public void restoreOriginalSpeed() {
        if (!isSpeedModified) return;
        this.speed = (int) originalSpeed;
        isSpeedModified = false;
        System.out.println("Speed restored to original value.");
    }

    //공격력 증가
    public void boostAttack(double boostPercentage, long duration) {
        int originalDamage = this.damage;
        this.damage *= (1.0 + boostPercentage);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                damage = originalDamage;
                System.out.println("Attack restored to original value.");
            }
        }, duration);
    }

    
    
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
