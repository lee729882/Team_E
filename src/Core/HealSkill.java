package Core;

import Entities.Player;

public class HealSkill extends Skill {

    private double healPercentage;

    public HealSkill(String name, SkillType type, double healPercentage) {
        super(name, type);
        if (type != SkillType.HEAL) {
            throw new IllegalArgumentException("SkillType must be HEAL for HealSkill.");
        }
        this.healPercentage = healPercentage;
    }

    @Override
    public void activate(Player player, Player opponent) {
        if (getType() == SkillType.HEAL) {
            player.healAllCreatures(healPercentage);
            System.out.println(getName() + " activated: Healing " + (healPercentage * 100) + "% of all friendly creatures' health!");
        } else {
            System.out.println("Invalid skill type for HealSkill!");
        }
    }
}
