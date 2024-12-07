package Panels;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Core.ButtonListener;
import Core.GameConstants;

//게임 종료 패널(GameOverPanel) 클래스
//- 게임 종료 화면을 구성하며, 승리자 표시 및 메뉴/종료 버튼을 제공
public class GameOverPanel extends JPanel implements GameConstants {

    String winner; // 승리한 플레이어 이름

    // 생성자: 패널 초기화
    public GameOverPanel(String winner) {
    	setLayout(null); // 레이아웃 매니저를 사용하지 않고 수동으로 컴포넌트 배치
        ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "GameOver.jpg")); // 배경 이미지
        JLabel backgroundLabel = new JLabel(); // 배경 레이블
        backgroundLabel.setIcon(background); // 배경 이미지 설정
        this.winner = winner; // 승리자 이름 저장

        // 승리자 레이블 생성
        JLabel winnerLabel = new JLabel("Winner: " + winner + "!"); // 승리자 이름 표시
        winnerLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30)); // 글꼴 설정
        winnerLabel.setForeground(Color.WHITE); // 글자 색상 설정


        // 메뉴 버튼 생성
        JButton menuButton = new JButton("Menu"); // "Menu" 버튼 생성
        menuButton.setActionCommand("OK"); // 버튼의 액션 커맨드 설정
        
        // 종료 버튼 생성
        JButton quitButton = new JButton("Quit"); // "Quit" 버튼 생성
        quitButton.setActionCommand("quit"); // 버튼의 액션 커맨드 설정

        // 컴포넌트 추가
        add(menuButton); // 메뉴 버튼 추가
        add(quitButton); // 종료 버튼 추가
        add(winnerLabel); // 승리자 레이블 추가
        add(backgroundLabel); // 배경 레이블 추가

        // 컴포넌트 위치 및 크기 설정
        backgroundLabel.setBounds(0, 0, 1200, 675); // 배경 이미지 크기 및 위치
        winnerLabel.setBounds(500, 295, 350, 50); // 승리자 레이블 크기 및 위치
        menuButton.setBounds(380, 500, 200, 70); // 메뉴 버튼 크기 및 위치
        quitButton.setBounds(630, 500, 200, 70); // 종료 버튼 크기 및 위치

        // 버튼에 이벤트 리스너 추가
        menuButton.addActionListener(new ButtonListener(this)); // 메뉴 버튼 클릭 이벤트 처리
        quitButton.addActionListener(new ButtonListener(this)); // 종료 버튼 클릭 이벤트 처리
    }

}
