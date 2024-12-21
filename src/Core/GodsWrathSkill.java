package Core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import Entities.Player;
import Entities.Creature;

/**
 * GodsWrathSkill 클래스는 파란색 레이저로 적 유닛들에게 피해를 입히는 스킬
 */
public class GodsWrathSkill extends Skill {

    private int damage; // 각 레이저가 입히는 피해량
    private ArrayList<Point> strikeLocations; // 레이저가 떨어지는 위치
    private boolean isActive; // 스킬이 활성 상태인지 여부

    /**
     * GodsWrathSkill 객체를 생성합니다.
     *
     * @param name   스킬의 이름.
     * @param type   스킬의 유형 (GODS_WRATH이어야 함).
     * @param damage 각 레이저가 입히는 피해량.
     */
    public GodsWrathSkill(String name, SkillType type, int damage) {
        super(name, type);
        if (type != SkillType.GODS_WRATH) {
            throw new IllegalArgumentException("SkillType은 GODS_WRATH이어야 합니다.");
        }
        this.damage = damage;
        this.strikeLocations = new ArrayList<>();
        this.isActive = false;
    }

    /**
     * 스킬을 활성화하여 모든 적 유닛에게 피해를 입히고 레이저 효과를 생성
     *
     * @param player   스킬을 사용하는 플레이어.
     * @param opponent 스킬의 효과를 받는 상대 플레이어.
     */
    @Override
    public void activate(Player player, Player opponent) {
        if (isActive) {
            System.out.println("신의 격노가 이미 활성화되어 있습니다.");
            return;
        }

        isActive = true;
        strikeLocations.clear();

        // 각 적 유닛에 대한 레이저 위치 생성
        opponent.getCreatures().forEach(creature -> {
            Point creaturePosition = new Point(creature.getPosition());
            strikeLocations.add(creaturePosition);

            // 유닛에 피해를 입힘
            creature.takeDamage(damage);
            System.out.println("레이저가 위치 " + creaturePosition + "에서 유닛에 " + damage + " 피해를 입혔습니다.");
        });

        System.out.println("신의 격노 스킬이 활성화되었습니다.");

        // 일정 시간 후 스킬 비활성화
        new Timer(1000, e -> {
            isActive = false;
            System.out.println("신의 격노 스킬 효과가 종료되었습니다.");
        }).start();
    }

    /**
     * 스킬의 그래픽 효과
     *
     * @param g 그리기에 사용되는 Graphics 객체.
     */
    public void draw(Graphics g) {
        if (!isActive || strikeLocations.isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g; // Graphics2D로 변환
        Color originalColor = g2d.getColor(); // 기존 색상 저장
        BasicStroke originalStroke = (BasicStroke) g2d.getStroke(); // 기존 선 스타일 저장

        g2d.setColor(Color.BLUE); // 레이저의 색상을 파란색으로 설정
        g2d.setStroke(new BasicStroke(15)); // 레이저 두께를 15로 설정

        // 각 레이저 위치에 레이저 효과를 그림
        for (Point strike : strikeLocations) {
            g2d.drawLine(strike.x, 0, strike.x, 600); // 두꺼운 레이저 선 그리기 (바닥까지)
            g2d.fillOval(strike.x - 15, 600 - 15, 30, 30); // 충돌 지점 그리기
        }

        // 기존 그래픽 설정 복원
        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }

    /**
     * 스킬의 레이저 위치 리스트를 반환
     *
     * @return 레이저 위치 리스트.
     */
    public ArrayList<Point> getStrikeLocations() {
        return strikeLocations;
    }

    /**
     * 스킬이 현재 활성 상태인지 여부를 반환합니다.
     *
     * @return 스킬이 활성 상태이면 true, 그렇지 않으면 false.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * 각 레이저가 입히는 피해량을 반환
     *
     * @return 피해량 값.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * 각 레이저가 입히는 피해량을 설정
     *
     * @param damage 새로운 피해량 값.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * 스킬의 현재 상태를 디버깅용으로 출력
     */
    public void debugState() {
        System.out.println("[DEBUG] 신의 격노 상태:");
        System.out.println("활성화 여부: " + isActive);
        System.out.println("피해량: " + damage);
        System.out.println("레이저 위치: " + strikeLocations);
    }
}
