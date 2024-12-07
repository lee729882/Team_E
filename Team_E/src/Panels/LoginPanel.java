package Panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Core.ButtonListener;
import Core.GameConstants;

// 로그인 패널(LoginPanel) 클래스
// - 게임 시작 전 플레이어 이름을 입력받는 화면을 구성
public class LoginPanel extends JPanel implements GameConstants {

    static JTextField firstLoginField, secondLoginField; // 플레이어 1과 플레이어 2의 사용자 이름 입력 필드

    // 생성자: 로그인 패널 초기화
    public LoginPanel() {
        setLayout(null); // 레이아웃 매니저 사용하지 않고 수동으로 컴포넌트 배치

        // 배경 이미지 설정
        ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "Login.jpg"));
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(background);

        // 플레이어 1 이름 입력 레이블
        JLabel firstLoginLabel = new JLabel("Enter player 1 username: ");
        // 플레이어 2 이름 입력 레이블
        JLabel secondLoginLabel = new JLabel("Enter player 2 username: ");
        // 레이블 폰트 및 색상 설정
        firstLoginLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        secondLoginLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        firstLoginLabel.setForeground(Color.WHITE);
        secondLoginLabel.setForeground(Color.WHITE);

        // 플레이어 1과 2 이름 입력 필드 생성
        firstLoginField = new JTextField(20); // 플레이어 1 입력 필드
        secondLoginField = new JTextField(20); // 플레이어 2 입력 필드

        // 확인 버튼 생성
        JButton OKButton = new JButton("OK"); // "OK" 버튼 생성
        OKButton.setActionCommand("OK"); // 버튼의 액션 커맨드 설정

        // 컴포넌트 추가
        add(OKButton); // 확인 버튼 추가
        add(firstLoginLabel); // 플레이어 1 레이블 추가
        add(secondLoginLabel); // 플레이어 2 레이블 추가
        add(firstLoginField); // 플레이어 1 입력 필드 추가
        add(secondLoginField); // 플레이어 2 입력 필드 추가
        add(backgroundLabel); // 배경 레이블 추가

        // 컴포넌트 위치 및 크기 설정
        backgroundLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // 배경 이미지 크기 및 위치
        firstLoginLabel.setBounds(350, 295, 350, 50); // 플레이어 1 레이블 크기 및 위치
        firstLoginField.setBounds(670, 303, 250, 40); // 플레이어 1 입력 필드 크기 및 위치
        secondLoginLabel.setBounds(350, 365, 350, 50); // 플레이어 2 레이블 크기 및 위치
        secondLoginField.setBounds(670, 373, 250, 40); // 플레이어 2 입력 필드 크기 및 위치
        OKButton.setBounds(505, 550, 200, 60); // 확인 버튼 크기 및 위치

        // 확인 버튼에 이벤트 리스너 추가
        OKButton.addActionListener(new ButtonListener(this));
    }
}
