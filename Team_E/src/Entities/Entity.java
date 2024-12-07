package Entities;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import Core.Hitbox;
import Core.EntityConstants;
import Core.GameConstants;

//게임 내 엔티티(Entity) 클래스
// - 생명체(Creature), 타워(Tower) 등의 공통 속성과 동작을 정의
public abstract class Entity implements GameConstants, EntityConstants {

    private Point position;// 엔티티의 위치
    private Hitbox hitbox;// 엔티티의 히트박스

    private int teamSide;
    private int type;
    private int evolution;

	 
    // 생성자: 엔티티의 팀, 타입, 진화 단계를 설정하고 초기 위치를 정의
    public Entity(int teamSide, int type, int evolution) {
        this.type = type; //엔티티 타입 (예: TOWER_TYPE, CREATURE_TYPE 등)
        this.teamSide = teamSide; // 팀 방향 (LEFT_TEAM 또는 RIGHT_TEAM)
        this.evolution = evolution; // 엔티티의 진화 단계

        // 타워 생성
        if (type == TOWER_TYPE) {
            if (teamSide == LEFT_TEAM) {
                this.position = new Point(LEFT_TOWER); // 왼쪽 팀 타워 위치
            } else {
                this.position = new Point(RIGHT_TOWER); // 오른쪽 팀 타워 위치
            }

            // create a creature
        } else {
            if (teamSide == LEFT_TEAM) {
                this.position = new Point(LEFT_SPAWN); // 왼쪽 팀 생성 위치
            } else {
                this.position = new Point(RIGHT_SPAWN);  // 오른쪽 팀 생성 위치
            }
        }
    }

    // 가장 가까운 적(파괴 가능한 객체)을 반환
    public Destructible getEnemyAhead(Player other) {
        LinkedList<Creature> creatures = other.getCreatures();

        // 적 생명체가 없으면 적 타워를 반환
        if (creatures.isEmpty()) {
            return other.getTower();

        } else {
            if (this.teamSide == LEFT_TEAM) {
                if (creatures.getFirst().getPosition().x < other.getTower().getPosition().x) {
                    return creatures.getFirst(); // 가까운 생명체 반환
                } else {
                    return other.getTower(); // 타워 반환
                }

             // 오른쪽 팀의 경우: 가장 오른쪽의 생명체가 타워보다 가까운지 확인
            } else {
                if (creatures.getFirst().getPosition().x > other.getTower().getPosition().x) {
                    return creatures.getFirst(); // 가까운 생명체 반환
                } else {
                    return other.getTower(); // 타워 반환
                }
            }
        }
    }

    // 엔티티의 현재 위치 반환
    public Point getPosition() {
        return this.position;
    }

    // 엔티티의 위치 설정
    public void setPosition(Point position) {
        this.position = position;
    }

    // 엔티티의 히트박스 반환
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    // 엔티티의 팀 방향 반환
    public int getTeamSide() {
        return teamSide;
    }

    // 엔티티 타입 반환
    public int getType() {
        return type;
    }

    // 엔티티의 진화 단계 반환
    public int getEvolution() {
        return evolution;
    }

    // 현재 스프라이트를 반환하는 추상 메서드 (서브클래스에서 구현 필요)
    public abstract BufferedImage getCurrentSprite();

}