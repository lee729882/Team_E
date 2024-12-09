package Entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Core.GameConstants;

public class EnemyAI {
    private Player aiPlayer;
    private Player opponentPlayer;
    private Random random;
    private double difficultyMultiplier; // 난이도 계수
    private int maxUnitCost; // 소환 가능한 유닛의 최대 비용
    private double summonProbability; // 유닛 소환 확률
    private double evolveProbability; // 진화 확률
    
    // 난이도 별 초기 골드와 시간에 따른 골드 획득량 설정
    private int initialGold;
    private int goldPerInterval; // 시간당 획득할 골드 양
    private long lastGoldUpdateTime; // 마지막 골드 업데이트 시간
    
    private long summonCooldown; // 유닛 소환 쿨타임
    private long lastSummonTime; // 마지막 소환 시간
    
    // 기본 생성자: 난이도를 설정하지 않으면 기본 값 사용
    public EnemyAI(Player aiPlayer, Player opponentPlayer) {
        this(aiPlayer, opponentPlayer, 1.0); // 기본 난이도는 1.0 (보통)
    }
    
    public EnemyAI(Player aiPlayer, Player opponentPlayer, double difficultyMultiplier) {
        this.aiPlayer = aiPlayer;
        this.opponentPlayer = opponentPlayer;
        this.random = new Random();
        this.difficultyMultiplier = difficultyMultiplier;

     // 쉬움 난이도 (AI가 약함)
        if (difficultyMultiplier == 0.5) {
            this.maxUnitCost = 300;  // 기본 유닛 비용
            this.summonProbability = 0.3; // 유닛 소환 확률 낮음
            this.evolveProbability = 0.3; // 진화 확률 낮음
            this.initialGold = 100;  // 적은 초기 골드
            this.summonCooldown = 10000; // 10초 (쉬움에서 더 긴 쿨타임)

        } 
        // 보통 난이도
        else if (difficultyMultiplier == 1.0) {
            this.maxUnitCost = 400; // 기본 유닛 비용
            this.summonProbability = 0.5; // 유닛 소환 확률 보통
            this.evolveProbability = 0.5; // 진화 확률 보통
            this.initialGold = 200;  // 보통 난이도에서 기본 초기 골드
            this.summonCooldown = 7000; // 7초 (보통에서 평균적인 쿨타임)

        } 
        // 어려움 난이도 (AI가 강함)
        else {
            this.maxUnitCost = 500;  // 유닛 비용 높음
            this.summonProbability = 0.8; // 유닛 소환 확률 높음
            this.evolveProbability = 0.9; // 진화 확률 높음
            this.initialGold = 500;  // 많은 초기 골드
            this.summonCooldown = 5000; // 5초 (어려움에서 더 짧은 쿨타임)

        }
        aiPlayer.setGold(this.initialGold);  // Player 객체에 초기 골드 설정
        this.lastSummonTime = System.currentTimeMillis(); // 소환 시작 시간 설정

    }

    public Player getAiPlayer() {
        return aiPlayer;
    }

    // AI 동작 자동화
    public void automate(Player other) {
        manageUnits(); // 유닛 소환 및 관리

        // Creatures 처리
        for (Creature creature : aiPlayer.getCreatures()) {
            handleCreature(creature);
        }

        // Turrets 처리
        for (Turret turret : aiPlayer.getTower().getTurrets()) {
            if (turret != null) {
                handleTurret(turret, other);
            }
        }
    }

    // Creature 처리
    private void handleCreature(Creature creature) {
        // 타겟 확인 및 갱신
        if (creature.getCurrentTarget() == null || creature.getCurrentTarget().isDestroyed()) {
            Destructible newTarget = findTargetInRange(creature);
            creature.setCurrentTarget(newTarget);
        }

        // Ranged 유닛의 투사체 처리
        if (creature instanceof Ranged) {
            processProjectiles((Ranged) creature);
        }

        // 타겟이 있으면 공격
        if (creature.getCurrentTarget() != null) {
            if (isWithinAttackRange(creature, creature.getCurrentTarget())) {
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - creature.getTimeStartedAttack();

                if (timeElapsed >= creature.getAttackSpeed()) {
                    creature.attack(creature.getCurrentTarget());
                    creature.setIdling(false);
                }
            } else {
                // 타겟이 사거리 밖이면 새 타겟 찾기
                creature.setCurrentTarget(findTargetInRange(creature));
            }
        } else {
            // 타겟이 없으면 이동
            if (!checkCollisionWhileMoving(creature)) {
                creature.move();
                creature.setIdling(false);
            } else {
                creature.setIdling(true);
            }
        }
    }

    // Turret 처리
    private void handleTurret(Turret turret, Player other) {
        Destructible enemyAhead = turret.getEnemyAhead(other);
        turret.setCurrentTarget(enemyAhead);

        processProjectiles(turret);

        if (enemyAhead != null && turret.getRangebox().checkCollide(enemyAhead.getHitbox())) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - turret.getTimeStartedAttack();

            if (timeElapsed >= turret.getAttackSpeed()) {
                turret.attack(enemyAhead);
                turret.setIdling(false);
            }
        } else {
            turret.setIdling(true);
        }
    }

    private int targetUnitCost = -1; // 현재 목표 비용 (-1은 초기값)

 // 이전 상태를 추적
    private int previousGold = -1;
    private int previousTargetCost = -1;

    private void manageUnits() {
        // 유닛 소환 확률 계산 (난이도에 따라 다르게 설정)
        double summonProbability = this.summonProbability; // 난이도에 맞는 소환 확률

        // 목표 비용 초기화 (처음 설정되었거나 대기열이 가득 찬 경우)
        if (targetUnitCost == -1 || aiPlayer.getSummonQueue().size() >= GameConstants.MAX_CREATURES_IN_QUEUE) {
            // maxUnitCost 이하로 목표 비용을 설정
            targetUnitCost = GameConstants.UNIT_COSTS[random.nextInt(GameConstants.NUM_DIFFERENT_CREATURES)];

            // 목표 비용이 maxUnitCost보다 크다면 maxUnitCost로 설정
            if (targetUnitCost > maxUnitCost) {
                targetUnitCost = maxUnitCost;
            }

            logIfStateChanged("AI 새 목표 비용 설정: " + targetUnitCost);
        }

        // 상태 변경 확인
        logIfStateChanged("AI 골드: " + aiPlayer.getGold() + ", 목표 비용: " + targetUnitCost);

        // 유닛 소환 쿨타임 확인
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSummonTime >= summonCooldown) {  // 쿨타임이 지나면 소환 가능
            // 목표 비용에 도달한 경우 유닛 소환 (소환 확률 고려)
            if (aiPlayer.getGold() >= targetUnitCost) {
                int unitType = findUnitTypeByCost(targetUnitCost);

                // 소환 확률을 고려하여 유닛을 소환
                if (unitType != -1 && random.nextDouble() < summonProbability) {
                    aiPlayer.queueCreature(targetUnitCost, unitType);
                    logIfStateChanged("AI가 유닛을 소환 대기열에 추가: 타입 " + unitType + ", 남은 골드: " + aiPlayer.getGold());
                    targetUnitCost = -1; // 목표 비용 초기화
                    lastSummonTime = currentTime; // 마지막 소환 시간 업데이트
                } else {
                    logIfStateChanged("AI가 유닛을 소환하지 않음: 소환 확률 미달");
                }
            }
        } else {
            logIfStateChanged("AI가 쿨타임 동안 대기 중입니다.");
        }

        // 진화 로직: AI가 진화할 수 있는 경우 진화 확률에 따라 진화
        if (aiPlayer.getGold() >= aiPlayer.getEvolutionCost() && random.nextDouble() < evolveProbability) {
            aiPlayer.evolve();
            logIfStateChanged("AI가 진화했습니다. 현재 진화 단계: " + aiPlayer.getCurrentEvolution());
        }

        // 소환 대기열 처리
        if (!aiPlayer.getSummonQueue().isEmpty()) {
            aiPlayer.summonCreature();
            logIfStateChanged("AI가 유닛을 소환.");
        }
    }




    private void logIfStateChanged(String message) {
        int currentGold = aiPlayer.getGold();
        if (previousGold != currentGold || previousTargetCost != targetUnitCost) {
            System.out.println(message);
            previousGold = currentGold;
            previousTargetCost = targetUnitCost;
        }
    }

    // 비용으로 유닛 타입 찾기
    private int findUnitTypeByCost(int cost) {
        for (int i = 0; i < GameConstants.NUM_DIFFERENT_CREATURES; i++) {
            if (GameConstants.UNIT_COSTS[i] == cost) {
                return i;
            }
        }
        return -1; // 유효한 유닛 타입을 찾지 못한 경우
    }

    // 투사체 처리
    private void processProjectiles(Ranged ranged) {
        List<Projectile> projectiles = ranged.getProjectiles();
        Iterator<Projectile> iterator = projectiles.iterator();

        while (iterator.hasNext()) {
            Projectile proj = iterator.next();
            proj.move();

            if (ranged.getCurrentTarget() != null && proj.checkCollide(ranged.getCurrentTarget())) {
                ranged.getCurrentTarget().takeDamage(ranged.getDamage());
                iterator.remove();
            } else if (isProjectileOutOfRange(proj, ranged)) {
                iterator.remove();
            }
        }
    }

    private void processProjectiles(Turret turret) {
        for (int i = 0; i < turret.getProjectiles().size(); i++) {
            Projectile proj = turret.getProjectiles().get(i);
            proj.move();

            if (proj.checkCollide(turret.getCurrentTarget())) {
                turret.getCurrentTarget().takeDamage(turret.getDamage());
                turret.getProjectiles().remove(i);
                i--;
            } else if (isProjectileOutOfRange(proj, turret)) {
                turret.getProjectiles().remove(i);
                i--;
            }
        }
    }

    private boolean isProjectileOutOfRange(Projectile proj, Ranged ranged) {
        double range = ranged.getRangebox().getBoundingBox().getWidth();

        if (ranged.getTeamSide() == GameConstants.LEFT_TEAM) {
            return (proj.getPosition().x - proj.getInitialPos().x) > range;
        } else {
            return (proj.getInitialPos().x - proj.getPosition().x) > range;
        }
    }

    private boolean isProjectileOutOfRange(Projectile proj, Turret turret) {
        double range = turret.getRangebox().getBoundingBox().getWidth();

        if (turret.getTeamSide() == GameConstants.LEFT_TEAM) {
            return (proj.getPosition().x - proj.getInitialPos().x) > range;
        } else {
            return (proj.getInitialPos().x - proj.getPosition().x) > range;
        }
    }

    private boolean checkCollisionWhileMoving(Creature creature) {
        for (Creature enemy : opponentPlayer.getCreatures()) {
            if (enemy == null) {
                continue;
            }
            if (creature.getHitbox().checkCollide(enemy.getHitbox())) {
                return true;
            }
        }
        return false;
    }

    private Destructible findTargetInRange(Creature creature) {
        for (Creature enemy : opponentPlayer.getCreatures()) {
            if (creature.getRangebox().checkCollide(enemy.getHitbox()) && !enemy.isDestroyed()) {
                return enemy;
            }
        }

        if (creature.getRangebox().checkCollide(opponentPlayer.getTower().getHitbox()) && !opponentPlayer.getTower().isDestroyed()) {
            return opponentPlayer.getTower();
        }

        return null;
    }

    private boolean isWithinAttackRange(Creature creature, Destructible target) {
        return creature.getRangebox().checkCollide(target.getHitbox());
    }

    private int calculateUnitCost(int unitType) {
        return GameConstants.START_GOLD + (aiPlayer.getCurrentEvolution() * GameConstants.EVOLVE_MULTIPLIER);
    }

    public boolean checkGameOver() {
        return aiPlayer.checkGameOver();
    }
}
