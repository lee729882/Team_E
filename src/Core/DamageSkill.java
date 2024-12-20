package Core;

import Entities.Player;

public class DamageSkill extends Skill {

    private double damagePercentage; // 체력에 비례한 데미지 비율

    public DamageSkill(String name, SkillType type, double damagePercentage) {
        super(name, type);
        this.damagePercentage = damagePercentage;
    }

    @Override
    public void activate(Player player, Player opponent) {
        // 상대 플레이어의 모든 유닛에 체력 비례 데미지를 입힘
        if (getType() == SkillType.DAMAGE) {
            opponent.damageAllCreaturesByPercentage(damagePercentage);
            System.out.println(getName() + " activated: Dealing " + (damagePercentage * 100) + "% damage to all enemy creatures!");
        } else {
            System.out.println("Invalid skill type for DamageSkill!");
        }
    }
}
