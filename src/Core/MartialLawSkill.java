package Core;

import java.awt.Point;
import Entities.Player;
import Entities.Creature;

/**
 * MartialLawSkill 클래스는 계엄령 스킬을 나타내며, 비용 없이 특정 병사를 여러 명 소환합니다.
 */
public class MartialLawSkill extends Skill {

    private int summonCount; // 소환할 병사의 수

    /**
     * MartialLawSkill 객체를 생성합니다.
     *
     * @param name         스킬의 이름.
     * @param type         스킬의 유형 (MARTIAL_LAW이어야 함).
     * @param summonCount  소환할 병사의 수.
     */
    public MartialLawSkill(String name, SkillType type, int summonCount) {
        super(name, type);
        if (type != SkillType.MARTIAL_LAW) {
            throw new IllegalArgumentException("SkillType은 MARTIAL_LAW이어야 합니다.");
        }
        this.summonCount = summonCount;
    }

    /**
     * 스킬을 활성화하여 비용 없이 특정 병사를 소환합니다.
     *
     * @param player   스킬을 사용하는 플레이어.
     * @param opponent 상대 플레이어 (사용되지 않음).
     */
    @Override
    public void activate(Player player, Player opponent) {
        System.out.println("계엄령 스킬이 활성화되었습니다: 비용 없이 병사 소환.");

        for (int i = 0; i < summonCount; i++) {
            Creature creature = player.createCreature(Player.FOURTH_TYPE); // 4번째 병사 생성
            Point spawnPosition = player.getTeamSide() == Player.LEFT_TEAM
                    ? new Point(Player.LEFT_SPAWN)
                    : new Point(Player.RIGHT_SPAWN);

            creature.setPosition(spawnPosition);
            player.getCreatures().add(creature);

            System.out.println("병사가 소환되었습니다: 위치 = " + spawnPosition);
        }
    }

    /**
     * 소환할 병사의 수를 반환합니다.
     *
     * @return 소환할 병사의 수.
     */
    public int getSummonCount() {
        return summonCount;
    }

    /**
     * 소환할 병사의 수를 설정합니다.
     *
     * @param summonCount 새로운 병사 수.
     */
    public void setSummonCount(int summonCount) {
        this.summonCount = summonCount;
    }
}
