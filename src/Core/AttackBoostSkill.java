package Core;

import Entities.Player;

public class AttackBoostSkill extends Skill {
    private double boostPercentage; // 공격력 증가 비율
    private long duration; // 지속 시간

    public AttackBoostSkill(String name, SkillType type, double boostPercentage, long duration) {
        super(name, type);
        if (type != SkillType.ATTACK_BOOST) {
            throw new IllegalArgumentException("SkillType must be ATTACK_BOOST for AttackBoostSkill.");
        }
        this.boostPercentage = boostPercentage;
        this.duration = duration;
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println(getName() + " activated: Boosting attack by " + (boostPercentage * 100) + "% for " + duration + "ms.");
        player.boostAttackOfAllCreatures(boostPercentage, duration);
    }
}
