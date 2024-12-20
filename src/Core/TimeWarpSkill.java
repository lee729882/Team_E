package Core;

import Entities.Creature;
import Entities.Player;
import java.util.List;

/**
 * TimeWarpSkill 클래스는 아군과 적 모두에게 시간 왜곡 효과를 제공합니다.
 * 아군은 속도 증가, 적은 속도 감소 효과를 받습니다.
 */
public class TimeWarpSkill extends Skill {
    private double allySpeedBoost;    // 아군 속도 증가 비율 (0.0 ~ 1.0)
    private double enemySpeedReduction; // 적 속도 감소 비율 (0.0 ~ 1.0)
    private long duration;           // 지속 시간 (밀리초)

    /**
     * TimeWarpSkill 생성자
     * @param name 스킬 이름
     * @param allySpeedBoost 아군 속도 증가 비율 (0.0 ~ 1.0)
     * @param enemySpeedReduction 적 속도 감소 비율 (0.0 ~ 1.0)
     * @param duration 지속 시간 (밀리초)
     */
    public TimeWarpSkill(String name, double allySpeedBoost, double enemySpeedReduction, long duration) {
        super(name, SkillType.RANDOM); // SkillType은 필요 시 새로 정의
        if (allySpeedBoost < 0.0 || allySpeedBoost > 1.0 || enemySpeedReduction < 0.0 || enemySpeedReduction > 1.0) {
            throw new IllegalArgumentException("Speed adjustment percentages must be between 0.0 and 1.0.");
        }
        this.allySpeedBoost = allySpeedBoost;
        this.enemySpeedReduction = enemySpeedReduction;
        this.duration = duration;
    }

    /**
     * 스킬을 활성화합니다.
     * @param player 스킬을 사용하는 플레이어
     * @param opponent 상대 플레이어
     */
    @Override
    public void activate(Player player, Player opponent) {
        System.out.println("Activating TimeWarpSkill: " + getName());
        
        // 아군 유닛에 속도 증가 적용
        List<Creature> allies = player.getCreatures();
        for (Creature ally : allies) {
            ally.applySpeedBoost(allySpeedBoost, duration);
        }
        System.out.println("All allied creatures have received a " + (allySpeedBoost * 100) + "% speed boost.");

        // 적 유닛에 속도 감소 적용
        List<Creature> enemies = opponent.getCreatures();
        for (Creature enemy : enemies) {
            enemy.applySlow(enemySpeedReduction, duration);
        }
        System.out.println("All enemy creatures have been slowed by " + (enemySpeedReduction * 100) + "%.");

        // 지속 시간이 끝난 후 원래 상태 복구
        scheduleRecovery(allies, enemies);
    }

    /**
     * 효과가 끝난 후 상태를 복구합니다.
     * @param allies 아군 유닛 리스트
     * @param enemies 적 유닛 리스트
     */
    private void scheduleRecovery(List<Creature> allies, List<Creature> enemies) {
        new Thread(() -> {
            try {
                Thread.sleep(duration); // 지속 시간 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 아군 상태 복구
            for (Creature ally : allies) {
                ally.restoreOriginalSpeed();
            }
            System.out.println("All allied creatures have restored their original speed.");

            // 적 상태 복구
            for (Creature enemy : enemies) {
                enemy.restoreOriginalSpeed();
            }
            System.out.println("All enemy creatures have restored their original speed.");
        }).start();
    }
}
