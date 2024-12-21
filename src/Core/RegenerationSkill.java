package Core;

import java.util.Timer;
import java.util.TimerTask;

import Entities.Player;
//ㅁㄴㅇㅁㄴㅇ
public class RegenerationSkill extends Skill {
    private int healAmount; // 매초 회복량
    private long duration; // 지속 시간

    public RegenerationSkill(String name, SkillType type, int healAmount, long duration) {
        super(name, type);
        if (type != SkillType.REGENERATION) {
            throw new IllegalArgumentException("SkillType must be REGENERATION for RegenerationSkill.");
        }
        this.healAmount = healAmount;
        this.duration = duration;
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println(getName() + " activated: Regenerating " + healAmount + " HP for all creatures every second.");
        new Timer().scheduleAtFixedRate(new TimerTask() {
            private long elapsedTime = 0;

            @Override
            public void run() {
                if (elapsedTime >= duration) {
                    this.cancel();
                    return;
                }
                player.healAllCreatures(healAmount / 100.0); // 비율에 따라 체력 회복
                elapsedTime += 1000; // 1초 단위로 실행
            }
        }, 0, 1000);
    }
}
