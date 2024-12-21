package Core;

public enum SkillType {
	WALL("적을 막기 위한 임시 벽을 생성합니다."),
	STUN("모든 적 유닛을 일정 시간 동안 기절시킵니다."),
	DAMAGE("모든 적 유닛에게 데미지를 입힙니다."),
	SLOW("적 유닛의 속도를 감소시킵니다."),
	EXECUTE("무작위 적 유닛을 즉시 처치합니다."),
	RANDOM("무작위 스킬을 발동합니다."),
	TIME_WARP("아군 유닛의 속도를 증가시키고 적의 속도를 감소시킵니다."),
	ALL_KILL("모든 적 유닛을 즉시 제거합니다."),
	GODS_WRATH("모든 적을 대상으로 강력한 공격을 가합니다."),
	MARTIAL_LAW("방어를 강화하고 적을 약화시킵니다."),
	TOWER_STRIKE("적 타워에 강력한 데미지를 입힙니다."),
	DEMONIC_DICE("무작위 효과를 발생시키는 주사위를 굴립니다."),
	HEAL("모든 아군 유닛을 회복시킵니다."),
	ATTACK_BOOST("아군 유닛의 공격력을 증가시킵니다."),
	HEAL_TOWER("아군 타워를 회복시킵니다."),
	MASS_SLOW("모든 적의 속도를 크게 감소시킵니다."),
	REGENERATION("아군 유닛의 체력을 점진적으로 회복시킵니다."),
	WEAKEN("적 유닛의 공격력을 감소시킵니다.");


    private final String description;

    SkillType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
