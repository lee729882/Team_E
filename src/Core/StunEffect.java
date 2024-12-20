package Core;

public class StunEffect {
    private boolean active; // 스턴 활성화 여부
    private long endTime;   // 스턴 종료 시간

    // 기본 생성자
    public StunEffect() {
        this.active = false;
        this.endTime = 0;
    }

    // 스턴 효과를 적용
    public void apply(long duration) {
        this.active = true;
        this.endTime = System.currentTimeMillis() + duration;
        System.out.println("Stun applied for " + duration + "ms");
    }

    // 스턴 상태 업데이트
    public void update() {
        if (active && System.currentTimeMillis() >= endTime) {
            this.active = false;
            System.out.println("Stun expired");
        }
    }

    // 현재 스턴 상태를 반환
    public boolean isActive() {
        return active;
    }
}
