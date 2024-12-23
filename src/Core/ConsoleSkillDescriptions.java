package Core;

public class ConsoleSkillDescriptions {

    public static void main(String[] args) {
        // 게임 설명 출력
        printGameDescription();

        // 각 스킬의 설명 출력
        for (SkillType skill : SkillType.values()) {
            System.out.println(skill.name() + ":\n" + skill.getDescription() + "\n");
        }
    }

    // 게임 설명을 출력하는 메서드
    private static void printGameDescription() {
        System.out.println("=== 게임 설명 ===");
        System.out.println("이 게임은 전략과 스킬을 활용하여 적을 물리치고 자신의 방어를 강화하는 전투 게임입니다.");
        System.out.println("플레이어는 다양한 스킬을 선택하여 적의 진격을 막거나, 아군을 지원하며 승리를 이끌어야 합니다.");
        System.out.println("각 스킬은 독특한 효과를 가지고 있으며, 전략적인 사용이 승패를 좌우합니다.");
        System.out.println("게임을 시작하기 전에 스킬 설명을 읽고, 자신에게 적합한 스킬을 선택하세요.\n");
    }
}
