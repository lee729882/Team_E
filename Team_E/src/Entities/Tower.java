package Entities;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.awt.Color;

//타워(Tower) 클래스
//- Destructible를 상속받아 타워의 동작(터렛 추가/제거, 그리기 등)을 처리
public class Tower extends Destructible {
	private LinkedList<Turret> turrets = new LinkedList<Turret>(); // 타워에 배치된 터렛 리스트
    private BufferedImage[] towerSprites; // 타워의 스프라이트 배열
    private BufferedImage[] turretSprites; // 터렛의 스프라이트 배열
    private BufferedImage[] projectileSprites; // 투사체의 스프라이트 배열
    private BufferedImage currentSprite; // 현재 타워 스프라이트

    // 생성자: 타워 초기화
    public Tower(int teamSide, int type, int evolution, BufferedImage[] towerSprites, BufferedImage[] turretSprites,
            BufferedImage[] projectileSprites) {
    	super(teamSide, type, evolution); // 부모 클래스(Destructible) 생성자 호출
        this.towerSprites = towerSprites; // 타워 스프라이트 설정
        this.turretSprites = turretSprites; // 터렛 스프라이트 설정
        this.currentSprite = this.towerSprites[evolution]; // 현재 타워 스프라이트 설정
        this.projectileSprites = projectileSprites; // 투사체 스프라이트 설정

        // 터렛 슬롯 초기화 (비어 있는 상태로 설정)
        for (int i = 0; i < MAX_TURRET_SPOTS; i++) {
            this.turrets.add(null);
        }
    }
    
    // 터렛 추가
    public void addTurret(int turretSpot) {
        this.turrets.set(turretSpot, new Turret(getTeamSide(), turretSpot, getEvolution(),
                turretSprites[this.getEvolution()], projectileSprites[this.getEvolution()]));
    }

    // 터렛 제거
    public void removeTurret(int turretSpot) {
        this.turrets.remove(turretSpot);
    }

    // 타워 및 터렛을 화면에 그리기
    public void draw(Graphics g) {
    	int healthBar; // 현재 체력 바의 길이
        int percentage; // 체력 퍼센트
        int healthBarX; // 체력 바의 x 좌표
        double fullBar = (double) TOWER_WIDTH; // 체력 바의 전체 길이
         
        // 팀 방향에 따라 체력 바 위치 설정
        if (this.getTeamSide() == LEFT_TEAM) {
            percentage = (int) ((this.getHealth() * 100.0f) / TOWER_HEALTH);
            healthBar = (int) (fullBar * percentage / 100.0); // 체력 바의 길이 계산
            g.setColor(Color.RED);
            g.fillRect(this.getPosition().x, this.getPosition().y - 30, healthBar, 25);
        } else {
            percentage = (int) ((this.getHealth() * 100.0f) / TOWER_HEALTH);
            healthBar = (int) (fullBar * percentage / 100.0); // 체력 바의 길이 계산
            healthBarX = this.getPosition().x + TOWER_WIDTH - healthBar - 2; // 오른쪽 팀 체력 바의 시작 x 좌표
            g.setColor(Color.RED);
            g.fillRect(healthBarX, this.getPosition().y - 30, healthBar - 2, 25);
        }
        
        // 타워 스프라이트 그리기
        g.drawImage(this.currentSprite, this.getPosition().x, this.getPosition().y, null);
        
        // 터렛 그리기
        if (this.turrets.size() > 0) {
            for (Turret turret : this.turrets) {
                if (turret != null) {
                    turret.draw(g);
                }
            }
        }
    }

    // 현재 타워 스프라이트 반환
    public BufferedImage getCurrentSprite() {
        return this.currentSprite;
    }
    // 현재 타워 스프라이트 설정
    public void setCurrentSprite(BufferedImage currentSprite) {
        this.currentSprite = currentSprite;
    }
    // 터렛 리스트 반환
    public LinkedList<Turret> getTurrets() {
        return turrets;
    }

}
