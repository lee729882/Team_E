package Core;

import Entities.Creature;
import Entities.Player;

import java.util.List;

/**
 * SlowSkill 클래스는 적 유닛의 속도를 일정 시간 동안 감소시키는 스킬을 정의합니다.
 */
public class SlowSkill extends Skill {
    private double slowPercentage; // 속도 감소 비율 (0.0 ~ 1.0)
    private long duration;         // 지속 시간 (밀리초)

    /**
     * SlowSkill 생성자
     * @param name 스킬 이름
     * @param slowPercentage 감소 비율 (0.0 ~ 1.0)
     * @param duration 지속 시간 (밀리초)
     */
    public SlowSkill(String name, double slowPercentage, long duration) {
        super(name, SkillType.SLOW);
        if (slowPercentage < 0.0 || slowPercentage > 1.0) {
            throw new IllegalArgumentException("Slow percentage must be between 0.0 and 1.0.");
        }
        this.slowPercentage = slowPercentage;
        this.duration = duration;
    }

    /**
     * 스킬을 활성화합니다.
     * @param player 스킬을 사용하는 플레이어
     * @param opponent 상대 플레이어
     */
    @Override
    public void activate(Player player, Player opponent) {
        System.out.println("Activating SlowSkill: " + getName());

        List<Creature> enemyCreatures = opponent.getCreatures();

        if (enemyCreatures.isEmpty()) {
            System.out.println("No enemy creatures to slow down.");
            return;
        }

        for (Creature creature : enemyCreatures) {
            creature.applySlow(slowPercentage, duration);
        }

        System.out.println("All enemy creatures slowed by " + (slowPercentage * 100) + "% for " + duration + "ms.");
    }
}
