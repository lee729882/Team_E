package Core;

import Entities.Player;

public class MassSlowSkill extends Skill {
    private double slowPercentage; // 속도 감소 비율
    private long duration; // 지속 시간

    public MassSlowSkill(String name, SkillType type, double slowPercentage, long duration) {
        super(name, type);
        if (type != SkillType.MASS_SLOW) {
            throw new IllegalArgumentException("SkillType must be MASS_SLOW for MassSlowSkill.");
        }
        this.slowPercentage = slowPercentage;
        this.duration = duration;
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println(getName() + " activated: Slowing all enemy creatures by " + (slowPercentage * 100) + "% for " + duration + "ms.");
        opponent.applySlowToAllCreatures(slowPercentage, duration);
    }
}
