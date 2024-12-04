package Core;

// 엔티티 관련 상수를 정의하는 인터페이스
public interface EntityConstants {

    // 타워와 터렛 관련 상수
    public static final int TOWER_WIDTH = 108; // 타워의 너비
    public static final int TOWER_HEIGHT = 185; // 타워의 높이
    public static final int TOWER_HEALTH = 500; // 타워의 체력
    public static final int TURRET_WIDTH = 28; // 터렛의 너비
    public static final int TURRET_HEIGHT = 15; // 터렛의 높이
    public static final int TURRET_MULTIPLIER = 5; // 터렛 스탯 배율

    // 터렛 관련 스탯
    public static final int TURRET_HEALTH = 10; // 터렛의 체력
    public static final int TURRET_ATTACK = 10; // 터렛의 공격력
    public static final int TURRET_RANGE = 150; // 터렛의 공격 범위
    public static final long TURRET_ATTACK_SPEED = 600; // 터렛의 공격 속도 (밀리초 단위)
    public static final int BASE_GOLD_BUY = 1000; // 터렛 구매 비용
    public static final int BASE_GOLD_SELL = 500; // 터렛 판매 시 획득 골드
    public static final int TURRET_UPGRADE = 3; // 터렛 업그레이드 최대 단계
    public static final int MAX_TURRET_SPOTS = 3; // 터렛 설치 가능 최대 개수

    // 터렛 타입
    public static final int FIRST_TURRET = 0; // 첫 번째 터렛
    public static final int SECOND_TURRET = 1; // 두 번째 터렛
    public static final int THIRD_TURRET = 2; // 세 번째 터렛

    // 생명체 관련 상수
    public static final int CREATURE_HEIGHT = 44; // 생명체의 높이
    public static final int CREATURE_WIDTH = 19; // 생명체의 너비
    public static final double KILL_REWARD = 0.5; // 처치 시 보상 비율
    public static final int INITIAL_SPRITE_INDEX = 0; // 초기 스프라이트 인덱스
    public static final int IDLE_ACTIVITY = 0; // 대기 상태
    public static final int MOVE_ACTIVITY = 1; // 이동 상태
    public static final int ATTACK_ACTIVITY = 2; // 공격 상태
    public static final int FIRST_SPRITE = 0; // 첫 번째 스프라이트
    public static final int LAST_MOVE_SPRITE = 7; // 이동 스프라이트 마지막 인덱스
    public static final int LAST_ATTACK_SPRITE = 2; // 공격 스프라이트 마지막 인덱스
    public static final int IDLE_SPRITE = 0; // 대기 스프라이트
    public static final int ARM_HEIGHT = 13; // 팔 높이
    public static final int[] CREATURE_HEALTHS = { 100, 50, 200, 70 }; // 생명체별 체력
    public static final int[] CREATURE_DAMAGES = { 7, 5, 10, 15 }; // 생명체별 공격력
    public static final int[] CREATURE_RANGES = { 7, 120, 4, 100 }; // 생명체별 공격 범위
    public static final int[] STAT_MULTIPLIERS = { 2, 2, 3, 3 }; // 스탯 배율

    // 첫 번째 생명체 기본 스탯
    public static final int FIRST_COST = 200; // 비용
    public static final double FIRST_KILL_GOLD = FIRST_COST * KILL_REWARD; // 처치 보상 골드
    public static final int FIRST_HEALTH = 100; // 체력
    public static final int FIRST_ATTACK = 7; // 공격력
    public static final int FIRST_RANGE = 7; // 공격 범위
    public static final int FIRST_SPEED = 4; // 이동 속도
    public static final long FIRST_ATTACK_SPEED = 600; // 공격 속도
    public static final int FIRST_MULTIPLIER = 2; // 배율

    // 두 번째 생명체 기본 스탯
    public static final int SECOND_COST = 300;
    public static final double SECOND_KILL_GOLD = SECOND_COST * KILL_REWARD;
    public static final int SECOND_HEALTH = 50;
    public static final int SECOND_ATTACK = 5;
    public static final int SECOND_RANGE = 120;
    public static final int SECOND_SPEED = 4;
    public static final long SECOND_ATTACK_SPEED = 600;
    public static final int SECOND_MULTIPLIER = 2;

    // 세 번째 생명체 기본 스탯
    public static final int THIRD_COST = 500;
    public static final double THIRD_KILL_GOLD = THIRD_COST * KILL_REWARD;
    public static final int THIRD_HEALTH = 200;
    public static final int THIRD_ATTACK = 10;
    public static final int THIRD_RANGE = 5;
    public static final int THIRD_SPEED = 4;
    public static final long THIRD_ATTACK_SPEED = 800;
    public static final int THIRD_MULTIPLIER = 3;

    // 네 번째 생명체 기본 스탯
    public static final int FOURTH_COST = 600;
    public static final double FOURTH_KILL_GOLD = FOURTH_COST * KILL_REWARD;
    public static final int FOURTH_HEALTH = 100;
    public static final int FOURTH_ATTACK = 20;
    public static final int FOURTH_RANGE = 100;
    public static final int FOURTH_SPEED = 4;
    public static final long FOURTH_ATTACK_SPEED = 700;
    public static final int FOURTH_MULTIPLIER = 3;

    // 투사체 관련 상수
    public static final int PROJECTILE_SPEED = 5; // 투사체 속도
    public static final int TURRET_PROJECTILE = 1; // 터렛 투사체
    public static final int SECOND_PROJECTILE = 2; // 두 번째 투사체
    public static final int FOURTH_PROJECTILE = 3; // 네 번째 투사체

    // 투사체 크기
    public static final int MISSILE_WIDTH = 25; // 미사일 너비
    public static final int MISSILE_HEIGHT = 11; // 미사일 높이
    public static final int ARROW_WIDTH = 30; // 화살 너비
    public static final int ARROW_HEIGHT = 10; // 화살 높이
    public static final int FRUIT_WIDTH = 12; // 과일 너비
    public static final int FRUIT_HEIGHT = 12; // 과일 높이

}
