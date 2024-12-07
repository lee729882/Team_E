package Panels;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Core.ButtonListener;
import Core.GameConstants;

// 메뉴 패널(MenuPanel) 클래스
// - 게임의 메인 메뉴 화면을 구성
public class MenuPanel extends JPanel implements GameConstants {

    // 생성자: 메뉴 패널 초기화
    public MenuPanel() {
        setLayout(null); // 레이아웃 매니저 사용하지 않고 수동으로 컴포넌트 배치

        // 배경 이미지 설정
        ImageIcon background = new ImageIcon(this.getClass().getResource(BACKGROUND_PATH + "Menu.jpg"));
        JLabel backgroundLabel = new JLabel(); // 배경 레이블 생성
        backgroundLabel.setIcon(background); // 배경 이미지 설정

        // "Rules" 버튼 생성
        JButton rulesButton = new JButton("Rules"); // 게임 규칙 보기 버튼
        rulesButton.setActionCommand("rules"); // 버튼의 액션 커맨드 설정

        // "Start Game" 버튼 생성
        JButton playButton = new JButton("Start Game"); // 게임 시작 버튼
        playButton.setActionCommand("start"); // 버튼의 액션 커맨드 설정

        // 컴포넌트 추가
        add(rulesButton); // "Rules" 버튼 추가
        add(playButton); // "Start Game" 버튼 추가
        add(backgroundLabel); // 배경 레이블 추가

        // 컴포넌트 위치 및 크기 설정
        backgroundLabel.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT); // 배경 이미지 크기 및 위치
        rulesButton.setBounds(305, 550, 270, 60); // "Rules" 버튼 크기 및 위치
        playButton.setBounds(655, 550, 270, 60); // "Start Game" 버튼 크기 및 위치

        // 버튼에 이벤트 리스너 추가
        rulesButton.addActionListener(new ButtonListener(this)); // "Rules" 버튼 클릭 이벤트 처리
        playButton.addActionListener(new ButtonListener(this)); // "Start Game" 버튼 클릭 이벤트 처리
    }
}
