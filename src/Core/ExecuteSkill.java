package Core;

import Entities.Creature;
import Entities.Player;
import java.util.List;
import java.util.Random;

/**
 * ExecuteSkill 클래스는 적의 무작위 유닛을 제거하는 스킬을 정의합니다.
 * 이 클래스는 독립적으로 설계되어, 다양한 상황에서 사용할 수 있습니다.
 */
public class ExecuteSkill extends Skill {

    private double successRate; // 처형 성공 확률

    /**
     * ExecuteSkill 생성자
     * @param name 스킬 이름
     * @param type 스킬 타입 (SkillType.EXECUTE)
     * @param successRate 성공 확률 (0.0 ~ 1.0)
     */
    public ExecuteSkill(String name, SkillType type, double successRate) {
        super(name, type);
        if (type != SkillType.EXECUTE) {
            throw new IllegalArgumentException("SkillType must be EXECUTE for ExecuteSkill.");
        }
        if (successRate < 0.0 || successRate > 1.0) {
            throw new IllegalArgumentException("Success rate must be between 0.0 and 1.0.");
        }
        this.successRate = successRate;
    }

    /**
     * 스킬을 활성화합니다.
     * @param player 스킬을 사용하는 플레이어
     * @param opponent 상대 플레이어
     */
    @Override
    public void activate(Player player, Player opponent) {
        System.out.println("Attempting to activate ExecuteSkill: " + getName());

        // 적 유닛 리스트 가져오기
        List<Creature> enemyCreatures = opponent.getCreatures();

        if (enemyCreatures.isEmpty()) {
            System.out.println("No enemy creatures to execute.");
            return;
        }

        // 성공 확률 검사
        if (new Random().nextDouble() > successRate) {
            System.out.println("ExecuteSkill failed due to probability check.");
            return;
        }

        // 무작위 적 유닛 선택
        Creature target = enemyCreatures.get(new Random().nextInt(enemyCreatures.size()));

        // 유닛 제거
        opponent.getCreatures().remove(target);
        System.out.println("Executed creature: " + target + ". Remaining creatures: " + enemyCreatures.size());

        // 로그 출력
        logExecution(target, opponent);
    }

    /**
     * 처형 로그를 출력합니다.
     * @param target 제거된 유닛
     * @param opponent 상대 플레이어
     */
    private void logExecution(Creature target, Player opponent) {
        System.out.println("[LOG] ExecuteSkill activated:");
        System.out.println("[LOG] Target Creature: " + target);
        System.out.println("[LOG] Remaining creatures for " + opponent.getUserName() + ": " + opponent.getCreatures().size());
    }

    /**
     * 성공 확률을 반환합니다.
     * @return 성공 확률
     */
    public double getSuccessRate() {
        return successRate;
    }

    /**
     * 성공 확률을 설정합니다.
     * @param successRate 성공 확률 (0.0 ~ 1.0)
     */
    public void setSuccessRate(double successRate) {
        if (successRate < 0.0 || successRate > 1.0) {
            throw new IllegalArgumentException("Success rate must be between 0.0 and 1.0.");
        }
        this.successRate = successRate;
    }

    /**
     * 디버깅용 상태 출력 메서드
     */
    public void debugState() {
        System.out.println("[DEBUG] ExecuteSkill State:");
        System.out.println("[DEBUG] Name: " + getName());
        System.out.println("[DEBUG] Type: " + getType());
        System.out.println("[DEBUG] Success Rate: " + successRate);
    }
}
