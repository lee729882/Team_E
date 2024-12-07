package Entities;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

//원거리 공격 생명체(Ranged) 클래스
//- Creature 클래스를 상속하며, 투사체(Projectile)를 생성하고 공격 동작을 처리
public class Ranged extends Creature {

	 private LinkedList<Projectile> projectiles = new LinkedList<Projectile>(); // 생성된 투사체 리스트
	 private BufferedImage projectileSprite; // 투사체의 스프라이트 이미지
	 private int projectileType; // 투사체의 타입 (SECOND_PROJECTILES 또는 FOURTH_PROJECTILES)

    public Ranged(int team, int type, int evolution, BufferedImage[] move, BufferedImage[] attack,
            BufferedImage projectileSprite) {
        super(team, type, evolution, move, attack); // 부모 클래스(Creature) 생성자 호출
        this.projectileSprite = projectileSprite; // 투사체 이미지 설정
        if (type == SECOND_TYPE) {
            this.projectileType = SECOND_PROJECTILES; // 두 번째 타입의 투사체
        } else {
            this.projectileType = FOURTH_PROJECTILES; // 네 번째 타입의 투사체
        }
    }

    // 공격 동작: 투사체를 생성하고 타겟을 설정
    public void attack(Destructible target) {
    	this.setCurrentTarget(target); // 공격 타겟 설정
        Point projPos; // 투사체 생성 위치
        
        // 공격 애니메이션 스프라이트 업데이트
        if (this.getCurrentAttackIndex() == LAST_ATTACK_SPRITE) {
            this.setCurrentAttackIndex(FIRST_SPRITE); // 첫 번째 스프라이트로 초기화
        } else {
            this.setCurrentAttackIndex(this.getCurrentAttackIndex() + 1); // 다음 스프라이트로 진행
        }
        this.setCurrentSprite(this.getAttackSprites()[this.getCurrentAttackIndex()]);

        // 팀 방향에 따라 투사체 생성 위치 설정
        if (this.getTeamSide() == LEFT_TEAM) {
            projPos = new Point(this.getPosition().x + CREATURE_WIDTH, this.getPosition().y + ARM_HEIGHT);
            // 새로운 투사체 생성 및 리스트에 추가
            Projectile projectile = new Projectile(this.getTeamSide(), this.projectileType, this.getDamage(), projPos,
                    projectileSprite);
            this.projectiles.add(projectile);
            this.setTimeStartedAttack(System.currentTimeMillis());
        } else {
            projPos = new Point(this.getPosition().x, this.getPosition().y + ARM_HEIGHT);
            // 새로운 투사체 생성 및 리스트에 추가
            Projectile projectile = new Projectile(this.getTeamSide(), this.projectileType, this.getDamage(), projPos,
                    projectileSprite);
            this.projectiles.add(projectile);
         // 공격 시작 시간 업데이트
            this.setTimeStartedAttack(System.currentTimeMillis());
        }
    }

    // 원거리 생명체와 투사체를 화면에 그리기
    public void draw(Graphics g) {
    	// 생명체 그리기
        g.drawImage(this.getCurrentSprite(), this.getPosition().x, this.getPosition().y, null);
        // 투사체 그리기
        for (int i = 0; i < this.projectiles.size(); i++) {
            this.projectiles.get(i).draw(g);
        }
    }

    // 현재 생명체가 생성한 투사체 리스트 반환
    public LinkedList<Projectile> getProjectiles() {
        return this.projectiles;
    }

}