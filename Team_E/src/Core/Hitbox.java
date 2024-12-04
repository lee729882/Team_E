package Core;

import java.awt.Point;
import java.awt.Rectangle;

// 충돌 판정을 위한 히트박스를 정의하는 클래스
public class Hitbox {

    private Point position; // 히트박스의 위치
    private int height, width; // 히트박스의 높이와 너비
    private Rectangle boundingBox; // 충돌 감지를 위한 사각형 객체

    // 생성자: 위치와 크기를 기반으로 히트박스 초기화
    public Hitbox(Point position, int w, int h) {
        this.position = position; // 히트박스 위치 설정
        this.width = w; // 히트박스 너비 설정
        this.height = h; // 히트박스 높이 설정
        this.boundingBox = new Rectangle(position.x, position.y, width, height); // 사각형 생성
    }

    // 충돌 여부를 확인하는 메서드
    // 다른 히트박스와 사각형이 교차하면 충돌로 간주
    public boolean checkCollide(Hitbox other) {
        if (this.boundingBox.intersects(other.getBoundingBox())) { // 교차 여부 확인
            return true; // 충돌 발생
        } else {
            return false; // 충돌 없음
        }
    }

    // 히트박스의 위치를 업데이트
    public void update(Point point) {
        this.position = point; // 새로운 위치 설정
        this.boundingBox.setLocation(this.position); // 사각형의 위치도 갱신
    }

    // -----------------------------------------
    // Getter 및 Setter 메서드

    // 히트박스의 위치 반환
    public Point getPosition() {
        return position;
    }

    // 히트박스의 위치 설정
    public void setPosition(Point position) {
        this.position = position;
    }

    // 히트박스의 높이 반환
    public int getHeight() {
        return height;
    }

    // 히트박스의 너비 반환
    public int getWidth() {
        return width;
    }

    // 충돌 감지를 위한 사각형 객체 반환
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
