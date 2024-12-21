package Core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import Entities.Player;

public class TowerStrikeSkill extends Skill {

    private int damage; // 레이저 피해량
    private ArrayList<Point> strikeLocations; // 레이저 위치 저장

    public TowerStrikeSkill(String name, SkillType type, int damage) {
        super(name, type);
        if (type != SkillType.TOWER_STRIKE) {
            throw new IllegalArgumentException("SkillType must be TOWER_STRIKE for TowerStrikeSkill.");
        }
        this.damage = damage;
        this.strikeLocations = new ArrayList<>();
    }

    @Override
    public void activate(Player player, Player opponent) {
        System.out.println(getName() + " skill activated!");

        // 적 타워의 위치 가져오기
        Point towerPosition = opponent.getTower().getPosition();

        // 레이저 위치 저장
        strikeLocations.clear();
        strikeLocations.add(new Point(towerPosition.x + 50, towerPosition.y)); // 중앙에 레이저 위치 설정

        // 타워에 데미지 적용
        opponent.getTower().takeDamage(damage);
        System.out.println("Tower hit at position: " + towerPosition + " for " + damage + " damage.");
    }

    // 그래픽 효과를 그리는 메서드
    public void draw(Graphics g) {
        if (strikeLocations.isEmpty()) {
            return;
        }

        g.setColor(Color.RED); // 빨간색 레이저 설정
        for (Point strike : strikeLocations) {
            // 레이저 선을 크게 그리기 (너비를 조정)
            g.fillRect(strike.x - 15, 0, 30, strike.y + 100); // 레이저가 더 아래로 내려오도록 Y 값 조정

            // 충돌 지점 표시
            g.fillOval(strike.x - 20, strike.y + 100, 40, 40); // 큰 원으로 충돌 지점 강조
        }
    }
}
