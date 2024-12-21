package Core;

import Entities.Player;

public class AllKillSkill extends Skill {

    public AllKillSkill(String name, SkillType type) {
        super(name, type);
        if (type != SkillType.ALL_KILL) {
            throw new IllegalArgumentException("SkillType must be ALL_KILL for AllKillSkill.");
        }
    }

    @Override
    public void activate(Player player, Player opponent) {
        // 상대방 모든 유닛 제거
        if (!opponent.getCreatures().isEmpty()) {
            opponent.getCreatures().clear();
            System.out.println(getName() + " activated: All enemy creatures have been killed!");
        } else {
            System.out.println(getName() + " activated, but there were no creatures to kill.");
        }
    }
}
