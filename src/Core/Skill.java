package Core;

import java.util.Random;

import Entities.Player;

public class Skill {
    private String name;
    private SkillType type;

    public Skill(String name, SkillType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public SkillType getType() {
        return type;
    }

    // 스킬 효과 발동
    public void activate(Player player, Player opponent) {
        switch (type) {
            case WALL:
                player.getTower().addTemporaryWall(); // 임시 벽 생성
                break;
            case STUN:
                opponent.stunAllCreatures(3000); // 3초 동안 스턴
                break;
            case DAMAGE:
                opponent.damageAllCreaturesByPercentage(0.1); // 체력 10% 데미지
                break;
            case SLOW:
                player.getCreatures(); // 아군 유닛 체력 완전 회복
                break;
            case EXECUTE:
                opponent.executeRandomCreature(); // 적 유닛 하나 제거
                break;
            case RANDOM:
                activateRandomSkill(player, opponent); // 랜덤 스킬 발동
                break;
            case TIME_WARP:
                System.out.println("Time Warp is a specialized skill. Please use the TimeWarpSkill class.");
                break;
            default:
                throw new IllegalArgumentException("Unknown skill type: " + type);
        }
    }

    private void activateRandomSkill(Player player, Player opponent) {
        Random random = new Random();
        SkillType[] randomSkills = {SkillType.WALL, SkillType.STUN, SkillType.DAMAGE, SkillType.SLOW, SkillType.EXECUTE};
        SkillType randomSkill = randomSkills[random.nextInt(randomSkills.length)];
        new Skill(randomSkill.name(), randomSkill).activate(player, opponent);
    }
}
