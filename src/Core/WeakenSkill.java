package Core;

import Entities.Player;
import Entities.Creature;
import java.util.List;

public class WeakenSkill extends Skill {

    private double weakenPercentage; // 공격력 감소 비율
    private long duration; // 지속 시간 (밀리초)

    public WeakenSkill(String name, double weakenPercentage, long duration) {
        super(name, SkillType.WEAKEN);
        this.weakenPercentage = weakenPercentage;
        this.duration = duration;
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println(getName() + " activated: Weakening enemy creatures.");

        // 상대 플레이어의 모든 유닛 약화
        List<Creature> enemyCreatures = opponent.getCreatures();
        for (Creature creature : enemyCreatures) {
            double originalDamage = creature.getDamage();
            int weakenedDamage = (int) (originalDamage * (1.0 - weakenPercentage));
            creature.setDamage(weakenedDamage);

            System.out.println("Weakened creature: Original Damage = " + originalDamage 
                                + ", Weakened Damage = " + weakenedDamage);

            // 일정 시간 후 공격력을 복구
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    creature.setDamage((int) originalDamage);
                    System.out.println("Restored creature's damage: " + originalDamage);
                }
            }, duration);
        }
    }
}
