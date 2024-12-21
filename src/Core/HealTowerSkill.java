package Core;

import Entities.Player;

public class HealTowerSkill extends Skill {

    private int healAmount; // 회복량

    public HealTowerSkill(String name, SkillType type, int healAmount) {
        super(name, type);
        if (type != SkillType.HEAL_TOWER) {
            throw new IllegalArgumentException("SkillType must be HEAL_TOWER for HealTowerSkill.");
        }
        this.healAmount = healAmount;
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println(getName() + " activated: Healing tower for " + healAmount + " HP.");
        player.healTower(healAmount);
    }
}
