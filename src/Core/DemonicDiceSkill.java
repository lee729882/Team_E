package Core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Entities.Creature;
import Entities.Player;

public class DemonicDiceSkill extends Skill {

    private int killCount; // 즉사시킬 유닛의 수

    public DemonicDiceSkill(String name, SkillType type, int killCount) {
        super(name, type);
        if (type != SkillType.DEMONIC_DICE) {
            throw new IllegalArgumentException("SkillType must be DEMONIC_DICE for DemonicDiceSkill.");
        }
        this.killCount = killCount;
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println("Demonic Dice skill activated!");

        // 아군과 적군 유닛 리스트 병합
        List<Creature> allCreatures = new ArrayList<>();
        allCreatures.addAll(player.getCreatures()); // 아군 유닛 추가
        allCreatures.addAll(opponent.getCreatures()); // 적군 유닛 추가

        // 유닛 리스트를 무작위로 섞기
        Collections.shuffle(allCreatures);

        // 무작위로 killCount 만큼의 유닛 제거
        for (int i = 0; i < Math.min(killCount, allCreatures.size()); i++) {
            Creature target = allCreatures.get(i);
            target.takeDamage(target.getHealth()); // 즉사 처리
            System.out.println("Killed creature: " + target);
        }
    }
    public void draw(Graphics g, List<Creature> killedCreatures) {
        g.setColor(Color.RED); // 빨간색 표시
        for (Creature creature : killedCreatures) {
            Point position = creature.getPosition(); // 유닛의 위치 가져오기
            g.fillOval(position.x - 10, position.y - 10, 20, 20); // 유닛 위에 붉은 원 표시
        }
    }


}
